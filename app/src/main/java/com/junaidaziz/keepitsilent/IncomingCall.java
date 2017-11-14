package com.junaidaziz.keepitsilent;

import android.content.*;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;


/*
 * Created by junaidaziz on 11/13/17.
 */

public class IncomingCall extends android.content.BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;

        Log.d("BroadCast: ", " Recieved");

        toggleSoundMode();

        //        Intent background = new Intent(context, BackgroundService.class);
//        context.startService(background);
    }


    public void toggleSoundMode(){
        Log.d("Permission: ", "Happy Jingles");

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int currentMode = audio.getRingerMode();

        switch (currentMode){
            case AudioManager.RINGER_MODE_NORMAL:
                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Toast.makeText(context, "Viration Activated", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                audio.setStreamVolume(AudioManager.STREAM_RING, audio.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
                audio.setStreamVolume(AudioManager.STREAM_NOTIFICATION, audio.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
                audio.setStreamVolume(AudioManager.STREAM_SYSTEM, audio.getStreamMaxVolume(AudioManager.STREAM_SYSTEM), 0);
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);

                Toast.makeText(context, "Normal mode Activated", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
