package roeevakrat.betterme;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Administrator on 24/09/2017.
 */

public class WeekChartXAxisValueFormatter implements IAxisValueFormatter {
    private String[] values;

    public WeekChartXAxisValueFormatter(String[] axisStrings) {
        this.values = axisStrings;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        try {
            int index = (int) value;
            return values[index];

        } catch (Exception e) {
            return "";
        }
    }

}
