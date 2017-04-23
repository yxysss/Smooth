package library.smooth;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by Y.X.Y on 2017/4/22 0022.
 */
public class SmoothLoader {

    private static SmoothLoader smoothLoader = null;

    public static SmoothLoader getSmoothLoader(Context context) {
        if(smoothLoader == null) {
            synchronized (SmoothLoader.class) {
                if(smoothLoader == null) {
                    smoothLoader = new SmoothLoader(context);
                }
            }
        }
        return smoothLoader;
    }

    private static Context context = null;

    private static ExecutorService service = null;

    private static ConcurrentHashMap<String, DownloadThread> threadHashMap = new ConcurrentHashMap<>();

    // private static ConcurrentHashMap<String, onStartListener> startListenerHashMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, onErrorListener> errorListenerHashMap = new ConcurrentHashMap<>();

    private static ConcurrentHashMap<String, onFinishedListener> finishedListenerHashMap = new ConcurrentHashMap<>();

    private static Handler sHandler = new Handler() {
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            String filename = bundle.getString("filename");
            boolean isfinished = bundle.getBoolean("isfinished");
            threadHashMap.remove(filename);
            String filepath = getCacheDir(filename);
            String finishfilepath = getCacheDir("over"+filename);
            File file = new File(filepath);
            if (isfinished && file.exists()) {
                // Toast.makeText(context, "文件下载完成", Toast.LENGTH_LONG).show();
                file.renameTo(new File(finishfilepath));
                onFinishedListener onFinishedListener = finishedListenerHashMap.get(filename);
                if (onFinishedListener != null) {
                    onFinishedListener.onFinished();
                }
            }
            else {
                // Toast.makeText(context, "文件下载中断", Toast.LENGTH_LONG).show();
                onErrorListener onErrorListener = errorListenerHashMap.get(filename);
                if (onErrorListener != null) {
                    onErrorListener.onError();
                }
            }
        }
    };

    private SmoothLoader(Context context) {
        this.context = context;
        this.service = Executors.newCachedThreadPool();
    }

    public void addDownloadTask(String fileurl, onStartListener onStartListener, onErrorListener onErrorListener, onFinishedListener onFinishedListener) {
        String filename = hashKeyForDisk(fileurl);
        int lastindex = fileurl.lastIndexOf("/");
        String name = fileurl.substring(lastindex+1, fileurl.length());
        filename = filename + "+" + name;
        Log.d("DownloadTask", filename);
        String filepath = getCacheDir(filename);
        String finishfilepath = getCacheDir("over"+filename);
        File finishfile = new File(finishfilepath);
        if(finishfile.exists()) {
            // 文件已经下载完成
            Toast.makeText(context, "文件已经下载完成", Toast.LENGTH_LONG).show();
            onFinishedListener.onFinished();
            return ;
        }
        File file = new File(filepath);
        URL url = null;
        try {
            url = new URL(fileurl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (url == null) {
            Toast.makeText(context, "URL无效", Toast.LENGTH_LONG).show();
            return ;
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            DownloadThread downloadThread = new DownloadThread(file, url, 0, filename, sHandler);
            threadHashMap.put(filename, downloadThread);
            // startListenerHashMap.put(filename, onStartListener);
            onStartListener.onStart();
            errorListenerHashMap.put(filename, onErrorListener);
            finishedListenerHashMap.put(filename, onFinishedListener);
            // 添加View
            //Future<> future = service.submit(downloadThread);
            service.submit(downloadThread);
        } else {
            DownloadThread downloadThread = threadHashMap.get(filename);
            int progress = 0;
            if(downloadThread == null) {
                try {
                    FileInputStream fin = new FileInputStream(file);
                    progress = fin.available();
                    fin.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                downloadThread = new DownloadThread(file, url, progress, filename, sHandler);
                threadHashMap.put(filename, downloadThread);
                // startListenerHashMap.put(filename, onStartListener);
                onStartListener.onStart();
                errorListenerHashMap.put(filename, onErrorListener);
                finishedListenerHashMap.put(filename, onFinishedListener);
                //添加View
                //Future<> future = service.submit(downloadThread);
                service.submit(downloadThread);

            } else {
                // 文件正在被下载
                Toast.makeText(context, "文件正在被下载", Toast.LENGTH_LONG).show();
            }
        }
    }

    private static String getCacheDir(String uniqueName) {
        String cachePath;
        // 如果SD卡被挂载了 或者 是内置SD卡， 那么使用/mnt/sdcard 路径
        // 否则使用内置存储 /data/data 路径
        // 内置存储一般安装系统固件，多出来的一些空间可以安装应用软件
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    private static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public String getCacheDir() {
        return getCacheDir("");
    }

}
