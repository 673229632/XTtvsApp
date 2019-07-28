package com.gcsoft.xttvsapp.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

/**
 * @author zhangrui.i
 * @version V1.0
 * @Description: 语音播报服务.
 * @date 2019年6月17日14:28:13
 */
public class TextToSpeechUtil {

    public Context context;

    /**
     * 创建自带语音对象.
     */
    private TextToSpeech textToSpeech = null;

    public TextToSpeechUtil(Context context) {
        this.context = context;
        initTTS(context);
    }

    public TextToSpeech getTextToSpeech() {
        return textToSpeech;
    }

    private void initTTS(final Context context) {
        //实例化自带语音对象
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.SIMPLIFIED_CHINESE);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "数据丢失或不支持", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    /**
     * 语音播报.
     *
     * @param data 播报内容
     */
    public void startAutoTextToSpeech(String data) {
        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            textToSpeech.setPitch(1.0f);
            // 设置语速
            textToSpeech.setSpeechRate(0.9f);
            //输入中文，若不支持的设备则不会读出来
            textToSpeech.speak(data, TextToSpeech.QUEUE_FLUSH, null);
            // Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
        }
    }
}
