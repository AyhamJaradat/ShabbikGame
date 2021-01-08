package com.palteam.shabbik.utils;

import android.content.Context;

import com.palteam.shabbik.R;

public class LettersUtils {
    public static void setContext() {

    }


    public static String getAlphbit(Context con) {
        return con.getResources().getString(R.string.alphbt);
    }

    public static String get_Default_Alphbit(Context con) {
        return con.getResources().getString(R.string.default_alphbt);
    }

    public static char getHamza(Context context) {
        return context.getResources().getString(R.string.hamza).charAt(0);
    }

    public static char getTa(Context context) {
        return context.getResources().getString(R.string.ta).charAt(0);
    }

    public static char getAlf(Context con) {
        return con.getResources().getString(R.string.alf).charAt(0);
    }

    public static char getBa2(Context context) {
        return context.getResources().getString(R.string.ba).charAt(0);
    }

    public static char getThal(Context context) {
        return context.getResources().getString(R.string.thal).charAt(0);
    }

    public static char getRaa(Context context) {
        return context.getResources().getString(R.string.ra).charAt(0);
    }

    public static char getLam(Context context) {
        return context.getResources().getString(R.string.lam).charAt(0);
    }

    public static char getMem(Context context) {
        return context.getResources().getString(R.string.meem).charAt(0);
    }

}
