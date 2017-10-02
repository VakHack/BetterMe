package roeevakrat.betterme;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * Created by Administrator on 30/09/2017.  R.raw.app_resume
 */

public class SoundEffectPlayer {

    MediaPlayer effect;
    AudioManager audioStat;

    SoundEffectPlayer(Context context, int resourceName){

        effect = MediaPlayer.create(context, resourceName);
        audioStat = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public void play(){

        if(audioStat.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){

            effect.start();
        }
    }
}
