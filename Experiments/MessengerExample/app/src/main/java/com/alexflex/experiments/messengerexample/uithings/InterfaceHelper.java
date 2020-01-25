package com.alexflex.experiments.messengerexample.uithings;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

class InterfaceHelper {

    //создание диалогов типа "вы уверены?"
    public static AlertDialog createAlertDialog(@NonNull AlertDialog.Builder builder,
                                                  Drawable icon,
                                                  @NonNull String title,
                                                  @NonNull String message,
                                                  String negativeButtonText,
                                                  DialogInterface.OnClickListener negativeListener,
                                                  String neutralButtonText,
                                                  DialogInterface.OnClickListener neutralListener,
                                                  String positiveButtonText,
                                                  DialogInterface.OnClickListener positiveListener,
                                                  boolean isCancelable,
                                                  DialogInterface.OnCancelListener cancelListener) {
        builder.setTitle(title);
        if (icon != null)
            builder.setIcon(icon);
        builder.setMessage(message);
        if (negativeButtonText != null){
            builder.setNegativeButton(negativeButtonText, negativeListener);
        }
        if (neutralButtonText != null){
            builder.setNeutralButton(neutralButtonText, neutralListener);
        }
        if (positiveButtonText != null){
            builder.setPositiveButton(positiveButtonText, positiveListener);
        }
        builder.setCancelable(isCancelable);
        builder.setOnCancelListener(cancelListener);
        return builder.create();
    }

    public static Notification createNotification(NotificationCompat.Builder builder,
                                                  Context context,
                                                  String channelId,
                                                  @DrawableRes int smallIcon,
                                                  boolean doShowWhen,
                                                  long when,
                                                  @DrawableRes int largeIcon,
                                                  String contentTitle,
                                                  String contentText,
                                                  boolean autoCancel,
                                                  PendingIntent intent) {
        builder.setSmallIcon(smallIcon);
        if (doShowWhen || when>0) {
            builder.setShowWhen(true);
            builder.setWhen(when);
        }
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon));
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setAutoCancel(autoCancel);
        builder.setContentIntent(intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(channelId);
        }
        return builder.build();
    }

    //проверяем, есть ли google play services на целевом девайсе
    public static boolean checkIfGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode == ConnectionResult.SUCCESS;
    }
}
