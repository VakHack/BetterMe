package roeevakrat.betterme;

import android.content.SharedPreferences;
import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 22/11/2017.
 */

public class MapToSharedprefConvertor {

    static void convertMapToSharedpref(HashMap<String, Integer> dataMap, SharedPreferences sp){

        SharedPreferences.Editor editor;

        for(String key: dataMap.keySet()){

            editor = sp.edit();
            editor.putInt(key, dataMap.get(key));
            editor.apply();
        }
    }

    static HashMap<String, Integer> convertSharedprefsToMap(SharedPreferences sp){

        HashMap<String, Integer> map = new HashMap<>();

        Map<String, ?> entries = sp.getAll();

        for (Map.Entry<String, ?> entry : entries.entrySet()) {

            String key = entry.getKey();

            //some of the keys leading to saved non-integer values. we ignore them here
            if(entries.get(key) instanceof Integer){

                map.put(key, sp.getInt(key, 0));
            }
        }

        return map;
    }
}
