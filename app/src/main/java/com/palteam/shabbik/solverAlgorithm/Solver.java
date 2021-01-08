package com.palteam.shabbik.solverAlgorithm;

import android.content.Context;
import android.util.Log;

import com.palteam.shabbik.beans.CorrectWord;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.Methods;
import com.palteam.shabbik.utils.QuestionsData;

import java.util.ArrayList;

public class Solver  implements IConstants {
    private char[] puzzle;
    private int maxSize;
    private int pathLength = 0;

    private boolean[] used;
    private StringBuilder stringSoFar;

    public static boolean[][] matrix;
    private TreeWord trie;

    private String puzzleStr;

    private String wordsTrueList = " ";

    private ArrayList<CorrectWord> allPossibbleWord;
    private ArrayList<CorrectWord> allCorrectWord;
    private QuestionsData wordsData;
    private Context con;

    String AllPossibleWords = "";

    // private final static Logger logger = Logger.getLogger(Solver.class);

    public Solver(int size, String puzzle, Context con) {
        this.con = con;
        wordsTrueList = " ";
        this.puzzleStr = puzzle;
        maxSize = size * size;
        stringSoFar = new StringBuilder(maxSize);
        allPossibbleWord = new ArrayList<CorrectWord>();
        allCorrectWord = new ArrayList<CorrectWord>();
        used = new boolean[maxSize];
        // WordList =new Hashtable<String,String>();
        if (puzzle.length() == maxSize) {
            this.puzzle = puzzle.toCharArray();
        } else {
            this.puzzle = puzzle.substring(0, maxSize).toCharArray();
        }
    }

    public Solver(int size, String puzzle) {

        this.puzzleStr = puzzle;
        maxSize = size * size;
        stringSoFar = new StringBuilder(maxSize);

        used = new boolean[maxSize];
        if (puzzle.length() == maxSize) {
            this.puzzle = puzzle.toCharArray();
        } else {
            this.puzzle = puzzle.substring(0, maxSize).toCharArray();
        }
    }

    public static void initialMatrix(int size) {

        matrix = connectivityMatrix(size);

    }

    public void setPuzzle(String puzzle)

    {
        this.puzzleStr = puzzle;
        if (puzzle.length() == maxSize) {
            this.puzzle = puzzle.toCharArray();
        } else {
            this.puzzle = puzzle.substring(0, maxSize).toCharArray();
        }

    }

    public void solve(TreeWord wordTree) {
        wordsData = QuestionsData.getInstence();
        trie = wordTree;

        Log.i("", "xxx Word tree " + wordTree);
        if (wordTree == null) {
            trie.addAll(Methods.initialSolver(0, con));
        }

        initialMatrix(4);

        for (int i = 0; i < maxSize; i++) {
            this.pathLength = 1;
            int path[] = new int[16];
            path[this.pathLength - 1] = i;
            //

            traverseAt(i, path);
        }
    }

    public String backGroundSolve(TreeWord wordTree) {

        trie = wordTree;

        initialMatrix(4);

        AllPossibleWords = "";

        for (int i = 0; i < maxSize; i++) {
            this.pathLength = 1;
            int path[] = new int[16];
            path[this.pathLength - 1] = i;
            //
            backgroundTraverseAt(i, path);

        }

        return AllPossibleWords;

    }

    private void backgroundTraverseAt(int origin, int path[]) {

        // System.out.println("origin"+origin);
        stringSoFar.append(puzzle[origin]);
        used[origin] = true;

        // Check if we have a valid word
        if ((stringSoFar.length() >= MINIMUM_WORD_LENGTH)
                && !backgroundIsRepeated(stringSoFar.toString())
                && (trie.contains(stringSoFar.toString()))) {

            AllPossibleWords += new String(stringSoFar.toString()) + ",";

        }
        // Find where to go next
        for (int destination = 0; destination < maxSize; destination++) {

            if (matrix[origin][destination]
                    && !used[destination]
                    && (trie.isPrefix(stringSoFar.toString()
                    + puzzle[destination]))) {

                path[this.pathLength] = destination;
                this.pathLength++;
                backgroundTraverseAt(destination, path);

            }

        }
        this.pathLength--;
        used[origin] = false;
        stringSoFar.deleteCharAt(stringSoFar.length() - 1);
    }

    private void traverseAt(int origin, int path[]) {

        // System.out.println("origin"+origin);
        stringSoFar.append(puzzle[origin]);
        used[origin] = true;

        // Check if we have a valid word
        if ((stringSoFar.length() >= MINIMUM_WORD_LENGTH)
                && !isRepeated(stringSoFar.toString())
                && (trie.contains(stringSoFar.toString()))) {
            // Log.i("here", "" + trie.size());
            // Log.e("word", stringSoFar.toString());
            int score = Methods.calculateScore(path, this.pathLength);
            int p[] = new int[this.pathLength];
            for (int i = 0; i < pathLength; i++) {
                p[i] = path[i];

            }

            allPossibbleWord.add(new CorrectWord(score, new String(stringSoFar
                    .toString()), p, this.pathLength, this.puzzleStr));
        }
        // Find where to go next
        for (int destination = 0; destination < maxSize; destination++) {

            if (matrix[origin][destination]
                    && !used[destination]
                    && (trie.isPrefix(stringSoFar.toString()
                    + puzzle[destination]))) { // System.out.println("destination"+destination);

                // if(pathLength>=0){
                path[this.pathLength] = destination;
                this.pathLength++;
                traverseAt(destination, path);
                // }
            }

        }
        this.pathLength--;
        used[origin] = false;
        stringSoFar.deleteCharAt(stringSoFar.length() - 1);
    }

    private boolean backgroundIsRepeated(String Word) {

        return AllPossibleWords.contains("," + Word + ",");
    }

    private boolean isRepeated(String Word) {
        for (int i = 0; i < allCorrectWord.size(); i++) {
            if (allPossibbleWord.get(i).getWord().equals(Word)
                    && allPossibbleWord.get(i).getPuzzleString()
                    .equals(puzzleStr)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<CorrectWord> getAllPossibbleWord() {
        return allPossibbleWord;
    }

    public void setAllPossibbleWord(ArrayList<CorrectWord> allPossibbleWord) {
        this.allPossibbleWord = allPossibbleWord;
    }

    public ArrayList<CorrectWord> getAllCorrectWord() {
        return allCorrectWord;
    }

    public void setAllCorrectWord(ArrayList<CorrectWord> allCorrectWord) {
        this.allCorrectWord = allCorrectWord;
    }

    public boolean isSelected(String word) {
        if (wordsTrueList.contains(" " + word + " "))
            return true;
        else {
            return false;
        }
    }

    public int isExist(String word) {

        for (int i = 0; i < allPossibbleWord.size(); i++) {

            if (allPossibbleWord.get(i).getWord().equals(word)
                    && allPossibbleWord.get(i).getPuzzleString()
                    .equals(puzzleStr)) {
                allCorrectWord.add(allPossibbleWord.get(i));
                wordsTrueList += "" + word + " ";
                return allPossibbleWord.get(i).getScores();
            }
            if (isSelected(word))
                return -1;
        }
        return 0;
    }

    static boolean[] connectivityRow(int x, int y, int size) {
        boolean[] squares = new boolean[size * size];
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                final int calX = x + offsetX;
                final int calY = y + offsetY;
                if ((calX >= 0) && (calX < size) && (calY >= 0)
                        && (calY < size))
                    squares[calY * size + calX] = true;
            }
        }

        squares[y * size + x] = false;// the current x, y is false

        return squares;
    }

    /**
     * Returns the matrix of connectivity between two points. Point i can go to
     * point j iff matrix[i][j] is true Square (x, y) is equivalent to point
     * (size * y + x). For example, square (1,1) is point 5 in a puzzle of size
     * 4
     *
     * @param size the size of the puzzle
     * @return the connectivity matrix
     */
    public static boolean[][] connectivityMatrix(int size) {
        boolean[][] matrix = new boolean[size * size][];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                matrix[y * size + x] = connectivityRow(x, y, size);
            }
        }
        return matrix;
    }

}
