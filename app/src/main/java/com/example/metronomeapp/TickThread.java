package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.EditText;
import androidx.annotation.NonNull;

/**
 * Thread class that outlines what subordinate threads consist of when created by the main.
 * @param playTick MediaPlayer that takes care of playing the tick noise.
 * @param playA4 MediaPlayer that plays a 440hz tone for instrument tuning
 *
 * @param TempoInput EditText field that accepts a BPM amount entered by the user.
 */

public class TickThread extends Thread {
    MediaPlayer playTick;
    MediaPlayer playA4;
    EditText TempoInput;

    public TickThread(MediaPlayer playTick, MediaPlayer playA4, EditText TempoInput) {
        this.playA4 = playA4;
        this.playTick = playTick;
        this.TempoInput = TempoInput;
    }

    @Override
    public void run() {
        long tempo = Integer.valueOf(TempoInput.getText().toString());
        //System.out.println("Thread started");
        while(!isInterrupted()) {
            playTick.start();
            //System.out.println((long) calcInterval(tempo));
            SystemClock.sleep((long) calcInterval(tempo));
        }
    }


    //Math function that returns a pause interval in milliseconds given a desired pace in BPM.
    public float calcInterval(@NonNull float tempo) {
        if (tempo >= 100) {
            if (((60 / tempo) * 1000) <= 0) {
                return 0;
            } else {
                return ((60 / tempo) * 1000) - 2;
            }
        } else {
            return ((60 / tempo) * 1000) - 2;
        }
    }

}
