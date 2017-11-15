package com.junaidaziz.keepitsilent;

import android.Manifest;
import android.app.NotificationManager;
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
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import android.media.AudioManager;

public class MainActivity extends AppCompatActivity {

    Context context;
    NotificationManager notificationManager;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 101;
    static boolean audioPermission =false;
    static boolean readPhonePermission =false;
    static Button soundBtn;
    static Button readCalls;
    static Button audio;
    static Button notificationsBtn;
    static TextView btn_textView;
    static TextView step_1_textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = this;
        Log.d("Activity: ", "onCreate()");

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//        audioPermission = checkForAudioPermission();
//        readPhonePermission = checkForReadPhonePermission();

//        Intent incomingCall = new Intent(this.context, IncomingCall.class);
//
//        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, incomingCall, PendingIntent.FLAG_NO_CREATE) != null);
//        if (alarmRunning == false){
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, incomingCall, 0);
//            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1800000, pendingIntent);
//        }



        soundBtn = findViewById(R.id.soundBtn);
        readCalls = findViewById(R.id.read_calls_permission);
        audio = findViewById(R.id.audio_permission_Btn);
        btn_textView = findViewById(R.id.btn_text);
        step_1_textView = findViewById(R.id.step_1_textview);
        notificationsBtn = findViewById(R.id.notification_Btn);


    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Activity: ", "onResume()");

        readCalls.setEnabled(false);
        readCalls.setText(R.string.permission_granted);
        audio.setEnabled(false);
        audio.setText(R.string.permission_granted);
        soundBtn.setEnabled(false);
        notificationsBtn.setEnabled(false);
        notificationsBtn.setText(R.string.permission_granted);


        if (!checkForAudioPermission()){
            audio.setEnabled(true);
            audio.setText(R.string.audio);
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askForAudioPermission(context);
                }
            });
        }

        if (!checkForReadPhonePermission()){
            readCalls.setEnabled(true);
            readCalls.setText(R.string.read_calls);
            readCalls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askForPhonePermission();
                }
            });
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!checkForNotificationsPermission()){
                notificationsBtn.setEnabled(true);
                notificationsBtn.setText(R.string.notifications);
                notificationsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askForNotificationsPermission();
                    }
                });
                Toast.makeText(context, "Notification Access Asked", Toast.LENGTH_LONG).show();
            }  else {
                Toast.makeText(context, "Notification Access Granted", Toast.LENGTH_LONG).show();
            }
        }

        if (checkForPermissions()){
            soundBtn.setEnabled(true);
            soundBtn.setOnClickListener(onSoundBtnListener);
            btn_textView.setVisibility(View.INVISIBLE);
            step_1_textView.setText(R.string.step_1_done);
        }
    }

    Button.OnClickListener onSoundBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                try {
                    //toggleSoundMode();
                    Intent intent = new Intent();
                    intent.setAction("ax.androidexample.mybroadcast");
                    sendBroadcast(intent);
                    Log.d("BroadCast: ", "intent initiated");



                } catch (Exception e) {
                    Log.d("toggle sound", "fail at button, reason:", e);
                }
        }
    };

    public boolean checkForPermissions(){
            if (checkForAudioPermission() && checkForReadPhonePermission() && checkForNotificationsPermission()){
                return true;
            }  else {
                return false;
            }
    }


    public boolean checkForAudioPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!Settings.System.canWrite(context)){
                Log.d("Permission: ", "at API 23+ statement");
               return false;
            } else if (Settings.System.canWrite(context)) {
                Log.d("Permission: ", "Audio Granted");
                return true;
            }
        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED){
            Log.d("Permission: ", "Audio Granted");
            return true;
        }
        return false;
    }

    public void askForAudioPermission(final Context c){
        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + c.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        readPhonePermission = true;

                        if (checkForReadPhonePermission() && checkForAudioPermission()){
                            soundBtn.setEnabled(true);
                        }

                    } catch (Exception e) {
                        Log.d("Copying Database", "fail at menu location, reason:", e);
                    }
                } else {
                    Toast.makeText(context, "Permission Not granted", Toast.LENGTH_LONG).show();
                }
            }
            case Intent.FLAG_ACTIVITY_NEW_TASK: {

            }
        }
    }

    public boolean checkForReadPhonePermission(){

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(context, "READ PHONE STATE PERMISSION GRANTED", Toast.LENGTH_LONG).show();
            Log.d("Permission: ", "Read Phone Granted");
            return true;
        } else {
            return false;
        }

    }

    public void askForPhonePermission(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_PHONE_STATE},
                MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
    }

    public boolean checkForNotificationsPermission(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && notificationManager.isNotificationPolicyAccessGranted()) {
            Log.d("Permission: ", "Notifications Granted");
            return true;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        } else {
            return false;
        }
    }

    public void askForNotificationsPermission(){
        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
        startActivity(intent);
    }




}
