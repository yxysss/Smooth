package library.smooth;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Y.X.Y on 2017/4/22 0022.
 */
public class DownloadThread implements Runnable{

    // public Object object = new Object();

    // public boolean suspend = false;

    private URL url = null;

    private File file;

    private int progress;

    private String filename;

    private boolean isfinished = false;

    private Handler sHandler;

    public DownloadThread(File file, URL url, int progress, String filename, Handler sHandler) {
        this.file = file;
        this.url = url;
        this.progress = progress;
        this.filename = filename;
        this.sHandler = sHandler;
    }

    public void run() {
        Log.d("DownloadThread", "Reach");
        if (url == null) return ;
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(8*1000);
            connection.setConnectTimeout(8*1000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("range", "bytes="+ progress + "-");
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            int filelength = connection.getContentLength();
            String type = connection.getContentType();
            Log.d("ContentType", type);
            BufferedInputStream bin = new BufferedInputStream(connection.getInputStream());
            FileOutputStream fout = new FileOutputStream(file, true);
            byte[] bytes = new byte[10000024];
            int length;
            int totallength = 0;
            while( (length = bin.read(bytes, 0, bytes.length)) != -1) {

                totallength += length;
                fout.write(bytes, 0 ,length);

                Log.d("DownloadThread", "progress : " + totallength + "/" + filelength);
            }
            isfinished = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putString("filename", filename);
            bundle.putBoolean("isfinished", isfinished);
            message.setData(bundle);
            sHandler.sendMessage(message);
        }
    }
}
