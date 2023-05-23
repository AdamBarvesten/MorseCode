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
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class LearnABC extends AppCompatActivity implements SensorEventListener {

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
    Vibrator vibrator;
    MediaPlayer mediaPlayer;
    MediaPlayer mediaPlayerPositive;
    MediaPlayer mediaPlayerNegative;
    private long lastEventTime = 0;
    private final long EVENT_THRESHOLD_MS = 700;

    private LinearLayout buttonSet1;
    private LinearLayout buttonSet2;
    private boolean isToggleOn = false;

    private SensorManager sensorManager;
    private Sensor gravitySensor;

    private long startTime = 0;
    private boolean isConditionMet = false;
    private Character currentLetter = 'a';


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_abc);


        //gravity
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);


        vibrationManager = new VibrationManager(this);
        handler = new Handler();

        mainImage = findViewById(R.id.mainImage);
        randomMorseView = findViewById(R.id.randomLetter);
        generateAlphabeticalOutput();
        mediaPlayer = MediaPlayer.create(this, R.raw.whoosh);
        mediaPlayerPositive = MediaPlayer.create(this, R.raw.pling);
        mediaPlayerPositive.setVolume(0.5f, 0.5f);
        mediaPlayerNegative = MediaPlayer.create(this, R.raw.error2);
        Button enterButton = findViewById(R.id.enterButton);
        mEdit = findViewById(R.id.editTextLetter);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        //hide keyboard
        mEdit.setCursorVisible(false);
        mEdit.setFocusableInTouchMode(false);
        mEdit.setFocusable(false);

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

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_layout_help_morse);
        Button helpButton = findViewById(R.id.help_button2);
        ImageView helpImage3 = findViewById(R.id.help_picture3);
        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helpImage3.setImageResource(R.drawable.vaxkaka_help_morse_alph);
                dialog.show();
            }
        });

        buttonSet1 = findViewById(R.id.buttonSet1);
        buttonSet2 = findViewById(R.id.buttonSet2);

        // Toggle button to swap between sets
        Button toggleButton = findViewById(R.id.switch1);
        toggleButton.setOnClickListener(v -> {
            isToggleOn = !isToggleOn;
            updateButtonSetsVisibility();
        });

        // Set initial button set visibility
        //updateButtonSetsVisibility();

        // SENSOR
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private void generateAlphabeticalOutput(){

        randomLetterOutput = morseCode.getMorse(currentLetter);
        generateMorseImage(randomLetterOutput);
        randomMorseView.setText("");

        if(currentLetter == 'z'){
            currentLetter = 'a';
        }else{
            currentLetter++;
        }
    }

    private void generateMorseImage(String s){
        String letter = morseCode.getLetter(s);
        mainImage.setImageResource(getResources().getIdentifier(letter + "morse_letter","drawable",getPackageName()));
    }

    private void checkAnswer(Editable text) {
        if(randomLetterOutput.equals(text.toString().toLowerCase())){
            View popupView = getLayoutInflater().inflate(R.layout.popup, null);

// Create the PopupWindow
            PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(popupView, Gravity.TOP, 0, 0);
            popupView.postDelayed(new Runnable(){
                public void run()
                {
                    popupView.setVisibility(View.INVISIBLE);
                }
            }, 1500);
            generateAlphabeticalOutput();
            mediaPlayerPositive.start();
            vibrator.vibrate(500);
            return;
        }else {
            View popupView = getLayoutInflater().inflate(R.layout.popup_wrong, null);
            PopupWindow popupWindow = new PopupWindow(popupView,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.showAtLocation(popupView, Gravity.TOP, 0, 0);
            popupView.postDelayed(new Runnable(){
                public void run()
                {
                    popupView.setVisibility(View.INVISIBLE);
                }
            }, 1500);

            mediaPlayerNegative.start();
            vibrator.vibrate(100);
        }
    }

    private Runnable longPressRunnable = () -> {
        isLongPress = true;
        appendDash();
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

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastEventTime >= EVENT_THRESHOLD_MS) {
                    if (mAccel > 12) {
                        mediaPlayer.start();
                        generateAlphabeticalOutput();
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
        // Check if the sensor event is from the gravity sensor
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float y = event.values[1];
            // Check the condition
            if (y < -5 && !isConditionMet) {
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - startTime >= 600) {
                    isConditionMet = true;
                    //Toast.makeText(this, "Condition met for 600ms", Toast.LENGTH_SHORT).show();
                    randomMorseView.setText(randomLetterOutput);
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

    private void updateButtonSetsVisibility() {
        if (isToggleOn) {
            buttonSet1.setVisibility(View.GONE);
            buttonSet2.setVisibility(View.VISIBLE);
        } else {
            buttonSet1.setVisibility(View.VISIBLE);
            buttonSet2.setVisibility(View.GONE);
        }
    }
    //SENSOR
}