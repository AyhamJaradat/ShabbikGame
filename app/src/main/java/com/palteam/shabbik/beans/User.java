package com.palteam.shabbik.beans;

import com.palteam.shabbik.utils.IConstants;

public class User  extends BaseBean implements Comparable<User>{
    private String userFirstName, userLastName, userEmail, userFBId, userKey,
            userNickName;

    private int highestScore, latestScore, numberOfWords, numberOfSentences;

    public User() {

        // Initialize all strings to NULL_VALUE in case anything sent to server without filled.
        userFirstName = IConstants.NULL_VALUE;
        userLastName = IConstants.NULL_VALUE;
        userEmail = IConstants.NULL_VALUE;
        userFBId = IConstants.NULL_VALUE;
        userKey = IConstants.NULL_VALUE;
        userNickName = IConstants.NULL_VALUE;
    }

    public User(String userFirstName, String userLastName, String userEmail,
                String userFBId, String userKey, String userNickName) {

        this.userEmail = userEmail;
        this.userFirstName = userFirstName;
        this.userFBId = userFBId;
        this.userNickName = userNickName;
        this.userLastName = userLastName;
        this.userKey = userKey;
    }

    public User(String fName, String lName, String email, String userKey,
                String facebookID) {

        this.userEmail = email;
        this.userFirstName = fName;
        this.userFBId = facebookID;
        this.userLastName = lName;
        this.userKey = userKey;

    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public int getLatestScore() {
        return latestScore;
    }

    public void setLatestScore(int latestScore) {
        this.latestScore = latestScore;
    }

    public int getNumberOfWords() {
        return numberOfWords;
    }

    public void setNumberOfWords(int numberOfWords) {
        this.numberOfWords = numberOfWords;
    }

    public int getNumberOfSentences() {
        return numberOfSentences;
    }

    public void setNumberOfSentences(int numberOfSentences) {
        this.numberOfSentences = numberOfSentences;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserFBId() {
        return userFBId;
    }

    public void setUserFBId(String userFBId) {
        this.userFBId = userFBId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    @Override
    public String toString() {
        return super.toString() + "User [userFirstName=" + userFirstName
                + ", userLastName=" + userLastName + ", userEmail=" + userEmail
                + ", userFBId=" + userFBId + ", userKey=" + userKey
                + ", userNickName=" + userNickName + "]";
    }

    @Override
    public int compareTo(User user) {
        return (this.getUserFirstName() + " " + this.getUserLastName()).compareTo(user.getUserFirstName() + " " + user.getUserLastName());
    }
}
