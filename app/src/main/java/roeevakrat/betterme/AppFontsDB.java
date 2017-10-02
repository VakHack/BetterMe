package roeevakrat.betterme;

/**
 * Created by Administrator on 30/09/2017.
 */

public class AppFontsDB {
    private static final AppFontsDB ourInstance = new AppFontsDB();
    public static AppFontsDB getInstance() {
        return ourInstance;
    }

    final private String sarif;
    final private String sanSarif;

    private AppFontsDB() {

        sarif = "fonts/Cubano-Regular.otf";
        sanSarif = "fonts/GT Eesti Pro Text Book.otf";
    }

    public String getSarif() {
        return sarif;
    }

    public String getSanSarif() {
        return sanSarif;
    }
}
