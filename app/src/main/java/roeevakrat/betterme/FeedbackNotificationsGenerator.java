package roeevakrat.betterme;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 14/10/2017.
 */

public class FeedbackNotificationsGenerator {

    private SharedPreferences appMap;

    private Context appContext;
    private FeedbackAssessor assessor;

    private DateFormatter firstRunDate;

    //details about current individual feedback
    boolean weeklyFeedback;
    boolean generalFeedback;
    FeedbackAssessor.DailyFeedback dailyFeedback;

    public FeedbackNotificationsGenerator(Context context) {

        appContext = context;
        assessor = new FeedbackAssessor(context);

        appMap = context.getApplicationContext().getSharedPreferences(SharedPreferenceDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        firstRunDate = getFirstRunDate();
    }

    private DateFormatter getFirstRunDate() {

        DateFormatter defaultDate = new DateFormatter();
        String firstRunDateStr = appMap.getString(SharedPreferenceDB.getInstance().FIRST_RUN_DATE, defaultDate.getDate());

        return new DateFormatter(firstRunDateStr);
    }

    private String getWeeklyNotificationTitle(){

        if(generalFeedback){

            return "BetterMe Report: Well Done!";

        } else {

            return "BetterMe Report: Try Harder";
        }
    }

    private String getWeeklyNotificationBody(){

        final String generalyGood = "Your graph shows general improvement trend!";
        final String generalyBad = "Your graph does not show general improvement trend";

        final String weeklyGood = "\nHowever, this week separately shows an improvement trend. Keep at it!";
        final String weeklyBad = "\nHowever, this week separately does not show an improvement trend";

        if(generalFeedback && weeklyFeedback){

            return generalyGood;

        } else if(generalFeedback && !weeklyFeedback){

            return generalyGood + weeklyBad;

        } else if(!generalFeedback && !weeklyFeedback){

            return generalyBad;

        } else {

            return generalyBad + weeklyGood;
        }
    }

    private String getDailyNotificationTitle(){

        if(dailyFeedback == FeedbackAssessor.DailyFeedback.REDUCED_BEHAVIOR){

            return "BetterMe Report: Well Done!";

        } else if(dailyFeedback == FeedbackAssessor.DailyFeedback.ADDED_BEHAVIOR || dailyFeedback == FeedbackAssessor.DailyFeedback.EVEN_BEHAVIOR) {

            return "BetterMe Report: Try harder";

        } else {

            return "BetterMe Report: Zero Clicks";
        }
    }

    private String getDailyNotificationBody(){

        if(dailyFeedback == FeedbackAssessor.DailyFeedback.REDUCED_BEHAVIOR){

            return "You made some progress comparing to yesterday";

        } else if (dailyFeedback == FeedbackAssessor.DailyFeedback.ADDED_BEHAVIOR || dailyFeedback == FeedbackAssessor.DailyFeedback.EVEN_BEHAVIOR) {

            return "Your bad habits number was not reduced comparing to yesterday";

        } else {

            return "Today's bad-habit counter shows zero. Did you forgot to update BetterMe? If not, well done!";
        }
    }

    private int pickWeeklyNotificationIcon(){

        if(generalFeedback){

            return R.drawable.trophy;
        } else {

            return R.drawable.goal;
        }
    }

    private int pickDailyNotificationIcon(){

        if(dailyFeedback == FeedbackAssessor.DailyFeedback.REDUCED_BEHAVIOR || dailyFeedback == FeedbackAssessor.DailyFeedback.ZERO_BEHAVIOR){

            return R.drawable.trophy;

        } else {

            return R.drawable.goal;
        }
    }

    public void weeklyNotification() {


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(appContext.getResources(), pickWeeklyNotificationIcon()))
                .setContentTitle(getWeeklyNotificationTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getWeeklyNotificationBody()))
                .setAutoCancel(true);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    public void dailyNotification() {

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(appContext)
                .setSmallIcon(R.drawable.dislike)
                .setLargeIcon(BitmapFactory.decodeResource(appContext.getResources(), pickDailyNotificationIcon()))
                .setContentTitle(getDailyNotificationTitle())
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getDailyNotificationBody()))
                .setAutoCancel(true);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private boolean isTodayAWeekFinished() {

        DateFormatter todaysDate = new DateFormatter();

        int daysSinceFirstDay = firstRunDate.calculateIntervalBetweenDates(todaysDate);

        return (daysSinceFirstDay % 7) == 0;
    }

    public void generateNotificationForDailyUser(){

        //user that subscribed to daily notification will receive a weekly update at the end of every week
        if(isTodayAWeekFinished()){

            generalFeedback = assessor.isThisRangeOfDatesShowsImprovment(firstRunDate, new DateFormatter());
            weeklyFeedback = assessor.isThisRangeOfDatesShowsImprovment(new DateFormatter(-7), new DateFormatter(-1));

            weeklyNotification();

        } else {

            dailyFeedback = assessor.isThisDayBetterThanLastDay();

            dailyNotification();
        }
    }

    public void generateNotificationForWeeklyUser(){

        weeklyNotification();
    }

}

