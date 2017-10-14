package roeevakrat.betterme;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 12/10/2017.
 */

public class NotificationsAlarmSetter {

    private Context mainScreenContext;

    //database
    private SharedPreferences appMap;
    private SharedPreferences.Editor appMapEditor;

    private PendingIntent pendingNotificationIntent;
    private AlarmManager notificationAlarm;

    Calendar dailyHour;

    public NotificationsAlarmSetter(Context context){

        mainScreenContext = context;
        appMap = context.getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //set the daily details
        dailyHour = Calendar.getInstance();
        dailyHour.set(Calendar.HOUR_OF_DAY, 20);

        notificationAlarm = (AlarmManager)mainScreenContext.getSystemService(ALARM_SERVICE);

        Intent notificationIntent = new Intent(mainScreenContext, NotificationsReceiver.class);
        pendingNotificationIntent = PendingIntent.getBroadcast(mainScreenContext, 100, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //setting default on daily
        setNotificationsAlarm(AlarmManager.INTERVAL_DAY);
    }

    private void setFlagOfNotificationsInterval(long interval){

        appMapEditor = appMap.edit();
        appMapEditor.putLong(KeysDB.getInstance().NOTIFICATIONS_INTERVAL_CATEGORY, interval);
        appMapEditor.apply();
    }

    public void setNotificationsAlarm(long interval) {

        setFlagOfNotificationsInterval(interval);

        notificationAlarm.setRepeating(AlarmManager.RTC_WAKEUP, dailyHour.getTimeInMillis(), interval, pendingNotificationIntent);
    }

    public void cancelNotificationsAlarm(){

        notificationAlarm.cancel(pendingNotificationIntent);
    }
}
