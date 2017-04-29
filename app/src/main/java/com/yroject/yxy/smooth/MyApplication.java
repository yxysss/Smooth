package com.yroject.yxy.smooth;

import android.app.Application;
import android.os.Bundle;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Y.X.Y on 2017/4/22 0022.
 */
public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
