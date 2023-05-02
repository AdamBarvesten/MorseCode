package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
public class Vibration extends AppCompatActivity {
    Button shortVibrate;
    Button longVibrate;
    Button vibrationl2;
    Button vibrations2;
    Button vibrationl3;

    Button vibrations3;
    Button letter;
    Vibrator vibrator;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

        shortVibrate = findViewById(R.id.short_vibrate);
        longVibrate = findViewById(R.id.long_vibrate);
        vibrationl2 = findViewById(R.id.vibration_l2);
        vibrations2 = findViewById(R.id.vibration_s2);
        vibrationl3 = findViewById(R.id.vibration_l3);
        vibrations3 = findViewById(R.id.vibration_s3);
        letter = findViewById(R.id.letters);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //Alternativ 1 - kort
        shortVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(200);
            }
        });

        //Alternativ 1 - lång
        longVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(500);
            }
        });

        //Alternativ 2 - kort
        vibrations2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(100);
            }
        });

        //Alternativ 2 - lång
        vibrationl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(300);
            }
        });

        //Alternativ 3 - kort
        vibrations3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(200);
            }
        });

        //Alternativ 3 - lång
        vibrationl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(400);
            }
        });

        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long[] timings = new long[] {200, 200, 500};
                int[] amplitudes = new int[] { 100, 0, 100 };
                int repeatIndex = -1;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
                }
            }
        });

    }


}