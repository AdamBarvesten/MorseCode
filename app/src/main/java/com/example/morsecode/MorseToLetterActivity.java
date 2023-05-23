package com.example.morsecode;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MorseToLetterActivity extends AppCompatActivity implements SensorEventListener {

    private final MorseCode morseCode = new MorseCode();
    private String randomLetter;
    private ImageView mainImage;
    private TextView randomLetterView;

    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerPositive;
    MediaPlayer mediaPlayerNegative;
    Vibrator vibrator;

    private long lastEventTime = 0;
    private final long EVENT_THRESHOLD_MS = 700;

    private int motioncoefficient = 12;
    private SensorManager sensorManager;
    private Sensor gravitySensor;
    private long startTime = 0;
    private boolean isConditionMet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_to_letter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        mainImage = findViewById(R.id.mainImage);
        randomLetterView = findViewById(R.id.randomLetter);
        generateRandomletter();
        mediaPlayer = MediaPlayer.create(this, R.raw.whoosh);
        mediaPlayerPositive = MediaPlayer.create(this, R.raw.pling);
        mediaPlayerPositive.setVolume(0.3f, 0.3f);
        mediaPlayerNegative = MediaPlayer.create(this, R.raw.error2);
        Button enterButton = findViewById(R.id.enterButton);
        EditText mEdit = findViewById(R.id.editTextLetter);
        enterButton.setOnClickListener(v -> {
            checkAnswer(mEdit.getText());
            mEdit.setText("");
        });
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

       /* Button motcoef1 = findViewById(R.id.motcoef_1);
        Button motcoef2 = findViewById(R.id.motcoef_2);
        Button motcoef3 = findViewById(R.id.motcoef_3);
        Button motcoef4 = findViewById(R.id.motcoef_4);


        motcoef1.setOnClickListener(v -> motioncoefficient = 3);
        motcoef2.setOnClickListener(v -> motioncoefficient = 6);
        motcoef3.setOnClickListener(v -> motioncoefficient = 12);
        motcoef4.setOnClickListener(v -> motioncoefficient = 25);*/
        // SENSOR
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
        //SENSOR

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_layout_help_letter);
        Button helpButton2 = findViewById(R.id.help_button2);
        ImageView helpImage2 = findViewById(R.id.help_picture2);
        helpButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpImage2.setImageResource(R.drawable.vaxkaka_help_letter);
                dialog.show();
            }
        });
    }



    private void checkAnswer(Editable text) {
        if(randomLetter.equals(text.toString().toLowerCase())){
            Toast.makeText(getApplicationContext(), "Correct!!", Toast.LENGTH_SHORT).show();
            generateRandomletter();
            mediaPlayerPositive.start();
            vibrator.vibrate(500);
            return;
        }
        Toast.makeText(getApplicationContext(), "Wrong!!", Toast.LENGTH_SHORT).show();
        mediaPlayerNegative.start();
        vibrator.vibrate(100);
    }

    private void generateMorseImage(String s){
        mainImage.setImageResource(getResources().getIdentifier(s + "morse_code","drawable",getPackageName()));
    }

    private void generateRandomletter(){
        randomLetter = morseCode.getRandomLetter();
        generateMorseImage(randomLetter);
        randomLetterView.setText("");
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
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastEventTime >= EVENT_THRESHOLD_MS) {
                    if (mAccel > motioncoefficient) {
                        mediaPlayer.start();
                        generateRandomletter();
                        vibrator.vibrate(100);
                        lastEventTime = currentTime;
                    }
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float y = event.values[1];
            if (y < -5 && !isConditionMet) {
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - startTime >= 600) {
                    isConditionMet = true;
                    randomLetterView.setText(randomLetter);
                }
            } else {
                startTime = 0;
                isConditionMet = false;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    //SENSOR
}