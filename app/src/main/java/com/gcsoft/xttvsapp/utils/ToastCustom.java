package com.gcsoft.xttvsapp.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.form.TextToSpeechContent;

/**
 * @author zhangrui.i
 * @version V1.0
 * @Description: TODO
 * @date
 */
public class ToastCustom {

    private boolean canceled = true;
    private Handler handler;
    private Toast toast;
    private TimeCount time;
    private TextView tv_patientname;
    private TextView tv_sex;
    private TextView tv_timezone;
    private TextView tv_bedposition;

    private static ToastCustom instance;

    public static ToastCustom getInstance(Context context) {
        if (instance == null) {
            instance = new ToastCustom(context);
        }
        return instance;
    }

    public ToastCustom(Context context) {
        this(context, new Handler());
    }

    public ToastCustom(Context context, Handler handler) {
        this.handler = handler;

        View layout = LayoutInflater.from(context).inflate(R.layout.toast_main, null, false);
        tv_patientname = layout.findViewById(R.id.tv_patientname);
        tv_sex = layout.findViewById(R.id.tv_sex);
        tv_timezone = layout.findViewById(R.id.tv_timezone);
        tv_bedposition = layout.findViewById(R.id.tv_bedposition);

        if (toast == null) {
            toast = new Toast(context);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
    }

    /**
     * @param text     要显示的内容
     * @param duration 显示的时间长
     *                 <p>
     *                 根据LENGTH_MAX进行判断
     *                 如果不匹配，进行系统显示
     *                 如果匹配，永久显示，直到调用hide()
     */
    public void show(String text, TextToSpeechContent textToSpeechContent, int duration) {
        //1000是消失渐变时间
        time = new TimeCount(duration, 1000);
        tv_patientname.setText(textToSpeechContent.getPatientName());
        tv_sex.setText(textToSpeechContent.getPatientSex());
        tv_timezone.setText(textToSpeechContent.getTimtimeZone());
        tv_bedposition.setText(textToSpeechContent.getBedPosition());

        if (canceled) {
            time.start();
            canceled = false;
            showUntilCancel();
        }
    }

    /**
     * 隐藏Toast
     */
    public void hide() {
        if (toast != null) {
            toast.cancel();
        }
        canceled = true;
    }

    private void showUntilCancel() {
        if (canceled) {
            return;
        }
        toast.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showUntilCancel();
            }
        }, 10);
    }

    /**
     * 计时器.
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            // 总时长,计时的时间间隔
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            // 计时完毕时触发
            hide();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // 计时过程显示
        }

    }
}
