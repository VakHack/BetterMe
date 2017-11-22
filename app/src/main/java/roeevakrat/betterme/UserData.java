package roeevakrat.betterme;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 21/11/2017.
 */

public class UserData implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    private HashMap<String, Integer> badHabitsMap;
    private String firstRunDate;

    public UserData(HashMap<String, Integer> badHabitsMap, String firstRunDate){

        this.badHabitsMap = badHabitsMap;
        this.firstRunDate = firstRunDate;
    }

    public HashMap<String, Integer> getBadHabitsMap() {
        return badHabitsMap;
    }

    public String getFirstRunDate() {
        return firstRunDate;
    }
}
