package roeevakrat.betterme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import static android.content.Context.MODE_PRIVATE;

public class WidgetReceiver extends BroadcastReceiver {

    //database
    SharedPreferences appMap;
    SharedPreferences.Editor mapEditor;

    DateFormatter todaysDate = new DateFormatter();

    private void incTodaysCounterByOne(){

        int prevCounterValue = appMap.getInt(todaysDate.getDate(), 0);

        mapEditor = appMap.edit();
        mapEditor.putInt(todaysDate.getDate(), prevCounterValue + 1);
        mapEditor.apply();
    }

    private void setFlagToOpenedByWidget(Context context){

        appMap = context.getSharedPreferences(SharedPreferenceDB.getInstance().SHARED_PREFS, MODE_PRIVATE);

        mapEditor = appMap.edit();
        mapEditor.putBoolean(SharedPreferenceDB.getInstance().APP_OPENED_BY_WIDGET, true);
        mapEditor.apply();
    }

    private void runApp(Context context) {

        PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage(SharedPreferenceDB.getInstance().PACKAGE_NAME);
        context.startActivity(launchIntent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        setFlagToOpenedByWidget(context);
        runApp(context);
    }
}
