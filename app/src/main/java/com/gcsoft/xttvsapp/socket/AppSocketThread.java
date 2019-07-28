package com.gcsoft.xttvsapp.socket;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.gcsoft.xttvsapp.form.AppConstants;
import com.gcsoft.xttvsapp.utils.MyApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author zhangrui.i
 */
public class AppSocketThread extends Thread {

    private Context context;

    private String TAG = "socket thread";
    private DatagramSocket datagramSocket;
    Handler inHandler;

    public AppSocketThread(Handler handlerin, Context context) {
        inHandler = handlerin;
        this.context = context;
        datagramSocket = ((MyApplication) context).getDatagramSocket();
    }

    /**
     * 实时接受数据
     */
    @Override
    public void run() {
        Log.i(TAG, "{实时接受数据线程}开始运行...");

        try {
            //接收端监听指定端口
            if (datagramSocket == null) {
                datagramSocket = new DatagramSocket(AppConstants.PORT);
                ((MyApplication) context).setDatagramSocket(datagramSocket);
            }
            while (true) {
                //定义数据包,用于存储数据
                byte[] buf = new byte[64 * 1024];
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                //通过服务的receive方法将收到数据存入数据包中,receive()为阻塞式方法
                datagramSocket.receive(dp);
                //通过数据包的方法获取其中的数据
                String ip = dp.getAddress().getHostAddress();
                String data = new String(dp.getData(), 0, dp.getLength(), "gbk");

                /***** 返回ACK消息数据报*/
                // 组装数据报
                byte[] bufback = "success".getBytes();
                DatagramPacket sendPacket = new DatagramPacket(bufback, bufback.length, dp.getAddress(), dp.getPort());
                // 发送消息
                datagramSocket.send(sendPacket);

                Message msg = inHandler.obtainMessage();
                msg.obj = data;
                // 结果返回给UI处理
                inHandler.sendMessage(msg);
            }
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}