package roeevakrat.betterme;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * Created by Administrator on 26/09/2017.
 */

public class FeedbackAssessor extends FragmentActivity {

    DateGenerator thisMonday;
    SharedPreferences countersMap;

    FeedbackAssessor(Context context){

        thisMonday = new DateGenerator();
        thisMonday.setDateToDayInCurrentWeek(1);

        countersMap = context.getApplicationContext().getSharedPreferences("betterMePrefs", MODE_PRIVATE);
    }

    private Double weeklyLinearRegressionSlope(DateGenerator firstDayOfWeek){

        SimpleRegression weeklyRegression = new SimpleRegression();
        DateGenerator weekDatesIterator = new DateGenerator(firstDayOfWeek);

        for(int i = 1; i <= 7; ++i) {

            weekDatesIterator.setDateToDayInCurrentWeek(i);

            weeklyRegression.addData(i, countersMap.getInt(weekDatesIterator.getDate(), 0));
        }

        return weeklyRegression.getSlope();
    }

    private boolean isThisWeekBetterThanLastWeekTrend(){

        //set to last monday
        DateGenerator lastMonday = new DateGenerator();
        lastMonday.addDaysToDate(-7);
        thisMonday.setDateToDayInCurrentWeek(1);

        Log.e("bettermelog", "this week slope: " + weeklyLinearRegressionSlope(thisMonday));
        Log.e("bettermelog", "last week slope: " + weeklyLinearRegressionSlope(lastMonday));

        return weeklyLinearRegressionSlope(thisMonday) < weeklyLinearRegressionSlope(lastMonday);
    }

    private boolean isThisWeekTrendNegative(){

        Log.e("bettermelog", "this week slope: " + weeklyLinearRegressionSlope(thisMonday));

        return weeklyLinearRegressionSlope(thisMonday) < 0;
    }

    public boolean isDesereveAPositiveFeedback(int daysSinceFirstRun){

        if(daysSinceFirstRun == 7){
            return isThisWeekTrendNegative();
        } else {
            return isThisWeekBetterThanLastWeekTrend();
        }
    }
}
