package com.palteam.shabbik.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.palteam.shabbik.R;

import java.util.Calendar;

//import com.palteam.shabbik.social.facebook.FacebookManager;
import com.palteam.shabbik.solverAlgorithm.Solver;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.Methods;
import com.palteam.shabbik.utils.QuestionsData;
import com.palteam.shabbik.utils.Sounds;

public class SplashActivity extends Activity implements IConstants {
    private final String TAG = SplashActivity.class.getSimpleName();

    private Context context;
    private QuestionsData questionData;
    private int gameId = 0;
    private boolean[] readingFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashlytics.getInstance().log("App Started in Splash Screen!!");


        AppUtils.logHeap(TAG);
        setContentView(R.layout.activity_splash);
//        setContentView(R.layout.splash_activity);

        context = getApplicationContext();

    }
    private void startAnimation() {

        final ImageView splashImageView = (ImageView) findViewById(R.id.SplashImageView);
        splashImageView.setBackgroundResource(R.drawable.splash_anim);

        final AnimationDrawable frameAnimation = (AnimationDrawable) splashImageView
                .getBackground();

        splashImageView.post(new Runnable() {
            @Override
            public void run() {
                frameAnimation.start();
            }
        });

    }

    @Override
    protected void onResume() {

        startAnimation();

        if (getIntent().getExtras() != null) {
            gameId = getIntent().getExtras().getInt(GAME_ID, -1);
        }

        init();

        new InitializeGameAsyncTask().execute();


        super.onResume();
    }

    private void init() {

        // Initialize notification id to zero if not initialized.
        SharedPreferences sharedPreferences = getSharedPreferences(
                IConstants.SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        if (sharedPreferences.getInt(IConstants.Preferences.NOTIFICAION_ID, -1) == -1) {

            Editor editor = sharedPreferences.edit();
            editor.putInt(IConstants.Preferences.NOTIFICAION_ID, 0);
            editor.commit();
        }

    }

    private void startGame() {
        Intent toActivty;
        if (gameId <= 0) {
            // normal start
            toActivty = new Intent(getApplicationContext(),
                    MainMenuActivity.class);

        } else {
            // started from notification
            toActivty = new Intent(getApplicationContext(),
                    RoundsViewActivity.class);
            toActivty.putExtra(GAME_ID, gameId);

        }
        startActivity(toActivty);
        finish();

    }

    @Override
    protected void onDestroy() {
        Log.i("Imin destroy", "");
        super.onDestroy();
        LinearLayout splash =   (LinearLayout) this
                .findViewById(R.id.SplashLayout);
        unbindDrawables(splash);

    }
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                Log.i("view",""+i);
            }
            ((ViewGroup) view).removeAllViews();
            view.setBackgroundResource(0);
        }
    }
    // / private inner class
    private class InitializeGameAsyncTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;

        public InitializeGameAsyncTask() {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {

            Calendar calendar = Calendar.getInstance();
            long startTime = calendar.getTimeInMillis();

            Sounds.intial(context);
            readingFlag = new boolean[2];
            questionData = QuestionsData.getInstence();

            if (questionData.getTree() == null
                    || questionData.getNumberOfSentences() <= 0) {
                readDictionariesFile();
            }

            // Refresh Facebook connection.
//            FacebookManager facebookManager = new FacebookManager(
//                    SplashActivity.this);
//            facebookManager.refreshConnection();

            long endTime = Calendar.getInstance().getTimeInMillis();

            long duration = endTime - startTime;

            long diff = 2000 - duration;

            try {

                if (diff > 0) {
                    Thread.sleep((long) (2000 - duration));
                }

            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return "results";
        }

        protected void onPostExecute(String results) {
//
//            new Thread(new Runnable() {
//
//                @Override
//                public void run() {
//                    Intent generatePuzzleIntetn = new Intent(context,
//                            GeneratePuzzleWordsService.class);
//                    startService(generatePuzzleIntetn);
//
//                }
//            }).start();
//
//            // call startGame Function
            startGame();
        }

    }

    public boolean isFinishReading() {
        // for (int i = 0; i < readingFlag.length; i++) {
        // if (!readingFlag[i])
        // return false;
        // }
        return readingFlag[0];
    }

    private void readDictionariesFile() {
        Solver.initialMatrix(4);
        // new Thread() {
        // @Override
        // public void run() {
        // readingFlag[0] = false;
        Methods.readFromFile(context);
        // readingFlag[0] = true;
        // }
        // }.start();
        // new Thread() {
        // @Override
        // public void run() {
        //
        // Log.e("Start displaying word ", "1");
        // readingFlag[1] = false;
        // questionData.setTree(Methods.initialSolver(0, context));
        // readingFlag[1] = true;
        // Log.e("finish", "1");
        //
        // }
        // }.start();
        //

    }

}
