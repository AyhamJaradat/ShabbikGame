package com.palteam.shabbik.beans;

public class CorrectWord {

    private int scores;
    private String word;
    private int[] path;
    private int pathLength;
    private String puzzleString;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getPuzzleString() {
        return puzzleString;
    }

    public void setPuzzleString(String puzzleString) {
        this.puzzleString = puzzleString;
    }

    public CorrectWord() {

    }

    public int[] getPath() {
        return path;
    }

    public void setPath(int[] path) {
        this.path = path;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }

    public CorrectWord(int scores, String word, int[] path, int pathLength,
                       String puzzle) {

        this.scores = scores;
        this.word = word;
        this.path = path;
        this.pathLength = pathLength;
        this.puzzleString = puzzle;
    }

    public int getScores() {
        return scores;
    }

    public void setScores(int score) {
        this.scores = score;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

}
