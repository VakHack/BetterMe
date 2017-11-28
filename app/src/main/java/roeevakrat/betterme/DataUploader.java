package roeevakrat.betterme;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import java.util.HashMap;

public class DataUploader extends IntentService {

    private SharedPreferences appMap;
    private ServerHandler server;

    public DataUploader() {
        super("DataUploader");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        appMap = getSharedPreferences(SharedPreferenceDB.getInstance().SHARED_PREFS, MODE_PRIVATE);
        server = new FirebaseServerHandler(this);

        return super.onStartCommand(intent, flags, startId);
    }

    private String getFirstRunDate(){

        DateFormatter today = new DateFormatter();

        return appMap.getString(SharedPreferenceDB.getInstance().FIRST_RUN_DATE, today.getDate());
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
