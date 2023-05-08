package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MorseToLetterActivity extends AppCompatActivity {

    private final MorseCode morseCode = new MorseCode();
    private String randomLetter;
    private ImageView mainImage;
    private TextView randomLetterView;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_to_letter);

        mainImage = findViewById(R.id.mainImage);
        randomLetterView = findViewById(R.id.randomLetter);
        generateRandomletter();

        Button enterButton = findViewById(R.id.enterButton);
        EditText mEdit = findViewById(R.id.editTextLetter);
        enterButton.setOnClickListener(v -> {
            checkAnswer(mEdit.getText());
            mEdit.setText("");
        });


        // SENSOR
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        //SENSOR
    }

    private void checkAnswer(Editable text) {
        if(randomLetter.equals(text.toString().toLowerCase())){
            Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_SHORT).show();
            generateRandomletter();

            return;
        }
        Toast.makeText(getApplicationContext(), "Wrong!!", Toast.LENGTH_SHORT).show();
    }

    private void generateMorseImage(String s){
        mainImage.setImageResource(getResources().getIdentifier(s + "morse_code","drawable",getPackageName()));
    }

    private void generateRandomletter(){
        randomLetter = morseCode.getRandomLetter();
        randomLetterView.setText(randomLetter);
        generateMorseImage(randomLetter);
    }

    //SENSOR
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                generateRandomletter();
                //Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
    //SENSOR
}