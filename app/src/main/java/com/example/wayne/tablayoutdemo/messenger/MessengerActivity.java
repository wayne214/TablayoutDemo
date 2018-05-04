package com.example.wayne.tablayoutdemo.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.wayne.tablayoutdemo.R;

public class MessengerActivity extends AppCompatActivity {
    private static final String TAG="MessengerActivity";
    private Messenger mService;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder services) {
            // 使用服务端返回的IBinder创建一个Messenger对象
            mService = new Messenger(services);
            // 创建消息message对象
            Message msg = Message.obtain(null, MyConstants.MSG_FORM_CLIENT);
            // 创建bundle对象
            Bundle data = new Bundle();
            data.putString("msg", "hello, this is client.");
            // msg设置数据
            msg.setData(data);
            // 通过replyTo将Messenger对象返回给服务端
            msg.replyTo = mGetReplyMessenger;
            try {
                // messenger向服务端发送数据
                mService.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    // 处理服务端发来的消息
    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyConstants.MSG_FORM_SERVICE:
                    Log.i(TAG, "receive msg from services: " + msg.getData().getString("reply"));

                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }
    // 创建Messenger对象
    private final Messenger mGetReplyMessenger = new Messenger(new MessengerHandler());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = new Intent(this,MessengerService.class);
        // 绑定service
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        // 解除绑定
        unbindService(mConnection);
        super.onDestroy();
    }
}
