package roeevakrat.betterme;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by Administrator on 24/09/2017.
 */

public class WeeklyChartDataFormatter implements IValueFormatter {

//    private int mFormat;
//
//    public WeeklyChartDataFormatter() {
//        mFormat = new Integer("###,###,##0.0"); // use one decimal
//    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        try {
            return String.valueOf(Math.round(value));

        } catch (Exception e) {
            return "";
        }
    }
}
