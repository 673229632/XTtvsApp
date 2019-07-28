package com.gcsoft.xttvsapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.ProgressBar;
import com.alibaba.fastjson.JSON;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.form.AppConstants;
import com.gcsoft.xttvsapp.form.InstructionContants;
import com.gcsoft.xttvsapp.form.SocketSendInfoForm;
import com.gcsoft.xttvsapp.form.TextToSpeechContent;
import com.gcsoft.xttvsapp.socket.AppSocketThread;
import com.gcsoft.xttvsapp.utils.CommonUtils;
import com.gcsoft.xttvsapp.utils.TextToSpeechUtil;
import com.gcsoft.xttvsapp.utils.ToastCustom;

import java.io.IOException;

/**
 * 床位图.
 *
 * @author zhangrui.i
 */
public class BedPictureActivity extends AppCompatActivity {

    private String TAG = "===Client===";

    private WebView webView;
    private ProgressBar mprogressBar;

    private Handler mhandler;
    private AppSocketThread socketThread;
    private TextToSpeechUtil textToSpeechUtil;
    private ToastCustom toastcustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bed_picture);

        mprogressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        //开启DOM
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        //WebViewClient主要帮助WebView处理各种通知、请求事件
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                //页面加载完成
                mprogressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //页面开始加载
                mprogressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean key = true;
                try {
                    if (url.startsWith("https:")
                            || url.startsWith("http:")) {
                        view.loadUrl(url);
                    }
                    return key;
                } catch (Exception e) {
                    key = false;
                    return key;
                }
            }
        };

        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                return super.onJsAlert(webView, url, message, result);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            //加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mprogressBar.setProgress(newProgress);
            }
        };

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("shared_setting", Context.MODE_PRIVATE);
        String bedurl = preferences.getString("socket_server_bedurl", AppConstants.BEDURL);

        //加载url
        webView.loadUrl(CommonUtils.getWebUrl(getApplicationContext()) + bedurl);

        // 播报语音
        textToSpeechUtil = new TextToSpeechUtil(getApplicationContext());
        // 显示吐司
        toastcustom = ToastCustom.getInstance(BedPictureActivity.this);

        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {

                    if (msg.obj != null) {
                        SocketSendInfoForm socketSendInfoForm =
                                JSON.parseObject(String.valueOf(msg.obj), SocketSendInfoForm.class);
                        if (socketSendInfoForm != null) {
                            if (InstructionContants.IS_TEXT_TO_SPEECH.equals(socketSendInfoForm.getIsTextToSpeech())) {
                                TextToSpeechContent textToSpeechContent = socketSendInfoForm.getTextToSpeechContent();
                                if (textToSpeechContent != null) {
                                    String data = CommonUtils.getTextToSpeechContentForTemplate(textToSpeechContent);
                                    toastcustom.show(data, textToSpeechContent, Integer.valueOf(textToSpeechContent.getShowTime()));
                                    textToSpeechUtil.startAutoTextToSpeech(data);
                                }
                            }
                        }
                    }
                } catch (Exception ee) {
                    Log.i(TAG, "加载过程出现异常");
                    textToSpeechUtil.getTextToSpeech().stop();
                    textToSpeechUtil.getTextToSpeech().shutdown();
                }
            }
        };

        try {
            startSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        textToSpeechUtil.getTextToSpeech().stop();
        textToSpeechUtil.getTextToSpeech().shutdown();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        textToSpeechUtil.getTextToSpeech().stop();
        textToSpeechUtil.getTextToSpeech().shutdown();
        super.onDestroy();
    }

    public void startSocket() throws IOException {
        socketThread = new AppSocketThread(mhandler, getApplicationContext());
        socketThread.start();
    }
}
