package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.metronomeapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    int tempo;

    EditText TempoInput;

    ToggleButton startStopBtn;

    boolean doRun = false;

    private Handler handler = new Handler();

    public MainActivity() {
        super(R.layout.fragment_dashboard);
    }

    final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        startStopBtn = findViewById(R.id.startStopBtn);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();

        startStopBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startThread();
                } else {

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

    public void startThread() {
        TickThread thread = new TickThread(playTick, tempo);
        thread.start();
    }

    class TickThread extends Thread {
        MediaPlayer playTick;
        int tempo;

        TickThread(MediaPlayer playTick, int tempo) {
            this.playTick = playTick;
            this.tempo = tempo;
        }

        @Override
        public void run() {
            while (true) {
                tempo = Integer.valueOf(TempoInput.getText().toString());
                playTick.start();
                SystemClock.sleep((long) calcInterval(tempo));
                //showToast(String.valueOf(calcInterval(tempo)));
            }
        }
    }
}

