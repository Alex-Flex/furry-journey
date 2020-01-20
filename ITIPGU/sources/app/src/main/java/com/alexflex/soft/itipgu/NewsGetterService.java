package com.alexflex.soft.itipgu;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;
import com.alexflex.soft.itipgu.logic.CommonMethods;
import com.alexflex.soft.itipgu.logic.MyApp;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.IOException;
import static android.os.Build.VERSION_CODES.O;

public class NewsGetterService extends Service {

    private long interval;
    private final String URL = "http://tehnikum.ucoz.ru/";
    private final String ACTIVITY_NAME = "SettingsActivity";
    private final String INTERVAL_IN_MINUTES = "update_time";
    private final String LAST_HEADING = "last_heading";
    private volatile String lastHeading; //при первом запуске сервиса записываем заголовок первой новости сюда
    private volatile String newHeading;  //обновлённый заголовок (если порядок записей на сайте изменён) помещается сюда
    private String newsText;  //текст новости, будет показан прямо в уведомлении
    private final String CHANNEL_ID = "iti_notifications";
    private NotificationManager manager;
    private NotificationChannel notificationChannel;
    private final int NOTIFICATION_ID = 13;
    private Thread task;
    private final String NOTIFICATIONS_ENABLED = "notifications_enabled";
    private final String problemWasShown = "problemShown";

    public NewsGetterService() { }

    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int start){
        if(!CommonMethods.isOnline(this)){
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
        }
        try {
            interval = 60000 * getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).getInt(INTERVAL_IN_MINUTES, 5);
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            firstLaunch(); //lastHeading настроен
            Thread.currentThread().join(5000);
            //обновление новостей в отдельном потоке
            Runnable refreshing = new Runnable() {
                @Override
                public void run() {
                    //почти вечный цикл
                    while (!(Thread.interrupted())) {
                        try {
                            if (!(CommonMethods.isOnline(MyApp.getContext()))) {
                                //если нет интернета, засыпаем на минуту
                                Thread.sleep(60_000);
                            } else {
                                refreshNews();
                                Thread.currentThread().join(5000);
                                if(newHeading.equals("")){
                                    if(getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).getInt(problemWasShown, 0) != 1) {
                                        getNotification(getResources().getString(R.string.server_not_available),
                                                getResources().getString(R.string.SNA_not_expanded),
                                                getResources().getString(R.string.SNA_expanded));
                                        getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).edit().putInt(problemWasShown, 1).apply();
                                    }
                                }
                                if (!(lastHeading.equals(newHeading))) {
                                    manager.notify(NOTIFICATION_ID, getNotification(newHeading, getResources().getString(R.string.updates_on_site),
                                            newsText));
                                    getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).edit().putString(LAST_HEADING, newHeading).apply();
                                }
                                Thread.sleep(interval);
                            }
                        } catch(InterruptedException e){
                            break;
                        } catch(NullPointerException ignored){ }
                    }
                }
            };
            task = new Thread(refreshing);
            task.start();
        } catch (InterruptedException ignored) {}

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).edit().putBoolean(NOTIFICATIONS_ENABLED, false).apply();
        getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).edit().putInt(problemWasShown, 1).apply();
        task.interrupt();
        manager.cancelAll();
        stopSelf();
    }

    //строим уведомление для показа
    public Notification getNotification(String heading, String textWhenNotExpanded, String textWhenExpanded){
        Notification.Builder builder;
        if(Build.VERSION.SDK_INT >= O)
            builder = new Notification.Builder(getApplicationContext(), CHANNEL_ID);
        else
            builder = new Notification.Builder(getApplicationContext());

        builder.setSmallIcon(R.drawable.notify)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.iti))
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 1,
                        new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle(heading)
                .setContentText(textWhenNotExpanded)
                .setStyle(new Notification.BigTextStyle().bigText(textWhenExpanded))
                .setColor(Color.CYAN);

        if(Build.VERSION.SDK_INT >= O) {
            notificationChannel = new NotificationChannel("id", "name", NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId("id");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                    .build());
        } else {
            builder.setDefaults(Notification.DEFAULT_ALL);
        }
        return builder.build();
    }

    //обновляем новости
    public void refreshNews(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(URL).get();
                    Elements titles = document.getElementsByClass("eTitle");
                    Elements texts = document.getElementsByClass("Message");
                    newsText = texts.get(0).text();
                    newHeading = titles.get(0).text();
                } catch (IOException ignored) {}
            }
        }).start();
    }

    public void firstLaunch(){
        lastHeading = getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).getString(LAST_HEADING, null);
        if(lastHeading != null){
            getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).edit().putString(LAST_HEADING, lastHeading).apply();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Document document = Jsoup.connect(URL).get();
                        Elements titles = document.getElementsByClass("eTitle");
                        lastHeading = titles.get(0).text();
                        getApplicationContext().getSharedPreferences(ACTIVITY_NAME, MODE_PRIVATE).edit().putString(LAST_HEADING, lastHeading).apply();
                    } catch (IOException ignored) { }
                }
            }).start();
        }
    }
}
