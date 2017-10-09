package roeevakrat.betterme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.os.*;

public class CounterScreen extends AppCompatActivity {

    //counter title
    TextView counterTitleTop;
    TextView counterTitleMid;
    TextView counterTitleBottom;

    Typeface titleFont;
    TextView dateTitle;
    Typeface dateFont;

    //screen buttons
    ImageView counterButton;
    ImageView helpButton;
    ImageView chartButton;
    ImageView yesterdayButton;
    ImageView tommorowButton;
    TextView counterView;
    Typeface counterViewFont;

    //menu bar views
    ImageView whiteMenuButton;
    ImageView blackMenuButton;
    View menuLayout;

    //help screen view
    TextView overlay;
    ImageView counterButtonHelpArrow;
    TextView counterButtonHelpText;
    ImageView counterViewHelpArrow;
    TextView counterViewHelpText;
    ImageView yesterdayButtonHelpArrow;
    TextView yesterdayButtonHelpText;
    ImageView chartHelpArrow;
    TextView chartHelpText;

    //database
    SharedPreferences countersMap;
    SharedPreferences.Editor countersEditor;

    DateGenerator countersDate;
    FeedbackAssessor assessor;

    SoundEffectPlayer screenEffect;
    SoundEffectPlayer buttonEffect;

    Boolean isMenuBarOn;

    private void setTodaysCounter(int val){

        countersEditor = countersMap.edit();
        countersEditor.putInt(countersDate.getDate(), val);
        countersEditor.apply();

        counterView.setText(String.valueOf(val));
    }

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }

    private void refreshCounter(){

        counterView.setText(String.valueOf(countersMap.getInt(countersDate.getDate(), 0)));
    }

    private void refreshCurrentDate(){

        dateTitle.setText(countersDate.getDate());
    }

    private void goToFeedbackScreen(int daysSinceFirstRun){

        if(assessor.isDesereveAPositiveFeedback(daysSinceFirstRun))
        {
            Intent positiveFeedbackIntent = new Intent(CounterScreen.this, PositiveFeedbackScreen.class);
            startActivity(positiveFeedbackIntent);

        } else {

            Intent negativeFeedbackIntent = new Intent(CounterScreen.this, NegativeFeedbackScreen.class);
            startActivity(negativeFeedbackIntent);
        }
    }

    private void setAssessmentFlagToTrue(){

        countersEditor = countersMap.edit();
        countersEditor.putBoolean(KeysDB.getInstance().TODAYS_CHECK_FOR_FEEDBACK + countersDate.getDate(), true);
        countersEditor.apply();
    }

    private void buttonPressedAnimation(){

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(170);
        anim.setRepeatMode(Animation.REVERSE);

        counterButton.startAnimation(anim);
    }

    private boolean isFirstRunOfCounterScreen(){

        return countersMap.getBoolean(KeysDB.getInstance().COUNTER_SCREEN_FIRST_RUN, true);
    }

    private void setToNotFirstRunOfCounterScreen(){

        countersEditor = countersMap.edit();
        countersEditor.putBoolean(KeysDB.getInstance().COUNTER_SCREEN_FIRST_RUN, false);
        countersEditor.apply();
    }

    private void setRunByWidgetFlagToFalse(){

        countersEditor = countersMap.edit();
        countersEditor.putBoolean(KeysDB.getInstance().APP_OPENED_BY_WIDGET, false);
        countersEditor.apply();
    }

    private boolean isAppStartedByWidget(){

        return countersMap.getBoolean(KeysDB.getInstance().APP_OPENED_BY_WIDGET, false);
    }

    private void clickCounterButtonAfterDelay(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                buttonPressedAnimation();
                buttonEffect.play();
                setTodaysCounter(countersMap.getInt(countersDate.getDate(), 0) + 1);
            }
        }, 700);
    }

    private void clickCounterIfAppCalledByWidget(){

        if(isAppStartedByWidget()){

            setRunByWidgetFlagToFalse();

            refreshCurrentDate();
            setTodaysCounter(countersMap.getInt(countersDate.getDate(), 0));

            clickCounterButtonAfterDelay();
        }
    }

    private void feedbackCheckAndRun(){

        //if today assessment for feedback already preformed - skip feedback method
        boolean isFeedbackPerformedToday = countersMap.getBoolean(KeysDB.getInstance().TODAYS_CHECK_FOR_FEEDBACK + countersDate.getDate(), false);
        int daysSinceFirstRun = countersDate.calculateDaysSinceDate(countersMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, countersDate.getDate()));

        if(!isFeedbackPerformedToday) {

            setAssessmentFlagToTrue();

            if (daysSinceFirstRun > 0 && daysSinceFirstRun % 7 == 0) {
                goToFeedbackScreen(daysSinceFirstRun);
            }
        }
    }

    private void createWidgetInstallNotification(){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Check Out Our Widget")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("Install BetterMe home-screen button for an easier daily bad-habit tracking!"))
                .setContentText("Install BetterMe home-screen button for an easier daily bad-habit tracking!")
                .setSmallIcon(R.drawable.dislike)
                .setAutoCancel(true);
                //.setSound(defaultSoundUri)
                //.setContentIntent(pendingIntent);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private void dateChangeUpdates(){

        DateGenerator todaysDate = new DateGenerator();

        refreshCurrentDate();
        refreshCounter();

        if(todaysDate.equals(countersDate)){

            tommorowButton.setVisibility(View.INVISIBLE);

        } else {

            tommorowButton.setVisibility(View.VISIBLE);
        }
    }

    private void setHelpScreenViewVisibility(int visibility){

        overlay.setVisibility(visibility);
        counterViewHelpArrow.setVisibility(visibility);
        counterViewHelpText.setVisibility(visibility);
        counterButtonHelpArrow.setVisibility(visibility);
        counterButtonHelpText.setVisibility(visibility);
        yesterdayButtonHelpText.setVisibility(visibility);
        yesterdayButtonHelpArrow.setVisibility(visibility);
        chartHelpText.setVisibility(visibility);
        chartHelpArrow.setVisibility(visibility);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_screen);

        //updating package name
        KeysDB.getInstance().PACKAGE_NAME = getApplicationContext().getPackageName();

        //initialize date var
        countersDate = new DateGenerator();
        assessor = new FeedbackAssessor(this.getApplicationContext());

        //initialize shared preferences
        countersMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //setting menu bar state to "off"
        isMenuBarOn = false;

        //initialize texts and views
        counterButton = (ImageView)findViewById(R.id.counterButton);
        yesterdayButton = (ImageView)findViewById(R.id.yesterdayButton);
        tommorowButton = (ImageView)findViewById(R.id.tommorowButton);
        helpButton = (ImageView)findViewById(R.id.helpButton);
        counterView = (TextView)findViewById(R.id.counterView);
        dateTitle = (TextView)findViewById(R.id.date);
        chartButton = (ImageView)findViewById(R.id.chartButton);
        counterTitleTop = (TextView)findViewById(R.id.counterTitleTop);
        counterTitleMid = (TextView)findViewById(R.id.counterTitleMid);
        counterTitleBottom = (TextView)findViewById(R.id.counterTitleBottom);

        //initialize menu bar view
        menuLayout = findViewById(R.id.menuBar);
        whiteMenuButton = (ImageView)findViewById(R.id.whiteMenuButton);
        blackMenuButton = (ImageView)findViewById(R.id.blackMenuButton);

        //initialize help screen views
        overlay = (TextView)findViewById(R.id.overlay);
        counterButtonHelpArrow = (ImageView)findViewById(R.id.counterHelpArrow);
        counterButtonHelpText = (TextView) findViewById(R.id.counterHelpText);
        counterViewHelpArrow = (ImageView)findViewById(R.id.counterViewHelpArrow);
        counterViewHelpText = (TextView) findViewById(R.id.counterViewHelpText);
        yesterdayButtonHelpArrow = (ImageView)findViewById(R.id.dayButtonHelpArrow);
        yesterdayButtonHelpText = (TextView)findViewById(R.id.dayButtonHelpText);
        chartHelpArrow = (ImageView)findViewById(R.id.chartHelpArrow);
        chartHelpText = (TextView)findViewById(R.id.chartHelpText);

        //set help view invisible
        setHelpScreenViewVisibility(View.INVISIBLE);

        //initialize sound effects
        screenEffect = new SoundEffectPlayer(this, R.raw.app_resume);
        buttonEffect = new SoundEffectPlayer(this, R.raw.counter_pressed);

        //update texts fonts
        setTextFont(counterTitleTop, titleFont, AppFontsDB.getInstance().getTitleFont());
        setTextFont(counterTitleMid, titleFont, AppFontsDB.getInstance().getTitleFont());
        setTextFont(counterTitleBottom, titleFont, AppFontsDB.getInstance().getTitleFont());
        setTextFont(dateTitle, dateFont, AppFontsDB.getInstance().getBodyFont());
        setTextFont(counterView, counterViewFont, AppFontsDB.getInstance().getTitleFont());
        setTextFont(counterButtonHelpText, counterViewFont, AppFontsDB.getInstance().getHelpScreenFont());
        setTextFont(counterViewHelpText, counterViewFont, AppFontsDB.getInstance().getHelpScreenFont());
        setTextFont(yesterdayButtonHelpText, counterViewFont, AppFontsDB.getInstance().getHelpScreenFont());
        setTextFont(chartHelpText, counterViewFont, AppFontsDB.getInstance().getHelpScreenFont());

        screenEffect.play();

        clickCounterIfAppCalledByWidget();

        refreshCurrentDate();
        refreshCounter();
        feedbackCheckAndRun();

        //when viewing today's counter - next day's counter is irrelevant - set it's button view to invisible
        tommorowButton.setVisibility(View.INVISIBLE);

        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonEffect.play();
                buttonPressedAnimation();

                setTodaysCounter(countersMap.getInt(countersDate.getDate(), 0) + 1);
            }
        });

        whiteMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuLayout.setVisibility(View.VISIBLE);
            }
        });

        blackMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuLayout.setVisibility(View.INVISIBLE);
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            setHelpScreenViewVisibility(View.VISIBLE);
            }
        });

        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent chartScreenIntent = new Intent(CounterScreen.this, WeeklyChart.class);
                startActivity(chartScreenIntent);
            }
        });

        yesterdayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countersDate.addDaysToDate(-1);

                dateChangeUpdates();
            }
        });

        tommorowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                countersDate.addDaysToDate(1);

                dateChangeUpdates();
            }
        });

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setHelpScreenViewVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        clickCounterIfAppCalledByWidget();

        refreshCurrentDate();
        refreshCounter();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isFirstRunOfCounterScreen()){

            setToNotFirstRunOfCounterScreen();

            createWidgetInstallNotification();
        }
    }
}
