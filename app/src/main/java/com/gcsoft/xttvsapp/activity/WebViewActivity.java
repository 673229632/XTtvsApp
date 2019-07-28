package com.gcsoft.xttvsapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.*;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.form.SocketSendInfoForm;
import com.gcsoft.xttvsapp.socket.AppSocketThread;
import com.gcsoft.xttvsapp.utils.CommonUtils;

import java.io.IOException;

/**
 * 浏览器模式.
 *
 * @author zhangrui.i
 */
public class WebViewActivity extends AppCompatActivity {

    private String TAG = "===Client===";

    private WebView webView;
    private ProgressBar progressBar;
    private TextView socketIp;
    private Handler mhandler;

    private AppSocketThread socketThread;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        //进度条
        progressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);
        socketIp = findViewById(R.id.socket_ip);

        // webview设置
        WebSettings webSettings = webView.getSettings();
        //开启DOM
        webSettings.setDomStorageEnabled(true);
        //允许使用js
        webSettings.setJavaScriptEnabled(true);
        /**
         * LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
         * LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
         * LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
         * LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
         */
        //不使用缓存，只从网络获取数据.
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 设置可以支持缩放
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);

        //自适应屏幕
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setPluginState(WebSettings.PluginState.ON);

        //WebViewClient主要帮助WebView处理各种通知、请求事件
        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                    }
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {//页面开始加载
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {//页面加载完成
                progressBar.setVisibility(View.GONE);
            }

        };

        //WebChromeClient主要辅助WebView处理Javascript的对话框、网站图标、网站title、加载进度等
        WebChromeClient webChromeClient = new WebChromeClient() {
            //不支持js的alert弹窗，需要自己监听然后通过dialog弹窗
            @Override
            public boolean onJsAlert(WebView webView, String url, String message, JsResult result) {
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(webView.getContext());
                localBuilder.setMessage(message).setPositiveButton("确定", null);
                localBuilder.setCancelable(false);
                localBuilder.create().show();

                //注意:
                //必须要这一句代码:result.confirm()表示:
                //处理结果为确定状态同时唤醒WebCore线程
                //否则不能继续点击按钮
                result.confirm();
                return true;
            }

            //获取网页标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i("ansen", "网页标题:" + title);
            }

            //加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        };

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        webView.setInitialScale(60);
        webView.loadUrl(CommonUtils.getWebUrl(getApplicationContext()) + "dialysispatienttv");

        mhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    if (msg.obj != null) {
                        SocketSendInfoForm socketSendInfoForm =
                                JSON.parseObject(String.valueOf(msg.obj), SocketSendInfoForm.class);
                        if (socketSendInfoForm != null) {
                            // 血透请求
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeXtUrl())) {
                                String url = CommonUtils.getWebUrl(getApplicationContext()) + socketSendInfoForm.getTypeXtUrl();
                                webView.loadUrl(url);
                            }
                            // 一般请求
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeUrl())) {
                                webView.loadUrl(socketSendInfoForm.getTypeUrl());
                            }
                            // 文本
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeXtUrl())) {

                            }
                            // 客户端ip
                            if (!CommonUtils.isblank(socketSendInfoForm.getClientIpAddress())) {
                                socketIp.setText(socketSendInfoForm.getClientIpAddress());
                            }
                        }
                    }
                } catch (Exception ee) {
                    Log.i(TAG, "加载过程出现异常");
                    ee.printStackTrace();
                }
            }
        };

        try {
            startSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startSocket() throws IOException {
        socketThread = new AppSocketThread(mhandler, getApplicationContext());
        socketThread.start();
    }

}
