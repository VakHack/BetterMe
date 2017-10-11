package roeevakrat.betterme;

/**
 * Created by Administrator on 01/10/2017.
 */

public class KeysDB {
    private static final KeysDB ourInstance = new KeysDB();

    public static KeysDB getInstance() {
        return ourInstance;
    }

    public final String WEEKLY_CHART_SCREEN_FIRST_RUN = "weeklyChartScreenFirstRun";
    public final String FIRST_RUN_DATE = "firstRunDate";
    public final String TODAYS_CHECK_FOR_FEEDBACK = "todaysChecked: ";
    public final String COUNTER_SCREEN_FIRST_RUN = "counterScreenFirstRun";
    public final String SHARED_PREFS = "betterMePrefs";
    public final String APP_FIRST_RUN_BOOL  = "isItTheFirstRun";
    public final String APP_OPENED_BY_WIDGET = "isOpenedByWidget";
    public final String LOGGED_IN_CLOUD = "isLoggedIn";
    public final String USER_PASSWORD = "userPassword";
    public final String USERNAME = "username";
    public final String LOG_KEY  = "bettermelog";

    public String PACKAGE_NAME;
}
