package com.palteam.shabbik.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.palteam.shabbik.R;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.LettersUtils;
import com.palteam.shabbik.utils.Methods;

import java.util.Random;

public class GeneratePuzzleWordsService extends Service implements IConstants {
    private Context context;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        context = getApplicationContext();
        String firstAllWords = "";
        String secondAllWords = "";

        // All Arabic letters
        String allArabicLetters = context.getResources().getString(
                R.string.alphbt);
        String allArabicLetters2 = allArabicLetters;

        // 16 random letters
        String firstStrPuzzle = getpuzzle(allArabicLetters, context);
        String secondStrPuzzle = Methods.getRevertedPuzzle(firstStrPuzzle);

        firstAllWords = Methods.generatePuzzleFromDictionary(context,
                firstStrPuzzle);
        secondAllWords = Methods.generatePuzzleFromDictionary(context,
                secondStrPuzzle);

        // save puzzle and it is words to default file
        AppUtils.writeToFile(DEFAULT_PUZZEL_FILENAME, firstStrPuzzle + ":"
                + firstAllWords, context);

        AppUtils.writeToFile(REVERT_PUZZEL_FILENAME, secondStrPuzzle + ":"
                + secondAllWords, context);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putBoolean(IS_DEFAULT_PUZZL_DIRTY, false);
        editor.commit();

        return Service.START_NOT_STICKY;
    }

    // generate 16 random letter of the puzzle
    private String getpuzzle(String AllArabicLetters, Context con) {

        int[] letters = new int[AllArabicLetters.length()];
        String str = getDefaultPuzzle(LettersUtils.get_Default_Alphbit(context), letters);

        StringBuilder bd = new StringBuilder(AllArabicLetters);
        int c = 0;
        Random rand = new Random();
        while (c < 8) {
            int n = rand.nextInt(bd.length());
            if (letters[n] < 2) {
                letters[n]++;
                str += bd.charAt(n);

                c++;
            } else {
                bd.deleteCharAt(n);
            }
        }
        return str;
    }

    private String getDefaultPuzzle(String defaultArabicLetters, int letters[]) {

        String str = "";

        StringBuilder bd = new StringBuilder(defaultArabicLetters);
        int c = 0;
        Random rand = new Random();
        while (c < 8) {
            int n = rand.nextInt(bd.length());
            letters[n]++;
            str += bd.charAt(n);

            c++;

            bd.deleteCharAt(n);

        }
        return str;
    }

}
