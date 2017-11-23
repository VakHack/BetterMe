package roeevakrat.betterme;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PositiveFeedbackScreen extends AppCompatActivity {

    TextView titleText;
    Typeface titleFont;

    TextView bodyText;
    Typeface bodyFont;

    ImageView backButton;

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_positive_feedback_screen);

        titleText = (TextView)findViewById(R.id.positiveFeedbackTitle);
        bodyText = (TextView)findViewById(R.id.positiveFeedbackBody);
        backButton = (ImageView)findViewById(R.id.backFromPositive);

        setTextFont(titleText, titleFont, FontsDB.getInstance().getTitleFont());
        setTextFont(bodyText, bodyFont, FontsDB.getInstance().getBodyFont());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent counterScreenIntent = new Intent(PositiveFeedbackScreen.this, CounterScreen.class);
                startActivity(counterScreenIntent);
            }
        });
    }
}
