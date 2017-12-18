package com.yaxon.frameWork.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import com.yaxon.frameWork.common.ActivityController;

/**
 * Created by GJP on 2017/9/7.
 */
public class ForceOfflineReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Warning");
        builder.setMessage("You are force to be offline. Please try to login again");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityController.finishAll();
                Intent intent = new Intent();//重新启动的Activity
                context.startActivity(intent);
            }
        });
    }
}
