package roeevakrat.betterme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 14/10/2017.
 */

public class FeedbackNotificationsGenerator {

    private SharedPreferences appMap;

    private Context appContext;
    private FeedbackAssessor assessor;

    private DateGenerator firstRunDate;

    private final String positiveTile = "Well Done!";
    private final String weeklyPositiveBody = "This week you have shown some improvement";
    private final String dailyPositiveBody = "This week you have shown some improvement";


    public FeedbackNotificationsGenerator(Context context) {

        appContext = context;
        assessor = new FeedbackAssessor(context);

        appMap = context.getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        firstRunDate = getFirstRunDate();
    }

    public DateGenerator getFirstRunDate(){

        DateGenerator defaultDate = new DateGenerator();
        String firstRunDateStr = appMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, defaultDate.getDate());

        return new DateGenerator(firstRunDateStr);
    }

    public void weeklyNotification(boolean isPositive) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(appContext.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Check Out Our Widget")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Install BetterMe home-screen button for an easier daily bad-habit tracking!"))
                .setSmallIcon(R.drawable.dislike)
                .setAutoCancel(true);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public void dailyNotification(boolean isPositive) {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(appContext.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Check Out Our Widget")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Install BetterMe home-screen button for an easier daily bad-habit tracking!"))
                .setSmallIcon(R.drawable.dislike)
                .setAutoCancel(true);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private boolean isTodayAWeekFinished() {

        DateGenerator todaysDate = new DateGenerator();

        int daysSinceFirstDay = firstRunDate.calculateIntervalBetweenDates(todaysDate);

        return (daysSinceFirstDay % 7) == 0;
    }


    public boolean isThisWeekDeservePositiveFeedback() {

        if (isThisTheFirstWeek()) {

            return assessor.isThisWeekTrendNegative();

        } else {

            return assessor.isThisWeekTrendBetterThanLastWeekTrend();
        }
    }

    private boolean isThisTheFirstWeek(){

        DateGenerator firstWeekDay = new DateGenerator(-7);
        DateGenerator firstRunDate = new DateGenerator(appMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, firstWeekDay.getDate()));

        return firstWeekDay.equals(firstRunDate);
    }

    public void generateNotificationForDailyUser(){

        //user that subscribed to daily notification will receive a weekly update at the end of every week
        if(isTodayAWeekFinished()){

            weeklyNotification(isThisWeekDeservePositiveFeedback());

        } else {

            dailyNotification(assessor.isThisDayBetterThanLastDay());
        }
    }

    public void generateNotificationForWeeklyUser(){

        weeklyNotification(isThisWeekDeservePositiveFeedback());
    }

}

