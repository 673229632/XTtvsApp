package com.gcsoft.xttvsapp.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.gcsoft.xttvsapp.form.AppConstants;
import com.gcsoft.xttvsapp.form.SettingForm;
import com.gcsoft.xttvsapp.form.TextToSpeechContent;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangrui.i
 */
public class CommonUtils {

    /**
     * 保存配置信息.
     *
     * @param context     Context
     * @param settingForm SettingForm
     */
    public static boolean saveSharedPreferences(Context context, SettingForm settingForm) {
        //保存文件名字为"shared",保存形式为Context.MODE_PRIVATE即该数据只能被本应用读取
        SharedPreferences preferences = context.getSharedPreferences("shared_setting", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("socket_server_ip", settingForm.getIp());
        editor.putInt("socket_server_port", settingForm.getPort());
        editor.putString("socket_server_xtname", settingForm.getXtname());

        editor.putBoolean("socket_server_power_up", settingForm.isPowerup());
        editor.putBoolean("socket_server_param_fullscreen", settingForm.isFullscreen());
        editor.putInt("socket_server_param_height", settingForm.getHeight());
        editor.putInt("socket_server_param_width", settingForm.getWidth());

        editor.putFloat("socket_server_alpha", (settingForm.getAlpha() <= 0.95f ? settingForm.getAlpha() : 0.95f));

        editor.putString("socket_server_bedurl", settingForm.getBedurl());
        //提交数据
        return editor.commit();
    }

    /**
     * 调用吐司的方式.
     *
     * @param context Context
     * @param info    String
     */
    public static void showToastInfo(final Context context, final String info) {
        Log.i("socket thread", info);
    }

    /**
     * 获取安卓设备当前的IP地址（有线或无线）
     *
     * @return
     */
    public static String getClientIP() {

        List<String> ipaddressList = new ArrayList<>();
        try {
            InetAddress candidateAddress = null;
            // 遍历所有的网络接口
            for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
                // 在所有的接口下再遍历IP
                for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
                    // 排除loopback类型地址
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            ipaddressList.add(inetAddr.getHostAddress());
                        } else if (candidateAddress == null) {
                            // site-local类型的地址未被发现，先记录候选地址
                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            /*if (candidateAddress != null) {
                ipaddressList.add(candidateAddress.getHostAddress());
                return ListJoinToString(ipaddressList, " | ");
            }
            // 如果没有发现 non-loopback地址.只能用最次选的方案
            InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
            if (jdkSuppliedAddress == null) {
                throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
            }
            ipaddressList.add(jdkSuppliedAddress.getHostAddress());*/
            return ListJoinToString(ipaddressList, " | ");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将集合拼接为字符串.
     *
     * @param stringList List
     * @param key        String
     * @return String
     */
    public static String ListJoinToString(List<String> stringList, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (String string : stringList) {
            i++;
            if (i < stringList.size()) {
                stringBuilder.append(string + key);
            } else {
                stringBuilder.append(string);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 焦点变化,按钮大小改变.
     *
     * @param v
     * @param hasFocus
     * @param button
     */
    public static void onFocusChangeNew(View v, boolean hasFocus, Button button) {
        if (hasFocus) {
            button.animate().scaleX(1.05f).scaleY(1.2f).setDuration(300).start();
        } else {
            button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start();
        }
    }

    /**
     * 判断悬浮窗服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断字符串是否为空.
     *
     * @param param Object
     * @return boolean
     */
    public static boolean isblank(Object param) {
        return null == param || "".equals(param.toString().trim());
    }

    /**
     * 通用地址段.
     *
     * @param context Context
     * @return String
     */
    public static String getWebUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("shared_setting", Context.MODE_PRIVATE);
        String ip = preferences.getString("socket_server_ip", AppConstants.IP);
        int port = preferences.getInt("socket_server_port", AppConstants.XTPORT);
        String xtname = preferences.getString("socket_server_xtname", AppConstants.XTNAME);
        return "http://" + ip + ":" + port + "/" + xtname + "/";
    }

    /**
     * 获取本地软件版本号名称.
     */
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext().getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 处理语音模板.
     *
     * @param textToSpeechContent
     * @return
     */
    public static String getTextToSpeechContentForTemplate(TextToSpeechContent textToSpeechContent) {
        // 返回的结果
        String resultStr = textToSpeechContent.getTemplate();
        // 模板
        String templateName = textToSpeechContent.getTemplate();
        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(templateName);
        while (m.find()) {
            String filedname = m.group(1);
            Method[] methods = textToSpeechContent.getClass().getMethods();
            for (Method method : methods) {
                System.out.println(method.getName());
                if (method.getName().toLowerCase().equals(("get" + filedname).toLowerCase())) {
                    try {
                        String value = (String) method.invoke(textToSpeechContent);
                        System.out.println(value);
                        resultStr = resultStr.replaceAll("\\{" + filedname + "\\}", value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.print(resultStr);
        return resultStr;
    }

}
