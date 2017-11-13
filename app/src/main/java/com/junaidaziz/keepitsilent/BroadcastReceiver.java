package com.junaidaziz.keepitsilent;

import android.content.*;


/**
 * Created by junaidaziz on 11/13/17.
 */

public class BroadcastReceiver extends android.content.BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
    }
}
