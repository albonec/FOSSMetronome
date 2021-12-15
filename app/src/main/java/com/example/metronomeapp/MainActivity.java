package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
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

    Thread thread;

    boolean doRun, isClicked = true;

    final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);
    final MediaPlayer playA4 = MediaPlayer.create(this, R.raw.A);

    public MainActivity() {
        super(R.layout.fragment_dashboard);
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        TempoInput = findViewById(R.id.TempoInput);

        startBtn = findViewById(R.id.startButton);
        stopBtn = findViewById(R.id.stopButton);

        thread = null;
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        startBtn.setOnClickListener(v -> {
            thread = new Thread(() -> {
                tempo = Integer.valueOf(TempoInput.getText().toString());
                if(tempo == 440) {playA4.start();}
                while (true) {
                    try {
                        Thread.sleep(tempo);
                        playTick.start();
                        Log.d("time", "run: ");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        });

        stopBtn.setOnClickListener(v -> {
            if(thread != null) {
                try {
                    thread.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
}

