package com.example.morsecode;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MorseCode {
    private static final char[] swedish = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z'};

    private static final String[] morse = { ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..",
            ".---", "-.-", ".-..", "--", "-.", "---", ".---.", "--.-", ".-.",
            "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.." };

    private static final Map<Character, String> SE_TO_MORSE = new HashMap<>();
    private static final Map<String, Character> MORSE_TO_SE = new HashMap<>();

    static {
        for (int i = 0; i < swedish.length; i++) {
            SE_TO_MORSE.put(swedish[i], morse[i]);
            MORSE_TO_SE.put(morse[i], swedish[i]);
        }
    }

    public MorseCode(){

    }

    public String getMorse(char c){
        return SE_TO_MORSE.get(c);
    }

    public String getLetter(String s){
        return String.valueOf(MORSE_TO_SE.get(s));
    }

    public String getRandomLetter(){
        int rnd = new Random().nextInt(swedish.length);
        return String.valueOf(swedish[rnd]);
    }

    public String getRandomMorse(){
        int rnd = new Random().nextInt(morse.length);
        return String.valueOf(morse[rnd]);
    }



}
