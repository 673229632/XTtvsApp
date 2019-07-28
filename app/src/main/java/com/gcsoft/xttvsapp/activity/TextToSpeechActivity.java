package com.gcsoft.xttvsapp.activity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.form.TextToSpeechContent;
import com.gcsoft.xttvsapp.utils.TextToSpeechUtil;
import com.gcsoft.xttvsapp.utils.ToastCustom;

import java.util.Locale;

public class TextToSpeechActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;

    private EditText editText;

    private TextToSpeechUtil textToSpeechUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_to_speech);

        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        textToSpeechUtil = new TextToSpeechUtil(TextToSpeechActivity.this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            String data = editText.getText().toString();
            startAuto(data);
        }
    }

    private void startAuto(String data) {
        TextToSpeechContent textToSpeechContent = new TextToSpeechContent();
        textToSpeechContent.setTemplate("请{patientName}到{bedPosition}上机");
        textToSpeechContent.setShowTime("10000");
        textToSpeechContent.setPatientName(data);
        textToSpeechContent.setPatientSex("男");
        textToSpeechContent.setTimtimeZone("上午");
        textToSpeechContent.setBedPosition("二病区5号床");
        ToastCustom.getInstance(TextToSpeechActivity.this).show(data, textToSpeechContent, 50000);
        textToSpeechUtil.startAutoTextToSpeech(data);
    }

    @Override
    protected void onDestroy() {
        textToSpeechUtil.getTextToSpeech().stop();
        textToSpeechUtil.getTextToSpeech().shutdown();
        super.onDestroy();
    }
}