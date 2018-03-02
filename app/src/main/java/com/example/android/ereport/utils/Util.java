package com.example.android.ereport.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by krogers on 2/7/18.
 */

public class Util {
    //holds the IP ADDRESS or URL of the server
    public static final String SERVER_URL = "http://192.168.8.101/";
    //variables for alarm activity
    private static AlarmManager alarmManager;
    private static PendingIntent pendingIntent;

    //start the alarm
    public static void startAlarm(Context context) {
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Toast.makeText(context.getApplicationContext(), "Setting alarm", Toast.LENGTH_LONG).show();
//        Intent intent = new Intent(context.getApplicationContext(), LocationAlarm.class);
//        pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intent, 0);
//        alarmManager.setRepeating(AlarmManager.RTC, SystemClock.elapsedRealtime() + 5000, 5000, pendingIntent);
    }

    //stop the alarm
    public static void stopAlarm(Context context) {
        alarmManager.cancel(pendingIntent);
        Toast.makeText(context.getApplicationContext(), "Stopping alarm", Toast.LENGTH_LONG).show();
    }

}