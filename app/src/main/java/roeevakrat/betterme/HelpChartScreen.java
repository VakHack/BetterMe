package roeevakrat.betterme;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpChartScreen extends AppCompatActivity {

    TextView titleText;
    Typeface titleFont;

    TextView bodyText;
    Typeface bodyFont;

    ImageView backToChartButton;

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_screen_weekly_chart);

        titleText = (TextView)findViewById(R.id.WeeklyChartHelpTitle);
        bodyText = (TextView)findViewById(R.id.WeeklyChartHelpBody);
        backToChartButton = (ImageView)findViewById(R.id.backToWeeklyChart);

        setTextFont(titleText, titleFont, AppFontsDB.getInstance().getSarif());
        setTextFont(bodyText, bodyFont, AppFontsDB.getInstance().getSanSarif());

        backToChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HelpChartScreen.this, WeeklyChart.class));
            }
        });
    }
}
