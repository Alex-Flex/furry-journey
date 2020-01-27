package com.alexflex.soft.itipgu.logic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alexflex.soft.itipgu.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CommonMethods {

    //сейчас вечернее время?
    public static boolean isNightHere(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        return (hours >= 17) || (hours < 8);
    }

    //Получаем время года
    public static int getQuarter(){
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        //нумерация месяцев начинается с 0 -- январь
        //и заканчивается 11 -- декабрь
        switch (month){
            //зима
            case 11:
            case 0:
            case 1:
                return 1;

            //весна
            case 2:
            case 3:
            case 4:
                return 2;

            //лето
            case 5:
            case 6:
            case 7:
                return 3;

            //осень
            case 8:
            case 9:
            case 10:
                return 4;
        }
        return 0;
    }

    //проверяем состояние сети
    public static boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                assert cm != null;
                NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                if (wifiInfo != null && wifiInfo.isConnected()) {
                    return true;
                }
                wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                if (wifiInfo != null && wifiInfo.isConnected()) {
                    return true;
                }
                wifiInfo = cm.getActiveNetworkInfo();
                return wifiInfo != null && wifiInfo.isConnected();
            } catch (NullPointerException ignored) {
                return false;
            }
    }

    static void whenImOffline(final Context context, final ViewGroup mainView, final NewsParserTask task) {
        mainView.removeAllViews();
        task.cancel(true);
        LinearLayout l = new LinearLayout(context);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        l.setGravity(Gravity.CENTER);
        TextView tw = new TextView(context);
        tw.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        tw.setText(R.string.no_connection);
        tw.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainView.removeAllViews();
                new NewsParserTask(context, mainView).execute();
            }
        });

        tw.setTextSize(35);

        l.addView(tw);
        mainView.addView(l);
    }

    static void generateLoadingScreen(Context context, ViewGroup layout){
        ProgressBar bar = new ProgressBar(context);
        LinearLayout l = new LinearLayout(context);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        l.setGravity(Gravity.CENTER);
        l.addView(bar);
        layout.addView(l);
    }

    static Drawable drawableFromURL(URL url) {
        try {
            InputStream is = url.openStream();
            return Drawable.createFromStream(is, "src");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //открываем что-то типа "поделиться в соцсетях"
    public static void openInOtherApp(Activity activity, String appName, String url){
        final String VK_APP_PACKAGE_NAME = "com.vkontakte.android";
        final String TELEGA_APP_PACKAGE_NAME = "org.telegram.messenger";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo.isEmpty()) return;

        for (ResolveInfo info: resInfo) {
            if (info.activityInfo == null) continue;
            switch (appName) {
                case "VK":
                    if (info.activityInfo.packageName.equals(VK_APP_PACKAGE_NAME)) {
                        intent.setPackage(info.activityInfo.packageName);
                        break;
                    } else continue;
                case "TG":
                    if (info.activityInfo.packageName.equals(TELEGA_APP_PACKAGE_NAME)) {
                        intent.setPackage(info.activityInfo.packageName);
                        break;
                    }
            }
        }
        activity.startActivity(intent);
    }

    //копирование в буфер обмена
    public static void copyToClipboard(Context context, String label, String text){
        ClipboardManager clipboardManager = (ClipboardManager) context
                .getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text));
    }

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

    //проверяем, есть ли google play services на целевом девайсе
    public static boolean checkIfGooglePlayServicesAvailable(Context context){
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        return resultCode != ConnectionResult.SUCCESS;
    }

    public static boolean isUserRealGangster(Context context){
        try {
            context.getPackageManager()
                    .getPackageInfo("com.rockstargames.gtasa", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
