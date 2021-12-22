package com.example.metronomeapp;

import android.media.MediaPlayer;
import android.os.SystemClock;
import android.widget.EditText;

public class TickThread extends Thread implements Runnable {
    MediaPlayer playTick;
    MediaPlayer playA4;
    EditText TempoInput;
    long tempo;

    TickThread(MediaPlayer playTick, MediaPlayer playA4, long tempo, EditText TempoInput) {
        this.playTick = playTick;
        this.playA4 = playA4;
        this.tempo = tempo;
        this.TempoInput = TempoInput;
    }

    @Override
    public void run() {
        if(tempo == 440) {
            playA4.start();
        }
        while(!isInterrupted()) {
            tempo = Integer.valueOf(TempoInput.getText().toString());
            playTick.start();
            SystemClock.sleep(tempo);
        }
    }

}
