package com.gcsoft.xttvsapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.service.MyService;
import com.gcsoft.xttvsapp.utils.CommonUtils;
import com.gcsoft.xttvsapp.utils.MyApplication;

/**
 * 主页面.
 *
 * @author zhangrui.i
 */
public class MainActivity extends AppCompatActivity {

    private TextView title_v;

    private Button btn_browser;

    private Button btn_floatingwindow;

    private Button btn_bedpic;

    private Button btn_videoview;

    private Button btn_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title_v = findViewById(R.id.title_v);
        title_v.setText("模式选择 v" + CommonUtils.getLocalVersionName(getApplication()));

        btn_browser = findViewById(R.id.btn_browser);
        btn_floatingwindow = findViewById(R.id.btn_floatingwindow);
        btn_bedpic = findViewById(R.id.btn_bedpic);

        btn_videoview = findViewById(R.id.btn_videoview);

        btn_setting = findViewById(R.id.btn_setting);

        this.judeBtnIsUse();

        // 浏览器模式
        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFloatView();
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
        });
        btn_browser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_browser);
            }
        });
        // 悬浮窗模式
        btn_floatingwindow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFloatView();
                Intent myServiceIntent = new Intent(MainActivity.this, MyService.class);
                startService(myServiceIntent);
                judeBtnIsUse();
            }
        });
        btn_floatingwindow.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_floatingwindow);
            }
        });
        // 床位图
        btn_bedpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFloatView();
                Intent intent = new Intent(MainActivity.this, BedPictureActivity.class);
                startActivity(intent);
            }
        });
        btn_bedpic.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_bedpic);
            }
        });
        // 播放视频
        btn_videoview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFloatView();
                Intent intent = new Intent(MainActivity.this, TextToSpeechActivity.class);
                startActivity(intent);
            }
        });
        btn_videoview.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_videoview);
            }
        });
        // 设置
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFloatView();
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        btn_setting.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_setting);
            }
        });
    }

    /**
     * 将悬浮View从WindowManager中移除，需要与createFloatView()成对出现
     */
    protected void removeFloatView() {
        View mView = ((MyApplication) getApplication()).getmView();
        WindowManager mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (mWindowManager != null && mView != null) {
            mWindowManager.removeViewImmediate(mView);
            ((MyApplication) getApplication()).setmView(null);
            ((MyApplication) getApplication()).setWmParams(new WindowManager.LayoutParams());
            if (((MyApplication) getApplication()).getDatagramSocket() != null) {
                ((MyApplication) getApplication()).getDatagramSocket().close();
                ((MyApplication) getApplication()).setDatagramSocket(null);
            }
            ((MyApplication) getApplication()).setIscheckExit(true);
        }
        Intent myServiceIntent = new Intent(MainActivity.this, MyService.class);
        stopService(myServiceIntent);
    }

    @Override
    protected void onStart() {
        this.judeBtnIsUse();
        super.onStart();
    }

    @Override
    protected void onResume() {
        this.judeBtnIsUse();
        super.onResume();
    }

    @SuppressLint("ResourceAsColor")
    private void judeBtnIsUse() {
        if (CommonUtils.isServiceRunning(getApplicationContext(),
                "com.gcsoft.xttvsapp.service.MyService")) {
            btn_floatingwindow.setText("悬浮窗模式(正在运行...)");
        } else {
            btn_floatingwindow.setText("悬浮窗模式");
        }
    }
}
