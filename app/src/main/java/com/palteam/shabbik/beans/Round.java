package com.palteam.shabbik.beans;

public class Round extends BaseBean {
    private int roundNumber, firstUserScore, secondUserScore, gameId,
            roundSentence;
    private String roundConfigration;
    private boolean isFinished;
    private String timeString;
    private String isDirty;

    public String getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(String isDirty) {
        this.isDirty = isDirty;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getFirstUserScore() {
        return firstUserScore;
    }

    public void setFirstUserScore(int firstUserScore) {
        this.firstUserScore = firstUserScore;
    }

    public int getSecondUserScore() {
        return secondUserScore;
    }

    public void setSecondUserScore(int secondUserScore) {
        this.secondUserScore = secondUserScore;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public String getRoundConfigration() {
        return roundConfigration;
    }

    public void setRoundConfigration(String roundConfigration) {
        this.roundConfigration = roundConfigration;
    }

    public int getRoundSentence() {
        return roundSentence;
    }

    public void setRoundSentence(int roundSentence) {
        this.roundSentence = roundSentence;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

}
