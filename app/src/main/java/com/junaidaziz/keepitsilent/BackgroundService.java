package com.junaidaziz.keepitsilent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
        Log.d("Service: ", "onBind");

        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);

        Log.d("Service: ", "onCreate");
    }

    private Runnable myTask = new Runnable() {
        @Override
        public void run() {

            Log.d("Service: ", "myTask");
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        Log.d("Service: ", "onDestroy");

        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!this.isRunning){
            this.isRunning = true;
            this.backgroundThread.start();

            Log.d("Service: ", "onStartCommand");

            SystemClock.sleep(1000);

            Intent intent2 = new Intent();
            intent2.setAction("ax.androidexample.mybroadcast");
            sendBroadcast(intent2);
            Log.d("BroadCast: ", "intent initiated");



        }



        return START_REDELIVER_INTENT;
    }
}
