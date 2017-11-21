package roeevakrat.betterme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScreen extends AppCompatActivity {

    TextView titleText;
    Typeface titleFont;

    TextView bodyText;
    Typeface bodyFont;

    Typeface buttonFont;
    Button startButton;

    SharedPreferences appPerfs;
    SharedPreferences.Editor prefsEditor;

    DateGenerator firstRunDate;

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }

    private void goToCounterActivity(){
        Intent counterScreenIntent = new Intent(WelcomeScreen.this, CounterScreen.class);
        startActivity(counterScreenIntent);
    }

    private void proceedToCounterIfNotFirstRun(){
        if(appPerfs.getBoolean(KeysDB.getInstance().APP_FIRST_RUN_BOOL, true)){

            //edit condition to be the first run
            prefsEditor = appPerfs.edit();
            prefsEditor.putBoolean(KeysDB.getInstance().APP_FIRST_RUN_BOOL, false);
            prefsEditor.apply();

            //keep the date of the first day of training
            firstRunDate = new DateGenerator();
            prefsEditor = appPerfs.edit();
            prefsEditor.putString(KeysDB.getInstance().FIRST_RUN_DATE, firstRunDate.getDate());
            prefsEditor.apply();

        } else {
            //if it is not the first run - jump to second activity
            goToCounterActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        //initialize preferences
        appPerfs = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        proceedToCounterIfNotFirstRun();

        titleText = (TextView)findViewById(R.id.welcomeText);
        bodyText = (TextView)findViewById(R.id.introduction);
        startButton = (Button)findViewById(R.id.button);

        setTextFont(titleText, titleFont, AppFontsDB.getInstance().getTitleFont());
        setTextFont(bodyText, bodyFont, AppFontsDB.getInstance().getBodyFont());
        setTextFont(startButton, buttonFont, AppFontsDB.getInstance().getBodyFont());

        startButton.setTypeface(Typeface.createFromAsset(getAssets(), AppFontsDB.getInstance().getTitleFont()));

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToCounterActivity();

                finish();
            }
        });
    }
}
