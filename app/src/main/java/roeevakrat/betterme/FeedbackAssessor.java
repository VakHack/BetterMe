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

    public enum DailyFeedback {
        ZERO_BEHAVIOR, EVEN_BEHAVIOR, ADDED_BEHAVIOR, REDUCED_BEHAVIOR
    }

    FeedbackAssessor(Context context){

        appMap = context.getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);
    }

    private Double rangeOfDaysLinearRegressionSlope(DateGenerator fromDate, DateGenerator toDate){

        SimpleRegression weeklyRegression = new SimpleRegression();

        DateGenerator weekDatesIterator = new DateGenerator(fromDate);

        int range = fromDate.calculateIntervalBetweenDates(toDate);

        for(int i = 0; i < range; ++i) {

            weeklyRegression.addData(i, appMap.getInt(weekDatesIterator.getDate(), 0));
            weekDatesIterator.addDaysToDate(1);
        }

        return weeklyRegression.getSlope();
    }

    public boolean isThisRangeOfDatesShowsImprovment(DateGenerator fromDate, DateGenerator toDate){

        return rangeOfDaysLinearRegressionSlope(fromDate, toDate) < 0;
    }

    public DailyFeedback isThisDayBetterThanLastDay() {

        DateGenerator today = new DateGenerator();
        DateGenerator yesterday = new DateGenerator(-1);

        int todaysCounter = appMap.getInt(today.getDate(), 0);
        int yesterdaysCounter = appMap.getInt(yesterday.getDate(), 0);

        if(todaysCounter == 0){

            return DailyFeedback.ZERO_BEHAVIOR;

        } else if (todaysCounter > yesterdaysCounter){

            return DailyFeedback.ADDED_BEHAVIOR;

        } else if (todaysCounter < yesterdaysCounter){

            return DailyFeedback.REDUCED_BEHAVIOR;
        } else {

            return DailyFeedback.EVEN_BEHAVIOR;
        }
    }

}
