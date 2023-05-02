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

    Button letter;
    Vibrator vibrator;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

       shortVibrate = findViewById(R.id.short_vibrate);
       longVibrate = findViewById(R.id.long_vibrate);
       letter = findViewById(R.id.letters);
       vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


       shortVibrate.setOnClickListener(view -> vibrator.vibrate(200));

        longVibrate.setOnClickListener(view -> vibrator.vibrate(500));

        letter.setOnClickListener(view -> {
            long[] timings = new long[] {200, 200, 500};
            int[] amplitudes = new int[] { 100, 0, 100 };
            int repeatIndex = -1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(timings, amplitudes, repeatIndex));
            }
        });

    }


}