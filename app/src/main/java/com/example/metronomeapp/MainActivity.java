package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.metronomeapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    int tempo;

    EditText TempoInput;

    Button startBtn, stopBtn;

    boolean doRun, isClicked = true;

    final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);
    final MediaPlayer playA4 = MediaPlayer.create(this, R.raw.a);

    public MainActivity() {
        super(R.layout.fragment_dashboard);
    }

    private ActivityMainBinding binding;

    HandlerThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        TempoInput = findViewById(R.id.TempoInput);

        startBtn = findViewById(R.id.startButton);
        stopBtn = findViewById(R.id.stopButton);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        startBtn.setOnClickListener(v -> {
            thread = new HandlerThread("thread");
            thread.start();
            Handler handler = new Handler(thread.getLooper()){
                @Override
                public void handleMessage(Message msg)
                {
                    super.handleMessage(msg);
                }
            };
            handler.post(runnable);
        });

        stopBtn.setOnClickListener(v -> {
            thread.quit();
        });

    }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //calculates interval (in milliseconds) at which to play the metronome tick based on tempo in BPM units.
    public float calcInterval(@NonNull float tempo) {
        if (tempo >= 100) {
            if (((60 / tempo) * 1000) <= 0) {
                return 0;
            } else {
                return ((60 / tempo) * 1000) - 10;
            }
        } else {
            return (60 / tempo) * 1000 - tempo/25;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while(true) {
                playTick.start();
                SystemClock.sleep((long)calcInterval(tempo));
            }
        }
    };

}

