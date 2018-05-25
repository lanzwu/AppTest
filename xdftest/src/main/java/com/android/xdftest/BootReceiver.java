package com.android.xdftest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhouxiangyu on 2018/5/11.
 */

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("zhouxiangyu", "Intent : "+intent.getAction());
        //context.startActivity(new Intent("ShowPictureTest"));
    }
}
