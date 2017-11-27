package roeevakrat.betterme;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;

public class DataUploader extends IntentService {

    private SharedPreferences appMap;
    private ServerHandler server;

    public DataUploader() {
        super("DataUploader");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        appMap = getSharedPreferences(KeysDB.getInstance().SHARED_PREFS, MODE_PRIVATE);
        server = new FirebaseServerHandler(this);

        return super.onStartCommand(intent, flags, startId);
    }

    private String getFirstRunDate(){

        DateGenerator today = new DateGenerator();
        String firstRunDateStr = appMap.getString(KeysDB.getInstance().FIRST_RUN_DATE, today.getDate());

        return new DateGenerator(firstRunDateStr).getDate();
    }

    private String uploadData(){

        HashMap<String, Integer> badHabitMap = MapToSharedprefConvertor.sharedprefsToMap(appMap);
        server.tryUploadData(new UserData(badHabitMap, getFirstRunDate()));

        return server.getStorageFeedback();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        uploadData();
    }
}
