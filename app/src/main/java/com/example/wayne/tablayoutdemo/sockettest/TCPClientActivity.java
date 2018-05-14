package com.example.wayne.tablayoutdemo.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.health.PackageHealthStats;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wayne.tablayoutdemo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TCPClientActivity extends Activity implements View.OnClickListener {
    private TextView mMsgTextView;
    private EditText mMsgEditText;
    private Button mSend;

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private PrintWriter mPrintWriter;
    private Socket mClietnSocket;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_RECEIVE_NEW_MSG: {
                    mMsgTextView.setText(mMsgTextView.getText() + (String)msg.obj);
                    break;
                }
                case MESSAGE_SOCKET_CONNECTED: {
                    mSend.setEnabled(true);
                    break;
                }
                default: break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcpclient);
        initView();
        mSend.setOnClickListener(this);

        Intent service = new Intent(this, TCPServerService.class);
        startService(service);
        new Thread() {
            @Override
            public void run() {
                connetTCPServer();
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (mClietnSocket != null) {
            try {
                mClietnSocket.shutdownInput();
                mClietnSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onDestroy();
    }

    private void initView() {
        mMsgTextView = findViewById(R.id.msg_container);
        mMsgEditText = findViewById(R.id.msg);
        mSend = findViewById(R.id.send);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.send:
                final String msg = mMsgEditText.getText().toString();
                if (!TextUtils.isEmpty(msg) && mPrintWriter != null) {
                    // 流操作不能再UI主线程中
                    new Thread() {
                        @Override
                        public void run() {
                            mPrintWriter.println(msg);
                        }
                    }.start();

                    mMsgEditText.setText("");
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "self" +time + ":" + msg + "\n";
                    mMsgTextView.setText(mMsgTextView.getText() + showedMsg);
                }
                break;
            default: break;
        }
    }

    private String formatDateTime(long time){

      return new SimpleDateFormat("(HH:mm:ss)").format(new Date(time));
    }

    private void connetTCPServer() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8688);
                mClietnSocket = socket;
                mPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mHandler.sendEmptyMessage(MESSAGE_SOCKET_CONNECTED);
                System.out.println("connect server success");
            } catch (IOException e) {
                SystemClock.sleep(1000);
                System.out.println("connect tcp server failed, retry...");
            }
        }

        try {
            // 接收服务器端的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (!TCPClientActivity.this.isFinishing()) {
                String msg = br.readLine();
                System.out.println("receive:" + msg);
                if (msg != null) {
                    String time = formatDateTime(System.currentTimeMillis());
                    final String showedMsg = "server" +time + ":" + msg + "\n";
                    mHandler.obtainMessage(MESSAGE_RECEIVE_NEW_MSG, showedMsg)
                            .sendToTarget();
                }
            }

            System.out.println("quit...");
            mPrintWriter.close();
            br.close();
            socket.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
