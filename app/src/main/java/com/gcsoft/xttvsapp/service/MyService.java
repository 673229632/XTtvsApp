package com.gcsoft.xttvsapp.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.form.InstructionContants;
import com.gcsoft.xttvsapp.form.SocketSendInfoForm;
import com.gcsoft.xttvsapp.socket.AppSocketThread;
import com.gcsoft.xttvsapp.utils.CommonUtils;
import com.gcsoft.xttvsapp.form.AppConstants;
import com.gcsoft.xttvsapp.utils.MyApplication;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * 启动悬浮窗Service.
 *
 * @author zhangrui.i
 */
public class MyService extends Service {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;
    private View mView;

    private Handler mhandler;
    private AppSocketThread socketThread;

    private WebView webView;
    private ProgressBar mprogressBar;
    private TextView socketIp;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showView();
        startSocket();
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("HandlerLeak")
    private void startSocket() {

        mhandler = new Handler() {
            @SuppressWarnings("AlibabaThreadPoolCreation")
            @Override
            public void handleMessage(Message msg) {
                try {
                    Log.i(TAG, "mhandler接收到msg=" + msg.what);
                    if (msg.obj != null) {
                        final SocketSendInfoForm socketSendInfoForm =
                                JSON.parseObject(String.valueOf(msg.obj), SocketSendInfoForm.class);
                        if (socketSendInfoForm != null) {
                            // 客户端ip
                            if (!CommonUtils.isblank(socketSendInfoForm.getClientIpAddress())) {
                                socketIp.setText(socketSendInfoForm.getClientIpAddress());
                            }
                            // app指令
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeInstruction())) {
                                if (InstructionContants.IS_SHOW_KEY.equals(socketSendInfoForm.getTypeInstruction())) {
                                    mView.setVisibility(View.VISIBLE);

                                    ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
                                    int sec = 30;
                                    // 显示时长
                                    if (socketSendInfoForm.getShowTimeLength() != null) {
                                        sec = socketSendInfoForm.getShowTimeLength();
                                    }
                                    scheduledExecutorService.schedule(new Runnable() {
                                        @Override
                                        public void run() {
                                            mView.setVisibility(View.INVISIBLE);
                                        }
                                    }, sec * 60 * 1000, TimeUnit.MILLISECONDS);

                                }
                                if (InstructionContants.IS_CLOSE_KEY.equals(socketSendInfoForm.getTypeInstruction())) {
                                    mView.setVisibility(View.INVISIBLE);
                                }
                            }
                            // 一般请求
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeUrl())) {
                                webView.loadUrl(socketSendInfoForm.getTypeUrl());
                            }
                            // 血透请求
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeXtUrl())) {
                                String url = CommonUtils.getWebUrl(getApplicationContext()) + socketSendInfoForm.getTypeXtUrl();
                                webView.loadUrl(url);
                            }
                            // 文本
                            if (!CommonUtils.isblank(socketSendInfoForm.getTypeXtUrl())) {

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
            socketThread = new AppSocketThread(mhandler, getApplicationContext());
            socketThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private void showView() {

        if (((MyApplication) getApplication()).getmView() == null) {
            mView = new View(getApplicationContext());
            mView = View.inflate(getApplicationContext(),
                    R.layout.activity_web_view, null);
            ((MyApplication) getApplication()).setmView(mView);
        }
        mView = ((MyApplication) getApplication()).getmView();
        mView.setBackgroundColor(R.color.colorWmbg);
        mView.setPadding(10, 10, 10, 10);
        /*--------------------------------------------------------------------------------------------*/

        mprogressBar = mView.findViewById(R.id.progressbar);
        webView = mView.findViewById(R.id.webview);
        socketIp = mView.findViewById(R.id.socket_ip);

        WebSettings webSettings = webView.getSettings();
        //开启DOM
        webSettings.setDomStorageEnabled(true);
        //支持javascript
        webSettings.setJavaScriptEnabled(true);

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setPluginState(WebSettings.PluginState.ON);

        // 设置出现缩放工具
        webSettings.setBuiltInZoomControls(true);
        // 设置可以支持缩放
        webSettings.setSupportZoom(true);

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
                view.loadUrl(url);
                return true;
            }
        };

        WebChromeClient webChromeClient = new WebChromeClient() {

            //加载进度回调
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                mprogressBar.setProgress(newProgress);
            }
        };

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.setInitialScale(50);

        //加载url
        webView.loadUrl(CommonUtils.getWebUrl(getApplicationContext()) + "dialysispatienttv");

        /*--------------------------------------------------------------------------------------------*/

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("shared_setting", Context.MODE_PRIVATE);

        //获取WindowManager
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        layoutParams = ((MyApplication) getApplication()).getWmParams();

        // 系统提示类型,重要
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.format = 1;
        // 不能抢占聚焦点
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.flags = layoutParams.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        // 排版不受限制
        layoutParams.flags = layoutParams.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

        layoutParams.alpha = preferences.getFloat("socket_server_alpha", AppConstants.PARAM_ALPHA);

        //调整悬浮窗口至左上角
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //以屏幕左上角为原点，设置x、y初始值
        layoutParams.x = 0;
        layoutParams.y = 0;

        // 全屏判断
        if (!preferences.getBoolean("socket_server_param_fullscreen", AppConstants.FULLSCREEN)) {
            //设置悬浮窗口长宽数据
            layoutParams.width = preferences.getInt("socket_server_param_width", AppConstants.PARAM_WIDTH);
            layoutParams.height = preferences.getInt("socket_server_param_height", AppConstants.PARAM_HEIGHT);
        }

        if (((MyApplication) getApplication()).isIscheckExit()) {
            ((MyApplication) getApplication()).setIscheckExit(false);
            //显示myFloatView图像
            mWindowManager.addView(mView, layoutParams);
        } else {
            if (mWindowManager != null && mView != null && !mView.isShown()) {
                mView.setVisibility(View.VISIBLE);
            }
        }
    }

}
