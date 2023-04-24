package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;

public class Vibration extends AppCompatActivity {
    Button btVibrate;
    Vibrator vibrator;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration);

       btVibrate = findViewById(R.id.bt_vibrate);
       vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

       btVibrate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               vibrator.vibrate(1000);
           }
       });

    }


}