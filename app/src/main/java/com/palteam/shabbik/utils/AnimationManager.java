package com.palteam.shabbik.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;

public class AnimationManager {

    public static final int CORRECT_WORD_ANIMATION_DURATION = 1000;

    /**
     * Apply fade animation on given view depends on the given parameters.
     *
     * @param view      view to apply animation on.
     * @param fromAlpha start alpha.
     * @param toAlpha   end alpha.
     * @param duration  animation duration to move from start to end alpha.
     */
    public void applyFadeAnimation(View view, float fromAlpha, float toAlpha, int duration) {

        AlphaAnimation alpha = new AlphaAnimation(fromAlpha, toAlpha);
        alpha.setDuration(duration);

        // Tell it to persist after the animation ends
        alpha.setFillAfter(true);

        view.startAnimation(alpha);
    }
}
