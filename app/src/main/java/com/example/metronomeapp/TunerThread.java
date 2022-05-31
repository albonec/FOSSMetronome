package com.example.metronomeapp;

import android.media.MediaPlayer;

public class TunerThread extends Thread {
    MediaPlayer string;

    TunerThread(MediaPlayer string) {this.string = string;}

    @Override
    public void run() {
        if(!isInterrupted()) {
            string.start();
        } else {
            string.stop();
        }
    }
}
