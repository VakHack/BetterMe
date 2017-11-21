package roeevakrat.betterme;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Administrator on 21/11/2017.
 */

public class UserData implements Serializable {

    public HashMap<String, Integer> badHabitsMap;

    public UserData(){

        badHabitsMap = new HashMap<>();
    }

    public UserData(HashMap<String, Integer> badHabitsMap){

        this.badHabitsMap = badHabitsMap;
    }

    public HashMap<String, Integer> getBadHabitsMap() {
        return badHabitsMap;
    }
}
