package com.example.metronomeapp;


import android.content.Intent;
import android.gesture.Gesture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.metronomeapp.databinding.ActivityMainBinding;

public class  MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    private ActivityMainBinding binding;

    private static MainActivity mainActivity = new MainActivity();

    boolean wasClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);

        Button startStopBtn, TunerButton;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        startStopBtn = findViewById(R.id.startStopButton);
        TunerButton = findViewById(R.id.TunerButton);
        final TickThread[] thread = new TickThread[1];


        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wasClicked) {
                    thread[0].interrupt();
                    wasClicked = false;
                    startStopBtn.setText("START");
                } else {
                    thread[0] = new TickThread(playTick, findViewById(R.id.TempoInput)); //using direct assignment rather than variable reference for less confusion.
                    thread[0].start();
                    wasClicked = true;
                    startStopBtn.setText("STOP");
                }
            }
        });

        TunerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, TunerActivity.class));
            }
        });

    }

    //Debug function to show text when necessary
    public static void showToast(String text) {
        Toast.makeText(mainActivity, text, Toast.LENGTH_SHORT).show();
    }
}

