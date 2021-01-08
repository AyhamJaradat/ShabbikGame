package com.palteam.shabbik.utils;

import com.palteam.shabbik.beans.Letter;
import com.palteam.shabbik.beans.Point;
import com.palteam.shabbik.beans.Sentence;

import java.util.ArrayList;
import java.util.Random;

public class SentenceDrower {
    private String TAG = getClass().getSimpleName();

    private ArrayList<Point> coloredPoints;
    // random generator
    Random randomGenerator = new Random();
    private Point lastPointDrown;
    ArrayList<Point> neighbors;
    private int randoumCounter = 0;
    private int nextNeighbor; // will hold a good neighbor index for next letter

    private int MAX_NUM_OF_TRIES = 1;
    private int numberOfLoops = 0;

    public boolean drowSentence(Sentence sentence,
                                ArrayList<Point> pointsArrayList) {

        // Initialize array to trace points with letter
        coloredPoints = new ArrayList<Point>();
        // start with first word , the first letter
        int wordIndexInSentence = 0;
        int letterIndexInWord = 0;

        // nextIndex is the next point to draw inside
        int nextIndex = 0;

        // we try to fit word in a location 4 times ... if not good we delete
        // previous word and repeat
        boolean goBackToPreviousWord = false; // flag to determine when word has
        // no place

        // when we do not find place for a letter .. we try to put the letter
        // before it in different neighbor
        boolean tryOtherNeighbor = false;

        // first Loop ,,, cover all words in the sentence
        while (wordIndexInSentence < sentence.getSentenceWords().size()) {

            // Log.d("in first loop", "word index " + wordIndexInSentence);
            numberOfLoops++;
            if (numberOfLoops >= (1000 * sentence.getSentenceWords().size())) {

                return false;
            }

            // when we deleted all words during backtracking ,, we start from
            // beginning
            if (wordIndexInSentence < 0) {

                //
                // Log.d("wordIndexInSentence < 0", "word index "
                // + wordIndexInSentence);

                wordIndexInSentence = 0;
                letterIndexInWord = 0;
                // re-set all flags

                goBackToPreviousWord = false;
                tryOtherNeighbor = false;

                // clear all letters and points
                for (Point point : pointsArrayList) {
                    point.setTaken(false);
                    point.setLetter(null);
                    point.setNumOfWordUsedIn(0);
                }
                coloredPoints.clear();

            }
            // when there are no place for word ,, we try new random for it ..
            // we only try 4 times ... then we remove word before it and try
            // again
            if (goBackToPreviousWord) {

                // Try new random for this word
                // try 4 times , if not found , delete word before it

                // we are trying for new word ,, reset flags
                goBackToPreviousWord = false;
                tryOtherNeighbor = false;
                nextIndex = randomGenerator.nextInt(16);// 0 ... 15
                randoumCounter++;
                letterIndexInWord = 0;
                // do not delete previous word if you are in first word
                if (randoumCounter > 4) {

                    if (wordIndexInSentence > 0) {
                        // enough trying , delete world before
                        // Log.i("Delete Word", "wordIndex "
                        // + (wordIndexInSentence - 1));
                        // get all points in previous word and set them free
                        for (Point point : sentence.getSentenceWords()
                                .get(wordIndexInSentence - 1).getWordPoints()) {
                            // Log.i("del Word Letter", point.getLetter()
                            // .getLetter());

                            point.decrementNumOfWordUsedIn();
                            if (point.getNumOfWordUsedIn() == 0) {
                                point.setTaken(false);
                                point.setLetter(null);
                            }
                        }
                        // clear the previous word history
                        sentence.getSentenceWords()
                                .get(wordIndexInSentence - 1).getWordPoints()
                                .clear();
                        // go back to previous word
                        wordIndexInSentence--;
                        letterIndexInWord = 0;
                        continue;
                    } else {
                        // i tried 4 time ,, and this is first word
                        // there is no previous word to delete
                        letterIndexInWord = 0;
                        continue;

                    }
                }
            } else {

                // NEW WORD ... find Random
                // Log.e("START New WORD", "word index " + wordIndexInSentence);
                nextIndex = randomGenerator.nextInt(16);// 0 ... 15
                letterIndexInWord = 0;
            }

            // keep searching for good start point
            if (pointsArrayList.get(nextIndex).isTaken()

                    && !pointsArrayList.get(nextIndex).hasThaSameLetterAs(
                    sentence.getSentenceWords()
                            .get(wordIndexInSentence).getWordLetters()
                            .get(letterIndexInWord)))
                continue;

            // Log.e("in first loop", "first nextIndex " + nextIndex);

            // middle loop ... cover all letters in a world
            while (letterIndexInWord < sentence.getSentenceWords()
                    .get(wordIndexInSentence).getWordLetters().size()) {

                // Log.e("in second loop", "letterIndexInWord "
                // + letterIndexInWord);

                // if no luck with this word ... try new random 4 times ... and
                // then go delete previous word
                if (letterIndexInWord < 0) {
                    // Log.e("in second loop", "letterIndexInWord less than 0");

                    // things are wrong
                    // go back to previous world
                    goBackToPreviousWord = true;
                    break;

                }

                // draw letter
                // set point is taken
                // give the letter to the point
                // mark it as taken
                if (!tryOtherNeighbor && !goBackToPreviousWord) {

                    if (pointsArrayList.get(nextIndex).isTaken()) {
                        pointsArrayList.get(nextIndex)
                                .incrementNumOfWordUsedIn();
                    } else {
                        pointsArrayList.get(nextIndex).setLetter(
                                new Letter(sentence.getSentenceWords()
                                        .get(wordIndexInSentence)
                                        .getWordLetters()
                                        .get(letterIndexInWord)));

                        // Increment numOfWordUsedIn
                        pointsArrayList.get(nextIndex)
                                .incrementNumOfWordUsedIn();
                        pointsArrayList.get(nextIndex).setTaken(true);
                    }

                    // Log.i("pointIndex " + nextIndex,
                    // "Letter "
                    // + sentence.getSentenceWords()
                    // .get(wordIndexInSentence)
                    // .getWordLetters()
                    // .get(letterIndexInWord).getLetter());

                    coloredPoints.add(pointsArrayList.get(nextIndex));
                    tryOtherNeighbor = false;

                }

                // when finishing a word successfully
                if (letterIndexInWord == sentence.getSentenceWords()
                        .get(wordIndexInSentence).getWordLetters().size() - 1) {
                    // Log.i("BREAK", "HERE !");
                    // this word is done .. reset random counter to use for next
                    // word
                    randoumCounter = 0;
                    // fill word points array.. keep history of each drawn word
                    int i;
                    for (i = 0; i < sentence.getSentenceWords()
                            .get(wordIndexInSentence).getWordLetters().size(); i++) {

                        sentence.getSentenceWords()
                                .get(wordIndexInSentence)
                                .getWordPoints()
                                .add(coloredPoints.get(coloredPoints.size()
                                        - sentence.getSentenceWords()
                                        .get(wordIndexInSentence)
                                        .getWordLetters().size() + i));

                        // Log.i("fill word",
                        // coloredPoints
                        // .get(coloredPoints.size()
                        // - sentence
                        // .getSentenceWords()
                        // .get(wordIndexInSentence)
                        // .getWordLetters()
                        // .size() + i)
                        // .getLetter().getLetter());
                    }

                    break;
                }
                // get the Letter To draw next
                Letter letter = sentence.getSentenceWords()
                        .get(wordIndexInSentence).getWordLetters()
                        .get(letterIndexInWord + 1);

                // get All neighbor points of this current point
                neighbors = pointsArrayList.get(nextIndex).getNeighbors();

                // each letter should be tried in all neighbors of previous one

                letter.incrementNeighborIndex(); // defined as -1 ... increment
                // to try first 0 index
                // Log.d("neighbor index",
                // "::" +
                // letter.getNeighborIndex());
                // Log.e("next letter to draw",
                // letter.getLetter());
                tryOtherNeighbor = false;
                // third loop // cover all neighbors of point
                while (letter.getNeighborIndex() < neighbors.size() + 1) {
                    // Log.e("in third loop", "start");

                    // we tried all neighbors ... go back change previous letter
                    // position
                    if (letter.getNeighborIndex() == neighbors.size()) {
                        // Log.e("in third loop",
                        // "NeighborIndex reached limits");
                        // we tried all neighbor and no one is good
                        // clear every thing this letter did ,, go back to
                        // letter before and try other neighbor
                        // you know what other neighbors by letter.neighborIndex
                        tryOtherNeighbor = true;
                        letter.setNeighborIndex(-1);
                        break;
                    }

                    nextNeighbor = neighbors.get(letter.getNeighborIndex())
                            .getIndexBasedOnPosition();

                    // Log.e("in third loop", "nextNeighbor " + nextNeighbor);
                    if (!pointsArrayList.get(nextNeighbor).isTaken()

                            || (pointsArrayList.get(nextNeighbor)
                            .hasThaSameLetterAs(letter) && !sentence
                            .getSentenceWords()
                            .get(wordIndexInSentence)
                            .isWordHasMoreThanOneLetter(letter))) {

                        // neighbor is good to be drawn

                        nextIndex = nextNeighbor;
                        // Log.e("in third loop", "nextNeighbor good nextIndex "
                        // + nextNeighbor);

                        break;
                    } else { // if neighbor is not good
                        letter.incrementNeighborIndex();

                    }

                }// end of most inner loop

                if (letter.getNeighborIndex() >= neighbors.size()) {

                    tryOtherNeighbor = true;
                    letter.setNeighborIndex(-1);

                }
                if (tryOtherNeighbor) {
                    // clear the last point i draw
                    // do not clear last letter in previous word
                    if (coloredPoints.size() != 0 && letterIndexInWord >= 0) {

                        lastPointDrown = coloredPoints
                                .get(coloredPoints.size() - 1);

                        lastPointDrown.decrementNumOfWordUsedIn();

                        if (lastPointDrown.getNumOfWordUsedIn() == 0) {
                            //
                            // Log.i("DELETE Letter", lastPointDrown.getLetter()
                            // .getLetter());
                            lastPointDrown.setLetter(null);
                            lastPointDrown.setTaken(false);

                        }

                        coloredPoints.remove(coloredPoints.size() - 1);
                        // go back to letter before the one we deleted
                        nextIndex = coloredPoints.get(coloredPoints.size() - 1)
                                .getIndexBasedOnPosition();
                        // Log.i("nextIndex", "" + nextIndex);
                    }
                    // go back to previous letter
                    letterIndexInWord--;

                } else {
                    // every things was good ... go to next letter
                    letterIndexInWord++;
                }

            }// end of middle loop

            if (!goBackToPreviousWord) {
                wordIndexInSentence++;
            }

        } // end of first loop
        return true;
    }

}
