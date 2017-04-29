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
        bindService(new Intent(this, DownloadService.class), connection, BIND_AUTO_CREATE);
        start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //smoothLoader.startThread();
                downloadBinder.download();
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
