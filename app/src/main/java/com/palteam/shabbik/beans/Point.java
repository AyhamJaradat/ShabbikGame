package com.palteam.shabbik.beans;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.palteam.shabbik.R;

import java.util.ArrayList;
import java.util.Random;

public class Point {

    private final String TAG = Point.class.getSimpleName();

    private ImageView image;
    private Letter letter;
    private boolean isClicked;
    private int posX, posY;
    private boolean isTaken;
    private int numOfWordUsedIn = 0;
    private ArrayList<Point> neighbors;

    public Point(ImageView imageView) {
        this.image = imageView;
        this.neighbors = new ArrayList<Point>();
    }

    public int getNumOfWordUsedIn() {
        return this.numOfWordUsedIn;
    }

    public void setNumOfWordUsedIn(int numOfWordUsedIn) {
        this.numOfWordUsedIn = numOfWordUsedIn;
    }

    public void incrementNumOfWordUsedIn() {
        this.numOfWordUsedIn++;
    }

    public void decrementNumOfWordUsedIn() {
        this.numOfWordUsedIn--;
    }

    public ImageView getImage() {
        return this.image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public void setImageResorce(int imageR) {

        this.image.setImageResource(imageR);
    }

    public Letter getLetter() {
        return letter;
    }

    public void setLetter(Letter letter) {

        if (letter != null) {

            this.letter = letter;
            this.image.setImageResource(letter.getImageId());

        } else {

            this.isTaken = false;
            this.image.setImageResource(R.drawable.b);// only during drawing:
            // null
        }
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }

    public boolean isNeighbor(Point point) {
        int prevX = point.getPosX();
        int prevY = point.getPosY();

        if (posX - prevX == 0 && Math.abs(posY - prevY) < 2) {
            return true;
        }
        if (posY - prevY == 0 && Math.abs(posX - prevX) < 2) {
            return true;
        }

        if (Math.abs(posY - prevY) == 1 && Math.abs(posX - prevX) == 1)
            return true;

        return false;

    }

    public void animate(Context context) {
        Animation anim = AnimationUtils
                .loadAnimation(context, R.anim.animation);
        image.startAnimation(anim);

    }

    public void setPosition(int x, int y) {
        posX = x;
        posY = y;
    }

    public int getIndexBasedOnPosition(int x, int y) {

        int index = -1;
        switch (x) {
            case 1:
                index = y;
                break;
            case 2:
                index = 4 + y;
                break;
            case 3:
                index = 8 + y;
                break;
            case 4:
                index = 12 + y;
                break;

        }

        return index - 1;
    }

    public int getIndexBasedOnPosition() {

        int index = -1;
        switch (this.posX) {
            case 1:
                index = posY;
                break;
            case 2:
                index = 4 + posY;
                break;
            case 3:
                index = 8 + posY;
                break;
            case 4:
                index = 12 + posY;
                break;

        }

        return index - 1;
    }

    public boolean hasThaSameLetterAs(Letter mLetter) {

        if (this.letter.getIndex() == mLetter.getIndex())
            return true;

        return false;
    }

    public void addToNeighbors(Point point) {
        this.neighbors.add(point);
    }

    public ArrayList<Point> getNeighbors() {
        Random randomGenerator = new Random();
        // Randomly re-arrange neighbors
        int i;
        int randomIndex;
        Point tempPoint;

        int randomNumber = randomGenerator.nextInt(neighbors.size());
        for (i = 0; i < randomNumber; i++) {
            randomIndex = randomGenerator.nextInt(neighbors.size() - 2);
            tempPoint = neighbors.remove(randomIndex);
            neighbors.add(tempPoint);
        }

        return neighbors;
    }

    public void setNeighbors(ArrayList<Point> neighbors) {
        this.neighbors = neighbors;
    }

}
