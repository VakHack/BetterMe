package roeevakrat.betterme;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;
import static roeevakrat.betterme.R.id.counterView;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    SharedPreferences countersMap;
    DateGenerator todaysDate;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            Intent incCounter = new Intent(context, WidgetBroadcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, incCounter, 0);

            RemoteViews ButtonCounterView = new RemoteViews(context.getPackageName(), R.layout.home_screen_widget);
            ButtonCounterView.setOnClickPendingIntent(R.id.widgetCounterButton, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, ButtonCounterView);
        }
    }

}

