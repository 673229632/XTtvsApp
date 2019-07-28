package com.gcsoft.xttvsapp.broad;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.gcsoft.xttvsapp.form.AppConstants;
import com.gcsoft.xttvsapp.service.MyService;

/**
 * 跟随系统启动服务.
 *
 * @author zhangrui.i
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences("shared_setting", Context.MODE_PRIVATE);
        if (preferences.getBoolean("socket_server_power_up", AppConstants.POWER_UP)) {
            Intent welcomeIntent = new Intent(context, MyService.class);
            context.startService(welcomeIntent);
        }
    }
}
