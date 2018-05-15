package com.example.wayne.tablayoutdemo.sockettest;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    private static final int MESSAGE_SCROLL_TO = 33;
    private static final int FRAME_COUNT = 30;
    private static final int DELAYED_TIME = 33;
    private int mCount = 0;



    final int startX = 0;
    final int deltax = -100;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    private GestureDetector mGestureDetector;

    private static final String TAG = "TCPClientActivity";
    private TextView mMsgTextView;
    private TextView mTouchView;
    private EditText mMsgEditText;
    private Button mSend;
    private Button mBtn2;

    private static final int MESSAGE_RECEIVE_NEW_MSG = 1;
    private static final int MESSAGE_SOCKET_CONNECTED = 2;

    private PrintWriter mPrintWriter;
    private Socket mClietnSocket;

    // 使用延时策略
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
                case MESSAGE_SCROLL_TO: {
                    mCount++;
                    if (mCount <= FRAME_COUNT){
                        float fraction = mCount / (float) FRAME_COUNT;
                        int scrollX = (int) (fraction * 100);
                        mTouchView.scrollTo(scrollX, 0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO, DELAYED_TIME);
                    }

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

        mGestureDetector =new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });
        // 解决长按屏幕无法拖动的现象
        mGestureDetector.setIsLongpressEnabled(false);




        // 属性动画
        // 为了兼容Android3.0以下不能使用属性动画，可以使用动画兼容库nineoldandroids
//        ObjectAnimator.ofFloat(mTouchView, "translationX", 0, 100).setDuration(5000).start();

        // 动画
//        final ValueAnimator animator = ValueAnimator.ofInt(1, 0).setDuration(1000);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float fraction = valueAnimator.getAnimatedFraction();
//                mTouchView.scrollTo(startX + (int)(deltax * fraction), 0);
//            }
//        });
//        animator.start();



        mTouchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "onTouch: "+ "点击了吗");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // VelocityTracker 速度追踪
                        VelocityTracker velocityTracker = VelocityTracker.obtain();
                        velocityTracker.addMovement(motionEvent);
                        velocityTracker.computeCurrentVelocity(1000);
                        int xVelocity = (int)velocityTracker.getXVelocity();
                        int yVelocity = (int)velocityTracker.getYVelocity();
                        Log.i(TAG, "onTouch: "+ xVelocity + ":" + yVelocity);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }

                return true;
            }
        });



//        Intent service = new Intent(this, TCPServerService.class);
//        startService(service);
//        new Thread() {
//            @Override
//            public void run() {
//                connetTCPServer();
//            }
//        }.start();


        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)mBtn2.getLayoutParams();
        params.width += 200;
        params.leftMargin += 100;
        mBtn2.requestLayout();
        // 或者mBtn2.setLayoutParams(params);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                // TouchSlop是系统所能识别出的被认为是滑动的最小距离
                int touchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
                Log.i(TAG, "onTouchEvent: " + touchSlop);
                if (y1 - y2 > 50) {
                    Toast.makeText(TCPClientActivity.this, "向上滑", Toast.LENGTH_SHORT).show();
                } else if (y2 - y1 > 50) {
                    Toast.makeText(TCPClientActivity.this, "向下滑", Toast.LENGTH_SHORT).show();
                }else if (x1 - x2 > 50) {
                    Toast.makeText(TCPClientActivity.this, "向左滑", Toast.LENGTH_SHORT).show();
                }else if (x2 - x1 > 50) {
                    Toast.makeText(TCPClientActivity.this, "向右滑", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onTouchEvent(event);
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
        mTouchView= findViewById(R.id.touch_container);
        mBtn2 = findViewById(R.id.btn2);
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
            case R.id.btn2:
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
