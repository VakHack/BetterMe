package roeevakrat.betterme;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;

public class WidgetBroadcastReceiver extends BroadcastReceiver {

    //database
    SharedPreferences appMap;
    SharedPreferences.Editor mapEditor;

    private void setFlagToOpenedByWidget(Context context){

        appMap = context.getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, Context.MODE_PRIVATE);

        mapEditor = appMap.edit();
        mapEditor.putBoolean(KeysDB.getInstance().APP_OPENED_BY_WIDGET, true);
        mapEditor.apply();
    }

    private void runApp(Context context) {

        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(KeysDB.getInstance().PACKAGE_NAME);
        context.startActivity(launchIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.e("bettermelog", "signal received");

        setFlagToOpenedByWidget(context);
        runApp(context);
    }
}
