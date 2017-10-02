package roeevakrat.betterme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    SharedPreferences countersMap;
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
    Button trendButton;
    ImageView helpButton;

    DateGenerator fromDate;
    DateGenerator toDate;

    String datesRange;

    boolean isTrendButtonPressed;

    RegressionTrendLineCalculator regressionCalculator;

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }

    private void initDateRangeAccordingToCurrentWeek(){

        fromDate.setDateToDayInCurrentWeek(1);
        toDate.setDateToDayInCurrentWeek(7);
    }

    private void updateRangeOfDatesByInterval(int interval){

        fromDate.addDaysToDate(interval);
        toDate.addDaysToDate(interval);
    }

    private void drawMainPlot(){

        chart.setData(mainLineData);
        chart.invalidate();
    }

    private void addNegativeFeedbackPic(){

    }

    private void addPositiveFeedbackPic(){

    }

    private void addWeeklyTrend(){

        chart.setData(multiPlotData);
        chart.invalidate();

        if(regressionCalculator.isTrendSlopePositive()){

            addNegativeFeedbackPic();
        } else {

            addPositiveFeedbackPic();
        }
    }

    private void sharedPrefToPlotEntries(){

        //add entries to main plot
        chartEntries = new ArrayList<>();
        DateGenerator iterateDates = new DateGenerator(fromDate);

        for(int i = 1; i <= 7; ++i) {

            iterateDates.setDateToDayInCurrentWeek(i);
            chartEntries.add(new Entry(i, countersMap.getInt(iterateDates.getDate(), 0)));
        }

        //add entries to trend plot
        regressionCalculator = new RegressionTrendLineCalculator(this.getApplicationContext(), fromDate.getDate(), toDate.getDate());

        trendEntries = new ArrayList<>();
        trendEntries.add(new Entry(1, regressionCalculator.predictYValOnRegressionPlot(1)));
        trendEntries.add(new Entry(7, regressionCalculator.predictYValOnRegressionPlot(7)));
    }

    private void insertDataToPlots(){

        sharedPrefToPlotEntries();

        mainPlot = new LineDataSet(chartEntries, "");
        mainLineData = new LineData(mainPlot);

        trendPlot = new LineDataSet(trendEntries, "");
        multiPlotData = new LineData(mainPlot, trendPlot);
    }

    private void designChart(){
        //x axis details
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10);
        xAxis.setTextColor(Color.WHITE);
        String[] weekDays = {"", "MON", "TUE", "WED", "THR", "FRI", "SAT", "SUN"};
        xAxis.setValueFormatter(new WeekChartXAxisValueFormatter(weekDays));

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
        Typeface tf = Typeface.createFromAsset(getAssets(), AppFontsDB.getInstance().getSanSarif());
        mainPlot.setValueTypeface(tf);
        mainPlot.setValueFormatter(new WeeklyChartDataFormatter());
        //mainPlot.setMode(LineDataSet.Mode.STEPPED);
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
        trendPlot.setLineWidth(2f);
        trendPlot.setDrawValues(false);
        trendPlot.setDrawFilled(false);
        trendPlot.setDrawCircleHole(false);
        trendPlot.setCircleColor(Color.rgb(255, 143, 86));
    }

    private void updateDateTitle(){

        datesRange = fromDate.getDate() + " - " + toDate.getDate();
        dateTitle.setText(datesRange);
    }

    private boolean isWeeklyChartFirstRun(){

        return countersMap.getBoolean(KeysDB.getInstance().WEEKLY_CHART_SCREEN_FIRST_RUN, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_chart);

        //initialize preferences
        countersMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //initialize trend button to 'off' state
        isTrendButtonPressed = false;

        //initialize dates
        fromDate = new DateGenerator();
        toDate = new DateGenerator();

        backButton = (ImageView)findViewById(R.id.backToCounter);
        lastWeekButton = (ImageView)findViewById(R.id.lastWeek);
        nextWeekButton = (ImageView)findViewById(R.id.nextWeek);
        chart = (LineChart)findViewById(R.id.chart);
        counterTitle = (TextView)findViewById(R.id.weekChartTitle);
        dateTitle = (TextView)findViewById(R.id.chartDatesTitle);
        trendButton = (Button) findViewById(R.id.weeklyTrendButton);
        helpButton = (ImageView)findViewById(R.id.weeklyChartHelpButton);

        //set fonts
        setTextFont(counterTitle, titleFont, AppFontsDB.getInstance().getSarif());
        setTextFont(dateTitle, dateFont, AppFontsDB.getInstance().getSanSarif());
        trendButton.setTypeface(Typeface.createFromAsset(getAssets(), AppFontsDB.getInstance().getSarif()));

        //set date range
        initDateRangeAccordingToCurrentWeek();
        updateDateTitle();

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

                updateDateTitle();
                insertDataToPlots();
                designChart();
                drawMainPlot();
            }
        });

        nextWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRangeOfDatesByInterval(7);

                updateDateTitle();
                insertDataToPlots();
                designChart();
                drawMainPlot();
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

                Intent chartHelp = new Intent(WeeklyChart.this, HelpChartScreen.class);
                startActivity(chartHelp);
            }
        });
    }
}
