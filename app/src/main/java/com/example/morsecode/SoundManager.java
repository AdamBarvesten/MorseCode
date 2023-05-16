package com.example.morsecode;

import android.media.AudioManager;
import android.media.ToneGenerator;

public class SoundManager {
    private final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private final MorseCode morseCode = new MorseCode();

    public SoundManager() {

    }

    public void playLetter( Character c) {
        playLetter(Character.toLowerCase(c), 2);
    }
    private void playLetter( Character c, int soundIdx){
        String morse = morseCode.getMorse(c);

        new Thread(() -> {
            synchronized (toneGenerator){
                try {
                    for(int i = 0; i < morse.length(); i++){
                        if(morse.charAt(i) == '.'){
                            playShortTone(soundIdx);
                        }else{
                            playLongTone(soundIdx);
                        }
                        toneGenerator.wait(100+500);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void playSound(int i, int len){
        switch (i){
            case 1 : toneGenerator.startTone(ToneGenerator.TONE_DTMF_8,len); break;
            case 2 : toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, len); break;
            case 3 : toneGenerator.startTone(ToneGenerator.TONE_SUP_DIAL, len); break;
            case 4 : toneGenerator.startTone(ToneGenerator.TONE_CDMA_DIAL_TONE_LITE, len); break;
        }
    }

    public void playLongTone(){ playLongTone(2);}
    public void playShortTone(){ playShortTone(2);}
    private void playLongTone(int i){playSound(i, 500);}
    private void playShortTone(int i){playSound(i, 100);}
    private void onBtnShort(int i){
        playShortTone(i);
    }
    private void onBtnLong(int i){ playLongTone(i);}
    private void onBtnLetter(int i){ playLetter('x', i); }

}
