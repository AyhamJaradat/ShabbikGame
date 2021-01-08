package com.palteam.shabbik.activities;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.palteam.shabbik.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.palteam.shabbik.beans.CorrectWord;
import com.palteam.shabbik.beans.Letter;
import com.palteam.shabbik.beans.Point;
import com.palteam.shabbik.beans.PowerUP;
import com.palteam.shabbik.beans.Sentence;
import com.palteam.shabbik.beans.Word;
import com.palteam.shabbik.configuration.Configuration;
import com.palteam.shabbik.configuration.GameConfiguration;
import com.palteam.shabbik.configuration.TrainingGameConfig;
import com.palteam.shabbik.solverAlgorithm.Solver;
import com.palteam.shabbik.solverAlgorithm.TreeWord;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.ArrowDrower;
import com.palteam.shabbik.utils.IConstants;
import com.palteam.shabbik.utils.Methods;
import com.palteam.shabbik.utils.PathDrower;
import com.palteam.shabbik.utils.PowerUps;
import com.palteam.shabbik.utils.QuestionsData;
import com.palteam.shabbik.utils.SentenceDrower;
import com.palteam.shabbik.utils.Sounds;
import com.palteam.shabbik.utils.UIData;

public class SentenceGameActivity extends Activity implements IConstants,
        OnClickListener {

    private final String TAG = SentenceGameActivity.class.getSimpleName();

    private int coins;
    private ImageView ahsantImage;
    private boolean isTimer = false;
    private boolean isMute = false;
    private boolean isPause;
    private Dialog dialogPowerUps;
    private LinearLayout powersLayout;
    private int powers[] = new int[3];
    private ImageView pauseImage;
    private int wordHintIndex;
    private boolean isWordHint = false;
    private CounterClass timer;
    private Sounds addPowerUpSound;
    private Sounds removePowerUpSound;
    private Sounds warningSound;
    private Sounds winningSound;
    private Sounds buttonSound;
    private Sounds WrongSound;
    private Sounds freezeSound;
    private Sounds unFreezeSound;
    private TextView textViewTime, scoreTextView;
    private long time;
    private long frozenTime;
    private boolean frozenMode = false;
    private String strPuzzle;
    private Dialog dialog;
    private UIData uiData;
    private String wordsSelected = " ";
    // private SharedPreferences sharedPreferences;
    private int whoAmI;
    /*
     * F pathDrower to draw Line with finger move
     */

    private PathDrower pathDrower;
    private ArrowDrower arrowDrower;

    /*
     * UI Elements
     */

    private TextView[] myTextViews = null;

    private Point previousPoint, currentPoint;

    private ArrayList<Letter> lettersArray;

    private ArrayList<Point> currentWordArrayList;

    private QuestionsData quest;
    int letterIndex = 0;

    private ArrayList<Point> pointsArrayList;

    private Sentence sentence;

    private int sentenceIndex;
    private int score;
    private String roundConfiguration;
    private int playWith;

    // game configuration
    private Configuration configObj;
    private Context context;
    private PowerUps powerUp;
//    private AdView banner;
    private int userId;
    private ShabbikDAO sqlLiteDB;
    Random generateRandom;
    private String[] defaultSentences;
    private SentenceDrower drow;

    private Typeface robotoBoldTypeface, robotoRegularTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.logHeap(TAG);

        setContentView(R.layout.letters_grid);
//        setContentView(R.layout.activity_sentence_game);
        initialSounds();
        initi();
        /*
         * initialize UI components
         */
        isPause = false;
        powerUp = PowerUps.getInstence( SENTENCE_GAME_VALUE);
        dialogPowerUps = powerUp.showPowerUpsDialog(this);
        sqlLiteDB = ShabbikDAO.getInstance(context);

        userId = sqlLiteDB.retrieveMainUserId();
        initiatPowersUp();

        dialogPowerUps.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (!isTimer) {
                    Intent toActivty = new Intent(getApplicationContext(),
                            SecondMenuActivity.class);
                    toActivty.putExtra(GAME_MODE, SENTENCE_GAME_VALUE);

                    SentenceGameActivity.this.finish();
                    startActivity(toActivty);

                }
            }
        });

        dialogPowerUps.show();

        wordsSelected = " ";

        /*
         * initialize data for questions
         */
        quest = QuestionsData.getInstence();
        quest.initializeAllSentenceWord();
        quest.initializeAllPossibleSentenceWords();
        /*
         * Create Game configuration object
         */
        TextView coinsText = (TextView) dialogPowerUps
                .findViewById(R.id.coin_score);

        playWith = getIntent().getExtras().getInt(PLAY_WITH);

        FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Play with :  " + playWith);
        if (playWith != PRACTICE_PLAYING) {

            int gameId = getIntent().getExtras().getInt(GAME_ID);
            int currentRoundId = getIntent().getExtras().getInt(
                    GAME_CURRENT_ROUND_ID);
            int currntUserTurnId = getIntent().getExtras().getInt(
                    GAME_USER_TURN_ID);

            configObj = GameConfiguration.getInstence();

            configObj.initializeGameConfiguration(gameId, currentRoundId,
                    currntUserTurnId, context);


            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"not practicing  : user ID " + userId);
            if (userId == configObj.getGame().getFirstUserId()) {
                whoAmI = I_AM_FIRST_USER;
                configObj.setWhoAmI(whoAmI);
                /* score = configObj.getScore(whoAmI); */

                // Am first user ,, use random sentence
                sentenceIndex = generateRandom.nextInt(quest
                        .getNumberOfSentences() - 1);


                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Not practicing , First user , Sentence index = "
                        + sentenceIndex);

                configObj.setSentenceIndex(sentenceIndex);

                sentence = createSentence(sentenceIndex);
                drow = new SentenceDrower();
                boolean isGood = drow.drowSentence(sentence, pointsArrayList);


                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Sentence drawer  "
                        + isGood);

                if (isGood) {
                    fillRemainingPoints(pointsArrayList);
                } else {
                    sentence = createSentence(sentenceIndex);
                    strPuzzle = defaultSentences[sentenceIndex];

                    FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Not practicing , First user , Str puzzel default "
                            + strPuzzle);

                    // clear all effects of drawer on points
                    // pointsArrayList =
                    // Methods.initializePointsArrayList(this);
                    strPuzzle = putPuzzleStringOnPoints(pointsArrayList,
                            strPuzzle);


                    FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Not practicing , First user , After put puzzel "
                            + strPuzzle);

                    setSentencewordWithPathesFromSolverAlgorithm(sentence,
                            strPuzzle);
                }

            } else if (userId == configObj.getGame().getSecondUserId()) {
                whoAmI = I_AM_SECOND_USER;
                configObj.setWhoAmI(whoAmI);
                // score = configObj.getScore(whoAmI);
                // am second user
                // get sentence index and sentence puzzle
                sentenceIndex = configObj.getSentenceIndex();
                sentence = createSentence(sentenceIndex);
                strPuzzle = configObj.getRound().getRoundConfigration();

                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+ "Not practicing , Second user , Puzzel from server "
                        + strPuzzle);

                Log.i(TAG, "xxx Str puzzle = " + strPuzzle);
                if (strPuzzle == null || strPuzzle.length() < 16) {

                    strPuzzle = defaultSentences[sentenceIndex];


                    FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Not practicing , Second user , Default puzzel "
                            + strPuzzle);
                }
                // pointsArrayList = Methods.initializePointsArrayList(this);
                strPuzzle = putPuzzleStringOnPoints(pointsArrayList, strPuzzle);

                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Not practicing , Second user , After put puzzel "
                        + strPuzzle);

                setSentencewordWithPathesFromSolverAlgorithm(sentence,
                        strPuzzle);

            } else {
                // error happends
                // the user is not one of the two games player
                Intent toActivity = new Intent(context, MainMenuActivity.class);

                toActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Toast.makeText(context,
                        getResources().getString(R.string.server_failer),
                        Toast.LENGTH_SHORT).show();
                startActivity(toActivity);
                SentenceGameActivity.this.finish();
                return;

            }
        } else {

            configObj = TrainingGameConfig.getInstence();

            configObj.setGameMode(SENTENCE_GAME_VALUE);
            whoAmI = I_AM_FIRST_USER;
            configObj.setWhoAmI(whoAmI);

            // if train ,, use random sentence
            sentenceIndex = generateRandom
                    .nextInt(quest.getNumberOfSentences() - 1);

            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Practicing , Sentence index " + sentenceIndex);

            configObj.setSentenceIndex(sentenceIndex);

            /*
             * choose one sentence and draw it
             */
            sentence = createSentence(sentenceIndex);
            drow = new SentenceDrower();
            boolean isGood = drow.drowSentence(sentence, pointsArrayList);

            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Sentence drawer "
                    + isGood);

            if (isGood) {
                fillRemainingPoints(pointsArrayList);
            } else {
                sentence = createSentence(sentenceIndex);
                strPuzzle = defaultSentences[sentenceIndex];


                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Practicing , Default puzzel " + strPuzzle);

                strPuzzle = putPuzzleStringOnPoints(pointsArrayList, strPuzzle);


                FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Practicing , After put puzzel " + strPuzzle);

                setSentencewordWithPathesFromSolverAlgorithm(sentence,
                        strPuzzle);
            }

        }

        score = 0;

        strPuzzle = "";

        try {

            for (int i = 0; i < pointsArrayList.size(); i++) {

                strPuzzle += pointsArrayList.get(i).getLetter().getLetter();
            }

            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"After tajme3 "
                    + strPuzzle);

            addSentencewords(sentence);

            configObj.updateRoundConfiguration(strPuzzle);
            /*
             * Add sentence layout above points
             */
            addSentenceTextViewsLayout(sentence);
            setHintLettersSentence();

        } catch (Exception e) {


            FirebaseCrashlytics.getInstance().log("INFO::"+TAG+"::"+"Fucker exception , "
                    + e.getMessage());

            Toast.makeText(context,
                    getResources().getString(R.string.server_failer),
                    Toast.LENGTH_SHORT).show();
            Intent toActivity = new Intent(context, MainMenuActivity.class);

            toActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(toActivity);
            SentenceGameActivity.this.finish();
            return;

        }


//        loadAd(R.id.banner_ad);

    }
    /*
     * Initialize Powers Up Dialog
     */
    private void initiatPowersUp() {

        coins = sqlLiteDB.getUserCoins(userId);
        coins += 15;
        TextView coinsText = (TextView) dialogPowerUps
                .findViewById(R.id.coin_score);
        coinsText.setText("" + coins);
        ImageView dialogButton = (ImageView) dialogPowerUps
                .findViewById(R.id.start_button);

        dialogButton.setOnClickListener(this);
        ImageView hintButton = (ImageView) dialogPowerUps
                .findViewById(R.id.hint_button);

        hintButton.setOnClickListener(this);
        ImageView freezeButton = (ImageView) dialogPowerUps
                .findViewById(R.id.freez_button);

        freezeButton.setOnClickListener(this);
        ImageView reButton = (ImageView) dialogPowerUps
                .findViewById(R.id.re_button);

        reButton.setImageResource(R.drawable.show_button_behavior);
        ImageView KashfHroofImage = (ImageView) dialogPowerUps
                .findViewById(R.id.kashf_horof);
        KashfHroofImage.setImageResource(R.drawable.text_show);
        reButton.setOnClickListener(this);
        powersLayout = (LinearLayout) dialogPowerUps
                .findViewById(R.id.powersLayout);
    }

    public void pauseDialog() {
        if (!isPause) {
            timer.cancel();
            if (time < 10000)
                warningSound.pauseSound();

            isPause = true;
        }
        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.setAlpha((float) 0.08);
        ImageView dialogButton = (ImageView) dialog
                .findViewById(R.id.start_button);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!frozenMode) {
                    timer = new CounterClass(time, 1000);
                    timer.start();
                    if (time < 10000)
                        warningSound.resumeSound();
                } else {
                    timer = new CounterClass(frozenTime, 1000);
                    timer.start();
                }
                isPause = false;
                mainLayout.setAlpha((float) 1.0);
                dialog.dismiss();

            }
        });
        ImageView ens7ab_Button = (ImageView) dialog
                .findViewById(R.id.ens7ab_button);
        ens7ab_Button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                saveConfigurationAndFinishGame();

            }
        });
        dialog.show();
    }


    private String putPuzzleStringOnPoints(ArrayList<Point> Points,
                                           String strPuzzel) {

        String str = "";
        StringBuilder bd = new StringBuilder(strPuzzel);
        int c = 0;

        // for (c = strPuzzel.length() - 1; c >= 0; c--) {
        for (c = 0; c < strPuzzel.length(); c++) {

            str += bd.charAt(c);

            int index = Methods.getCharIndex(context, bd.charAt(c));
            Letter letter = new Letter(lettersArray.get(index));
            Points.get(c).setLetter(letter);

        }

        return str;

    }

    @Override
    public void onPause() {
        super.onPause(); // Always call the superclass method first

        if (isTimer) {
            if (time < 10000)
                warningSound.pauseSound();
            timer.cancel();
            isPause = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (banner != null) {
//            banner.destroy();
//            banner = null;
//        }
        LinearLayout sentencelayout =   (LinearLayout) this
                .findViewById(R.id.mainLayout);
        unbindDrawables(sentencelayout );
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (dialogPowerUps.isShowing()) {
            if (!isTimer) {
                Intent toActivty = new Intent(getApplicationContext(),
                        SecondMenuActivity.class);
                toActivty.putExtra(GAME_MODE, WORD_GAME_VALUE);

                startActivity(toActivty);
                SentenceGameActivity.this.finish();

            }

        } else {
            pauseDialog();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isTimer) {
            if (time < 10000)
                warningSound.stop();
            timer.cancel();
        }
    }

    public void onResume() {
        super.onResume();
        if (isPause)
            pauseDialog();
    }
    /*
     remove layout resources
      */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
                android.util.Log.i("view", "" + i);
            }
            ((ViewGroup) view).removeAllViews();
            view.setBackgroundResource(0);
        }
    }


    private void addSentenceTextViewsLayout(Sentence sentence) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
        params.setMargins(5, 5, 5, 5);

        LinearLayout firstLayout = (LinearLayout) findViewById(R.id.firstRawLayout);
        LinearLayout secondLayout = (LinearLayout) findViewById(R.id.secondRawLayout);

        int numberOfWords = sentence.getSentenceWords().size();
        myTextViews = new TextView[numberOfWords];
        for (int i = 0; i < numberOfWords; i++) {
            // create a new text view
            TextView textDynamic = new TextView(this);

            textDynamic.setLayoutParams(params);
            // textDynamic.setBackgroundColor(Color.GREEN);
            textDynamic.setTextColor(Color.WHITE);
            textDynamic.setTextScaleX(1);
            textDynamic.setTextSize(getResources().getDimension(
                    R.dimen.text_size));
            textDynamic.setPadding(5, 5, 5, 5);
            textDynamic.setTypeface(robotoRegularTypeface);

            // set some properties of rowTextView or something
            String temp = "";
            int numberOfLettersInWord = sentence.getSentenceWords().get(i)
                    .getWordLetters().size();
            for (int j = 0; j < numberOfLettersInWord; j++) {
                temp += "_ ";
            }
            textDynamic.setText(temp);
            // save a reference to the textview for later
            myTextViews[i] = textDynamic;
        }
        int NumberOfWordsPerLine = 3;
        TextView[] firstThreeWordsTextView = new TextView[NumberOfWordsPerLine];
        for (int i = 0; i < myTextViews.length && i < NumberOfWordsPerLine; i++) {
            firstThreeWordsTextView[i] = myTextViews[i];
        }

        for (int i = firstThreeWordsTextView.length - 1; i >= 0; i--) {

            if (firstThreeWordsTextView[i] != null)
                firstLayout.addView(firstThreeWordsTextView[i]);

        }
        if (myTextViews.length > NumberOfWordsPerLine) {

            for (int i = myTextViews.length - 1; i >= NumberOfWordsPerLine; i--) {

                secondLayout.addView(myTextViews[i]);

            }

        }

    }

    private void fillRemainingPoints(ArrayList<Point> pointsList) {

        Random randomGenerator = new Random();
        int randomIndex;
        for (Point point : pointsList) {

            if (!point.isTaken() || point.getLetter() == null) {

                randomIndex = randomGenerator.nextInt(lettersArray.size());
                Letter letter = new Letter(lettersArray.get(randomIndex));
                point.setLetter(letter);
                point.setTaken(true);
            }
        }

    }

    private Sentence createSentence(int i) {

        Sentence sentence = new Sentence();
        ArrayList Words = this.quest.getStatment(i);
        for (int j = 0; j < Words.size(); j++) {
            Word word1 = new Word();
            int[] word = (int[]) Words.get(j);
            for (int k = 0; k < word.length; k++) {
                word1.getWordLetters().add(
                        new Letter(lettersArray.get(word[k])));
            }
            boolean selectedLetters[] = new boolean[word1.getWordLetters()
                    .size()];
            word1.setSelectedLetters(selectedLetters);
            sentence.getSentenceWords().add(word1);
        }

        return sentence;
    }

    private void showSucessAnimation() {

        ahsantImage.setAlpha(1.0F);
        AlphaAnimation alpha = new AlphaAnimation(1.0F, 0.0F);
        alpha.setDuration(1400); // Make animation instant
        alpha.setFillAfter(true); // Tell it to persist after the animation ends
        // And then on your layout
        ahsantImage.startAnimation(alpha);

    }

    private void initialSounds() {
        addPowerUpSound = new Sounds(context, 9);
        removePowerUpSound = new Sounds(context, 8);
        warningSound = new Sounds(context, 7);
        winningSound = new Sounds(context, 3);
        buttonSound = new Sounds(context, 1);
        WrongSound = new Sounds(context, 0);
        freezeSound = new Sounds(context, 5);
        unFreezeSound = new Sounds(context, 6);
    }

    private void initi() {

        context =getApplicationContext();

        pauseImage = (ImageView) findViewById(R.id.pause_button);
        // sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
        // MODE_PRIVATE);
        // LinearLayout topLayout = (LinearLayout) findViewById(R.id.TabLayout);
        // topLayout.setVisibility(View.INVISIBLE);

        pathDrower = new PathDrower(this);
        arrowDrower = new ArrowDrower(this);

        pointsArrayList = Methods.initializePointsArrayList(this);

        currentWordArrayList = new ArrayList<Point>();
        ahsantImage = (ImageView) findViewById(R.id.ahsantImgId);
        // ahsantImage.setImageResource(R.drawable.ahsant);
        ahsantImage.setAlpha(0.0F);
        generateRandom = new Random();
        defaultSentences = context.getResources().getStringArray(
                R.array.default_sentences);

        robotoBoldTypeface = Typeface.createFromAsset(context.getAssets(),
                IConstants.Fonts.ROBOTO_CONDENSED_BOLD);


        robotoRegularTypeface = Typeface.createFromAsset(context.getAssets(),
                IConstants.Fonts.ROBOTO_CONDENSED_REGULAR);
        pauseImage = (ImageView) findViewById(R.id.pause_button);

        scoreTextView = (TextView) findViewById(R.id.ScoreView);
        scoreTextView.setTypeface(robotoBoldTypeface);
        textViewTime = (TextView) findViewById(R.id.timerView);
        textViewTime.setTypeface(robotoRegularTypeface);

        /*
         * pause insialization
         */
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.pause_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // initialize letter Array
        uiData = new UIData();
        lettersArray = uiData.getLettersArray(context);
        // power Ups

        pauseImage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pauseDialog();
            }
        });
    }

    /**
     * Load advertisement in current view.
     */
//    private void loadAd(int viewId) {
//
//        try {
//
//            // Load ads.
//            banner = (AdView) findViewById(viewId);
//            AdRequest adRequest = new AdRequest.Builder().build();
//            banner.loadAd(adRequest);
//
//        } catch (Exception e) {
//
//            Log.i(TAG, "" + e.getMessage());
//        }
//
//    }

    public int randomWordHintIndex() {
        int c = 0;
        while (c < quest.getAllPossibleSentenceWords().size()) {
            Random rand = new Random();

            int n = rand.nextInt(quest.getAllPossibleSentenceWords().size());
            if (!isSelected(quest.getAllPossibleSentenceWords().get(n)
                    .getWord()))
                return n;
        }
        return c;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        /*
         * When ever "Down": clear and start new stack ,, new track. While it is
         * "move" : add to stack increment counter. when it's "UP" :check stack
         * ,, if wrong clear . if correct write word down . change color
         */
        if (pathDrower.isDoItOnce()) {
            pathDrower.initilizeBitmap(context);
            pathDrower.setDoItOnce(false);
        }
        if (arrowDrower.isDoItOnce()) {
            arrowDrower.initilizeBitmap();
            arrowDrower.setDoItOnce(false);
        }

        float x = e.getX();
        float y = e.getY();

        int imageIndex;

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:

                imageIndex = Methods.getImageIndex(x, y, pointsArrayList);
                if (imageIndex != -1) {
                    // printImagesIndex();
                    // for finger path drawing
                    pathDrower.setDownx(Methods.getXCenterOfPoint(pointsArrayList
                            .get(imageIndex)));
                    pathDrower.setDowny(Methods.getYCenterOfPoint(pointsArrayList
                            .get(imageIndex)));

                    Letter letter = pointsArrayList.get(imageIndex).getLetter();

                    pointsArrayList.get(imageIndex).setImageResorce(
                            letter.getLetterImageSelected());
                    pointsArrayList.get(imageIndex).animate(this);
                    // sound for button
                    buttonSound.play(0,context);
                    // clear currentWord
                    currentWordArrayList.clear();
                    // add Letter to currentWord
                    currentWordArrayList.add(pointsArrayList.get(imageIndex));
                    // save previous point to track neighbors only
                    previousPoint = pointsArrayList.get(imageIndex);

                }

                break;
            case MotionEvent.ACTION_MOVE:

                imageIndex = Methods.getImageIndex(x, y, pointsArrayList);
                if (imageIndex != -1) {
                    currentPoint = pointsArrayList.get(imageIndex);
                    if (previousPoint != null && previousPoint != currentPoint
                            && !isCurrentWordUsed(currentPoint)) {

                        // currentPoint = pointsArrayList.get(imageIndex);
                        // check if current point beside previous point
                        // TODO: check if current point is not already part of
                        // currentWord
                        if (currentPoint.isNeighbor(previousPoint)) {

                            // for finger path drawing
                            pathDrower.setUpx(Methods
                                    .getXCenterOfPoint(pointsArrayList
                                            .get(imageIndex)));
                            pathDrower.setUpy(Methods
                                    .getYCenterOfPoint(pointsArrayList
                                            .get(imageIndex)));

                            pathDrower.drawLine();
                            pathDrower.setDownx(pathDrower.getUpx());
                            pathDrower.setDowny(pathDrower.getUpy());

                            Letter letter = pointsArrayList.get(imageIndex)
                                    .getLetter();
                            pointsArrayList.get(imageIndex).setImageResorce(
                                    letter.getLetterImageSelected());
                            pointsArrayList.get(imageIndex).animate(this);
                            // sound for button
                            buttonSound.play(0,context);
                            // add Letter to currentWord
                            currentWordArrayList.add(currentPoint);
                            // save previous point
                            previousPoint = currentPoint;

                        }
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                imageIndex = Methods.getImageIndex(x, y, pointsArrayList);
                if (imageIndex != -1) {
                    if (previousPoint != null) {
                        // for finger path drowing
                        pathDrower.setUpx(e.getX());
                        pathDrower.setUpy(e.getY());

                        pathDrower.drawLine();

                    }
                }

                // check current word
                int correctWordIndex = checkCurrentWord(currentWordArrayList);

                for (Point point : currentWordArrayList) {

                    point.getImage().setImageResource(
                            point.getLetter().getImageId());
                }
                if (correctWordIndex == -1) {

                    // Wrong choice
                    WrongSound.play(0,context);
                    // TODO: add sounds , animations

                } else if (correctWordIndex == -2) {
                    // word is guessed before
                    removePowerUpSound.play(0,context);

                } else {
                    // Word is correct
                    arrowDrower.clearDrawing();
                    isWordHint = false;
                    showSucessAnimation();
                    winningSound.play(0,context);
                    // increment score

                    String wordString = drawWordInSentenceWordArea(correctWordIndex);

                    // add CorrectWord to QuestionData Array to be used in
                    // reportGame
                    addNewWordToAllSentenceWords(wordString, currentWordArrayList);

                    // TODO: check if all words are guessed , if yes finish the game
                    // update gameConfiguration object with all info (score ,
                    // sentence , sentenceLetterarrangment) save and send
                    // notification
                    boolean isAllWordGuessed = true;
                    for (Word word : sentence.getSentenceWords()) {
                        if (!word.isGuessed()) {
                            isAllWordGuessed = false;
                            break;
                        }
                    }

                    if (isAllWordGuessed) {
                        saveConfigurationAndFinishGame();
                    }

                }
                // clear finger path
                pathDrower.clearDrawing();

                break;

        }

        return true;
    }

    /**
     * add correct word to questionData to be used in report
     *
     * @param word
     * @param currentWordArrayList
     */
    private void addNewWordToAllSentenceWords(String word,
                                              ArrayList<Point> currentWordArrayList) {

        int[] path = new int[currentWordArrayList.size()];

        for (int i = 0; i < currentWordArrayList.size(); i++) {
            path[i] = currentWordArrayList.get(i).getIndexBasedOnPosition();
        }
        int score2 = Methods.calculateScore(path, path.length);
        score += score2;

        scoreTextView.setText("" + score);
        CorrectWord correctWord = new CorrectWord(score2, word, path,
                path.length, strPuzzle);

        quest.addToAllSentenceWord(correctWord);

    }

    private boolean isSelected(String word) {

        if (wordsSelected.contains(" " + word + " "))
            return true;

        return false;
    }

    private void addSentencewords(Sentence sentence) {
        String word = "";
        for (int i = 0; i < sentence.getSentenceWords().size(); i++) {
            int[] path = new int[sentence.getSentenceWords().get(i)
                    .getWordPoints().size()];

            for (int j = 0; j < sentence.getSentenceWords().get(i)
                    .getWordPoints().size(); j++) {
                path[j] = sentence.getSentenceWords().get(i).getWordPoints()
                        .get(j).getIndexBasedOnPosition();
                word += pointsArrayList.get(path[j]).getLetter().getLetter();
            }

            CorrectWord correctWord = new CorrectWord(Methods.calculateScore(
                    path, path.length), word, path, path.length, strPuzzle);
            Log.i("w: ", word);
            quest.addToAllPossibleSentenceWords(correctWord);
            word = "";

        }
    }

    private void setSentencewordWithPathesFromSolverAlgorithm(
            Sentence sentence, String puzzle) {

        String words[] = new String[sentence.getSentenceWords().size()];
        for (int i = 0; i < sentence.getSentenceWords().size(); i++) {

            String word = "";
            for (int j = 0; j < sentence.getSentenceWords().get(i)
                    .getWordLetters().size(); j++) {

                word += sentence.getSentenceWords().get(i).getWordLetters()
                        .get(j).getLetter();
                sentence.getSentenceWords().get(i).setWord(word);

            }

            Log.i("w1", word);
            words[i] = word;
        }
        // Arrays.sort(words);
        Collection<String> collec = Arrays.asList(words);

        Solver solver = new Solver(4, puzzle, context);
        TreeWord t = new TreeWord(collec);
        // Log.e("exist", "" + puzzle);
        solver.solve(t);

        ArrayList<CorrectWord> correctWordsArrayList = solver
                .getAllPossibbleWord();
        for (CorrectWord correctWord : correctWordsArrayList) {

            Log.i("correctWord1", "" + correctWord.getWord());
        }
        // quest.setAllPossibleSentenceWords(correctWordsArrayList);

        for (int i = 0; i < sentence.getSentenceWords().size(); i++) {

            for (CorrectWord correctWord : correctWordsArrayList) {

                if (sentence.getSentenceWords().get(i).getWord()
                        .equals(correctWord.getWord())) {
                    Log.i("correctWord", "" + correctWord.getWord());

                    for (int pathIndex : correctWord.getPath()) {
                        sentence.getSentenceWords().get(i).getWordPoints()
                                .add(pointsArrayList.get(pathIndex));

                    }
                    break;

                }
            }

        }

        // Log.e("solver",""+solver.getAllPossibbleWord().get(0).getPathLength());

    }

    /**
     * finish the game , update gameConfiguration object with all info (score ,
     * sentence , sentenceLetterarrangment) save and send notification
     */
    private void saveConfigurationAndFinishGame() {
        // save score
        // save sentence
        // save gameConfiguration

        warningSound.stop();

        roundConfiguration = "";
        for (int i = 0; i < pointsArrayList.size(); i++) {
            roundConfiguration += pointsArrayList.get(i).getLetter()
                    .getLetter();
        }

        configObj.updateRoundInfo(score, roundConfiguration, sentenceIndex,
                whoAmI);

        // Go To reportActivity
        Intent toActivity = new Intent(context, GameReportActivity.class);
        toActivity.putExtra(PLAY_WITH, playWith);

        startActivity(toActivity);

        SentenceGameActivity.this.finish();

    }

    private void setHintLettersSentence() {
        int numberOfWords = sentence.getSentenceWords().size();
        for (int i = 0; i < numberOfWords; i++) {

            if (!isSelected(quest.getAllPossibleSentenceWords().get(i)
                    .getWord()))
                drawHintWordInSentenceWordArea(i);

        }
    }

    private void drawHintWordInSentenceWordArea(int correctWordIndex) {

        TextView wordTextView = myTextViews[correctWordIndex];
        int numberOfLettersInWord = sentence.getSentenceWords()
                .get(correctWordIndex).getWordLetters().size();
        String wordAsString = "";
        ArrayList<Integer> wordindex = new ArrayList<Integer>();

        for (int i = 0; i < numberOfLettersInWord; i++) {
            if (!sentence.getSentenceWords().get(correctWordIndex)
                    .getSelectedLetters()[i])
                wordindex.add(i);
        }
        // int count=0;
        int n = 0;
        Random rnd = new Random();

        if (wordindex.size() > 2) {
            n = rnd.nextInt(wordindex.size());

            sentence.getSentenceWords().get(correctWordIndex)
                    .getSelectedLetters()[wordindex.get(n)] = true;
        }

        // count++;

        for (int j = 0; j < numberOfLettersInWord; j++) {
            if (wordindex.size() > 2 && j == wordindex.get(n))
                wordAsString += sentence.getSentenceWords()
                        .get(correctWordIndex).getWordLetters().get(j)
                        .getLetter();
            else if (sentence.getSentenceWords().get(correctWordIndex)
                    .getSelectedLetters()[j])
                wordAsString += sentence.getSentenceWords()
                        .get(correctWordIndex).getWordLetters().get(j)
                        .getLetter();
            else
                wordAsString += "_ ";
        }
        wordTextView.setText(wordAsString /* + "   " */);

    }

    private boolean isCurrentWordUsed(Point currentPoint) {

        for (Point point : currentWordArrayList) {
            if (point == currentPoint)
                return true;
        }
        return false;
    }

    private String drawWordInSentenceWordArea(int correctWordIndex) {

        TextView wordTextView = myTextViews[correctWordIndex];
        String wordAsString = quest.getSentencesWords(sentenceIndex).get(
                correctWordIndex);
        wordTextView.setText(wordAsString);
        wordsSelected += "" + wordAsString + " ";
        return wordAsString;
    }

    /*
     * Check if this word is one of the sentence words
     */
    private int checkCurrentWord(ArrayList<Point> currentWord) {

        // TODO: check if found word is already found before
        boolean isThereAMacth = false;
        boolean isItThisWord = true;
        int wordIndex = 0;
        // loop on all words
        for (wordIndex = 0; wordIndex < sentence.getSentenceWords().size(); wordIndex++) {

            Word word = sentence.getSentenceWords().get(wordIndex);

            isItThisWord = true;
            // compare only if both words has the same length and the word is
            // not already guessed
            if (word.getWordPoints().size() == currentWord.size()) {

                for (int i = 0; i < word.getWordPoints().size(); i++) {

                    Point pointFromSentenceWord = word.getWordPoints().get(i);

                    // compare each letter with this word
                    if (!pointFromSentenceWord.hasThaSameLetterAs(currentWord
                            .get(i).getLetter())) {
                        // it is not this word
                        isItThisWord = false;
                        break;

                    }

                }

                if (isItThisWord) {
                    isThereAMacth = true;
                    if (word.isGuessed()) {
                        return -2;
                    }
                    word.setGuessed(true);
                    break;
                }
            }

        }

        if (isThereAMacth)
            return wordIndex;
        else
            return -1;
    }

    private void setFreeze() {
        this.frozenMode = true;
        freezeSound.play(0,context);
        if (time < 10000)
            warningSound.pauseSound();
        timer.cancel();
        timer = new CounterClass(20000, 1000);
        timer.start();

        powerUp.setFreezeAction(this);
        for (Point point : pointsArrayList) {
            point.getLetter().setFreezMode(true);
            point.setImageResorce(point.getLetter().getImageId());
        }
    }

    private void setUnFreeze() {
        timer = new CounterClass(time, 1000);
        timer.start();
        if (time < 10000)
            warningSound.resumeSound();
        this.frozenMode = false;
        unFreezeSound.play(0,context);
        powerUp.setNormalBackground(this);
        for (Point point : pointsArrayList) {
            point.getLetter().setFreezMode(false);
            point.setImageResorce(point.getLetter().getImageId());
        }
    }

    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            if (!frozenMode) {

                String hms = String.format("%02d:%02d", 0, 0);

                textViewTime.setText(hms);
                // go to play report activity
                saveConfigurationAndFinishGame();
            } else
                setUnFreeze();

        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (!frozenMode) {
                if (!warningSound.isSilent(context)) {
                    warningSound.setMute();
                    isMute = true;
                } else {
                    if (isMute) {
                        warningSound.setUnMute();
                        isMute = false;
                    }
                }
                time = millisUntilFinished;
                long millis = millisUntilFinished;
                long minite = TimeUnit.MILLISECONDS.toMinutes(millis)
                        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                        .toHours(millis));
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes(millis));
                String hms = String.format("%02d:%02d", minite, seconds);
                if (minite == 0 && seconds == 9)
                    warningSound.play(0,context);

                textViewTime.setText(hms);
            } else
                frozenTime = millisUntilFinished;
        }
    }

    /*
     * Power Ups Configration
     */
    private void powerUpsConfiguration() {

        LinearLayout bounceLayout = (LinearLayout) findViewById(R.id.powerslayout);
        for (int i = 0; i < powerUp.getPowerUps().length; i++) {

            if (i == 0 && powerUp.getPowerUps()[i] > 0) {
                // hint
                for (int j = 0; j < powerUp.getPowerUps()[i]; j++) {
                    final ImageView imageview1 = new ImageView(this);
                    imageview1
                            .setImageResource(R.drawable.hint_button_behavior);
                    imageview1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrowDrower.isDoItOnce()) {
                                arrowDrower.initilizeBitmap();
                                arrowDrower.setDoItOnce(false);
                            }
                            if (!isWordHint) {
                                wordHintIndex = randomWordHintIndex();

                                isWordHint = true;
                                // powerUp.wordHintAction(
                                // quest.getAllSentenceWords(),
                                // wordHintIndex, powersUpPoints);

                                CorrectWord tempWord = quest
                                        .getAllPossibleSentenceWords().get(
                                                wordHintIndex);
                                arrowDrower.clearDrawing();
                                for (int k = 0; k < tempWord.getPathLength(); k++) {
                                    if (k > 0) {
                                        // from

                                        arrowDrower.setDownx(Methods
                                                .getXCenterOfPoint(pointsArrayList
                                                        .get(tempWord.getPath()[k - 1])));

                                        arrowDrower.setDowny(Methods
                                                .getYCenterOfPoint(pointsArrayList
                                                        .get(tempWord.getPath()[k - 1])));

                                        // to
                                        arrowDrower.setUpx(Methods
                                                .getXCenterOfPoint(pointsArrayList
                                                        .get(tempWord.getPath()[k])));
                                        arrowDrower.setUpy(Methods
                                                .getYCenterOfPoint(pointsArrayList
                                                        .get(tempWord.getPath()[k])));

                                        arrowDrower.drawArrow(20);

                                    }

                                }
                                imageview1.setVisibility(View.GONE);
                            }

                        }
                    });
                    bounceLayout.addView(imageview1);
                }
            } else if (i == 1 && powerUp.getPowerUps()[i] > 0) {// replay
                for (int j = 0; j < powerUp.getPowerUps()[i]; j++) {
                    final ImageView imageview1 = new ImageView(this);
                    imageview1
                            .setImageResource(R.drawable.show_button_behavior);
                    imageview1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setHintLettersSentence();
                            imageview1.setVisibility(View.GONE);
                        }
                    });
                    bounceLayout.addView(imageview1);
                }
            } else if (i == 2 && powerUp.getPowerUps()[i] > 0) {// freeze
                for (int j = 0; j < powerUp.getPowerUps()[i]; j++) {

                    final ImageView imageview1 = new ImageView(this);
                    imageview1.setImageResource(R.drawable.freeze_button_test);
                    imageview1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!frozenMode) {
                                setFreeze();
                                imageview1.setVisibility(View.GONE);
                            }
                        }
                    });
                    bounceLayout.addView(imageview1);
                }

            }

        }
    }

    @Override
    public void onClick(View v) {
        Intent toActivty;
        final TextView scoreText = (TextView) dialogPowerUps
                .findViewById(R.id.coin_score);
        // todo here from datbase
        coins = Integer.parseInt(scoreText.getText().toString());
        switch (v.getId()) {
            case R.id.start_button:
                // powers[1]++;
                // powers[2]++;
                if (pathDrower.isDoItOnce()) {
                    pathDrower.initilizeBitmap(context);
                    pathDrower.setDoItOnce(false);
                }
                powerUp.setPowerUps(powers);
                powerUpsConfiguration();
                /*
                 * set the Timer
                 */
                // set 5:00 minute
                sqlLiteDB.setUserCoins(userId, coins);
                timer = new CounterClass(120000, 1000);
                timer.start();
                isTimer = true;
                dialogPowerUps.dismiss();




                break;

            case R.id.hint_button:
                if (coins >= POWERUPS_SCORE_VALUE && powers[0] == 0
                        && powers[1] + powers[2] < 3) {
                    powers[0]++;
                    /*
                     * todo decrease the main score
                     */
                    final PowerUP hintPowerUp = new PowerUP(0, context, 5);

                    addPowerUpSound.play(0,context);
                    powersLayout.addView(hintPowerUp.getPowerUpimage());
                    coins -= hintPowerUp.getScore();
                    scoreText.setText("" + coins);
                    hintPowerUp.getPowerUpimage().setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO increase the the main score
                                    removePowerUpSound.play(0,context);
                                    hintPowerUp
                                            .getPowerUpimage().setVisibility(View.GONE);


                                    powers[0]--;
                                    coins += hintPowerUp.getScore();
                                    scoreText.setText("" + coins);
                                }
                            });
                }
                break;
            case R.id.re_button:
                if (coins >= POWERUPS_SCORE_VALUE
                        && powers[0] + powers[1] + powers[2] < 3) {
                    powers[1]++;
                    /*
                     * todo decrease the main score
                     */
                    final PowerUP showPowerUp = new PowerUP(3, context, 5);
                    addPowerUpSound.play(0,context);

                    powersLayout.addView(showPowerUp.getPowerUpimage());
                    coins -= showPowerUp.getScore();
                    scoreText.setText("" + coins);
                    showPowerUp.getPowerUpimage().setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO incsrease the the main score
                                    removePowerUpSound.play(0,context);

                                    showPowerUp
                                            .getPowerUpimage().setVisibility(View.GONE);
                                    powers[1]--;
                                    coins += showPowerUp.getScore();
                                    scoreText.setText("" + coins);
                                }
                            });
                }
                break;
            case R.id.freez_button:
                if (coins >= POWERUPS_SCORE_VALUE
                        && powers[0] + powers[1] + powers[2] < 3) {
                    powers[2]++;
                    final PowerUP freezPowerUp = new PowerUP(2, context, 5);
                    addPowerUpSound.play(0,context);
                    powersLayout.addView(freezPowerUp.getPowerUpimage());
                    coins -= freezPowerUp.getScore();
                    scoreText.setText("" + coins);
                    freezPowerUp.getPowerUpimage().setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO increase the the main score
                                    removePowerUpSound.play(0,context);

                                    freezPowerUp
                                            .getPowerUpimage().setVisibility(View.GONE);
                                    powers[2]--;
                                    coins += freezPowerUp.getScore();
                                    scoreText.setText("" + coins);
                                }
                            });
                }
                break;
        }

    }


}
