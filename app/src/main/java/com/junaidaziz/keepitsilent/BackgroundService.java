package com.junaidaziz.keepitsilent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/*
 * Created by junaidaziz on 11/13/17.
 */

public class BackgroundService extends Service {

    private Boolean isRunning;
    private Context context;
    private Thread backgroundThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning){
            this.isRunning = true;
            this.backgroundThread.start();
        }

        return START_STICKY;
    }
}
