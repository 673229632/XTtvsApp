package com.gcsoft.xttvsapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.gcsoft.xttvsapp.R;
import com.gcsoft.xttvsapp.utils.CommonUtils;
import com.gcsoft.xttvsapp.form.AppConstants;
import com.gcsoft.xttvsapp.form.SettingForm;

/**
 * 设置.
 *
 * @author zhangrui.i
 */
public class SettingActivity extends AppCompatActivity {

    private TextView textview_localip;
    private EditText editText_ip;
    private EditText editText_port;
    private EditText editText_xtname;

    private CheckBox checkBox_powerup;
    private CheckBox checkBox_fullscr;
    private EditText editText_height;
    private EditText editText_width;

    private EditText editText_alpha;

    private EditText editText_bedurl;

    private Button btn_save;

    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        textview_localip = findViewById(R.id.textview_localip);
        textview_localip.setText(CommonUtils.getClientIP());

        editText_ip = findViewById(R.id.editText_ip);
        editText_port = findViewById(R.id.editText_port);
        editText_xtname = findViewById(R.id.editText_xtname);

        checkBox_powerup = findViewById(R.id.checkBox_powerup);
        checkBox_fullscr = findViewById(R.id.radioButton_fillscr);
        editText_height = findViewById(R.id.editText_height);
        editText_width = findViewById(R.id.editText_width);

        editText_alpha = findViewById(R.id.editText_alpha);

        editText_bedurl = findViewById(R.id.editText_bedurl);

        btn_save = findViewById(R.id.btn_save);
        btn_back = findViewById(R.id.btn_back);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("shared_setting", Context.MODE_PRIVATE);
        editText_ip.setText(preferences.getString("socket_server_ip", AppConstants.IP));
        editText_port.setText(String.valueOf(preferences.getInt("socket_server_port", AppConstants.XTPORT)));
        editText_xtname.setText(preferences.getString("socket_server_xtname", AppConstants.XTNAME));

        checkBox_powerup.setChecked(preferences.getBoolean("socket_server_power_up", AppConstants.POWER_UP));
        checkBox_fullscr.setChecked(preferences.getBoolean("socket_server_param_fullscreen", AppConstants.FULLSCREEN));
        editText_width.setText(String.valueOf(preferences.getInt("socket_server_param_width", AppConstants.PARAM_WIDTH)));
        editText_height.setText(String.valueOf(preferences.getInt("socket_server_param_height", AppConstants.PARAM_HEIGHT)));

        editText_alpha.setText(String.valueOf(preferences.getFloat("socket_server_alpha", AppConstants.PARAM_ALPHA)));

        editText_bedurl.setText(preferences.getString("socket_server_bedurl", AppConstants.BEDURL));

        // 保存
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SettingForm settingForm = new SettingForm();
                settingForm.setIp(editText_ip.getText().toString());
                settingForm.setPort(Integer.valueOf(editText_port.getText().toString()));
                settingForm.setXtname(editText_xtname.getText().toString());

                settingForm.setPowerup(checkBox_powerup.isChecked());
                settingForm.setFullscreen(checkBox_fullscr.isChecked());
                settingForm.setHeight(Integer.valueOf(editText_height.getText().toString()));
                settingForm.setWidth(Integer.valueOf(editText_width.getText().toString()));

                settingForm.setAlpha(Float.valueOf(editText_alpha.getText().toString()));

                settingForm.setBedurl(editText_bedurl.getText().toString());

                if (CommonUtils.saveSharedPreferences(getApplicationContext(), settingForm)) {
                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "保存失败,请重试", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_save.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_save);
            }
        });

        // 返回
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_back.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                CommonUtils.onFocusChangeNew(v, hasFocus, btn_back);
            }
        });
    }
}
