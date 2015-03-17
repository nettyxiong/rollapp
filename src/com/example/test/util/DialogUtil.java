package com.example.test.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.example.test.R;

/**
 * Created by xs on 2014/12/13.
 */
public class DialogUtil {
    public static void showDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void exitDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.tips))
                .setMessage(activity.getString(R.string.if_exit))
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        activity.setResult(activity.RESULT_OK);
                        activity.finish();
                    }
                })
                .setNegativeButton(activity.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    public static void logoutDialog(final Activity activity) {
        new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.tips))
                .setMessage(activity.getString(R.string.if_logout))
                .setPositiveButton(activity.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        activity.setResult(activity.RESULT_OK);
                        activity.finish();
                    }
                })
                .setNegativeButton(activity.getString(R.string.cancle), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }
}
