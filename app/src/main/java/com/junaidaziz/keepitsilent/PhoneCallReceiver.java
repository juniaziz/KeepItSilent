package com.junaidaziz.keepitsilent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/*
 * Created by junaidaziz on 11/15/17.
 */

public class PhoneCallReceiver extends BroadcastReceiver {

    Context context;
    long elapsedTime;
    static MyPhoneStateListener myPhoneStateListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PhoneListener: ", "onReceive");
        this.context = context;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (myPhoneStateListener == null) {
            try {
                myPhoneStateListener = new MyPhoneStateListener();
                telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
            } catch (Exception e) {
                Log.d("PhoneListener: ", e + "");
            }
        }
    }

    private class MyPhoneStateListener extends PhoneStateListener{
        public void onCallStateChanged(int state, String incomingNumber){
            Log.d("PhoneListener: ", "onCallStateChanged " + state + " incomingNumber " + incomingNumber);
            if (state == 1){
//                String msg = "New Phone Call Event. Incomming Number : "+incomingNumber;
//                int duration = Toast.LENGTH_LONG;
//                Toast toast = Toast.makeText(context, msg, duration);
//                toast.show();
//                context.startService(new Intent(context, BackgroundService.class));
                Log.d("Service: ", "onCallStateChangeListener: state: " + state);

                Intent intentMyService = new Intent(context, TimerService.class);
                intentMyService.putExtra("incomingNumber", incomingNumber);
                context.startService(intentMyService);

                Log.d("PhoneListener: " , "service intent initiated");

            }
        }
    }
}


