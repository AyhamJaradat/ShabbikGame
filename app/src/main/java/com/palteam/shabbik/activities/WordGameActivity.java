package com.palteam.shabbik.activities;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
import com.palteam.shabbik.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.palteam.shabbik.beans.CorrectWord;
import com.palteam.shabbik.beans.Letter;
import com.palteam.shabbik.beans.Point;
import com.palteam.shabbik.beans.PowerUP;
import com.palteam.shabbik.beans.Puzzle;
import com.palteam.shabbik.configuration.Configuration;
import com.palteam.shabbik.configuration.GameConfiguration;
import com.palteam.shabbik.configuration.TrainingGameConfig;
import com.palteam.shabbik.services.GeneratePuzzleWordsService;
import com.palteam.shabbik.solverAlgorithm.Solver;
import com.palteam.shabbik.solverAlgorithm.TreeWord;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.AnimationManager;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.ArrowDrower;
import com.palteam.shabbik.utils.IConstants;
//import com.palteam.shabbik.utils.Log;
import com.palteam.shabbik.utils.Methods;
import com.palteam.shabbik.utils.PathDrower;
import com.palteam.shabbik.utils.PowerUps;
import com.palteam.shabbik.utils.QuestionsData;
import com.palteam.shabbik.utils.Sounds;
import com.palteam.shabbik.utils.UIData;

public class WordGameActivity extends Activity implements IConstants,
        OnClickListener {
    // -----------------------------------------------------------
    // Constants.
    // -----------------------------------------------------------
    private final String TAG = WordGameActivity.class.getSimpleName();

    // If timer reach this value an alarm sound will start to notify user
    // that the time almost done.
    private final int TIMER_ALARM_DURATION = 10000;


    // -----------------------------------------------------------
    // Views.
    // -----------------------------------------------------------
    private ImageView ahsantImage;
    private TextView wordTextView, scoreTextView, textViewTime;
//    private AdView banner;
    private LinearLayout powersLayout;
    private Dialog pauseDialog;
    private Dialog dialogPowerUps;


    // -----------------------------------------------------------
    // Sounds.
    // -----------------------------------------------------------
    private Sounds addPowerUpSound;
    private Sounds removePowerUpSound;
    private Sounds warningSound;
    private Sounds winningSound;
    private Sounds buttonSound;
    private Sounds wrongSound;
    private Sounds freezeSound;
    private Sounds unFreezeSound;


    // -----------------------------------------------------------
    // Flags.
    // -----------------------------------------------------------
    private boolean frozenMode = false;
    private boolean isTimer = false;
    private boolean isMute = false;
    private boolean isPaused = false;
    private boolean isWordHint = false;


    // -----------------------------------------------------------
    // Storage.
    // -----------------------------------------------------------
    private ShabbikDAO sqlLiteDB;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    // -----------------------------------------------------------
    // Lists.
    // -----------------------------------------------------------
    private ArrayList<Letter> lettersArray;
    private ArrayList<Point> currentWordArrayList;
    private ArrayList<Point> pointsArrayList;
    private int powers[] = new int[3];

    // -----------------------------------------------------------
    // Mix.
    // -----------------------------------------------------------

    // game configuration
    private Configuration configObj;
    private Solver solver;
    private QuestionsData wordsData;
    private PowerUps powerUp;
    private CounterClass timer;
    private Puzzle roundPuzzle;

    /*
     * Parameters to draw Line with finger move
     */
    private PathDrower pathDrower;
    private ArrowDrower arrowDrower;
    private Point previousPoint;
    private String wordTrue = "";
    private Context context;

    // -----------------------------------------------------------
    // Numbers.
    // -----------------------------------------------------------
    private long time;
    private long frozenTime;
    private int playWith;
    private int score;
    private int wordHintIndex;
    private int whoAmI;
    private int userId;
    private int coins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.logHeap(TAG);
//        setContentView(R.layout.activity_word_game);
        setContentView(R.layout.letters_grid);

        // initialize sounds
        initialSounds();

        // initialize UI components
        initialize();

        //  Open dialog for power ups to choose before he play
        initializeAndShowPowerUpsDialog();

        //Create Game configuration object
        playWith = getIntent().getExtras().getInt(PLAY_WITH);
        Log.i("playwith",""+playWith);
        if (playWith != PRACTICE_PLAYING) {
            Log.i("Im here not practice","");
            // I am playing against a User
            int gameId = getIntent().getExtras().getInt(GAME_ID);

            int currentRoundId = getIntent().getExtras().getInt(
                    GAME_CURRENT_ROUND_ID);
            int currentUserTurnId = getIntent().getExtras().getInt(
                    GAME_USER_TURN_ID);

            // current user turn ID should be equal to the user Id
            if (userId != currentUserTurnId) {

                WordGameActivity.this.finish();
                return;
            }
            Log.i("Im here not practice2","");
            configObj = GameConfiguration.getInstence();
            configObj.initializeGameConfiguration(gameId, currentRoundId,
                    currentUserTurnId, context);
            Log.i("userId2",""+configObj.getGame().getFirstUserId());
            if (userId == configObj.getGame().getFirstUserId()) {

                whoAmI = I_AM_FIRST_USER;
                configObj.setWhoAmI(whoAmI);

                // I am First Usr ,, I will generate new random string puzzle
                if (sharedPreferences.getBoolean(IS_DEFAULT_PUZZL_DIRTY, true)) {

                    // Default puzzle is dirty OR not generated yet
                    // read puzzle from string
                    readDefaultPuzzle();

                } else {

                    // default puzzle is NOT dirty
                    readGeneratedPuzzleFromFile();

                }

            } else if (userId == configObj.getGame().getSecondUserId()) {

                whoAmI = I_AM_SECOND_USER;
                configObj.setWhoAmI(whoAmI);
                roundPuzzle = sqlLiteDB.retrievePuzzles(configObj.getRound()
                        .getId());
                // I am second user ,, I will get the generated puzzle
                if (roundPuzzle == null) {
                    // read puzzle from string
                    readDefaultPuzzle();
                }

            }

        } else {

            // practice mode
            configObj = TrainingGameConfig.getInstence();
            whoAmI = I_AM_FIRST_USER;
            configObj.setWhoAmI(whoAmI);
            if (sharedPreferences.getBoolean(IS_DEFAULT_PUZZL_DIRTY, true)) {
                // default puzzle is dirty OR not generated yet
                // read puzzle from string
                readDefaultPuzzle();
            } else {
                // default puzzle is NOT dirty
                readGeneratedPuzzleFromFile();
            }
        }

        // All Time
        configObj.setGameMode(WORD_GAME_VALUE);
        configObj.updateRoundConfiguration(roundPuzzle.getPuzzle());
        score = 0; /* configObj.getScore(whoAmI); */

        // put puzzle on points
        putPuzzleStringOnPoints(pointsArrayList, roundPuzzle.getPuzzle(),
                false);

        // generate tree
        TreeWord tree = Methods.initialSolverFromString(
                roundPuzzle.getAllWords(), context);

        // set tree in questions data
        wordsData.setTree(tree);

        // Solve the matrix
        solver = new Solver(4, roundPuzzle.getPuzzle(), context);
        solver.solve(wordsData.getTree());

//        loadAd(R.id.banner_ad);
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
    /*
intialize layout resources
*/
    private void intializeDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                intializeDrawables(((ViewGroup) view).getChildAt(i));
                android.util.Log.i("view", "" + i);
            }

            view.setBackgroundResource(0);
        }
    }


    /**
     * Initialize & show the power ups dialog & set listener on it's dismiss event.
     */

    private void initializeAndShowPowerUpsDialog() {

        dialogPowerUps = powerUp.showPowerUpsDialog(this);

        dialogPowerUps.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

                if (!isTimer) {

                    finish();
                }
            }
        });

        initializePowersUpUI();

        dialogPowerUps.show();
    }

    /**
     * initialize the UI components in powerUp dialog
     */
    private void initializePowersUpUI() {

        // in case of practicing userId will be zero
        if (userId >= 0) {
            coins = sqlLiteDB.getUserCoins(userId);
        } else {
            coins = 0;
        }
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
        reButton.setImageResource(R.drawable.re_button_behavior);
        reButton.setOnClickListener(this);

        powersLayout = (LinearLayout) dialogPowerUps
                .findViewById(R.id.powersLayout);
    }

    /**
     * Read puzzle from files which they already generated in previous time & cached
     * in file storage to increase the performance of opening new game.
     */
    private void readGeneratedPuzzleFromFile() {

        readGeneratedPuzzle();

        readGeneratedRevertedPuzzle();
    }

    /**
     * Read generated puzzle from file storage.
     */
    private void readGeneratedPuzzle() {

        String fileData = AppUtils.readFromFile(
                DEFAULT_PUZZEL_FILENAME, context);

        String[] splitedData = fileData.split(":");

        if (splitedData.length == 2) {

            // read the puzzle and it is dictionary
            roundPuzzle.setPuzzle(splitedData[0]);
            roundPuzzle.setAllWords(splitedData[1]);

        } else {

            // read default
            roundPuzzle.setPuzzle(context.getString(R.string.default_puzzel));
            roundPuzzle.setAllWords(context
                    .getString(R.string.default_puzzel_words));
        }
    }

    /**
     * Read reverted state of the puzzle for file storage. used when user apply
     * reorder power ups.
     */
    private void readGeneratedRevertedPuzzle() {

        String fileData = AppUtils.readFromFile(REVERT_PUZZEL_FILENAME,
                context);

        String[] splitedData = fileData.split(":");

        if (splitedData.length == 2) {

            // read the puzzle and it is dictionary
            roundPuzzle.setRevertedPuzzle(splitedData[0]);
            roundPuzzle.setRevertedAllWords(splitedData[1]);

        } else {

            // read default
            roundPuzzle.setRevertedPuzzle(getString(R.string.default_puzzel_reversed));
            roundPuzzle
                    .setRevertedAllWords(getString(R.string.default_puzzel_words_reversed));
        }

    }

    /**
     * Read the default puzzle & it's reverted state from strings.xml, used when
     * their is no puzzle generated (puzzle is dirty). Usually happened while generator
     * not finish yet.
     */
    private void readDefaultPuzzle() {
        roundPuzzle.setPuzzle(context.getString(R.string.default_puzzel));
        roundPuzzle.setAllWords(context
                .getString(R.string.default_puzzel_words));
        roundPuzzle.setRevertedPuzzle(context
                .getString(R.string.default_puzzel_reversed));
        roundPuzzle.setRevertedAllWords(context
                .getString(R.string.default_puzzel_words_reversed));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Destroy banner ads if not destroyed.
//        if (banner != null) {
//            banner.destroy();
//            banner = null;
//        }
        LinearLayout wordlayout =   (LinearLayout) this
                .findViewById(R.id.mainLayout);
        unbindDrawables(wordlayout );
    }

    /**
     * initialize sounds objects.
     */
    private void initialSounds() {

        wrongSound = new Sounds(context, 0);
        buttonSound = new Sounds(context, 1);
        winningSound = new Sounds(context, 3);
        freezeSound = new Sounds(context, 5);
        unFreezeSound = new Sounds(context, 6);
        warningSound = new Sounds(context, 7);
        removePowerUpSound = new Sounds(context, 8);
        addPowerUpSound = new Sounds(context, 9);
    }


    @Override
    public void onPause() {
        super.onPause();

        if (isTimer) {

            applyPauseBehavior();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (isTimer) {

            if (time < TIMER_ALARM_DURATION) {
                warningSound.stop();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // If the activity was paused, show pause dialog for user to resume to the game
        // or exit.
        if (isPaused) {
            initializeAndShowPauseDialog();
        }
    }

    /**
     * Show fade animation when finding correct word.
     */
    private void showCorrectWordAnimation() {

        ahsantImage.setAlpha(1.0F);

        new AnimationManager().applyFadeAnimation(ahsantImage, 1.0F, 0.0F, AnimationManager.CORRECT_WORD_ANIMATION_DURATION);
    }

    /**
     * Called when pause behavior should applied.
     */
    private void applyPauseBehavior() {

        // Stop warning sound if it's running.
        if (time < TIMER_ALARM_DURATION) {
            warningSound.pauseSound();
        }

        timer.cancel();

        isPaused = true;
    }

    /**
     * initialize pause Dialog UI components
     * and show dialog
     */
    public void initializeAndShowPauseDialog() {

        // stop the counter and sounds if already not stopped.
        if (!isPaused) {

            applyPauseBehavior();
        }

        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.setAlpha((float) 0.08);
        ImageView startAgainButton = (ImageView) pauseDialog
                .findViewById(R.id.start_button);
        ImageView quitButton = (ImageView) pauseDialog
                .findViewById(R.id.ens7ab_button);

        // if button is clicked, close the custom dialog
        startAgainButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!frozenMode) {
                    timer = new CounterClass(time, 1000);
                    timer.start();
                    if (time < TIMER_ALARM_DURATION)
                        warningSound.resumeSound();
                } else {
                    timer = new CounterClass(frozenTime, 1000);
                    timer.start();
                }
                isPaused = false;
                mainLayout.setAlpha((float) 1.0);
                pauseDialog.dismiss();

            }
        });
        quitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseDialog.dismiss();
                saveConfigurationAndFinishGame();

            }
        });

        pauseDialog.show();
    }

    /**
     * add the letters from puzzle to the grid points
     *
     * @param pointsArrayList list of points in the grid
     * @param strPuzzle       the puzzle string
     * @param isFreeze        flag indicates if it is in freeze mode
     * @return the new generated puzzle which should be unchanged
     */
    private String putPuzzleStringOnPoints(ArrayList<Point> pointsArrayList,
                                           String strPuzzle, boolean isFreeze) {

        String str = "";

        StringBuilder bd = new StringBuilder(strPuzzle);
        int c = 0;

        while (c < 16) {
            str += bd.charAt(c);
            int index = Methods.getCharIndex(context, bd.charAt(c));
            Letter letter = new Letter(lettersArray.get(index));
            letter.setFreezMode(isFreeze);
            pointsArrayList.get(c).setLetter(letter);
            c++;
        }
        return str;
    }

    /**
     * initialize most of the UI components and variables
     */
    private void initialize() {
        context = getApplicationContext();

        pathDrower = new PathDrower(this);
        arrowDrower = new ArrowDrower(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
                MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Typeface robotoBoldTypeface = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_CONDENSED_BOLD);
        Typeface robotoRegularTypeface = Typeface.createFromAsset(context.getAssets(),
                Fonts.ROBOTO_CONDENSED_REGULAR);
        ahsantImage = (ImageView) findViewById(R.id.ahsantImgId);
        ahsantImage.setAlpha(0.0F);// make the image invisible
        scoreTextView = (TextView) findViewById(R.id.ScoreView);
        scoreTextView.setTypeface(robotoBoldTypeface);
        textViewTime = (TextView) findViewById(R.id.timerView);
        textViewTime.setTypeface(robotoRegularTypeface);
        ImageView pauseImage = (ImageView) findViewById(R.id.pause_button);
        wordTextView = new TextView(this);
        wordTextView.setTextColor(Color.WHITE);
        wordTextView.setTypeface(robotoRegularTypeface);
        wordTextView
                .setTextSize(getResources().getDimension(R.dimen.text_size));
        LinearLayout wordLayout = (LinearLayout) findViewById(R.id.firstRawLayout);
        wordLayout.addView(wordTextView);

        /*
         * pause dialog initialization
         */
        pauseDialog = new Dialog(this);
        pauseDialog.setContentView(R.layout.pause_dialog);
        pauseDialog.setCancelable(false);
        pauseDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        /*
         * get the data
         */
        wordsData = QuestionsData.getInstence();

        /*
         * initialize the points array list
         */
        pointsArrayList = Methods.initializePointsArrayList(this);

        // array list of points to track the the current word
        currentWordArrayList = new ArrayList<Point>();
        // initialize letter Array
        UIData uiData = new UIData();
        lettersArray = uiData.getLettersArray(this);

        powerUp = PowerUps.getInstence( WORD_GAME_VALUE);
        sqlLiteDB = ShabbikDAO.getInstance(context);

        // Get user id from database.
        userId = sqlLiteDB.retrieveMainUserId();

        roundPuzzle = new Puzzle();

        pauseImage.setOnClickListener(this);


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

    /**
     * change the UI to freeze mode
     */
    private void setFreeze() {
        this.frozenMode = true;
        freezeSound.play(0,context);
        if (time < TIMER_ALARM_DURATION)
            warningSound.pauseSound();
        timer.cancel();
        timer = new CounterClass(20000, 1000);
        timer.start();
        // change the backgrounds images
        LinearLayout wordlayout =   (LinearLayout) this
                .findViewById(R.id.mainLayout);
        intializeDrawables(wordlayout);
        powerUp.setFreezeAction(this);

        // change the images for all points
        for (Point point : pointsArrayList) {
            point.getLetter().setFreezMode(true);
            point.setImageResorce(point.getLetter().getImageId());
        }
    }

    /**
     * change the UI to unfreeze mode
     */
    private void setUnFreeze() {
        this.frozenMode = false;
        timer = new CounterClass(time, 1000);
        timer.start();
        if (time < TIMER_ALARM_DURATION)
            warningSound.resumeSound();

        unFreezeSound.play(0,context);
//        change the background images
        LinearLayout wordlayout =   (LinearLayout) this
                .findViewById(R.id.mainLayout);
        intializeDrawables(wordlayout);
        powerUp.setNormalBackground(this);
//        changes all images of points
        for (Point point : pointsArrayList) {
            point.getLetter().setFreezMode(false);
            point.setImageResorce(point.getLetter().getImageId());
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // if power ups dialog still there and not starting the game , exit
        if (dialogPowerUps.isShowing()) {
            if(!isTimer) {
                Intent toActivty = new Intent(getApplicationContext(),
                        SecondMenuActivity.class);
                toActivty.putExtra(GAME_MODE, WORD_GAME_VALUE);
                startActivity(toActivty);

                dialogPowerUps.dismiss();

                WordGameActivity.this.finish();
            }
        } else {
            // if during the playing time ,, show pause dialog
            initializeAndShowPauseDialog();
        }

    }

    /**
     * on touch the screen, detect the event and do the logic
     *
     * @param e event
     * @return true or false
     */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        /*
         * When ever "Down": clear and start new stack ,, new track. While it is
         * "move" : add to stack increment counter. when it's "UP" :check stack
         * ,, if wrong clear . if correct write word down
         */

        // we need to initialize the drawer classes only once
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
                    // for finger path draweing
                    // for finger path drawing
                    pathDrower.setDownx(Methods.getXCenterOfPoint(pointsArrayList
                            .get(imageIndex)));
                    pathDrower.setDowny(Methods.getYCenterOfPoint(pointsArrayList
                            .get(imageIndex)));

                    Letter letter = pointsArrayList.get(imageIndex).getLetter();

                    wordTrue += letter.getLetter();
                    pointsArrayList.get(imageIndex).setImageResorce(
                            letter.getSelectedImageId());
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
                    Point currentPoint = pointsArrayList.get(imageIndex);
                    if (previousPoint != null && previousPoint != currentPoint
                            && !isCurrentLetterPointUsed(currentPoint)) {

                        // currentPoint = pointsArrayList.get(imageIndex);
                        // check if current point beside previous point

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

                            wordTrue += letter.getLetter();
                            pointsArrayList.get(imageIndex).setImageResorce(
                                    letter.getSelectedImageId());
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
                // change color

                // check current word
                int wordScore = solver.isExist(wordTrue);
                // chancge images to normal

                for (Point point : currentWordArrayList) {

                    point.getImage().setImageResource(
                            point.getLetter().getImageId());
                }
                if (wordScore == 0) {
                    // Wrong choice
                    wrongSound.play(0,context);
                    if (isAllWordGuessed()) {
                        saveConfigurationAndFinishGame();
                    }

                } else if (wordScore == -1) {
                    removePowerUpSound.play(0,context);
                } else {
                    arrowDrower.clearDrawing();
                    isWordHint = false;
                    // Word is correct
                    // increment score , set a flag to know it is guessed

                    // drawWordInSentenceWordArea(correctWordIndex);
                    // showToast(getResources().getString(R.string.good_status));

                    showCorrectWordAnimation();
                    winningSound.play(0,context);

                    // increment score
                    /* configObj.increamentScore(wordScore, whoAmI); */
                    score += wordScore;
                    /* score = configObj.getScore(whoAmI); */
                    scoreTextView.setText("" + score);
                    wordTextView.setText(wordTrue);

                }

                // clear finger path
                wordTrue = "";
                pathDrower.clearDrawing();

                break;

        }

        return true;
    }

    /**
     * check if this point is already used in this word
     *
     * @param currentPoint the point to check
     * @return true if the point is already used, false otherwise
     */
    private boolean isCurrentLetterPointUsed(Point currentPoint) {
        for (Point point : currentWordArrayList) {
            if (point == currentPoint)
                return true;
        }
        return false;
    }

    /**
     * choose a random word to show as hint
     *
     * @return index of word
     */
    private int randomWordHintIndex() {
        int c = 0;
        while (c < solver.getAllPossibbleWord().size()) {
            Random rand = new Random();

            int n = rand.nextInt(solver.getAllPossibbleWord().size());
            if (!solver.isSelected(solver.getAllPossibbleWord().get(n)
                    .getWord())
                    && solver.getAllPossibbleWord().get(n).getPuzzleString()
                    .equals(roundPuzzle.getPuzzle()))
                return n;

            c++;
        }
        return c;
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
                long seconds;
                seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
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
     * Power Ups Configuration
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

                                CorrectWord tempWord = solver
                                        .getAllPossibbleWord().get(
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
                    imageview1.setImageResource(R.drawable.re_button_behavior);
                    imageview1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isWordHint) {

                                // get reverted puzzle
                                String revertedPuzzle = roundPuzzle.getRevertedPuzzle();
                                // put puzzle on points( consider if freeze or
                                // not)
                                boolean isFreez = pointsArrayList.get(0)
                                        .getLetter().isFreez();
                                putPuzzleStringOnPoints(
                                        pointsArrayList, revertedPuzzle, isFreez);
                                // get reverted all words
                                String revertedAllWords = roundPuzzle
                                        .getRevertedAllWords();

                                TreeWord tree = Methods
                                        .initialSolverFromString(
                                                revertedAllWords, context);
                                // set tree in questions data
                                wordsData.setTree(tree);

                                /*
                                 * Solve the matrix
                                 */
                                solver.setPuzzle(revertedPuzzle);
                                solver.solve(wordsData.getTree());

                                imageview1.setVisibility(View.GONE);

                            }
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

        final TextView scoreText = (TextView) dialogPowerUps
                .findViewById(R.id.coin_score);

        coins = Integer.parseInt(scoreText.getText().toString());

        switch (v.getId()) {

            // Pause button.
            case R.id.pause_button:
                initializeAndShowPauseDialog();
                break;

            case R.id.start_button:

                if (whoAmI == I_AM_FIRST_USER) {

                    // set default puzzle to dirty and start service to generate new
                    // one
                    editor = sharedPreferences.edit();
                    editor.putBoolean(IS_DEFAULT_PUZZL_DIRTY, true);
                    editor.commit();

                    Intent generateService = new Intent(getApplicationContext(),
                            GeneratePuzzleWordsService.class);
                    startService(generateService);

                } else {

                    sqlLiteDB.removePuzzle(configObj.getRound().getId());

                }

                if (pathDrower.isDoItOnce()) {
                    pathDrower.initilizeBitmap(context);
                    pathDrower.setDoItOnce(false);
                }

                powerUp.setPowerUps(powers);
                powerUpsConfiguration();

                timer = new CounterClass(120000, 1000);
                timer.start();
                isTimer = true;
                sqlLiteDB.setUserCoins(userId, coins);

                dialogPowerUps.dismiss();

                break;

            case R.id.hint_button:
                if (coins >= POWERUPS_SCORE_VALUE
                        && powers[0] + powers[1] + powers[2] < 3) {
                    powers[0]++;
                    /*
                     * to do decrease the main score
                     */

                    final PowerUP hintPowerUp = new PowerUP(0,context,
                            POWERUPS_SCORE_VALUE);

                    addPowerUpSound.play(0,context);
                    powersLayout.addView(hintPowerUp.getPowerUpimage());
                    coins -= hintPowerUp.getScore();
                    scoreText.setText("" + coins);
                    hintPowerUp.getPowerUpimage().setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    removePowerUpSound.play(0,context);
                                    hintPowerUp
                                            .getPowerUpimage().setVisibility(View.GONE);
                                    coins += hintPowerUp.getScore();
                                    scoreText.setText("" + coins);

                                    powers[0]--;
                                }
                            });
                }
                break;
            case R.id.re_button:
                if (coins >= POWERUPS_SCORE_VALUE && powers[1] == 0
                        && powers[0] + powers[2] < 3) {
                    powers[1]++;
                    final PowerUP rePowerUp = new PowerUP(1, context,
                            POWERUPS_SCORE_VALUE);
                    addPowerUpSound.play(0,context);
                    powersLayout.addView(rePowerUp.getPowerUpimage());
                    coins -= rePowerUp.getScore();
                    scoreText.setText("" + coins);
                    rePowerUp.getPowerUpimage().setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    removePowerUpSound.play(0,context);

                                    rePowerUp
                                            .getPowerUpimage().setVisibility(View.GONE);
                                    coins += rePowerUp.getScore();
                                    scoreText.setText("" + coins);
                                    powers[1]--;

                                }
                            });
                }
                break;
            case R.id.freez_button:
                if (coins >= POWERUPS_SCORE_VALUE
                        && powers[0] + powers[1] + powers[2] < 3) {
                    powers[2]++;

                    final PowerUP freezPowerUp = new PowerUP(2, context,
                            POWERUPS_SCORE_VALUE);
                    addPowerUpSound.play(0,context);
                    powersLayout.addView(freezPowerUp.getPowerUpimage());
                    coins -= freezPowerUp.getScore();
                    scoreText.setText("" + coins);
                    freezPowerUp.getPowerUpimage().setOnClickListener(
                            new OnClickListener() {

                                @Override
                                public void onClick(View v) {


                                    freezPowerUp
                                            .getPowerUpimage().setVisibility(View.GONE);
                                    coins += freezPowerUp.getScore();
                                    scoreText.setText("" + coins);
                                    powers[2]--;
                                    removePowerUpSound.play(0,context);
                                }
                            });
                }
                break;
        }
    }

    /**
     * finish the game , update gameConfiguration object with all info (score ,
     */
    private void saveConfigurationAndFinishGame() {

        // go to play report activity
        Intent toPlayReportActivity = new Intent(getApplicationContext(),
                GameReportActivity.class);
        warningSound.stop();
        wordsData.setAllPossibleWords(solver.getAllPossibbleWord());
        wordsData.setAllChosenWords(solver.getAllCorrectWord());

        configObj.updateRoundInfo(score, roundPuzzle.getPuzzle(), -1, whoAmI);
        toPlayReportActivity.putExtra(PLAY_WITH, playWith);

        startActivity(toPlayReportActivity);

        WordGameActivity.this.finish();

    }

    private boolean isAllWordGuessed() {
        return solver.getAllCorrectWord().size() == solver
                .getAllPossibbleWord().size();
    }


}
