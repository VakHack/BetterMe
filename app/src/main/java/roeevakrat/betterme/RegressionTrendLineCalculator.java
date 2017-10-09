package roeevakrat.betterme;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * Created by Administrator on 28/09/2017.
 */

public class RegressionTrendLineCalculator extends FragmentActivity {

    SharedPreferences countersMap;
    SimpleRegression regression;

    RegressionTrendLineCalculator(Context context, String fromDate, String toDate){

        countersMap = context.getApplicationContext().getSharedPreferences("betterMePrefs", MODE_PRIVATE);

        generateDatesRangeRegression(fromDate, toDate);
    }

    private void generateDatesRangeRegression(String fromDate, String toDate){

        regression = new SimpleRegression();

        DateGenerator from = new DateGenerator(fromDate);
        DateGenerator to = new DateGenerator(toDate);

        int numOfDaysInRange = from.calculateDaysSinceDate(to);

        for(int i = 0; i <= numOfDaysInRange; ++i) {

            Log.e("bettermelog", "regression days in range:" + from.getDate());

            regression.addData(i, countersMap.getInt(from.getDate(), 0));
            from.addDaysToDate(1);
        }
    }

    public int predictYValOnRegressionPlot(int xVal){

        Double doubleVal = regression.predict(xVal);
        Integer intVal = doubleVal.intValue();

        return intVal;
    }

    public boolean isTrendSlopePositive(){

        return regression.getSlope() > 0;
    }
}
