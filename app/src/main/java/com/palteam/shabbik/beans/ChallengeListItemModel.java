package com.palteam.shabbik.beans;

import android.net.Uri;

public class ChallengeListItemModel {

    public enum ChallengeType {
        MY_CHALLENGE, FRIENDS_CHALLENGES, DONE_CHALLENGES
    }

    ;

    private int gameId;
    private Uri imageUri;
    private String name;
    private String roundNumber;
    private String timeAgo;
    private String statusMessage;
    private ChallengeType type;

    public ChallengeListItemModel() {
        super();
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(String roundNumber) {
        this.roundNumber = roundNumber;
    }

    public String getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public ChallengeType getType() {
        return type;
    }

    public void setType(ChallengeType type) {
        this.type = type;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    @Override
    public String toString() {
        return "ChallengeListItemModel [imageUri=" + imageUri + ", name="
                + name + ", roundNumber=" + roundNumber + ", timeAgo="
                + timeAgo + ", statusMessage=" + statusMessage + ", type="
                + type + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((imageUri == null) ? 0 : imageUri.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((roundNumber == null) ? 0 : roundNumber.hashCode());
        result = prime * result
                + ((statusMessage == null) ? 0 : statusMessage.hashCode());
        result = prime * result + ((timeAgo == null) ? 0 : timeAgo.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChallengeListItemModel other = (ChallengeListItemModel) obj;
        if (imageUri == null) {
            if (other.imageUri != null)
                return false;
        } else if (!imageUri.equals(other.imageUri))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (roundNumber == null) {
            if (other.roundNumber != null)
                return false;
        } else if (!roundNumber.equals(other.roundNumber))
            return false;
        if (statusMessage == null) {
            if (other.statusMessage != null)
                return false;
        } else if (!statusMessage.equals(other.statusMessage))
            return false;
        if (timeAgo == null) {
            if (other.timeAgo != null)
                return false;
        } else if (!timeAgo.equals(other.timeAgo))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

}
