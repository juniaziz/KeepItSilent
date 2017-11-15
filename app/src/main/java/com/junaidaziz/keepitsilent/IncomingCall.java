package com.junaidaziz.keepitsilent;

import android.app.NotificationManager;
import android.content.*;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


/*
 * Created by junaidaziz on 11/13/17.
 */

public class IncomingCall extends android.content.BroadcastReceiver {

    Context context;
    static AudioManager audio;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        Log.d("BroadCast: ", " Recieved");

        toggleSoundMode();

        //        Intent background = new Intent(context, BackgroundService.class);
//        context.startService(background);
    }

    public void handleAudioManager(){


        int currentMode = audio.getRingerMode();

        Log.d("BroadCast: ", "currentmode " + currentMode);
        Log.d("BroadCast: ", "case 1: " + AudioManager.RINGER_MODE_NORMAL);
        Log.d("BroadCast: ", "case 2: " + AudioManager.RINGER_MODE_VIBRATE);

        switch (currentMode) {
            case AudioManager.RINGER_MODE_NORMAL:
                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Log.d("BroadCast: ", "case: RINGER MODE NORMAL");
                Toast.makeText(context, "Viration Activated", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.d("BroadCast: ", "case; RINGER MODE VIBRATE");
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                handleRingVolume();
                Toast.makeText(context, "Normal mode Activated", Toast.LENGTH_LONG).show();
                break;
        }

    }

    public void handleRingVolume(){
        audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
        audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
        audio.setStreamVolume(AudioManager.STREAM_SYSTEM, audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        audio.setStreamVolume(AudioManager.STREAM_ALARM, audio.getStreamMaxVolume(AudioManager.STREAM_ALARM), 0);
        Toast.makeText(context, "Max volume", Toast.LENGTH_LONG).show();
    }


    public void toggleSoundMode(){
        Log.d("BroadCast: ", "toggleSoundMode");

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            handleAudioManager();
        }
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            int currentMode = notificationManager.getCurrentInterruptionFilter();

            Log.d("BroadCast: ", "currentmode " + currentMode);
            Log.d("BroadCast: ", "case 1: " + NotificationManager.INTERRUPTION_FILTER_ALARMS);
            Log.d("BroadCast: ", "case 2: " + NotificationManager.INTERRUPTION_FILTER_ALL);
            Log.d("BroadCast: ", "case 3: " + NotificationManager.INTERRUPTION_FILTER_NONE);
            Log.d("BroadCast: ", "case 4: " + NotificationManager.INTERRUPTION_FILTER_PRIORITY);
            Log.d("BroadCast: ", "case 5: " + NotificationManager.INTERRUPTION_FILTER_UNKNOWN);

            switch (currentMode){
                case NotificationManager.INTERRUPTION_FILTER_ALL: {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
                    break;
                }
                case NotificationManager.INTERRUPTION_FILTER_UNKNOWN: {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                    handleRingVolume();
                    break;
                }
                case NotificationManager.INTERRUPTION_FILTER_ALARMS: {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                    handleRingVolume();
                    break;
                }
                case NotificationManager.INTERRUPTION_FILTER_NONE: {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                    handleRingVolume();
                    break;
                }
                case NotificationManager.INTERRUPTION_FILTER_PRIORITY: {
                    notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                    handleRingVolume();
                    break;
                }
            }


        }
    }
}
