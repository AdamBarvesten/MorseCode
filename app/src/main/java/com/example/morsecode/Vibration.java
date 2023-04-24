package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
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


       shortVibrate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               vibrator.vibrate(200);
           }
       });

        longVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(500);
            }
        });

        letter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vibrator.vibrate(200);
                
                vibrator.vibrate(500);
            }
        });

    }


}