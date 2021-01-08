package com.palteam.shabbik.utils;

import android.content.Context;

import com.palteam.shabbik.R;
import com.palteam.shabbik.beans.Letter;

import java.util.ArrayList;

public class UIData {

    private int[] letterImages;
    private int[] selectedLetterImages;

    private int[] letterFreezeImages;
    private int[] selectedLetterFreezeImages;
    int numberOfLetters = 35;

    private ArrayList<Letter> lettersArray;

    public UIData() {
        this.letterImages = new int[numberOfLetters];
        this.selectedLetterImages = new int[numberOfLetters];
        this.letterFreezeImages = new int[numberOfLetters];
        this.selectedLetterFreezeImages = new int[numberOfLetters];
        this.lettersArray = new ArrayList<Letter>();

    }

    public int[] getLetterImagesArray() {

        letterImages[0] = R.drawable.a;
        letterImages[1] = R.drawable.b;
        letterImages[2] = R.drawable.ta_marbota; // should be ta2 marbo6ah ,,
        // not found
        // in image
        letterImages[3] = R.drawable.t;
        letterImages[4] = R.drawable.th;
        letterImages[5] = R.drawable.jem;
        letterImages[6] = R.drawable.hha;
        letterImages[7] = R.drawable.kha;
        letterImages[8] = R.drawable.dal;
        letterImages[9] = R.drawable.thal;
        letterImages[10] = R.drawable.r;
        letterImages[11] = R.drawable.zen;
        letterImages[12] = R.drawable.s;
        letterImages[13] = R.drawable.sh;
        letterImages[14] = R.drawable.sad;
        letterImages[15] = R.drawable.dad;
        letterImages[16] = R.drawable.ta;
        letterImages[17] = R.drawable.tha;
        letterImages[18] = R.drawable.ean;
        letterImages[19] = R.drawable.ghen;
        letterImages[20] = R.drawable.fa;
        letterImages[21] = R.drawable.kaf;
        letterImages[22] = R.drawable.kkaf;
        letterImages[23] = R.drawable.lam;
        letterImages[24] = R.drawable.mem;
        letterImages[25] = R.drawable.non;
        letterImages[26] = R.drawable.ha;
        letterImages[27] = R.drawable.waw;
        letterImages[28] = R.drawable.a_maksora;
        letterImages[29] = R.drawable.ya;
        letterImages[30] = R.drawable.hamza;
        letterImages[31] = R.drawable.a_down_hamza;
        letterImages[32] = R.drawable.a_up_hamza;
        letterImages[33] = R.drawable.hamza_mksora_normal;
        letterImages[34] = R.drawable.waw_hamza_normal;
        return letterImages;
    }

    public int[] getSelectedLetterImagesArray() {

        // selectedLetterImages = new int[numberOfLetters];

        selectedLetterImages[0] = R.drawable.a_selected;
        selectedLetterImages[1] = R.drawable.b_selected;
        selectedLetterImages[2] = R.drawable.ta_marbota_selected; // should be
        // ta2
        // marbo6ah ,, not
        // found
        // in image
        selectedLetterImages[3] = R.drawable.t_selected;
        selectedLetterImages[4] = R.drawable.th_selected;
        selectedLetterImages[5] = R.drawable.jem_selected;
        selectedLetterImages[6] = R.drawable.hha_selected;
        selectedLetterImages[7] = R.drawable.kha_selected;
        selectedLetterImages[8] = R.drawable.dal_selected;
        selectedLetterImages[9] = R.drawable.thal_selected;
        selectedLetterImages[10] = R.drawable.r_selected;
        selectedLetterImages[11] = R.drawable.zen_selected;
        selectedLetterImages[12] = R.drawable.s_selected;
        selectedLetterImages[13] = R.drawable.sh_selected;
        selectedLetterImages[14] = R.drawable.sad_selected;
        selectedLetterImages[15] = R.drawable.dad_selected;
        selectedLetterImages[16] = R.drawable.ta_selected;
        selectedLetterImages[17] = R.drawable.tha_selected;
        selectedLetterImages[18] = R.drawable.ean_selected;
        selectedLetterImages[19] = R.drawable.ghen_selected;
        selectedLetterImages[20] = R.drawable.fa_selected;
        selectedLetterImages[21] = R.drawable.kaf_selected;
        selectedLetterImages[22] = R.drawable.kkaf_selected;
        selectedLetterImages[23] = R.drawable.lam_selected;
        selectedLetterImages[24] = R.drawable.mem_selected;
        selectedLetterImages[25] = R.drawable.non_selected;
        selectedLetterImages[26] = R.drawable.ha_selected;
        selectedLetterImages[27] = R.drawable.waw_selected;
        selectedLetterImages[28] = R.drawable.a_maksora_selected;
        selectedLetterImages[29] = R.drawable.ya_selected;
        selectedLetterImages[30] = R.drawable.hamza_selected;
        selectedLetterImages[31] = R.drawable.a_down_hamza_selected;
        selectedLetterImages[32] = R.drawable.a_up_hamza_selected;
        selectedLetterImages[33] = R.drawable.hamza_mksora_selected;
        selectedLetterImages[34] = R.drawable.waw_hamza_selected;
        return selectedLetterImages;
    }

    public int[] getLetterImagesFreezeArray() {

        // letterImages = new int[numberOfLetters];

        letterFreezeImages[0] = R.drawable.alf_freez_normal;
        letterFreezeImages[1] = R.drawable.b_freez_normal;
        letterFreezeImages[2] = R.drawable.taa2_freez_normal; // should be ta2
        // marbo6ah ,,
        // not found
        // in image
        letterFreezeImages[3] = R.drawable.t_freez_normal;
        letterFreezeImages[4] = R.drawable.th_freez_normal;
        letterFreezeImages[5] = R.drawable.gem_freez_normal;
        letterFreezeImages[6] = R.drawable.hha_freez_normal;
        letterFreezeImages[7] = R.drawable.kha_freez_normal;
        letterFreezeImages[8] = R.drawable.dal_freez_normal;
        letterFreezeImages[9] = R.drawable.zal_freez_normal;
        letterFreezeImages[10] = R.drawable.r_freez_normal;
        letterFreezeImages[11] = R.drawable.zen_freez_normal;
        letterFreezeImages[12] = R.drawable.s_freez_normal;
        letterFreezeImages[13] = R.drawable.sh_freez_normal;
        letterFreezeImages[14] = R.drawable.sad_freez_normal;
        letterFreezeImages[15] = R.drawable.dad_freez_normal;
        letterFreezeImages[16] = R.drawable.ta_freez_normal;
        letterFreezeImages[17] = R.drawable.thaa_freez_normal;
        letterFreezeImages[18] = R.drawable.aean_freez_normal;
        letterFreezeImages[19] = R.drawable.ghen_freez_normal;
        letterFreezeImages[20] = R.drawable.f_freez_normal;
        letterFreezeImages[21] = R.drawable.qaf_freez_normal;
        letterFreezeImages[22] = R.drawable.kaf_freez_normal;
        letterFreezeImages[23] = R.drawable.lam_freez_normal;
        letterFreezeImages[24] = R.drawable.memm_freez_normal;
        letterFreezeImages[25] = R.drawable.nonn_freez_normal;
        letterFreezeImages[26] = R.drawable.haaa_freez_normal;
        letterFreezeImages[27] = R.drawable.waww_freez_normal;
        letterFreezeImages[28] = R.drawable.alf_maksora_freez_normal;
        letterFreezeImages[29] = R.drawable.yaa_freez_normal;
        letterFreezeImages[30] = R.drawable.hamzza_freez_normal;
        letterFreezeImages[31] = R.drawable.hamza_under_freez_normal;
        letterFreezeImages[32] = R.drawable.a_up_freez_normal;
        letterFreezeImages[33] = R.drawable.hamza_mksora_normal_f;
        letterFreezeImages[34] = R.drawable.waw_hamza_normal_f;
        return letterFreezeImages;
    }

    public int[] getSelectedLetterFreezeImagesArray() {

        // selectedLetterImages = new int[numberOfLetters];

        selectedLetterFreezeImages[0] = R.drawable.alf_freez_selected;
        selectedLetterFreezeImages[1] = R.drawable.b_freez_selected;
        selectedLetterFreezeImages[2] = R.drawable.taa2_freez_selected; // should
        // be
        // ta2
        // marbo6ah
        // ,,
        // not
        // found
        // in image
        selectedLetterFreezeImages[3] = R.drawable.t_freez_selected;
        selectedLetterFreezeImages[4] = R.drawable.th_freez_selected;
        selectedLetterFreezeImages[5] = R.drawable.gem_freez_selected;
        selectedLetterFreezeImages[6] = R.drawable.hha_freez_selected;
        selectedLetterFreezeImages[7] = R.drawable.kha_freez_selected;
        selectedLetterFreezeImages[8] = R.drawable.dal_freez_selected;
        selectedLetterFreezeImages[9] = R.drawable.zal_freez_selected;
        selectedLetterFreezeImages[10] = R.drawable.r_freez_selected;
        selectedLetterFreezeImages[11] = R.drawable.zen_freez_selected;
        selectedLetterFreezeImages[12] = R.drawable.s_freez_selected;
        selectedLetterFreezeImages[13] = R.drawable.sh_freez_selected;
        selectedLetterFreezeImages[14] = R.drawable.sad_freez_selected;
        selectedLetterFreezeImages[15] = R.drawable.dad_freez_selected;
        selectedLetterFreezeImages[16] = R.drawable.ta_freez_selected;
        selectedLetterFreezeImages[17] = R.drawable.thaa_freez_selected;
        selectedLetterFreezeImages[18] = R.drawable.aean_freez_selected;
        selectedLetterFreezeImages[19] = R.drawable.ghen_freez_selected;
        selectedLetterFreezeImages[20] = R.drawable.f_freez_selected;
        selectedLetterFreezeImages[21] = R.drawable.qaf_freez_selected;
        selectedLetterFreezeImages[22] = R.drawable.kaf_freez_selected;
        selectedLetterFreezeImages[23] = R.drawable.lam_freez_selected;
        selectedLetterFreezeImages[24] = R.drawable.memfreez__selected;
        selectedLetterFreezeImages[25] = R.drawable.nonn_freez_selected;
        selectedLetterFreezeImages[26] = R.drawable.haaa_freez_selected;
        selectedLetterFreezeImages[27] = R.drawable.waww_freez_selected;
        selectedLetterFreezeImages[28] = R.drawable.alf_maksora_freez_selected;
        selectedLetterFreezeImages[29] = R.drawable.yaa_freez_selected;
        selectedLetterFreezeImages[30] = R.drawable.hamzza_freez_selected;
        selectedLetterFreezeImages[31] = R.drawable.hamza_under_freez_selected;
        selectedLetterFreezeImages[32] = R.drawable.a_up_freez_selected;
        selectedLetterFreezeImages[33] = R.drawable.hamza_mksora_selected_f;
        selectedLetterFreezeImages[34] = R.drawable.waw_hamza_selected_f;
        return selectedLetterFreezeImages;
    }

    public ArrayList<Letter> getLettersArray(Context context) {

        int[] images = getLetterImagesArray();
        int[] selectedimages = getSelectedLetterImagesArray();

        int[] imagesFreez = getLetterImagesFreezeArray();
        int[] selectedimagesFreez = getSelectedLetterFreezeImagesArray();

        // lettersArray = new ArrayList<Letter>();

        lettersArray.add(new Letter(context.getResources().getString(
                R.string.alf), 0, images[0], selectedimages[0], imagesFreez[0],
                selectedimagesFreez[0]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.ba), 1, images[1], selectedimages[1], imagesFreez[1],
                selectedimagesFreez[1]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.ta2), 2, images[2], selectedimages[2], imagesFreez[2],
                selectedimagesFreez[2]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.ta), 3, images[3], selectedimages[3], imagesFreez[3],
                selectedimagesFreez[3]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.tha), 4, images[4], selectedimages[4], imagesFreez[4],
                selectedimagesFreez[4]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.jeem), 5, images[5], selectedimages[5],
                imagesFreez[5], selectedimagesFreez[5]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.hha), 6, images[6], selectedimages[6], imagesFreez[6],
                selectedimagesFreez[6]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.kha), 7, images[7], selectedimages[7], imagesFreez[7],
                selectedimagesFreez[7]));

        lettersArray.add(new Letter(context.getResources().getString(
                R.string.dal), 8, images[8], selectedimages[8], imagesFreez[8],
                selectedimagesFreez[8]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.thal), 9, images[9], selectedimages[9],
                imagesFreez[9], selectedimagesFreez[9]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.ra), 10, images[10], selectedimages[10],
                imagesFreez[10], selectedimagesFreez[10]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.zai), 11, images[11], selectedimages[11],
                imagesFreez[11], selectedimagesFreez[11]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.seen), 12, images[12], selectedimages[12],
                imagesFreez[12], selectedimagesFreez[12]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.sheen), 13, images[13], selectedimages[13],
                imagesFreez[13], selectedimagesFreez[13]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.sad), 14, images[14], selectedimages[14],
                imagesFreez[14], selectedimagesFreez[14]));

        lettersArray.add(new Letter(context.getResources().getString(
                R.string.dad), 15, images[15], selectedimages[15],
                imagesFreez[15], selectedimagesFreez[15]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.taa2), 16, images[16], selectedimages[16],
                imagesFreez[16], selectedimagesFreez[16]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.dad_dot), 17, images[17], selectedimages[17],
                imagesFreez[17], selectedimagesFreez[17]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.aein), 18, images[18], selectedimages[18],
                imagesFreez[18], selectedimagesFreez[18]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.aeien_dot), 19, images[19], selectedimages[19],
                imagesFreez[19], selectedimagesFreez[19]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.fa), 20, images[20], selectedimages[20],
                imagesFreez[20], selectedimagesFreez[20]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.qaf), 21, images[21], selectedimages[21],
                imagesFreez[21], selectedimagesFreez[21]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.kaf), 22, images[22], selectedimages[22],
                imagesFreez[22], selectedimagesFreez[22]));

        lettersArray.add(new Letter(context.getResources().getString(
                R.string.lam), 23, images[23], selectedimages[23],
                imagesFreez[23], selectedimagesFreez[23]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.meem), 24, images[24], selectedimages[24],
                imagesFreez[24], selectedimagesFreez[24]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.noon), 25, images[25], selectedimages[25],
                imagesFreez[25], selectedimagesFreez[25]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.ha), 26, images[26], selectedimages[26],
                imagesFreez[26], selectedimagesFreez[26]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.waw), 27, images[27], selectedimages[27],
                imagesFreez[27], selectedimagesFreez[27]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.yaMksora), 28, images[28], selectedimages[28],
                imagesFreez[28], selectedimagesFreez[28]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.ya), 29, images[29], selectedimages[29],
                imagesFreez[29], selectedimagesFreez[29]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.hamza), 30, images[30], selectedimages[30],
                imagesFreez[30], selectedimagesFreez[30]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.hamza_down), 31, images[31], selectedimages[31],
                imagesFreez[31], selectedimagesFreez[31]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.hamza_up), 32, images[32], selectedimages[32],
                imagesFreez[32], selectedimagesFreez[32]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.hamza_maksora), 33, images[33], selectedimages[33],
                imagesFreez[33], selectedimagesFreez[33]));
        lettersArray.add(new Letter(context.getResources().getString(
                R.string.hamza_waw), 34, images[34], selectedimages[34],
                imagesFreez[34], selectedimagesFreez[34]));
        return lettersArray;
    }
}
