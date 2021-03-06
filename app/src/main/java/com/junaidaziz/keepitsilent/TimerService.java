package com.junaidaziz.keepitsilent;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Created by root on 12/17/17.
 */

public class TimerService extends Service {

    private Timer mTimer;
    private Timer restorationTimer;
    private Handler mHandler = new Handler();

    private static final String SOUND_BROADCAST_ACTION = "com.junaidaziz.www";
    private static final int RESTORATION_TIME = 50000; //1200000;  //20 minutes
    private static final int ONE_HOUR_WAIT_TIME = 200000; //3600000; //60 minutes

    private static String incomingNumber;
    private static ArrayList<String> missedNumbersArray = new ArrayList<>();
    private static int missedCallCounter = 0;
    private static Intent intent2;
    private static boolean soundModeChanged = false;
    private ToggleSoundMode toggleSoundMode;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TIMER:: ", "onCreate");

        IntentFilter intentFilter = new IntentFilter(SOUND_BROADCAST_ACTION);
        toggleSoundMode = new ToggleSoundMode();
        registerReceiver(toggleSoundMode, intentFilter);

        soundModeChanged = false;
        intent2 = new Intent();
        intent2.setAction(SOUND_BROADCAST_ACTION);

        if (mTimer != null)
            mTimer = null;

        if (restorationTimer != null) {
            restorationTimer = null;
        }

        mTimer = new Timer();
        restorationTimer = new Timer();
        mTimer.schedule(new TerminateService(), ONE_HOUR_WAIT_TIME);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("TIMER:: ", "SERVICE CALLED");
        missedCallCounter = 0;

        incomingNumber = intent.getStringExtra("incomingNumber");
        Log.e("incomingNumber: ", "" + incomingNumber);

        missedNumbersArray.add(incomingNumber);

        for (int i = 0; i < missedNumbersArray.size(); i++){
            Log.e("TIMER:: ", "missedNumbersArray.get(" + i + "): " + missedNumbersArray.get(i));
        }

        if (missedNumbersArray.contains(incomingNumber)){
            Log.e("TIMER:: ", "if (missedNumbersArray.contains(incomingNumber))");
            for (int i = 0; i < missedNumbersArray.size(); i++){
                if (incomingNumber.equals(missedNumbersArray.get(i))){
//                    Log.e("TIMER:: ", "if (incomingNumber.equals(missedNumbersArray.get(i)))");
                    missedCallCounter++;
                }
            }
            Log.e("TIMER:: ", "missedCallCounter = " + missedCallCounter + " for number: " + incomingNumber);

            if (missedCallCounter >= 3 && !soundModeChanged) {
                Log.e("TIMER:: ", "Fire ToggleSoundMode Intent");
                intent2.putExtra("intentType", "MAX VOLUME");
                sendBroadcast(intent2);
                soundModeChanged = true;
                Log.e("TIMER:: ", "ToggleSoundMode intent initiated");
                Toast.makeText(this, "Sound Mode Changed: Max Volume", Toast.LENGTH_LONG).show();
                restorationTimer.schedule(new RestorationTimer(), RESTORATION_TIME);
            }
        } else {
            Log.e("TIMER:: ", "incomingNumber not found in missedNumbersArray");
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "MissedCallCounter: " + missedCallCounter, Toast.LENGTH_LONG).show();
            }
        });
        return Service.START_REDELIVER_INTENT;
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
        unregisterReceiver(toggleSoundMode);
        Log.e("TIMER:: ", "onDestroy");
    }

    private class TerminateService extends TimerTask {
        @Override
        public void run() {
                if (soundModeChanged){
                    intent2.putExtra("intentType", "RESTORE ORIGINAL");
                    sendBroadcast(intent2);
                    soundModeChanged = false;
                }
                missedNumbersArray.clear();
                missedCallCounter = 0;
                Log.e("TIMER:: ", "SERVICE ENDED");
                stopSelf();
        }
    }

    private class RestorationTimer extends TimerTask {
        @Override
        public void run() {
            intent2.putExtra("intentType", "RESTORE ORIGINAL");
            sendBroadcast(intent2);
            soundModeChanged= false;
            Log.e("TIMER:: ", "RESTORATION DONE");
        }
    }
}
