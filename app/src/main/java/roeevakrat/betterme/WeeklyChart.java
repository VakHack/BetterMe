package roeevakrat.betterme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class WeeklyChart extends AppCompatActivity {

    SharedPreferences appMap;
    LineChart chart;

    List<Entry> chartEntries;
    LineDataSet mainPlot;
    LineData mainLineData;

    List<Entry> trendEntries;
    LineDataSet trendPlot;
    LineData multiPlotData;

    TextView counterTitle;
    Typeface titleFont;
    TextView dateTitle;
    Typeface dateFont;
    ImageView backButton;
    ImageView lastWeekButton;
    ImageView nextWeekButton;
    TextView trendButton;
    ImageView helpButton;

    DateGenerator fromDate;
    DateGenerator toDate;

    String datesRange;

    boolean isTrendButtonPressed;

    //help layer views
    TextView overlay;
    TextView helpTextTrendButton;
    TextView helpTextWeekArrow;
    Typeface helpTextFont;
    ImageView arrowTrendButton;
    ImageView arrowWeekArrow;

    RegressionTrendLineCalculator regressionCalculator;

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }

    private void updateRangeOfDatesByInterval(int interval){

        fromDate.addDaysToDate(interval);
        toDate.addDaysToDate(interval);
    }

    private void drawMainPlot(){

        chart.setData(mainLineData);
        chart.invalidate();
    }

    private void addWeeklyTrend(){

        chart.setData(multiPlotData);
        chart.invalidate();
    }

    private void sharedPrefToPlotEntries(){

        //add entries to main plot
        chartEntries = new ArrayList<>();
        DateGenerator iterateDates = new DateGenerator(fromDate);

        for(int i = 0; i < 7; ++i) {

            chartEntries.add(new Entry(i, appMap.getInt(iterateDates.getDate(), 0)));
            iterateDates.addDaysToDate(1);
        }

        //add entries to trend plot
        regressionCalculator = new RegressionTrendLineCalculator(this.getApplicationContext(), fromDate.getDate(), toDate.getDate());

        //creating trend line by passing line between the starting date linear-regression value to the last date value
        trendEntries = new ArrayList<>();
        trendEntries.add(new Entry(0, regressionCalculator.predictYValOnRegressionPlot(1)));
        trendEntries.add(new Entry(6, regressionCalculator.predictYValOnRegressionPlot(7)));
    }

    private void insertDataToPlots(){

        sharedPrefToPlotEntries();

        mainPlot = new LineDataSet(chartEntries, "");
        mainLineData = new LineData(mainPlot);

        trendPlot = new LineDataSet(trendEntries, "");
        multiPlotData = new LineData(mainPlot, trendPlot);
    }

    private String[] getThisWeekDates(){

        String weekDates[] = new String[7];

        DateGenerator dateIncrement = new DateGenerator(fromDate);

        for(int i = 0; i < 7; ++i){

            weekDates[i] = dateIncrement.getDateShort();
            dateIncrement.addDaysToDate(1);
        }

        return weekDates;
    }

    private void designChart(){
        //x axis details
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        xAxis.setTextColor(Color.WHITE);
        String[] weekDates = getThisWeekDates();
        xAxis.setValueFormatter(new WeekChartXAxisValueFormatter(weekDates));

        //delete gridlines
        xAxis.setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false); // no right axis
        chart.getAxisLeft().setEnabled(false); // no left axis
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);

        //chart line details
        mainPlot.setFillColor(Color.WHITE);
        mainPlot.setCircleColor(Color.WHITE);
        mainPlot.setLineWidth(2.5f);
        mainPlot.setValueTextSize(20);
        mainPlot.setValueTextColor(Color.rgb(255, 241, 168));
        Typeface tf = Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getBodyFont());
        mainPlot.setValueTypeface(tf);
        mainPlot.setValueFormatter(new WeeklyChartDataFormatter());
        mainPlot.setColor(Color.WHITE);

        //chart general details
        chart.setDrawBorders(false);
        Description emptyDescription = new Description();
        emptyDescription.setText("");
        chart.setDescription(emptyDescription);
        chart.setBackgroundColor(Color.TRANSPARENT);

        //deleting legend
        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        //animation
        chart.animateX(1000, Easing.EasingOption.Linear);
        chart.animateY(1000, Easing.EasingOption.Linear);

        //trend line edit
        trendPlot.setColor(Color.rgb(255, 143, 86));
        trendPlot.enableDashedLine(20f, 5f, 0f);
        trendPlot.setLineWidth(3f);
        trendPlot.setDrawValues(false);
        trendPlot.setDrawFilled(false);
        trendPlot.setDrawCircleHole(false);
        trendPlot.setCircleColor(Color.rgb(255, 143, 86));
    }

    private void updateDateTitle(){

        datesRange = fromDate.getDate() + " - " + toDate.getDate();
        dateTitle.setText(datesRange);
    }

    private void setHelpScreenViewVisibility(int visibility){

        helpTextTrendButton.setVisibility(visibility);
        helpTextWeekArrow.setVisibility(visibility);
        overlay.setVisibility(visibility);
        arrowTrendButton.setVisibility(visibility);
        arrowWeekArrow.setVisibility(visibility);
    }

    private void weekChangeUpdates(){

        DateGenerator todaysDate = new DateGenerator();

        updateDateTitle();
        insertDataToPlots();
        designChart();
        drawMainPlot();

        if(todaysDate.equals(toDate)){

            nextWeekButton.setVisibility(View.INVISIBLE);

        } else {

            nextWeekButton.setVisibility(View.VISIBLE);
        }
    }

    private DateGenerator getFirstRunDate(){

        DateGenerator today = new DateGenerator();
        String firstRunDateStr = appMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, today.getDate());

        return new DateGenerator(firstRunDateStr);
    }

    private boolean isThisTheFirstWeek(){

        DateGenerator firstRun = getFirstRunDate();
        DateGenerator iterateDates = new DateGenerator();

        for(int i = 7; i >= 0; --i){

            if(iterateDates.equals(firstRun)){

                return true;
            }

            iterateDates.addDaysToDate(-1);
        }

        return false;
    }

    private boolean isThisTheCurrentWeek(){

        DateGenerator today = new DateGenerator();
        DateGenerator iterateDates = new DateGenerator(toDate);

        for(int i = 7; i >= 0; --i){

            if(iterateDates.equals(today)){

                return true;
            }

            iterateDates.addDaysToDate(-1);
        }

        return false;
    }

    private void hideLastWeekButtonIfFirstWeek(){

        if(isThisTheFirstWeek()){

            lastWeekButton.setVisibility(View.INVISIBLE);

        } else {

            lastWeekButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideNextWeekButtonIfCurrentWeek(){

        if(isThisTheCurrentWeek()){

            nextWeekButton.setVisibility(View.INVISIBLE);

        } else {

            nextWeekButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_chart);

        //initialize preferences
        appMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //initialize trend button to 'off' state
        isTrendButtonPressed = false;

        //initialize dates
        fromDate = new DateGenerator(-6);
        toDate = new DateGenerator();

        backButton = (ImageView)findViewById(R.id.backToCounter);
        lastWeekButton = (ImageView)findViewById(R.id.lastWeek);
        nextWeekButton = (ImageView)findViewById(R.id.nextWeek);
        chart = (LineChart)findViewById(R.id.chart);
        counterTitle = (TextView)findViewById(R.id.weekChartTitle);
        dateTitle = (TextView)findViewById(R.id.chartDatesTitle);
        trendButton = (TextView) findViewById(R.id.trendButton);
        helpButton = (ImageView)findViewById(R.id.weeklyChartHelpButton);

        helpTextTrendButton = (TextView)findViewById(R.id.trendButtonInfo);
        helpTextWeekArrow = (TextView)findViewById(R.id.weekArrowInfo);
        overlay = (TextView)findViewById(R.id.overlay);
        arrowTrendButton = (ImageView)findViewById(R.id.trendButtonArrow);
        arrowWeekArrow = (ImageView)findViewById(R.id.weekArrowArrow);

        //set fonts
        setTextFont(counterTitle, titleFont, FontsDB.getInstance().getTitleFont());
        setTextFont(dateTitle, dateFont, FontsDB.getInstance().getBodyFont());
        trendButton.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));

        helpTextFont = Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getHelpScreenFont());
        helpTextTrendButton.setTypeface(helpTextFont);
        helpTextWeekArrow.setTypeface(helpTextFont);

        setHelpScreenViewVisibility(View.INVISIBLE);

        //set date range
        updateDateTitle();

        //when viewing current week - next week view is irrelevant - set it's button view to invisible
        hideNextWeekButtonIfCurrentWeek();

        //if this is the first week of app using - previous weeks are irrelevant
        hideLastWeekButtonIfFirstWeek();

        insertDataToPlots();
        designChart();
        drawMainPlot();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weeklyChartIntent = new Intent(WeeklyChart.this, CounterScreen.class);
                startActivity(weeklyChartIntent);
            }
        });

        lastWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRangeOfDatesByInterval(-7);

                hideLastWeekButtonIfFirstWeek();
                weekChangeUpdates();
            }
        });

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRangeOfDatesByInterval(7);

                hideNextWeekButtonIfCurrentWeek();
                weekChangeUpdates();
            }
        });

        trendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!isTrendButtonPressed) {

                    isTrendButtonPressed = true;

                    trendButton.setText("Trend Off");
                    addWeeklyTrend();

                }else{

                    isTrendButtonPressed = false;

                    trendButton.setText("Trend On");
                    drawMainPlot();
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setHelpScreenViewVisibility(View.VISIBLE);

                lastWeekButton.setVisibility(View.VISIBLE);
                nextWeekButton.setVisibility(View.VISIBLE);
            }
        });

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setHelpScreenViewVisibility(View.INVISIBLE);
                hideLastWeekButtonIfFirstWeek();
                hideNextWeekButtonIfCurrentWeek();
            }
        });
    }
}
