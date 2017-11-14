package com.junaidaziz.keepitsilent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.content.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;
import android.media.AudioManager;

public class MainActivity extends AppCompatActivity {

    Context context;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 101;
    boolean audioPermission;
    boolean readPhonePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;

        audioPermission = checkForAudioPermission();
        readPhonePermission = checkForReadPhonePermission();

//        Intent incomingCall = new Intent(this.context, IncomingCall.class);
//
//        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, incomingCall, PendingIntent.FLAG_NO_CREATE) != null);
//        if (alarmRunning == false){
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, incomingCall, 0);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1800000, pendingIntent);
//        }

        Button soundBtn = findViewById(R.id.soundBtn);

        soundBtn.setOnClickListener(onSoundBtnListener);

    }

    Button.OnClickListener onSoundBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (checkForPermissions()) {
                try {
                    toggleSoundMode();
                } catch (Exception e) {
                    Log.d("toggle sound", "fail at button, reason:", e);
                }
            } else {
                Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_LONG).show();
            }

        }
    };

    public void toggleSoundMode(){
            Log.d("Permission: ", "Happy Jingles");

        AudioManager audio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        int currentMode = audio.getRingerMode();

        switch (currentMode){
            case AudioManager.RINGER_MODE_NORMAL:
                audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                Toast.makeText(context, "Viration Activated", Toast.LENGTH_LONG).show();
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                Toast.makeText(context, "Normal mode Activated", Toast.LENGTH_LONG).show();
                break;
        }



    }

    public boolean checkForPermissions(){

            if (audioPermission && readPhonePermission){
                return true;
            } else {
                return false;
            }


    }

    public boolean checkForAudioPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.System.canWrite(context)){
                Log.d("Permission: ", "at API 23+ statement");
                showPermissionDialog(context);
            } else if (Settings.System.canWrite(context)) {
                Log.d("Permission: ", "Granted");
                return true;
            }
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
            return true;
        }


        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        readPhonePermission = true;
                    } catch (Exception e) {
                        Log.d("Copying Database", "fail at menu location, reason:", e);
                    }
                } else {
                    Toast.makeText(AddAmountActivity.this, "Permission Not granted", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public boolean checkForReadPhonePermission(){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "READ PHONE STATE PERMISSION GRANTED", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        return false;
    }

    public void showPermissionDialog(final Context c){
        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + c.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



}
