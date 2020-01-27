package com.alexflex.soft.itipgu.logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.alexflex.soft.itipgu.NewsGetterService;

//после перезагрузки девайса всё будет ОК, прослушка уведов будет работать :)
public class BootFinishedReceiver extends BroadcastReceiver {

    private final String NOTIFICATIONS_ENABLED = "notifications_enabled";
    private final String ACTIVITY_NAME = "SettingsActivity";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(context.getSharedPreferences(ACTIVITY_NAME, Context.MODE_PRIVATE).getBoolean(NOTIFICATIONS_ENABLED, false)){
            intent = new Intent(context, NewsGetterService.class);
            context.startService(intent);
        }
    }
}
