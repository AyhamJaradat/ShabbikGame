package com.palteam.shabbik.beans;

import java.util.ArrayList;

public class Word {
    private ArrayList<Letter> wordLetters;
    private ArrayList<Point> wordPoints = new ArrayList<Point>();
    private String word;
    private boolean isDrawn;
    private boolean isGuessed;
    private boolean selectedLetters[];

    public boolean[] getSelectedLetters() {
        return selectedLetters;
    }

    public void setSelectedLetters(boolean[] selectedLetters) {
        this.selectedLetters = selectedLetters;
    }

    public Word() {
        wordLetters = new ArrayList<Letter>();
    }

    public boolean isGuessed() {
        return isGuessed;
    }

    public void setGuessed(boolean isGuessed) {
        this.isGuessed = isGuessed;
    }

    public ArrayList<Point> getWordPoints() {
        return wordPoints;
    }

    public void setWordPoints(ArrayList<Point> wordPoints) {
        this.wordPoints = wordPoints;
    }

    public ArrayList<Letter> getWordLetters() {
        return wordLetters;
    }

    public void setWordLetters(ArrayList<Letter> wordLetters) {
        this.wordLetters = wordLetters;
    }

    public boolean isDrawn() {
        return isDrawn;
    }

    public void setDrawn(boolean isDrawn) {
        this.isDrawn = isDrawn;
    }

    public boolean isWordHasMoreThanOneLetter(Letter letterToCheck) {

        int numberOfLetterInWord = 0;
        Letter tempLetter;
        for (Letter letter : wordLetters) {
            if (letter.getIndex() == letterToCheck.getIndex()) {
                numberOfLetterInWord++;
            }
        }

        if (numberOfLetterInWord >= 2)
            return true;

        return false;

    }

    /**
     * @param letterToCheck
     * @return
     */
    public boolean isLetterFarAway(Letter letterToCheck) {

        int numberOfLetterInWord = 0;
        Letter tempLetter;
        for (Letter letter : wordLetters) {
            if (letter.getIndex() == letterToCheck.getIndex()) {
                numberOfLetterInWord++;
            }
        }

        if (numberOfLetterInWord < 2)
            return true;

        int[] indexes = new int[numberOfLetterInWord];

        int j = 0;
        for (int i = 0; i < wordLetters.size(); i++) {
            tempLetter = wordLetters.get(i);
            if (tempLetter.getIndex() == letterToCheck.getIndex()) {
                indexes[j] = i;
                j++;
            }
        }

        int minimumMargin = 1000;
        for (int i = 1; i < numberOfLetterInWord; i++) {
            if (Math.abs(indexes[i] - indexes[i - 1]) < minimumMargin) {
                minimumMargin = Math.abs(indexes[i] - indexes[i - 1]);
            }

        }

        if (minimumMargin <= 2)
            return false;
        else
            return true;
    }

    public void setWord(String word) {
        this.word = word;

    }

    public String getWord() {
        return this.word;
    }

}
