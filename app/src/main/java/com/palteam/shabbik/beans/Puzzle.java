package com.palteam.shabbik.beans;

public class Puzzle extends BaseBean {
    private String puzzle;
    private String revertedPuzzle;
    private String allWords;
    private String revertedAllWords;

    public Puzzle() {
        super();
    }

    public Puzzle(int roundId, String puzzle, String allWords,
                  String reveertedPuzzle, String revertedAllWords) {
        super();
        this.id = roundId;
        this.puzzle = puzzle;
        this.allWords = allWords;
        this.revertedPuzzle = reveertedPuzzle;
        this.revertedAllWords = revertedAllWords;
    }

    public String getRevertedPuzzle() {
        return revertedPuzzle;
    }

    public void setRevertedPuzzle(String revertedPuzzle) {
        this.revertedPuzzle = revertedPuzzle;
    }

    public String getRevertedAllWords() {
        return revertedAllWords;
    }

    public void setRevertedAllWords(String revertedAllWords) {
        this.revertedAllWords = revertedAllWords;
    }

    public String getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(String puzzle) {
        this.puzzle = puzzle;
    }

    public String getAllWords() {
        return allWords;
    }

    public void setAllWords(String allWords) {
        this.allWords = allWords;
    }

    @Override
    public String toString() {
        return "Puzzle [id = " + id + ", puzzle=" + puzzle
                + ", revertedPuzzle=" + revertedPuzzle + ", allWords="
                + allWords + ", revertedAllWords=" + revertedAllWords + "]";
    }

}
