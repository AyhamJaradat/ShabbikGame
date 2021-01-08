package com.palteam.shabbik.beans;

public class Game extends BaseBean  {
    private int gameMode;
    private String timeString;
    private int firstUserId, secondUserId;
    private int currentRoundId, userTurnId;
    private String isDirty;
    private String isFinished;

    private User firstUser, secondUser;
    private Round firstRound, secondRound, thirdRound;

    public Round getFirstRound() {
        return firstRound;
    }

    public void setFirstRound(Round firstRound) {
        this.firstRound = firstRound;
    }

    public Round getSecondRound() {
        return secondRound;
    }

    public void setSecondRound(Round secondRound) {
        this.secondRound = secondRound;
    }

    public Round getThirdRound() {
        return thirdRound;
    }

    public void setThirdRound(Round thirdRound) {
        this.thirdRound = thirdRound;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }

    public int getCurrentRoundId() {
        return currentRoundId;
    }

    public void setCurrentRoundId(int currentRoundId) {
        this.currentRoundId = currentRoundId;
    }

    public int getUserTurnId() {
        return userTurnId;
    }

    public void setUserTurnId(int userTurnId) {
        this.userTurnId = userTurnId;
    }

    public String getIsDirty() {
        return isDirty;
    }

    public void setIsDirty(String isDirty) {
        this.isDirty = isDirty;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(int firstUserId) {
        this.firstUserId = firstUserId;
    }

    public int getSecondUserId() {
        return secondUserId;
    }

    public void setSecondUserId(int secondUserId) {
        this.secondUserId = secondUserId;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }

}
