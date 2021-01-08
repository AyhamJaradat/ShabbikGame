package com.palteam.shabbik.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.palteam.shabbik.R;
import com.palteam.shabbik.beans.Point;
import com.palteam.shabbik.solverAlgorithm.Solver;
import com.palteam.shabbik.solverAlgorithm.TreeWord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class Methods implements IConstants {

    public static ArrayList<Point> initializePointsArrayList(Activity activity) {

        ArrayList<Point> pointsArrayList = new ArrayList<Point>();

        Point point11, point12, point13, point14, point21, point22, point23, point24, point31, point32, point33, point34, point41, point42, point43, point44;

        /*
         * first row
         */
        point11 = new Point((ImageView) activity.findViewById(R.id.imageView11));
        point12 = new Point((ImageView) activity.findViewById(R.id.imageView12));
        point13 = new Point((ImageView) activity.findViewById(R.id.imageView13));
        point14 = new Point((ImageView) activity.findViewById(R.id.imageView14));

        point11.setPosition(1, 1);
        point12.setPosition(1, 2);
        point13.setPosition(1, 3);
        point14.setPosition(1, 4);

        pointsArrayList.add(point11);
        pointsArrayList.add(point12);
        pointsArrayList.add(point13);
        pointsArrayList.add(point14);
        /*
         * second row
         */

        point21 = new Point((ImageView) activity.findViewById(R.id.imageView21));
        point22 = new Point((ImageView) activity.findViewById(R.id.imageView22));
        point23 = new Point((ImageView) activity.findViewById(R.id.imageView23));
        point24 = new Point((ImageView) activity.findViewById(R.id.imageView24));

        point21.setPosition(2, 1);
        point22.setPosition(2, 2);
        point23.setPosition(2, 3);
        point24.setPosition(2, 4);

        pointsArrayList.add(point21);
        pointsArrayList.add(point22);
        pointsArrayList.add(point23);
        pointsArrayList.add(point24);
        /*
         * third row
         */
        point31 = new Point((ImageView) activity.findViewById(R.id.imageView31));
        point32 = new Point((ImageView) activity.findViewById(R.id.imageView32));
        point33 = new Point((ImageView) activity.findViewById(R.id.imageView33));
        point34 = new Point((ImageView) activity.findViewById(R.id.imageView34));

        point31.setPosition(3, 1);
        point32.setPosition(3, 2);
        point33.setPosition(3, 3);
        point34.setPosition(3, 4);

        pointsArrayList.add(point31);
        pointsArrayList.add(point32);
        pointsArrayList.add(point33);
        pointsArrayList.add(point34);
        /*
         * forth row
         */
        point41 = new Point((ImageView) activity.findViewById(R.id.imageView41));
        point42 = new Point((ImageView) activity.findViewById(R.id.imageView42));
        point43 = new Point((ImageView) activity.findViewById(R.id.imageView43));
        point44 = new Point((ImageView) activity.findViewById(R.id.imageView44));

        point41.setPosition(4, 1);
        point42.setPosition(4, 2);
        point43.setPosition(4, 3);
        point44.setPosition(4, 4);

        pointsArrayList.add(point41);
        pointsArrayList.add(point42);
        pointsArrayList.add(point43);
        pointsArrayList.add(point44);

        // add neighbors to each point

        int i = 0;
        // int[] neighbors = { 1, -1, 4, -4, 3, -3,5,-5 };
        for (i = 0; i < pointsArrayList.size(); i++) {
            Point point = pointsArrayList.get(i);

            if (i != 3 && i != 7 && i != 11 && i != 15)
                point.addToNeighbors(pointsArrayList.get(i + 1));

            if (i != 0 && i != 4 && i != 8 && i != 12)
                point.addToNeighbors(pointsArrayList.get(i - 1));

            if (i != 3 && i != 7 && i < 11)
                point.addToNeighbors(pointsArrayList.get(i + 5));

            if (i > 4 && i != 12 && i != 8)
                point.addToNeighbors(pointsArrayList.get(i - 5));

            if (i < 12)
                point.addToNeighbors(pointsArrayList.get(i + 4));
            if (i > 3)
                point.addToNeighbors(pointsArrayList.get(i - 4));

            if (i != 0 && i != 4 && i != 8 && i < 12)
                point.addToNeighbors(pointsArrayList.get(i + 3));

            if (i > 3 && i != 11 && i != 15 && i != 7)
                point.addToNeighbors(pointsArrayList.get(i - 3));

        }

        return pointsArrayList;

    }

    public static float getXCenterOfPoint(Point point) {

        int[] viewCoords = new int[2];
        point.getImage().getLocationOnScreen(viewCoords);

        float xImage = viewCoords[0];
        int imgWidth = point.getImage().getWidth();

        return (float) (xImage + (imgWidth / 2.0));
    }

    public static float getYCenterOfPoint(Point point) {
        int[] viewCoords = new int[2];
        point.getImage().getLocationOnScreen(viewCoords);

        float yImage = viewCoords[1];
        int imgHeight = point.getImage().getHeight();
        float yCenter = yImage;

        return (float) (yCenter + (imgHeight / 2.0));
    }

    public static int getImageIndex(float x, float y,
                                    ArrayList<Point> pointsArrayList) {
        int i = 0;
        // int errorMargin = 10;
        float xImageCenter, yImageCenter, radious;
        float dx, dy;
        boolean isItONImage = false;

        for (i = 0; i < pointsArrayList.size(); i++) {
            Point point = pointsArrayList.get(i);
            xImageCenter = Methods.getXCenterOfPoint(point);
            yImageCenter = Methods.getYCenterOfPoint(point);
            radious = (point.getImage().getWidth() / 2);

            dx = Math.abs(x - xImageCenter);
            dy = Math.abs(y - yImageCenter);

            if (dx + dy < radious) {
                isItONImage = true;
                break;
            }
            if (dx * dx + dy * dy < radious * radious) {
                isItONImage = true;
                break;
            }

        }
        if (isItONImage)
            return i;
        return -1;
    }

    public static float calculatPathAngle(int origin, int neigber) {
        float[] angles = {0, 180, 135, 90, 45};
        int sign = 1;
        int index = neigber - origin;
        if (index == -1)
            return angles[1];
        if (index == 1)
            return 0;
        if (index < 0) {
            // index=index+8;
            sign = -1;
            return angles[Math.abs(index) - 1] + 180;
        }
        Log.e("angle", "" + sign * angles[Math.abs(index) - 1]);
        return angles[Math.abs(index) - 1];

    }

    // return char index from character
    public static int getCharIndex(Context context, char chr) {
        int n = (chr - LettersUtils.getAlf(context));
        if (n < 0) {
            if (n == -6)
                n = 30;
            else if (n == -2)
                n = 31;
            else if (n == -4)
                n = 32;

            else if (n == -1)
                n = 33;
            else if (n == -3)
                n = 34;
        } else if (n > 25 && n < 36)
            n = (n - 6);

        return n;

    }

    // calculate the score based on complexity
    public static int calculateScore(int path[], int length) {
        int score = 1;
        for (int i = 1; i < length; i++) {
            if (Math.abs(path[i] - path[i - 1]) == 1) {
                score += 1;
            } else if (Math.abs(path[i] - path[i - 1]) == 4)
                score += 1;
            else
                score += 2;
        }
        return score;
    }

    public static String loadFileFromAsset(Context context, int lineIndex) {
        String line = "";
        try {

            InputStream is;

            switch (lineIndex) {
                case 0:
                    is = context.getAssets().open(DICTIONARY_FILENAME_1);
                    break;
                case 1:
                    is = context.getAssets().open(DICTIONARY_FILENAME_2);

                    break;
                case 2:
                    is = context.getAssets().open(DICTIONARY_FILENAME_3);

                    break;
                case 3:
                    is = context.getAssets().open(DICTIONARY_FILENAME_4);

                    break;
                default:
                    return "";

            }

            BufferedReader reader;
            reader = new BufferedReader(new InputStreamReader(is));
            line = reader.readLine();

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return line;

    }

    public static void readFromFile(Context context) {

        InputStream in = null;
        QuestionsData quest = QuestionsData.getInstence();
        quest.initializeStatment();
        quest.initilizeSentenceWords();
        BufferedReader reader;

        try {
            // in = assetManager.open(filename);
            in = context.getAssets().open(FILENAME);

            ArrayList<int[]> words;
            ArrayList<String> wordsInSentence;

            reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(" ");
                words = new ArrayList<int[]>();
                wordsInSentence = new ArrayList<String>();
                for (int i = 0; i < parts.length; i++) {
                    wordsInSentence.add(parts[i]);
                    int word[] = new int[parts[i].length()];
                    for (int j = 0; j < parts[i].length(); j++) {
                        int n = Methods.getCharIndex(context, (parts[i].charAt(j)));
                        word[j] = n;

                    }
                    words.add(word);
                }
                quest.addToStatment(words);
                quest.addSentenceWords(wordsInSentence);

            }
            in.close();
            in = null;

        } catch (IOException e) {
            Log.e("tag", "Failed reading");
        }

    }

    public static TreeWord initialSolverFromString(String line, Context context) {

        String[] str = line.split(",");

        Collection<String> collec = Arrays.asList(str);
        str = null;
        System.gc();
        Log.e("Tree Size= ", "" + collec.size());
        return new TreeWord(collec);

    }

    public static TreeWord initialSolver(int lineIndex, Context context) {

        String[] str = loadFileFromAsset(context, lineIndex).split(",");

        Collection<String> collec = Arrays.asList(str);
        str = null;
        System.gc();
        Log.e("Tree Size= ", "" + collec.size());
        return new TreeWord(collec);

    }

    public static String generatePuzzleFromDictionary(Context context,
                                                      String strPuzzle) {

        String AllWords = "";
        TreeWord tree;
        String words;

        for (int i = 0; i < NUMBER_OF_DATA_FILES; i++) {
            // read first line from dictionary file
            tree = Methods.initialSolver(i, context);
            // generate all possible words
            words = getAllPossibleWords(strPuzzle, tree);

            AllWords += words;

            words = null;
            tree.clear();
            tree = null;
        }
        Log.i(" generate pzl alW ", strPuzzle + ":" + AllWords);
        //AppUtils.writeToFile("defaultTextPuzzle",  strPuzzle + "\n" + AllWords, context);
        return AllWords;
    }

    /**
     * generate all possible words
     *
     * @param strPuzzle
     * @return
     */
    private static String getAllPossibleWords(String strPuzzle,
                                              TreeWord dictionaryTree) {
        Solver solver = new Solver(4, strPuzzle);
        return solver.backGroundSolve(dictionaryTree);
    }

    public static String getRevertedPuzzle(String firstPuzzle) {

        // randomize the puzzle

        String str = "";
        String puzzle = firstPuzzle;
        StringBuilder bd = new StringBuilder(puzzle);
        int c = 0;
        Random rand = new Random();
        while (c < 16) {
            int n = rand.nextInt(bd.length());
            str += bd.charAt(n);
            bd.deleteCharAt(n);
            c++;
        }

        return str;
    }
    //calculate the number of samples the bitmap need to make it smaller
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        Log.i("required: ", ""+reqHeight+"  "+reqWidth);
        Log.i("option: ", ""+height+"  "+width);

        if (height > reqHeight || width > reqWidth) {
            Log.i("Im inside if: ", ""+height+"  "+width);
            final int halfHeight = height ;
            final int halfWidth = width ;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                Log.i("Im inside while: ", ""+inSampleSize);
                inSampleSize *=2 ;
            }
        }

        return inSampleSize;
    }

    // make bitmap smaller to fit with the image view
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

}
