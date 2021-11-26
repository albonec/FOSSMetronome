package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.example.metronomeapp.databinding.ActivityMainBinding;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.fragment_dashboard);
    }

    int tempo;

    EditText TempoInput;

    Button startStopBtn;

    boolean[] isStopButtonPressed = {false};

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        TempoInput = findViewById(R.id.TempoInput);

        startStopBtn = findViewById(R.id.startStopButton);

        final MediaPlayer[] playTick = {MediaPlayer.create(this, R.raw.tick)};

        final Runnable loopTick = new Runnable() {
            @Override
            public void run() {
                if (playTick[0] != null) {
                    if (playTick[0].isPlaying()) {
                        playTick[0].stop();
                    }
                    playTick[0].start();
                }
            }
        };

        playTick[0].setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startStopBtn.postDelayed(loopTick, calcInterval(tempo));
            }
        });

        playTick[0].setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });

        playTick[0].setLooping(true);

        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playTick[0] != null && playTick[0].isPlaying()) {
                    playTick[0].stop();
                    playTick[0].release();
                    playTick[0] = null;
                    try {
                        playTick[0].prepare();
                    } catch (IllegalStateException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    loopTick.run();
                }
            }
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
    }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    //calculates interval (in microseconds) at which to play the metronome tick based on tempo in BPM units.
    public long calcInterval(@NonNull int tempo) {
        if (tempo <= 0) {
            return -1;
        } else {
            double rawInterval = 60 / tempo;
            long translatedInterval = Double.valueOf(1000 * rawInterval).longValue();
            return translatedInterval;
        }
    }
}