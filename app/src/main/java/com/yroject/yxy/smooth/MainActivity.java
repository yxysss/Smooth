package com.yroject.yxy.smooth;

import android.content.*;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import library.smooth.SmoothLoader;
import library.smooth.onErrorListener;
import library.smooth.onFinishedListener;
import library.smooth.onStartListener;

import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SmoothLoader smoothLoader;

    private Button start, pause;

    public static LinearLayout tasklayout;

    public static HashMap<Integer, View> viewHashMap = new HashMap<>();

    private TextView progress;

    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smoothLoader = SmoothLoader.getSmoothLoader(this);
        Log.d("MainThread", "MainThread" + " : " + getTaskId());
        bindService(new Intent(this, DownloadService.class), connection, BIND_AUTO_CREATE);
        // tasklayout = (LinearLayout) findViewById(R.id.tasklayout);
        IntentFilter intentFilter = new IntentFilter();
        Intent intent = new Intent();
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //smoothLoader.startThread();
                downloadBinder.download();
            }
        });
        pause = (Button) findViewById(R.id.pause);
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //smoothLoader.pauseThread();
                smoothLoader.addDownloadTask("http://192.168.1.102/QQ.apk", new onStartListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(MainActivity.this, "2号任务下载开始", Toast.LENGTH_LONG).show();
                    }
                }, new onErrorListener() {
                    @Override
                    public void onError() {
                        Toast.makeText(MainActivity.this, "2号任务下载失败", Toast.LENGTH_LONG).show();
                    }
                }, new onFinishedListener() {
                    @Override
                    public void onFinished() {
                        Toast.makeText(MainActivity.this, "2号任务下载完成", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        smoothLoader.getCacheDir();
    }
}
