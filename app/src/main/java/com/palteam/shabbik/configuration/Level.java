package com.palteam.shabbik.configuration;

import java.util.ArrayList;

public class Level {
    private int stage;
    private ArrayList<String> TrueWord;
    private int levelScore;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public void addCorrectWord(String word) {
        this.TrueWord.add(word);
    }

    public ArrayList<String> getTrueWord() {
        return TrueWord;
    }

    public void setTrueWord(ArrayList<String> trueWord) {
        TrueWord = trueWord;
    }

    public int getLevelScore() {
        return levelScore;
    }

    public void setLevelScore(int levelScore) {
        this.levelScore = levelScore;
    }

}
