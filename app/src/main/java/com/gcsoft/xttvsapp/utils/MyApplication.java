package com.gcsoft.xttvsapp.utils;

import android.app.Application;
import android.view.View;
import android.view.WindowManager;

import java.net.DatagramSocket;

/**
 * 应用中全局变量.
 *
 * @author zhangrui.i
 */
public class MyApplication extends Application {

    /**
     * 创建全局变量
     * 全局变量一般都比较倾向于创建一个单独的数据类文件
     * <p>
     * 这里使用了在Application中添加数据的方法实现全局变量
     * 注意在AndroidManifest.xml中的Application节点添加android:name=".MyApplication"属性
     */
    private WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();

    private View mView;

    private boolean ischeckExit = true;

    private DatagramSocket datagramSocket;

    public void setWmParams(WindowManager.LayoutParams wmParams) {
        this.wmParams = wmParams;
    }

    public WindowManager.LayoutParams getWmParams() {
        return wmParams;
    }

    public boolean isIscheckExit() {
        return ischeckExit;
    }

    public void setIscheckExit(boolean ischeckExit) {
        this.ischeckExit = ischeckExit;
    }

    public View getmView() {
        return mView;
    }

    public void setmView(View mView) {
        this.mView = mView;
    }

    public DatagramSocket getDatagramSocket() {
        return datagramSocket;
    }

    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
}