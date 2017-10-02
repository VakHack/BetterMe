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

    //tracker title
    TextView counterTitleTop;
    TextView counterTitleMid;
    TextView counterTitleBottom;

    Typeface titleFont;
    TextView dateTitle;
    Typeface dateFont;

    //tracker buttons
    ImageView counterButton;
    ImageView counterMinusButton;
    ImageView helpButton;
    ImageView chartButton;
    ImageView yesterdayButton;
    ImageView tommorowButton;
    TextView counterView;
    Typeface counterViewFont;

    //database
    SharedPreferences countersMap;
    SharedPreferences.Editor countersEditor;

    DateGenerator todaysDate;
    FeedbackAssessor assessor;

    SoundEffectPlayer screenEffect;
    SoundEffectPlayer buttonEffect;

    private void setTodaysCounter(int val){

        countersEditor = countersMap.edit();
        countersEditor.putInt(todaysDate.getDate(), val);
        countersEditor.apply();

        counterView.setText(String.valueOf(val));
    }

//    private void counterResetDialog(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(CounterScreen.this);
//        builder.setCancelable(true);
//        builder.setMessage("Do you want to reset " + todaysDate.getDate() + " counter?");
//
//        builder.setPositiveButton("Confirm",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        setTodaysCounter(0);
//                    }
//                });
//
//        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    private void setTextFont(TextView tv, Typeface tf, String font){
        tf = Typeface.createFromAsset(getAssets(), font);
        tv.setTypeface(tf);
    }

    private void refreshCounter(){

        counterView.setText(String.valueOf(countersMap.getInt(todaysDate.getDate(), 0)));
    }

    private void refreshCurrentDate(){

        dateTitle.setText(todaysDate.getDate());
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

//    //for debug purposes
//    private void enterHardCodedValues(){
//
//        DateGenerator twoWeeksAgo = new DateGenerator();
//        twoWeeksAgo.addDaysToDate(-14);
//
//        countersEditor = countersMap.edit();
//        countersEditor.putString(KeysDB.getInstance().FIRST_RUN_DATE, twoWeeksAgo.getDate());
//        countersEditor.apply();
//
//        Random rand = new Random(System.currentTimeMillis());
//
//        for(int i = 0; i <=14; ++i){
//
//            twoWeeksAgo.addDaysToDate(i);
//
//            countersEditor = countersMap.edit();
//            countersEditor.putInt(twoWeeksAgo.getDate(), rand.nextInt(21));
//            countersEditor.apply();
//        }
//    }

    private void setAssessmentFlagToTrue(){

        countersEditor = countersMap.edit();
        countersEditor.putBoolean(KeysDB.getInstance().TODAYS_CHECK_FOR_FEEDBACK + todaysDate.getDate(), true);
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
                setTodaysCounter(countersMap.getInt(todaysDate.getDate(), 0) + 1);
            }
        }, 700);
    }

    private void clickCounterIfAppCalledByWidget(){

        if(isAppStartedByWidget()){

            setRunByWidgetFlagToFalse();

            refreshCurrentDate();
            setTodaysCounter(countersMap.getInt(todaysDate.getDate(), 0));

            clickCounterButtonAfterDelay();
        }
    }

    private void feedbackCheckAndRun(){

        //if today assessment for feedback already preformed - skip feedback method
        boolean isFeedbackPerformedToday = countersMap.getBoolean(KeysDB.getInstance().TODAYS_CHECK_FOR_FEEDBACK + todaysDate.getDate(), false);
        int daysSinceFirstRun = todaysDate.calculateDaysSinceDate(countersMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, todaysDate.getDate()));

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_screen);

        //updating package name
        KeysDB.getInstance().PACKAGE_NAME = getApplicationContext().getPackageName();

        //initialize date var
        todaysDate = new DateGenerator();
        assessor = new FeedbackAssessor(this.getApplicationContext());

        //initialize preferences
        countersMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //initialize texts and views
        counterButton = (ImageView)findViewById(R.id.counterButton);
        counterMinusButton = (ImageView)findViewById(R.id.resetCounter);
        yesterdayButton = (ImageView)findViewById(R.id.yesterdayButton);
        tommorowButton = (ImageView)findViewById(R.id.tommorowButton);
        helpButton = (ImageView)findViewById(R.id.helpButton);
        counterView = (TextView)findViewById(R.id.counterView);
        dateTitle = (TextView)findViewById(R.id.date);
        chartButton = (ImageView)findViewById(R.id.chartButton);
        counterTitleTop = (TextView)findViewById(R.id.counterTitleTop);
        counterTitleMid = (TextView)findViewById(R.id.counterTitleMid);
        counterTitleBottom = (TextView)findViewById(R.id.counterTitleBottom);

        //initialize sound effects
        screenEffect = new SoundEffectPlayer(this, R.raw.app_resume);
        buttonEffect = new SoundEffectPlayer(this, R.raw.counter_pressed);

        //update texts fonts
        setTextFont(counterTitleTop, titleFont, AppFontsDB.getInstance().getSarif());
        setTextFont(counterTitleMid, titleFont, AppFontsDB.getInstance().getSarif());
        setTextFont(counterTitleBottom, titleFont, AppFontsDB.getInstance().getSarif());
        setTextFont(dateTitle, dateFont, AppFontsDB.getInstance().getSanSarif());
        setTextFont(counterView, counterViewFont, AppFontsDB.getInstance().getSarif());

        screenEffect.play();

        refreshCurrentDate();
        refreshCounter();
        feedbackCheckAndRun();

        clickCounterIfAppCalledByWidget();

        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonEffect.play();
                buttonPressedAnimation();

                setTodaysCounter(countersMap.getInt(todaysDate.getDate(), 0) + 1);
            }
        });

        counterMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int currentVal = countersMap.getInt(todaysDate.getDate(), 0);

                if(currentVal > 0){

                    setTodaysCounter(currentVal - 1);
                }
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent counterScreenIntent = new Intent(CounterScreen.this, HelpCounterScreen.class);
                startActivity(counterScreenIntent);
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

                todaysDate.addDaysToDate(-1);

                refreshCurrentDate();
                refreshCounter();
            }
        });

        tommorowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                todaysDate.addDaysToDate(1);

                refreshCurrentDate();
                refreshCounter();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshCurrentDate();

        clickCounterIfAppCalledByWidget();

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
