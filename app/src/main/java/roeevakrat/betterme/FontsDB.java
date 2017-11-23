package roeevakrat.betterme;

/**
 * Created by Administrator on 30/09/2017.
 */

public class FontsDB {
    private static final FontsDB ourInstance = new FontsDB();
    public static FontsDB getInstance() {
        return ourInstance;
    }

    final private String titleFont;
    final private String bodyFont;
    final private String helpScreenFont;

    private FontsDB() {

        titleFont = "fonts/Cubano-Regular.otf";
        bodyFont = "fonts/Linotte-SemiBold.otf";
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
