package roeevakrat.betterme;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        FeedbackNotificationsGenerator notificationsGenerator = new FeedbackNotificationsGenerator(context);

        SharedPreferences appMap = context.getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        DateGenerator today = new DateGenerator();
        String firstRunDateStr = appMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, today.getDate());
        DateGenerator firstRunDate = new DateGenerator(firstRunDateStr);

        Log.e("bettermelog", "notification alarm received");

        long notificationsInterval = appMap.getLong(KeysDB.getInstance().NOTIFICATIONS_INTERVAL_CATEGORY, 0);

        //in the first day, no progress can be measured - so no feedback generated
        if(!today.equals(firstRunDate)){

            if(notificationsInterval == AlarmManager.INTERVAL_DAY){

                notificationsGenerator.generateNotificationForDailyUser();

            } else if(notificationsInterval == AlarmManager.INTERVAL_DAY * 7){

                notificationsGenerator.generateNotificationForWeeklyUser();
            }
        }

    }
}
