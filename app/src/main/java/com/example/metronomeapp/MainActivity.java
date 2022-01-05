package com.example.metronomeapp;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.metronomeapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    private ActivityMainBinding binding;

    EditText TempoInput;

    boolean wasClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final MediaPlayer playTick = MediaPlayer.create(this, R.raw.tick);
        final MediaPlayer playA4 = MediaPlayer.create(this, R.raw.a);

        Button startStopBtn;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        startStopBtn = findViewById(R.id.startStopButton);
        final TickThread[] thread = new TickThread[1];


        startStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wasClicked) {
                    thread[0].interrupt();
                    wasClicked = false;
                    startStopBtn.setText("START");
                } else {
                    thread[0] = new TickThread(playTick, playA4, findViewById(R.id.TempoInput));
                    thread[0].start();
                    wasClicked = true;
                    startStopBtn.setText("STOP");
                }
            }
        });

    }

    //Debug function to show text when necessary
    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}

