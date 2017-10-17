package com.kenai.huafu.DataTrasmation;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.kenai.huafu.devinfocheck.R;

/**
 * Created by zhang on 2017/9/29.
 */

public class AlarmSoundPlay {

    private SoundPool mSoundPool;
    private int PlayingID = -1;
    public boolean IsSoundPlaying = false;
    public void PlaySound(Context context,int SoundID) {
        mSoundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,5);
        mSoundPool.load(context,SoundID,1);
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                PlayingID = soundPool.play(i,1,1,0,-1,1);
                IsSoundPlaying  = true;
            }
        });
    }

    public void StopSondPlay(){
        if(PlayingID == -1){ return;}
        mSoundPool.stop(PlayingID);
        IsSoundPlaying  = false;
    }

}
