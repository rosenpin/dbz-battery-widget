package com.tomer.dragon.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Serivce extends Service {
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
                // TODO Auto-generated method stub
                while (true) {
                    try {
                        Thread.sleep(1200);
                        buildUpdate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //REST OF CODE HERE//
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
            int devider = 100 / pics[index].length;
        } catch (Exception e) {
            pics = Resources.gt_pics;

        }
        int devider = 100 / pics[index].length;
        int transform = (int) (Resources.getBatteryLevel(getApplicationContext()) / devider);
        if (transform >= pics[index].length) {
            transform -= 1;
        }
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
                System.out.println(e);
            }
            remoteViews.setTextViewText(R.id.percent, "");
            remoteViews.setViewVisibility(R.id.percent, 0);
        }
        if (sharedPreferences.getBoolean("time", true)) {
            try {

                remoteViews.setTextViewTextSize(R.id.time, 1, 25);
            } catch (Exception e) {
                System.out.println(e);
            }
            remoteViews.setViewVisibility(R.id.time, 1);
            String time = new SimpleDateFormat("MM-dd hh:mm").format(new java.util.Date());
            remoteViews.setTextViewText(R.id.time, time);
        } else {
            try {
                remoteViews.setTextViewTextSize(R.id.time, 1, 0);
            } catch (Exception e) {
                System.out.println(e);
            }
            remoteViews.setTextViewText(R.id.time, "");
            remoteViews.setViewVisibility(R.id.time, 0);
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
