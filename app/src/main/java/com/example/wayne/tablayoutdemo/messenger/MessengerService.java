package com.example.wayne.tablayoutdemo.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    // 处理客户端发来的消息
    private static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FORM_CLIENT:
                    Log.i(TAG, "handleMessage: " + msg.getData().getString("msg"));
                    Messenger client = msg.replyTo;
                    Message relpyMessage = Message.obtain(null, MyConstants.MSG_FORM_SERVICE);
                    Bundle bundle = new Bundle();
                    bundle.putString("reply", "嗯，你的消息我已经收到了，稍后回复你");
                    relpyMessage.setData(bundle);
                    try {
                        client.send(relpyMessage);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
    // 创建Messenger对象
    private final Messenger mMessenger = new Messenger(new MessengerHandler());
    public MessengerService() {
    }
    // 返回IBinder对象
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return mMessenger.getBinder();
    }
}
