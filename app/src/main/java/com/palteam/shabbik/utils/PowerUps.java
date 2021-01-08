package com.palteam.shabbik.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.palteam.shabbik.R;

public class PowerUps implements IConstants {

    private static PowerUps instance = null;
    private Activity activity;
    private int powerUps[];
    private Dialog dialogPowerUps;
    private LinearLayout bounceLayout;
    private int gameMode;

    private PowerUps() {
        powerUps = new int[3];
    }

    public static PowerUps getInstence( int gameMode) {
        if (instance == null) {
            instance = new PowerUps();
        }
        instance.gameMode = gameMode;


        return instance;

    }

    public void setFreezeAction(Activity activity) {
        LinearLayout mainLayout = (LinearLayout) activity
                .findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.mainfreeze_bg);
        LinearLayout tabLyout = (LinearLayout) activity
                .findViewById(R.id.TabLayout);
        tabLyout.setBackgroundResource(R.drawable.actionfreeze_bar_bag);
        TextView scoreText = (TextView) activity.findViewById(R.id.ScoreView);
        scoreText.setBackgroundResource(R.drawable.resultsfreeze_bg);
        LinearLayout sentenceLayout = (LinearLayout) activity
                .findViewById(R.id.SentenceAreaLayout);
        sentenceLayout.setBackgroundResource(R.drawable.textfreeze_bg);
        ImageView charBack = (ImageView) activity
                .findViewById(R.id.backImageForDraw);
        charBack.setBackgroundResource(R.drawable.characterfreeze_bg);

    }

    public void setNormalBackground(Activity activity) {
        LinearLayout mainLayout = (LinearLayout) activity
                .findViewById(R.id.mainLayout);
        mainLayout.setBackgroundResource(R.drawable.main_bg);
        LinearLayout tabLyout = (LinearLayout) activity
                .findViewById(R.id.TabLayout);
        tabLyout.setBackgroundResource(R.drawable.action_bar_bag);
        TextView scoreText = (TextView) activity.findViewById(R.id.ScoreView);
        scoreText.setBackgroundResource(R.drawable.results_bg);
        LinearLayout sentenceLayout = (LinearLayout) activity
                .findViewById(R.id.SentenceAreaLayout);
        sentenceLayout.setBackgroundResource(R.drawable.text_bg);
        ImageView charBack = (ImageView) activity
                .findViewById(R.id.backImageForDraw);
        charBack.setBackgroundResource(R.drawable.character_bg);

    }

    // public String revertLetters(ArrayList<Point> points, String puzzle,
    // ArrayList<Letter> arrayLetter) {
    //
    // // randomize the puzzle
    //
    // String str = "";
    // boolean isFreez = points.get(0).getLetter().isFreez();
    //
    // StringBuilder bd = new StringBuilder(puzzle);
    // int c = 1;
    // Random rand = new Random();
    // while (c <= 16) {
    // int n = rand.nextInt(bd.length());
    //
    // str += bd.charAt(n);
    //
    // int index = Methods.getCharIndex(bd.charAt(n));
    // Letter letter = new Letter(arrayLetter.get(index));
    // letter.setFreezMode(isFreez);
    // points.get(c - 1).setLetter(letter);
    //
    // bd.deleteCharAt(n);
    //
    // c++;
    // }
    //
    // return str;
    //
    // }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int[] getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(int[] powerUps) {
        this.powerUps = powerUps;
    }

    public Dialog showPowerUpsDialog(Context context) {
        // custom dialog
        dialogPowerUps = new Dialog(context);
        dialogPowerUps.setContentView(R.layout.powerups_start_play_activity);

        dialogPowerUps.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //
        // // let the dialog fill parent
        // WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        // lp.copyFrom(dialogPowerUps.getWindow().getAttributes());
        // lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        // lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        // dialogPowerUps.getWindow().setAttributes(lp);

        dialogPowerUps.setCancelable(true);
        dialogPowerUps.setCanceledOnTouchOutside(false);

        return dialogPowerUps;
    }

}
