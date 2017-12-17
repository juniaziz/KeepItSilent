package com.junaidaziz.keepitsilent;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/*
 * Created by root on 12/17/17.
 */

public class TimerService extends Service {

    private Timer mTimer;
    private Handler mHandler = new Handler();

    private static long serviceStartTime;
    private static long serviceCurrentTime;
    private static long serviceElapsedTime = 0;
    private static final int TIMER_INTERVAL = 5000;
    private static final int TIMER_DELAY = 0;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("TIMER:: ", "onCreate");


        if (mTimer != null)
            mTimer = null;
//
        serviceStartTime = System.currentTimeMillis();
        Log.e("TIMER:: ", "serviceStartTime: " + serviceStartTime);
        mTimer = new Timer();
//
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(getApplicationContext(), "serviceStartTime:: " + serviceStartTime, Toast.LENGTH_LONG).show();
//            }
//        });
//
        mTimer.scheduleAtFixedRate(new DisplayToastTimerTask(), TIMER_DELAY, TIMER_INTERVAL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TIMER:: ", "onStartCommand");

        serviceCurrentTime = System.currentTimeMillis();
        Log.e("TIMER:: ", "serviceCurrentTime: " + serviceCurrentTime);
        serviceElapsedTime = serviceCurrentTime - serviceStartTime;
        Log.e("TIMER:: ", "serviceElapsedTime: " + serviceElapsedTime);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Elapsed Time: " + serviceElapsedTime, Toast.LENGTH_LONG).show();
            }
        });

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TIMER:: ", "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.e("TIMER:: ", "onDestroy");
    }

    private class DisplayToastTimerTask extends TimerTask {

        @Override
        public void run() {
            Log.e("TIMER:: ", "timer run()");
            serviceCurrentTime = System.currentTimeMillis();
            Log.e("TIMER:: ", "currentTime " + serviceCurrentTime);
            serviceElapsedTime = serviceCurrentTime - serviceStartTime;
            Log.e("TIMER:: ", "timer elapsedTime: " + serviceElapsedTime);
            if (serviceElapsedTime >= 30000){
                Log.e("TIMER:: ", "SERVICE ENDED");
                mTimer.cancel();
                stopSelf();
            }
        }
    }
}
