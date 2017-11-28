package roeevakrat.betterme;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 01/10/2017.
 */

public class SharedPreferenceDB extends BetterMeDB {
    private static final SharedPreferenceDB ourInstance = new SharedPreferenceDB();

    public static SharedPreferenceDB getInstance() {
        return ourInstance;
    }

    public final String SHARED_PREFS = "betterMePrefs";

    public final String COUNTER_SCREEN_FIRST_RUN = "counterScreenFirstRun";
    public final String APP_FIRST_RUN_BOOL  = "isItTheFirstRun";
    public final String APP_OPENED_BY_WIDGET = "isOpenedByWidget";
    public final String LOGGED_IN_CLOUD = "isLoggedIn";
    public final String USERNAME = "username";
    public final String NOTIFICATIONS_INTERVAL_CATEGORY = "notificationsInterval";

    private String PackageName;
    private Context context;
    private SharedPreferences sp;

    final String[] strKeys = {"firstRunDate", "userPassword", "username", }

    private void init(Context context){

        this.context = context;
        PackageName = context.getPackageName();
        sp = context.getSharedPreferences(SharedPreferenceDB.getInstance().SHARED_PREFS, context.MODE_PRIVATE);
    }

    @Override
    public String getStr(StrOptions option) {
        return null;
    }

    @Override
    public void putStr(StrOptions option, String val) {

    }

    @Override
    public boolean getBool(BoolOptions option) {
        return false;
    }

    @Override
    public void putBool(BoolOptions option, boolean val) {

    }

    @Override
    public long getLong(LongOptions option) {
        return 0;
    }

    @Override
    public void putLong(LongOptions option, long val) {

    }

    @Override
    public int getDailyCounter(DateFormatter date) {
        return 0;
    }

    @Override
    public void putDailyCounter(DateFormatter date, int val) {

    }
}
