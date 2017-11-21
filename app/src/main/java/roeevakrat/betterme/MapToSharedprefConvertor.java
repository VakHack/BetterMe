package roeevakrat.betterme;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 22/11/2017.
 */

public class MapToSharedprefConvertor {

    static void convertMapToSharedpref(HashMap<String, Integer> dataMap, SharedPreferences sp){

        SharedPreferences.Editor editor = sp.edit();

        for(String key: dataMap.keySet()){

            editor.putInt(key, dataMap.get(key));
        }

        editor.apply();
    }

    static HashMap<String, Integer> convertSharedprefsToMap(SharedPreferences sp){

        HashMap<String, Integer> map = new HashMap<>();

        Map<String, ?> entries = sp.getAll();

        for (Map.Entry<String, ?> entry : entries.entrySet()) {

            map.put(entry.getKey(), sp.getInt(entry.getKey(), 0));
        }

        return map;
    }
}
