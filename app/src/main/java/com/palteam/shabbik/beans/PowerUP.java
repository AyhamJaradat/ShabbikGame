package com.palteam.shabbik.beans;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.palteam.shabbik.R;

public class PowerUP {
 private int id;
    private ImageView powerUpimage;
    private int score;
    private LinearLayout.LayoutParams layoutParams;

    public PowerUP(int id, Context context, int score) {
        this.score = score;
        int[] imageResourse = {R.drawable.hint_remove_selector, R.drawable.repeat_remove_selector,
                R.drawable.freeze_remove_selector, R.drawable.show_remove_selector};
        this.id = id;
        this.powerUpimage = new ImageView(context);
        this.powerUpimage.setImageResource(imageResourse[id]);

        layoutParams = new LinearLayout.LayoutParams((int) context
                .getResources().getDimension(R.dimen.hint_dimintion),
                (int) context.getResources().getDimension(
                        R.dimen.hint_dimintion));
        layoutParams.setMargins(5, 2, 5, 2);
        powerUpimage.setLayoutParams(layoutParams);

    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ImageView getPowerUpimage() {
        return powerUpimage;
    }

    public void setPowerUpimage(ImageView powerUpimage) {
        this.powerUpimage = powerUpimage;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}