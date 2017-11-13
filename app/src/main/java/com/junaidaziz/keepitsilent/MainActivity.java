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

public class MainActivity extends AppCompatActivity {

    Context context;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_SETTINGS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;


//        Intent incomingCall = new Intent(this.context, BroadcastReceiver.class);
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

            checkForPermission();

            if (permissionGranted()) {
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

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_SETTINGS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("Permission: ", "GRANTED");
                    return;
                } else {
                    Log.d("Permission: ", "NOT GRANTED");
                    Toast.makeText(MainActivity.this, "Permission Not granted", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void checkForPermission(){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_DENIED) {
            Log.d("Permission: ", "at if statement");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_SETTINGS},
                    MY_PERMISSIONS_REQUEST_WRITE_SETTINGS);

        } else {
            Log.d("Permission: ", "at else statement");
            return;
        }
    }

    public boolean permissionGranted() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }




}
