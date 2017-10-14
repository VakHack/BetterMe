package roeevakrat.betterme;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * Created by Administrator on 26/09/2017.
 */

public class FeedbackAssessor extends FragmentActivity {

    private SharedPreferences appMap;

    FeedbackAssessor(Context context){

        appMap = context.getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);
    }

    private Double rangeOfDaysLinearRegressionSlope(DateGenerator fromDate, int range){

        SimpleRegression weeklyRegression = new SimpleRegression();
        DateGenerator weekDatesIterator = new DateGenerator(fromDate);

        for(int i = 0; i < range; ++i) {

            weeklyRegression.addData(i, appMap.getInt(weekDatesIterator.getDate(), 0));
            weekDatesIterator.addDaysToDate(1);
        }

        return weeklyRegression.getSlope();
    }

    public boolean isThisWeekTrendBetterThanLastWeekTrend(){

        //set to last monday
        DateGenerator todaysWeek = new DateGenerator(-7);
        DateGenerator lastWeek = new DateGenerator(-14);

        return rangeOfDaysLinearRegressionSlope(todaysWeek, 7) < rangeOfDaysLinearRegressionSlope(lastWeek, 7);
    }

    public boolean isThisWeekTrendNegative(){

        DateGenerator todaysWeek = new DateGenerator(-7);

        return rangeOfDaysLinearRegressionSlope(todaysWeek, 7) < 0;
    }

    public boolean isThisDayBetterThanLastDay() {

        DateGenerator today = new DateGenerator();
        DateGenerator yesterday = new DateGenerator(-1);

        int todaysCounter = appMap.getInt(today.getDate(), 0);
        int yesterdaysCounter = appMap.getInt(yesterday.getDate(), 0);

        return todaysCounter < yesterdaysCounter;
    }

}
