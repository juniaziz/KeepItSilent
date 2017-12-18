package com.junaidaziz.keepitsilent;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.*;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;


/*
 * Created by junaidaziz on 11/13/17.
 */

public class ToggleSoundMode extends BroadcastReceiver {

    Context context;
    static AudioManager audio;
    static String intentType;
    private static final String TAG = "ToggleSoundMode";
    private static final String SOUND_BROADCAST_ACTION = "com.junaidaziz.www";
    NotificationManager notificationManager;

    private static int originalMode;
    private static int originalInterruptionFilter;
    private static int originalVolumeRing;
    private static int originalVolumeSystem;
    private static int originalVolumeNotification;
    private static int originalVolumeMusic;
    private static int originalVolumeAlarm;

    private static boolean soundModeChanged = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(SOUND_BROADCAST_ACTION)){
            this.context = context;
            audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Log.d("BroadCast: ", " Recieved");
            intentType = intent.getStringExtra("intentType");
            Log.e(TAG, "intentType: " + intentType);
            toggleSoundMode();
        }
    }

    public void toggleSoundMode(){
        if (intentType.equalsIgnoreCase("MAX VOLUME")){
            Log.e(TAG, "if (intentType.equalsIgnoreCase(\"MAX VOLUME\"))");

            storeOriginalState();

            //if the OS version is lower than Nougat
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                setVolumeMax();
            }       //if the OS version is Nougat or above
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                if (turnOffDoNotDisturbMode()) {
                    setVolumeMax();
                } else
                    Log.e(TAG, "turnOffDoNotDisturbMode returned false");
            }
        } else if (intentType.equalsIgnoreCase("RESTORE ORIGINAL")){
            Log.e(TAG, "else if (intentType.equalsIgnoreCase(\"RESTORE ORIGINAL\"))");

            if (soundModeChanged) {
                setOriginalState();
            } else {
                Log.e(TAG, "soundModeChanged: " + soundModeChanged);
            }

        }

    }

    @TargetApi(24)
    private boolean turnOffDoNotDisturbMode(){
        notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
        return true;
    }

    private void storeOriginalState(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            originalInterruptionFilter = notificationManager.getCurrentInterruptionFilter();
        }
        originalMode = audio.getRingerMode();
        originalVolumeNotification = audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        originalVolumeAlarm = audio.getStreamVolume(AudioManager.STREAM_ALARM);
        originalVolumeMusic = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        originalVolumeRing = audio.getStreamVolume(AudioManager.STREAM_RING);
        originalVolumeSystem = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);

        //Just for debugging
        Log.d("BroadCast: ", "originalInterruptionFilter " + originalInterruptionFilter);
        Log.d("BroadCast: ", "originalMode " + originalMode);
        Log.d("BroadCast: ", "originalVolumeNotification " + originalVolumeNotification);
        Log.d("BroadCast: ", "originalVolumeAlarm " + originalVolumeAlarm);
        Log.d("BroadCast: ", "originalVolumeMusic " + originalVolumeMusic);
        Log.d("BroadCast: ", "originalVolumeRing " + originalVolumeRing);
        Log.d("BroadCast: ", "originalVolumeSystem " + originalVolumeSystem);
    }

    public void setOriginalState(){

        soundModeChanged = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager.setInterruptionFilter(originalInterruptionFilter);
        }
        audio.setRingerMode(originalMode);
        audio.setStreamVolume(AudioManager.STREAM_RING, originalVolumeRing, 0);
        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, originalVolumeNotification, 0);
        audio.setStreamVolume(AudioManager.STREAM_SYSTEM, originalVolumeSystem, 0);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolumeMusic, 0);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, originalVolumeAlarm, 0);
    }

    public void setVolumeMax(){
        audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
        audio.setStreamVolume(AudioManager.STREAM_SYSTEM, audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, audio.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);

        soundModeChanged = true;

       // Toast.makeText(context, "Max volume", Toast.LENGTH_LONG).show();
    }
}


//    public void handleAudioManager(){
//
//        //This part of code is pretty self explanatory.
//
//        int currentMode = audio.getRingerMode();
//
//        Log.d("BroadCast: ", "currentmode " + currentMode);
//        Log.d("BroadCast: ", "case 1: " + AudioManager.RINGER_MODE_NORMAL);
//        Log.d("BroadCast: ", "case 2: " + AudioManager.RINGER_MODE_VIBRATE);
//
//        switch (currentMode) {
//            case AudioManager.RINGER_MODE_NORMAL: {
//                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                Log.d("BroadCast: ", "case: RINGER MODE NORMAL");
//                Toast.makeText(context, "Vibration Activated", Toast.LENGTH_LONG).show();
//                break;
//            }
//            case AudioManager.RINGER_MODE_VIBRATE: {
//                Log.d("BroadCast: ", "case; RINGER MODE VIBRATE");
//                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                setVolumeMax();
//                Toast.makeText(context, "Normal mode Activated", Toast.LENGTH_LONG).show();
//                break;
//            }
//            case AudioManager.RINGER_MODE_SILENT: {
//                Log.d("BroadCast: ", "case; RINGER MODE SILENT");
//                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
//                setVolumeMax();
//                Toast.makeText(context, "Normal mode Activated", Toast.LENGTH_LONG).show();
//                break;
//            }
//        }
//    }

//Make the change to the notification Interruption filter First....then change the Audio Settings
//        switch (currentMode){
//            case NotificationManager.INTERRUPTION_FILTER_ALL: {
//                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);  //Toggle On the Do not disturb Mode
//                break;
//            }
//            case NotificationManager.INTERRUPTION_FILTER_UNKNOWN: {
//                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);  //Toggle the normal mode
//                setVolumeMax(); //With the normal interruption filter tuned on, we only need to handle the volumes.
//                break;
//            }
//            case NotificationManager.INTERRUPTION_FILTER_ALARMS: {
//                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
//                setVolumeMax();
//                break;
//            }
//            case NotificationManager.INTERRUPTION_FILTER_NONE: {
//                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
//                setVolumeMax();
//                break;
//            }
//            case NotificationManager.INTERRUPTION_FILTER_PRIORITY: {
//                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
//                setVolumeMax();
//                break;
//            }
//        }

