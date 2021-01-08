package com.palteam.shabbik.beans;

import java.util.ArrayList;

public class Sentence {
    private ArrayList<Word> sentenceWords;
    private boolean isDrawn;

    public Sentence() {
        sentenceWords = new ArrayList<Word>();
    }

    public ArrayList<Word> getSentenceWords() {
        return sentenceWords;
    }

    public void setSentenceWords(ArrayList<Word> sentenceWords) {
        this.sentenceWords = sentenceWords;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean isDrawn) {
        this.isDrawn = isDrawn;
    }

}
