package com.example.wayne.tablayoutdemo.sockettest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TCPServerService extends Service {
    private boolean mIsServiceDestoryed = false;
    private String[] mDefineedMessages = new String[] {
            "你好呀，哈哈",
            "请问你叫什么名字？",
            "今天北京天气不错哦，shy",
            "给你讲个笑话：据说爱笑的人运气不会太差，不知道真假",
    };

    public TCPServerService() {
    }

    @Override
    public void onCreate() {
        new Thread(new TcpServer()).start();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mIsServiceDestoryed = true;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    private class TcpServer implements Runnable{

        @Override
        public void run() {
            ServerSocket serverSocket = null;
            try {
                // 监听本地8688接口
                serverSocket = new ServerSocket(8688);
            }catch (IOException e) {
                System.out.println("establish tcp server failed, port: 8688");
                e.printStackTrace();
                return;
            }

            while (!mIsServiceDestoryed) {
                try {
                    // 接收客户端请求
                    final Socket client = serverSocket.accept();
                    System.out.println("accept");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                responseClient(client);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }catch (IOException e) {
                    System.out.println("");
                }
            }
        }
    }

    private void responseClient(Socket client) throws IOException {
        // 用户接收客户端消息
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        // 用于向客户端发送消息
        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        out.println("欢迎来到聊天室");
        while (!mIsServiceDestoryed) {
            String str = in.readLine();
            System.out.println("msg from client:" + str);
            if (str == null) {
                // 客户端断开连接
                break;
            }

            int i = new Random().nextInt(mDefineedMessages.length);
            String msg = mDefineedMessages[i];
            out.println(msg);
            System.out.println("send:" + msg);
        }

        System.out.println("client quit.");
        // 关闭流
        out.close();
        in.close();
        client.close();

    }

}
