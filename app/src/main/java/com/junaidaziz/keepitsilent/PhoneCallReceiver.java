package com.junaidaziz.keepitsilent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/*
 * Created by junaidaziz on 11/15/17.
 */

public class PhoneCallReceiver extends BroadcastReceiver {

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("PhoneListener: ", "intent1 recieved");

        this.context = context;

        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            MyPhoneStateListener PhoneListener = new MyPhoneStateListener();

            telephonyManager.listen(PhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        } catch (Exception e) {
            Log.d("PhoneListener: ", e + "");
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
                context.startService(new Intent(context, BackgroundService.class));
                Log.d("Service: ", "onCallStateChangeListener");


            }
        }
    }
}


