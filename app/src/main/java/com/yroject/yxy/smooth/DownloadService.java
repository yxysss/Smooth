package com.yroject.yxy.smooth;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import library.smooth.SmoothLoader;
import library.smooth.onErrorListener;
import library.smooth.onFinishedListener;
import library.smooth.onStartListener;

import java.io.File;

/**
 * Created by Y.X.Y on 2017/4/23 0023.
 */
public class DownloadService extends Service {

    public DownloadBinder binder = new DownloadBinder();

    class DownloadBinder extends Binder {
        public void download() {
            final String fileurl = "http://192.168.1.102/QQ_500.apk";
            SmoothLoader.getSmoothLoader(DownloadService.this).addDownloadTask(fileurl, new onStartListener() {
                @Override
                public void onStart() {
                    Toast.makeText(DownloadService.this, "1号任务下载开始", Toast.LENGTH_LONG).show();
                }
            }, new onErrorListener() {
                @Override
                public void onError() {
                    Toast.makeText(DownloadService.this, "1号任务下载失败", Toast.LENGTH_LONG).show();
                }
            }, new onFinishedListener() {
                @Override
                public void onFinished() {
                    String filename = SmoothLoader.getSmoothLoader(DownloadService.this).getFinishFilename(fileurl);
                    File file = new File(filename);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(file),
                            "application/vnd.android.package-archive");
                    try {
                        DownloadService.this.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(DownloadService.this, "1号任务下载完成", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

}
