package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class LetterToMorseActivity extends AppCompatActivity {

    private final MorseCode morseCode = new MorseCode();
    private String randomLetterOutput;
    private ImageView mainImage;
    private TextView randomMorseView;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private SoundManager soundManager = new SoundManager();
    VibrationManager vibrationManager;
    private Handler handler;
    private boolean isLongPress = false;
    EditText mEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_to_morse);

        vibrationManager = new VibrationManager(this);
        handler = new Handler();

        mainImage = findViewById(R.id.mainImage);
        randomMorseView = findViewById(R.id.randomLetter);
        generateRandomOutput();

        Button enterButton = findViewById(R.id.enterButton);
        mEdit = findViewById(R.id.editTextLetter);
        enterButton.setOnClickListener(v -> {
            checkAnswer(mEdit.getText());
            mEdit.setText("");
        });

        Button dotButton = findViewById(R.id.dot_button);
        dotButton.setOnClickListener(v ->{
            appendDot();
        });

        Button dashButton = findViewById(R.id.dash_button);
        dashButton.setOnClickListener(v ->{
            appendDash();
        });

        Button backspaceButton = findViewById(R.id.backspace_button);
        backspaceButton.setOnClickListener(v ->{
            Editable text = mEdit.getText();
            int len = text.length();
            if(len > 0){
                text = text.delete(text.length()-1, text.length());
                mEdit.setText(text);
            }
        });

        Button toneButton = findViewById(R.id.tone_button);
        toneButton.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isLongPress = false;
                    handler.postDelayed(longPressRunnable, 400); // Adjust the long press duration as needed
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(longPressRunnable);
                    if (!isLongPress) {
                        appendDot();
                    }
                    break;
            }
            return false;
        });


        // SENSOR
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }


    private void generateRandomOutput(){
        randomLetterOutput = morseCode.getRandomMorse();
        randomMorseView.setText(randomLetterOutput);
        generateMorseImage(randomLetterOutput);
    }

    private void generateMorseImage(String s){
        String letter = morseCode.getLetter(s);
        mainImage.setImageResource(getResources().getIdentifier(letter + "morse_letter","drawable",getPackageName()));
    }

    private void checkAnswer(Editable text) {
        if(randomLetterOutput.equals(text.toString().toLowerCase())){
            Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_SHORT).show();
            generateRandomOutput();
            return;
        }
        Toast.makeText(getApplicationContext(), "Wrong!!", Toast.LENGTH_SHORT).show();
    }



    private Runnable longPressRunnable = new Runnable() {
        @Override
        public void run() {
            isLongPress = true;
            appendDash();
        }
    };

    private void appendDot() {
        soundManager.playShortTone();
        vibrationManager.playShortVibration();
        mEdit.append(".");
    }

    private void appendDash() {
        soundManager.playLongTone();
        vibrationManager.playLongVibration();
        mEdit.append("-");
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
                generateRandomOutput();
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