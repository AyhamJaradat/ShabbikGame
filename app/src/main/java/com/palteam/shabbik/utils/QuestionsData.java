package com.palteam.shabbik.utils;

import com.palteam.shabbik.beans.CorrectWord;
import com.palteam.shabbik.solverAlgorithm.TreeWord;

import java.util.ArrayList;

public class QuestionsData implements IConstants {
    private static QuestionsData instence = null;
    private ArrayList<ArrayList<int[]>> statments;
    private ArrayList<ArrayList<String>> SentencesAsString;



    private ArrayList<CorrectWord> allPossibleWords;
    private ArrayList<CorrectWord> allChosenWords;

    private ArrayList<CorrectWord> allSentenceWord;
    private ArrayList<CorrectWord> allPossibleSentenceWord;
    private TreeWord tree;

    private QuestionsData() {

    }

    public static QuestionsData getInstence() {
        if (instence == null) {
            instence = new QuestionsData();




            instence.statments = new ArrayList<ArrayList<int[]>>();
            instence.SentencesAsString = new ArrayList<ArrayList<String>>();
            instence.allChosenWords = new ArrayList<CorrectWord>();
            instence.allPossibleWords = new ArrayList<CorrectWord>();
            instence.allSentenceWord = new ArrayList<CorrectWord>();
            instence.allPossibleSentenceWord = new ArrayList<CorrectWord>();

        }

        return instence;

    }

    public ArrayList<CorrectWord> getAllPossibleSentenceWords() {
        return allPossibleSentenceWord;
    }

    public void setAllPossibleSentenceWords(
            ArrayList<CorrectWord> allSentenceWords) {
        this.allPossibleSentenceWord = allSentenceWords;
    }

    public TreeWord getTree() {
        return tree;
    }

    public void setTree(TreeWord tree) {
        this.tree = tree;
    }

    public void initializeAllSentenceWord() {
        allSentenceWord.clear();
    }

    public void initializeAllPossibleSentenceWords() {
        allPossibleSentenceWord.clear();
    }

    public void addToAllSentenceWord(CorrectWord correctWord) {
        allSentenceWord.add(correctWord);
    }

    public void addToAllPossibleSentenceWords(CorrectWord correctWord) {
        allPossibleSentenceWord.add(correctWord);
    }

    public ArrayList<CorrectWord> getAllSentenceWord() {
        return allSentenceWord;
    }

    public void setAllSentenceWord(ArrayList<CorrectWord> allSentenceWord) {
        this.allSentenceWord = allSentenceWord;
    }

    public ArrayList<CorrectWord> getAllPossibleWords() {
        return allPossibleWords;
    }

    public void setAllPossibleWords(ArrayList<CorrectWord> allPossibleWords) {
        this.allPossibleWords = allPossibleWords;
    }

    public ArrayList<CorrectWord> getAllChosenWords() {
        return allChosenWords;
    }

    public void setAllChosenWords(ArrayList<CorrectWord> allChosenWords) {
        this.allChosenWords = allChosenWords;
    }

    public ArrayList<String> getSentencesWords(int i) {
        return SentencesAsString.get(i);
    }

    public void addSentenceWords(ArrayList<String> wordsInSentence) {
        this.SentencesAsString.add(wordsInSentence);
    }

    public void addToStatment(ArrayList<int[]> words) {
        this.statments.add(words);
    }

    public void initilizeSentenceWords() {
        this.SentencesAsString = new ArrayList<ArrayList<String>>();
    }

    public void initializeStatment() {
        this.statments = new ArrayList<ArrayList<int[]>>();
    }

    public ArrayList<int[]> getStatment(int i) {
        return this.statments.get(i);
    }

    public int getNumberOfSentences() {
        return statments.size();
    }
}
