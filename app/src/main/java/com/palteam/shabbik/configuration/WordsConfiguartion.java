package com.palteam.shabbik.configuration;

import java.util.ArrayList;

public class WordsConfiguartion {
    private ArrayList<String> trueWordList;
    int score;

    public WordsConfiguartion() {
        this.trueWordList = new ArrayList<String>();
    }

    public void setWordSelected(String word) {
        this.trueWordList.add(word);
    }

    public ArrayList<String> getWordSelected() {
        return this.trueWordList;
    }

}
