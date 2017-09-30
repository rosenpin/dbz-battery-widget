package com.tomer.dbz.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.widget.RemoteViews;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdaterService extends Service {
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1200);
                        buildUpdate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void buildUpdate() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_layout);
        int index = sharedPreferences.getInt("selected", 0);
        int[][] pics;
        if (GT()) {
            pics = Resources.gt_pics;
        } else {
            pics = Resources.pics;
        }
        try {
            int divider = 100 / pics[index].length;
        } catch (Exception e) {
            pics = Resources.gt_pics;
        }
        float divider = 100 / pics[index].length;
        int min_battery = sharedPreferences.getInt("min_battery", 0);
        if (min_battery > 0) {
            divider += ((float) min_battery)/ 10f;
        }
        int transform = (int) (Resources.getBatteryLevel(getApplicationContext()) / divider);
        if (sharedPreferences.getBoolean("percent", true)) {
            try {
                remoteViews.setTextViewTextSize(R.id.percent, 1, 25);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            remoteViews.setViewVisibility(R.id.percent, 1);
            remoteViews.setTextViewText(R.id.percent, String.valueOf((int) (Resources.getBatteryLevel(getApplicationContext())) + "%"));
        } else {
            try {
                remoteViews.setTextViewTextSize(R.id.percent, 1, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            remoteViews.setTextViewText(R.id.percent, "");
            remoteViews.setViewVisibility(R.id.percent, 0);
        }
        if (sharedPreferences.getBoolean("time", true)) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Intent intent = new Intent(AlarmClock.ACTION_SHOW_ALARMS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
                remoteViews.setOnClickPendingIntent(R.id.time, pendingIntent);
            }
            try {

                remoteViews.setTextViewTextSize(R.id.time, 1, 25);
            } catch (Exception e) {
                e.printStackTrace();
            }
            remoteViews.setViewVisibility(R.id.time, 1);
            String time = SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(new Date());
            remoteViews.setTextViewText(R.id.time, time);
        } else {
            try {
                remoteViews.setTextViewTextSize(R.id.time, 1, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            remoteViews.setTextViewText(R.id.time, "");
            remoteViews.setViewVisibility(R.id.time, 0);
        }
        if (sharedPreferences.getBoolean("whitefont", false)) {
            remoteViews.setTextColor(R.id.time, Color.WHITE);
            remoteViews.setTextColor(R.id.percent, Color.WHITE);
        } else {
            remoteViews.setTextColor(R.id.time, Color.BLACK);
            remoteViews.setTextColor(R.id.percent, Color.BLACK);
        }
        remoteViews.setImageViewResource(R.id.update, pics[index][transform]);

        // Register an onClickListener
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent configPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.update, configPendingIntent);

        // Push update for this widget to the home screen
        ComponentName thisWidget = new ComponentName(this, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        manager.updateAppWidget(thisWidget, remoteViews);
    }

    private boolean GT() {
        return sharedPreferences.getBoolean("gt", false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
