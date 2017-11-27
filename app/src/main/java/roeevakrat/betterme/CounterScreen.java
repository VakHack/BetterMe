package roeevakrat.betterme;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v4.app.NotificationCompat;
import android.os.*;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class CounterScreen extends AppCompatActivity {

    //counter title
    private TextView counterTitleTop;
    private TextView counterTitleMid;
    private TextView counterTitleBottom;
    private TextView dateTitle;

    //screen buttons
    private ImageView counterButton;
    private ImageView helpButton;
    private ImageView chartButton;
    private ImageView yesterdayButton;
    private ImageView tomorrowButton;
    private TextView counterView;

    //menu bar views
    private ImageView whiteMenuButton;
    private ImageView blackMenuButton;
    private View menuLayout;
    private EditText editCounterLine;
    private TextView editCounterButton;
    private ImageView enterEdit;
    private TextView loginScreen;
    private TextView syncButton;
    private boolean isEditCounterLineOn;
    private Spinner notificationsSpinner;
    private TextView notificationsSetter;
    private TextView aboutMeButton;

    //help screen view
    private TextView overlay;
    private ImageView counterButtonHelpArrow;
    private TextView counterButtonHelpText;
    private ImageView counterViewHelpArrow;
    private TextView counterViewHelpText;
    private ImageView yesterdayButtonHelpArrow;
    private TextView yesterdayButtonHelpText;
    private ImageView chartHelpArrow;
    private TextView chartHelpText;

    //database
    private SharedPreferences appMap;
    private SharedPreferences.Editor appMapEditor;
    private ServerHandler server;

    private DateGenerator countersDate;
    private DateGenerator firstRunDate;

    private SoundEffectPlayer screenEffect;
    private SoundEffectPlayer buttonEffect;

    private NotificationsAlarmSetter alarmSetter;

    private void setTodaysCounter(int val){

        appMapEditor = appMap.edit();
        appMapEditor.putInt(countersDate.getDate(), val);
        appMapEditor.apply();

        counterView.setText(String.valueOf(val));
    }

    private void getFirstRunDate(){

        DateGenerator today = new DateGenerator();
        String firstRunDateStr = appMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, today.getDate());

        firstRunDate = new DateGenerator(firstRunDateStr);
    }

    private void refreshCounter(){

        counterView.setText(String.valueOf(appMap.getInt(countersDate.getDate(), 0)));
    }

    private void refreshCurrentDate(){

        dateTitle.setText(countersDate.getDate());
    }

    private void buttonPressedAnimation(){

        AlphaAnimation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(170);
        anim.setRepeatMode(Animation.REVERSE);

        counterButton.startAnimation(anim);
    }

    private boolean isFirstRunOfCounterScreen(){

        return appMap.getBoolean(KeysDB.getInstance().COUNTER_SCREEN_FIRST_RUN, true);
    }

    private void setToNotFirstRunOfCounterScreen(){

        appMapEditor = appMap.edit();
        appMapEditor.putBoolean(KeysDB.getInstance().COUNTER_SCREEN_FIRST_RUN, false);
        appMapEditor.apply();
    }

    private void setRunByWidgetFlagToFalse(){

        appMapEditor = appMap.edit();
        appMapEditor.putBoolean(KeysDB.getInstance().APP_OPENED_BY_WIDGET, false);
        appMapEditor.apply();
    }

    private boolean isAppStartedByWidget(){

        return appMap.getBoolean(KeysDB.getInstance().APP_OPENED_BY_WIDGET, false);
    }

    private void clickCounterButtonAfterDelay(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                buttonPressedAnimation();
                buttonEffect.play();
                setTodaysCounter(appMap.getInt(countersDate.getDate(), 0) + 1);
            }
        }, 700);
    }

    private void clickCounterIfAppCalledByWidget(){

        if(isAppStartedByWidget()){

            setRunByWidgetFlagToFalse();

            refreshCurrentDate();
            setTodaysCounter(appMap.getInt(countersDate.getDate(), 0));

            clickCounterButtonAfterDelay();
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


        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

    private void dateChangeUpdates(){

        refreshCurrentDate();
        refreshCounter();

        hideTomorrowsButtonIfCurrentDate();
        hideYesterdayButtonIfFirstDay();
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

        //if help screen presented - shows tomorrow and yesterday button visible for instruction purposes
        if(visibility == View.VISIBLE){

            tomorrowButton.setVisibility(View.VISIBLE);
            yesterdayButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideTomorrowsButtonIfCurrentDate(){

        DateGenerator todaysDate = new DateGenerator();

        if(todaysDate.equals(countersDate)){

            tomorrowButton.setVisibility(View.INVISIBLE);

        } else {

            tomorrowButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideYesterdayButtonIfFirstDay(){

        DateGenerator todaysDate = new DateGenerator();

        if(firstRunDate.equals(todaysDate)){

            yesterdayButton.setVisibility(View.INVISIBLE);

        } else {

            yesterdayButton.setVisibility(View.VISIBLE);
        }
    }

    private void showAboutMeDialog(){

        AlertDialog.Builder aboutDialog = new AlertDialog.Builder(this)
                .setMessage("I am a junior indie developer, and still quite new to the exquisite world " +
                        "of app development.\n\nIf you have any kind of note or suggestion, please, do not hesitate to " +
                        "contact me at:\n\nvakhack@gmail.com")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

        aboutDialog.show();
    }

    private boolean isLoginDetailsAvailable(){

        return appMap.getBoolean(KeysDB.getInstance().LOGGED_IN_CLOUD, false);
    }

//    private boolean tryLogin(){
//
//        String username = appMap.getString(KeysDB.getInstance().USERNAME, "username");
//        String password = appMap.getString(KeysDB.getInstance().USER_PASSWORD, "password");
//
//        return server.tryLogin(username, password);
//    }

    private String downloadData() {

        UserData data = server.tryRetrieveData();

        if (data != null) {

            HashMap<String, Integer> badHabitsMap = data.getBadHabitsMap();
            MapToSharedprefConvertor.mapToSharedpref(badHabitsMap, appMap);

            appMapEditor = appMap.edit();
            appMapEditor.putString(KeysDB.getInstance().FIRST_RUN_DATE, data.getFirstRunDate());
            appMapEditor.apply();
        }

        return server.getStorageFeedback();
    }

    private String uploadData(){

        HashMap<String, Integer> badHabitMap = MapToSharedprefConvertor.sharedprefsToMap(appMap);
        server.tryUploadData(new UserData(badHabitMap, firstRunDate.getDate()));

        return server.getStorageFeedback();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_screen);

        //updating package name
        KeysDB.getInstance().PACKAGE_NAME = getApplicationContext().getPackageName();

        //initialize date var
        countersDate = new DateGenerator();

        //initialize shared preferences
        appMap = getApplicationContext().getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        //initialize texts and views
        counterButton = (ImageView)findViewById(R.id.counterButton);
        yesterdayButton = (ImageView)findViewById(R.id.yesterdayButton);
        tomorrowButton = (ImageView)findViewById(R.id.tommorowButton);
        helpButton = (ImageView)findViewById(R.id.helpButton);
        counterView = (TextView)findViewById(R.id.counterView);
        dateTitle = (TextView)findViewById(R.id.date);
        chartButton = (ImageView)findViewById(R.id.chartButton);
        counterTitleTop = (TextView)findViewById(R.id.counterTitleTop);
        counterTitleMid = (TextView)findViewById(R.id.counterTitleMid);
        counterTitleBottom = (TextView)findViewById(R.id.counterTitleBottom);

        //initialize menu bar view
        menuLayout = findViewById(R.id.menuBarBackground);
        whiteMenuButton = (ImageView)findViewById(R.id.whiteMenuButton);
        blackMenuButton = (ImageView)findViewById(R.id.blackMenuButton);
        editCounterLine = (EditText)findViewById(R.id.editCounterLine);
        editCounterButton = (TextView)findViewById(R.id.editCounterButton);
        enterEdit = (ImageView)findViewById(R.id.enterEdit);
        loginScreen = (TextView)findViewById(R.id.login);
        notificationsSpinner = (Spinner)findViewById(R.id.notificationsSpinner);
        notificationsSetter = (TextView)findViewById(R.id.setNotifications);
        aboutMeButton = (TextView)findViewById(R.id.about);
        syncButton = (TextView)findViewById(R.id.syncWithCloud);

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

        //set menu bar invisible
        menuLayout.setVisibility(View.INVISIBLE);
        editCounterLine.setVisibility(View.INVISIBLE);
        enterEdit.setVisibility(View.INVISIBLE);

        //initialize sound effects
        screenEffect = new SoundEffectPlayer(this, R.raw.app_resume);
        buttonEffect = new SoundEffectPlayer(this, R.raw.counter_pressed);

        //initialize the notifications alarm
        alarmSetter = new NotificationsAlarmSetter(this);

        //if it is the first run, set the notifications alarm to daily
        if(isFirstRunOfCounterScreen()){

            alarmSetter.setNotificationsAlarm(AlarmManager.INTERVAL_DAY);
        }

        //update texts fonts
        counterTitleTop.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));
        counterTitleMid.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));
        counterTitleBottom.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));
        dateTitle.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));
        counterView.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getTitleFont()));
        counterButtonHelpText.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getHelpScreenFont()));
        counterViewHelpText.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getHelpScreenFont()));
        yesterdayButtonHelpText.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getHelpScreenFont()));
        chartHelpText.setTypeface(Typeface.createFromAsset(getAssets(), FontsDB.getInstance().getHelpScreenFont()));

        screenEffect.play();

        //dealing with server side saved data
        server = new FirebaseServerHandler(this);
        if(isLoginDetailsAvailable()){
            downloadData();
        }

        //in case called by home-screen widget
        clickCounterIfAppCalledByWidget();

        refreshCurrentDate();
        refreshCounter();

        //when viewing first day's counter last day is irrelevant
        getFirstRunDate();

        hideTomorrowsButtonIfCurrentDate();
        hideYesterdayButtonIfFirstDay();
        
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buttonEffect.play();
                buttonPressedAnimation();

                setTodaysCounter(appMap.getInt(countersDate.getDate(), 0) + 1);
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

        tomorrowButton.setOnClickListener(new View.OnClickListener() {
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

                hideYesterdayButtonIfFirstDay();
                hideTomorrowsButtonIfCurrentDate();
            }
        });

        editCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if(!isEditCounterLineOn){

                isEditCounterLineOn = true;

                editCounterLine.setVisibility(View.VISIBLE);
                enterEdit.setVisibility(View.VISIBLE);

            } else {

                isEditCounterLineOn = false;

                editCounterLine.setVisibility(View.INVISIBLE);
                enterEdit.setVisibility(View.INVISIBLE);
            }
            }
        });

        enterEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String editedCounterStr = editCounterLine.getText().toString();

                if(StringUtils.isNumeric(editedCounterStr) && Integer.parseInt(editedCounterStr) >= 0){

                    setTodaysCounter(Integer.parseInt(editedCounterStr));

                    enterEdit.setVisibility(View.INVISIBLE);
                    editCounterLine.setVisibility(View.INVISIBLE);

                } else {

                    editCounterLine.setText("Please enter a positive number");
                }
            }
        });

        menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                menuLayout.setVisibility(View.INVISIBLE);
            }
        });

        loginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent syncScreenIntent = new Intent(CounterScreen.this, LoginScreen.class);
                startActivity(syncScreenIntent);
            }
        });

        notificationsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selectedItem = adapterView.getItemAtPosition(i).toString();

                if(selectedItem.equals("Daily"))
                {
                    alarmSetter.setNotificationsAlarm(AlarmManager.INTERVAL_DAY);

                } else if(selectedItem.equals("Weekly")){

                    alarmSetter.setNotificationsAlarm(AlarmManager.INTERVAL_DAY * 7);

                } else {

                    alarmSetter.cancelNotificationsAlarm();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        notificationsSetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notificationsSpinner.performClick();
            }
        });

        aboutMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showAboutMeDialog();
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String toastMsg;

                if(!isLoginDetailsAvailable()){

                    toastMsg = "Have to be logged-in first";

                } else {

                    //tryLogin();

                    String feedback = downloadData();

                    refreshCounter();
                    hideYesterdayButtonIfFirstDay();
                    hideTomorrowsButtonIfCurrentDate();

                    toastMsg = feedback;
                }

                Toast.makeText(CounterScreen.this, toastMsg, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        hideYesterdayButtonIfFirstDay();
        hideTomorrowsButtonIfCurrentDate();

        clickCounterIfAppCalledByWidget();

        refreshCurrentDate();
        refreshCounter();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(isFirstRunOfCounterScreen()){

            setToNotFirstRunOfCounterScreen();

            createWidgetInstallNotification();
        }

        if(isLoginDetailsAvailable()){
            Intent uploader = new Intent(CounterScreen.this, DataUploader.class);
            this.startService(uploader);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //return always to the same page
        Intent counterScreenIntent = new Intent(CounterScreen.this, CounterScreen.class);
        startActivity(counterScreenIntent);
    }
}
