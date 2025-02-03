package com.example.metronomeapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.jtransforms.fft.DoubleFFT_1D;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class TunerActivity extends AppCompatActivity {

    private static final String TAG = "TunerActivity";
    private static final int SAMPLE_RATE = 44100;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private AudioRecord audioRecord;
    private boolean isRecording = false;
    private Thread recordingThread;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private TextView frequencyTextView;
    private TextView noteTextView;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
        if (!permissionToRecordAccepted) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        Button aButton = findViewById(R.id.AButton);
        Button dButton = findViewById(R.id.DButton);
        Button gButton = findViewById(R.id.GButton);
        Button eButton = findViewById(R.id.EButton);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRecording();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }

    private void startRecording() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e(TAG, "AudioRecord failed to initialize");
            return;
        }
        audioRecord.startRecording();
        isRecording = true;

        recordingThread = new Thread(() -> {
            short[] buffer = new short[BUFFER_SIZE];
            while (isRecording) {
                int bufferReadResult = audioRecord.read(buffer, 0, BUFFER_SIZE);
                if (bufferReadResult > 0) {
                    double frequency = calculateFrequency(buffer, bufferReadResult);
                    String note = getNoteFromFrequency(frequency);
                    mainHandler.post(() -> {
                        frequencyTextView.setText(String.format("%.2f Hz", frequency));
                        noteTextView.setText(note);
                    });
                }
            }
        }, "AudioRecorder Thread");
        recordingThread.start();
    }

    private void stopRecording() {
        isRecording = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        if (recordingThread != null) {
            try {
                recordingThread.join();
            } catch (InterruptedException e) {
                Log.e(TAG, "Error joining recording thread", e);
            }
            recordingThread = null;
        }
    }

    private double calculateFrequency(short[] buffer, int bufferSize) {
        // Convert short array to double array
        double[] doubleBuffer = new double[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            doubleBuffer[i] = (double) buffer[i];
        }

        // Apply FFT
        DoubleFFT_1D fft = new DoubleFFT_1D(bufferSize);
        double[] fftBuffer = new double[bufferSize * 2]; // FFT output is complex
        System.arraycopy(doubleBuffer, 0, fftBuffer, 0, bufferSize);
        fft.realForwardFull(fftBuffer);

        // Find the index of the peak frequency
        double maxMagnitude = 0;
        int maxMagnitudeIndex = 0;
        for (int i = 0; i < bufferSize; i++) {
            double real = fftBuffer[2 * i];
            double imaginary = fftBuffer[2 * i + 1];
            double magnitude = Math.sqrt(real * real + imaginary * imaginary);
            if (magnitude > maxMagnitude) {
                maxMagnitude = magnitude;
                maxMagnitudeIndex = i;
            }
        }

        // Calculate the frequency
        double frequency = (double) maxMagnitudeIndex * SAMPLE_RATE / bufferSize;
        return frequency;
    }

    private String getNoteFromFrequency(double frequency) {
        if (frequency < 441 && frequency > 439) {
            return "A";
        } else if (frequency < 197 && frequency > 195) {
            return "G";
        } else if (frequency < 294 && frequency > 292) {
            return "D";
        } else if (frequency < 660 && frequency > 658) {
            return "G";
        }
        return "";
    }
}
