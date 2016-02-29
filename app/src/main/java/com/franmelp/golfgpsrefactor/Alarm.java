package com.franmelp.golfgpsrefactor;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.IOException;

/**
 * Created by francescomelpignano on 28/02/16.
 */
public class Alarm {
    Context context;
    MediaPlayer mp;
    AudioManager mAudioManager;
    Uri alarmSound;


    public Alarm(Context c, String soundURI) { // constructor for my alarm controller class
        this.context = c;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        mp = new MediaPlayer();


        setUpsound(soundURI);

    }

    public void play(){
        if (!mp.isPlaying()){
            mp.setLooping(true);
            mp.start();
        }

    }

    public void pause(){
        mp.setLooping(false);
    }


    public void setUpsound(String soundURI){
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        try{
            alarmSound = Uri.parse(soundURI);
        }catch(Exception e){
            alarmSound = ringtoneUri;
        }

        mAudioManager.setStreamVolume(AudioManager.STREAM_ALARM, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM), AudioManager.FLAG_PLAY_SOUND);


        try{
            mp.setDataSource(context, alarmSound);
            mp.setAudioStreamType(AudioManager.STREAM_ALARM);
            mp.prepare();
        }catch (IOException e){

        }
    }


    public void releasePlayer(){
        mp.release();
    }
}
