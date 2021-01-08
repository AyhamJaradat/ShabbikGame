package com.palteam.shabbik.beans;

public class Letter {
    /*
     *
     * could use int as index instead of string
     */
    private String letter;

    // index is used as Id
    private int index;

    private int neighborIndex = -1;

    private int letterImage, letterImageSelected, freezLetterImage,
            freezImageSelected;

    private boolean isFreez = false;

    public Letter(Letter letterObj) {
        this.letter = letterObj.getLetter();
        this.index = letterObj.getIndex();
        this.letterImage = letterObj.getLetterImage();
        this.letterImageSelected = letterObj.getLetterImageSelected();
        this.freezLetterImage = letterObj.getFreezLetterImage();
        this.freezImageSelected = letterObj.getFreezImageSelected();
    }

    public Letter(String letter, int index, int normalImage,
                  int normalImageSelected, int freezImage, int freezImageSelected) {
        this.letter = letter;
        this.index = index;
        this.letterImage = normalImage;
        this.letterImageSelected = normalImageSelected;
        this.freezLetterImage = freezImage;
        this.freezImageSelected = freezImageSelected;
    }

    public boolean isFreez() {
        return isFreez;
    }

    public int getNeighborIndex() {
        return neighborIndex;
    }

    public void setNeighborIndex(int neighborIndex) {
        this.neighborIndex = neighborIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public void incrementNeighborIndex() {
        this.neighborIndex++;

    }

    public int getFreezLetterImage() {
        return freezLetterImage;
    }

    public int getImageId() {
        if (isFreez) {
            return freezLetterImage;
        } else {
            return letterImage;
        }

    }

    public int getSelectedImageId() {
        if (isFreez) {
            return freezImageSelected;
        } else {
            return letterImageSelected;
        }
    }

    // public void setFreezLetterImage(int freezLetterImage) {
    // this.freezLetterImage = freezLetterImage;
    // }

    public int getFreezImageSelected() {
        return freezImageSelected;
    }

    // public void setFreezImageSelected(int freezImageSelected) {
    // this.freezImageSelected = freezImageSelected;
    // }

    public int getLetterImage() {
        return letterImage;
    }

    // public void setLetterImage(int letterImage) {
    // this.letterImage = letterImage;
    // }

    public int getLetterImageSelected() {
        return this.letterImageSelected;
    }

    // public void setLetterImageSelected(int letterImageSelected) {
    // this.letterImageSelected = letterImageSelected;
    // }

    public void setFreezMode(boolean isFreez) {
        this.isFreez = isFreez;
    }

}
