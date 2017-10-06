package roeevakrat.betterme;

/**
 * Created by Administrator on 30/09/2017.
 */

public class AppFontsDB {
    private static final AppFontsDB ourInstance = new AppFontsDB();
    public static AppFontsDB getInstance() {
        return ourInstance;
    }

    final private String titleFont;
    final private String bodyFont;
    final private String helpScreenFont;

    private AppFontsDB() {

        titleFont = "fonts/Cubano-Regular.otf";
        bodyFont = "fonts/GT Eesti Pro Text Book.otf";
        helpScreenFont = "fonts/Stay_Writer.ttf";
    }

    public String getTitleFont() {
        return titleFont;
    }

    public String getBodyFont() {
        return bodyFont;
    }

    public String getHelpScreenFont() {return helpScreenFont;}
}
