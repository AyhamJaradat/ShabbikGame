package com.palteam.shabbik.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
import com.palteam.shabbik.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.palteam.shabbik.beans.CorrectWord;
import com.palteam.shabbik.beans.Letter;
import com.palteam.shabbik.beans.Point;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.configuration.Configuration;
import com.palteam.shabbik.configuration.GameConfiguration;
import com.palteam.shabbik.configuration.TrainingGameConfig;
import com.palteam.shabbik.sqllight.IDatabase;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.ui.adapters.CorrectWordsListAdapter;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.ArrowDrower;
import com.palteam.shabbik.utils.IConstants;
//import com.palteam.shabbik.utils.Log;
import com.palteam.shabbik.utils.Methods;
import com.palteam.shabbik.utils.QuestionsData;
import com.palteam.shabbik.utils.Sounds;
import com.palteam.shabbik.utils.UIData;
import com.palteam.shabbik.webservice.WebService;

public class GameReportActivity extends Activity implements OnClickListener,
        IConstants, IDatabase {

    private final String TAG = GameReportActivity.class.getSimpleName();

    private ImageView allWordsImageView, foundWordsImageView, doneImageView;
    private String puzzel, pointsTitle, sentenceInfo;
    private TextView pointsValueTextView, sentenceInfoTextView;
    private ListView correctWordsListView;
    private Sounds buttonClick;
    private ArrayList<CorrectWord> correctWordsData;
    private ArrayList<CorrectWord> allWordsData;
    private QuestionsData questionsData;
    private ArrayList<Letter> lettersArray;
    // private SharedPreferences sharedPreferences;
    private ArrayList<Point> pointsArrayList;
    private int playWith;
    private Configuration configObj;
    private int gameMode;
    private int score;
    private Context context;
    private ShabbikDAO sqlLiteDB;

    private ArrowDrower arrowDrower;
    private boolean isAllWords = false;
    private CorrectWordsListAdapter adapter;
    private int gameId;
    private int firstScrollVisibleItemCount = 0;

//    private AdView banner;
//    private InterstitialAd interstitial;
    private int numberOfTrialsToLoadInterstitial = 0;

    private Typeface robotoBoldTypeface, robotoRegularTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppUtils.logHeap(TAG);

//        loadFullScreenAd();
        setContentView(R.layout.activity_game_report);
//        setContentView(R.layout.activity_play_report);

        init();

//        loadAd(R.id.banner_ad);

        if (playWith != PRACTICE_PLAYING) {
            saveToGlobalDataBase();
        } else {
            // it is practicing mode
            // show done button
            setReportMenuVisible();
        }
        pointsTitle = getResources().getString(R.string.your_score) + " "
                + score;
        pointsValueTextView.setText(pointsTitle);

        if (gameMode == WORD_GAME_VALUE) {

            sentenceInfo = getResources().getString(R.string.you_found) + " "
                    + (correctWordsData.size() - 6) + " "
                    + getResources().getString(R.string.from) + " "
                    + (allWordsData.size() - 6) + " "
                    + getResources().getString(R.string.a_word);
            sentenceInfoTextView.setText(sentenceInfo);

        } else {
            sentenceInfo = getResources().getString(R.string.the_sentence_is)
                    + " ";

            for (CorrectWord word : allWordsData) {
                sentenceInfo += word.getWord();
                sentenceInfo += " ";
            }
            sentenceInfoTextView.setText(sentenceInfo);

        }

        setDataToUI(correctWordsData);
    }

    /**
     * Load advertisement in current view.
     */
    private void loadAd(int viewId) {

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
    }

    /**
     * Load full screen ad depends on specific behavior.
     */
    private void loadFullScreenAd() {

//        // Create the interstitial.
//        interstitial = new InterstitialAd(this);
//        interstitial.setAdUnitId(getString(R.string.game_report_interstitial_ad));
//
//        // Create ad request.
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        // Begin loading your interstitial.
//        interstitial.loadAd(adRequest);
    }

    /**
     * Check every 1 second for 7 seconds (time out is 7) in background thread if interstitial loaded to display it.
     */
//    private void displayFullScreenAd() {
//
//        final Timer timer = new Timer();
//
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        // Check timer for null, in case activity closed before done.
//                        if (interstitial.isLoaded() && timer != null) {
//
//                            interstitial.show();
//
//                            // Interstitial loaded so stop the timer after displaying it.
//                            timer.cancel();
//                            timer.purge();
//
//
//                        } else {
//
//                            // Check timer for null, in case activity closed before done.
//                            if (numberOfTrialsToLoadInterstitial++ == 7 && timer != null) {
//
//                                // Number of trials done so stop the timer.
//                                timer.cancel();
//                                timer.purge();
//                            }
//                        }
//                    }
//                });
//            }
//        };
//
//        timer.schedule(timerTask, 300, 1000);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (banner != null) {
//            banner.destroy();
//            banner = null;
//        }
        LinearLayout gameReportlayout =   (LinearLayout) this
                .findViewById(R.id.reportlayoutgame);
        unbindDrawables(gameReportlayout );
    }
    /*
remove layout resource
 */
    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup&&!(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                unbindDrawables(((ViewGroup) view).getChildAt(i));
                android.util.Log.i("view5", "" + i);

            }

            ((ViewGroup) view).removeAllViews();
            view.setBackgroundResource(0);
        }
        else if (view instanceof ViewGroup &&(view instanceof AdapterView))
        {
            for (int i = 0; i < ((AdapterView) view).getChildCount(); i++) {

                unbindDrawables(((AdapterView) view).getChildAt(i));
                android.util.Log.i("listview5", "" + i);

            }

            // ((AdapterView) view).removeAllViews();
            view.setBackgroundResource(0);
        }

    }


    /*
     * save the new values to global database if there us an Internet
     */
    private void saveToGlobalDataBase() {

        // update some values in local database
        // score , number of words, sentences
        int userId = sqlLiteDB.retrieveMainUserId();
        // last and highest score
        sqlLiteDB.updateUserLastScore(userId, score);
        int numberOfGuessed;
        if (gameMode == WORD_GAME_VALUE) {
            numberOfGuessed = correctWordsData.size();
            sqlLiteDB.incrementUserNumberOfWords(userId, numberOfGuessed);
        } else {
            // Increment sentence is it is guessed
            if (correctWordsData.size() == allWordsData.size()) {
                numberOfGuessed = 1;
                sqlLiteDB.incrementUserNumberOfSentences(userId,
                        numberOfGuessed);
            }
        }

        // check if there is Internet connection
        boolean isConnected = WebService.isConnectedToWeb(context);
        if (isConnected) {
            Log.i(TAG,"Start UpdateGameInfoAsyncTask");
            new UpdateGameInfoAsyncTask().execute();
        } else {
            setReportMenuVisible();
        }

    }

    private void setpuzzle(ArrayList<Point> Points, String puzzle,
                           ArrayList<CorrectWord> word, int position) {

        if (word.size() > 0 && position < word.size())
            puzzle = word.get(position).getPuzzleString();
        int c = 0;
        if (puzzle == null || puzzle.length() == 0 || puzzle == "")
            puzzle = puzzel;

        while (c < 16) {

            int index = Methods.getCharIndex(context, puzzle.charAt(c));
            pointsArrayList.get(c).setLetter(
                    new Letter(lettersArray.get(index)));

            c++;
        }
        //  int dp = (int) getResources().getDimension(R.dimen.letters_grid_width)/12;
        if (word.size() > 0 && position < word.size())
            for (int i = 0; i < word.get(position).getPathLength(); i++) {

                Letter letter = pointsArrayList.get(
                        word.get(position).getPath()[i]).getLetter();

                Points.get(word.get(position).getPath()[i]).setImageResorce(
                        letter.getLetterImageSelected());
                if (i > 0) {
                    // from

                    arrowDrower.setDownx(Methods.getXCenterOfPoint(Points
                            .get(word.get(position).getPath()[i - 1])));
                    arrowDrower.setDowny(Methods.getYCenterOfPoint(Points
                            .get(word.get(position).getPath()[i - 1])));

                    // to
                    arrowDrower.setUpx(Methods.getXCenterOfPoint(Points
                            .get(word.get(position).getPath()[i])));
                    arrowDrower.setUpy(Methods.getYCenterOfPoint(Points
                            .get(word.get(position).getPath()[i])));

                    arrowDrower.drawArrow(10);

                }

            }
    }

    // initiate required data
    private void init() {

        context = getApplicationContext();

        buttonClick = new Sounds(context, 2);
        // sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
        // MODE_PRIVATE);
        robotoBoldTypeface = Typeface.createFromAsset(context.getAssets(),
                IConstants.Fonts.ROBOTO_CONDENSED_BOLD);
        robotoRegularTypeface = Typeface.createFromAsset(context.getAssets(),
                IConstants.Fonts.ROBOTO_CONDENSED_REGULAR);

        pointsValueTextView = (TextView) findViewById(R.id.textViewForPointsValue);
        pointsValueTextView.setTypeface(robotoBoldTypeface);
        sentenceInfoTextView = (TextView) findViewById(R.id.textViewForSentenceInfo);
        sentenceInfoTextView.setTypeface(robotoRegularTypeface);
        correctWordsListView = (ListView) findViewById(R.id.listViewForTrueWords);
        allWordsImageView = (ImageView) findViewById(R.id.allWorrsButton);
        allWordsImageView.setOnClickListener(this);

        foundWordsImageView = (ImageView) findViewById(R.id.foundWordsButtton);
        foundWordsImageView.setOnClickListener(this);
        foundWordsImageView
                .setImageResource(R.drawable.kalemat_moktashafah_selected);

        doneImageView = (ImageView) findViewById(R.id.doneButtonImageView);
        doneImageView.setVisibility(View.GONE);
        doneImageView.setOnClickListener(this);
        pointsArrayList = Methods.initializePointsArrayList(this);
        arrowDrower = new ArrowDrower(this);
        // initialize letter Array
        UIData uiData = new UIData();
        lettersArray = uiData.getLettersArray(context);
        sqlLiteDB = ShabbikDAO.getInstance(context);

        // get data based on configuration object
        playWith = getIntent().getExtras().getInt(PLAY_WITH);
        if (playWith == PRACTICE_PLAYING) {
            configObj = TrainingGameConfig.getInstence();

            // Randomly show the full screen ads in training mode.
//            Random randomGenerator = new Random();
//            if (randomGenerator.nextInt(5) == 3) {
//
//                displayFullScreenAd();
//            }

        } else {
            configObj = GameConfiguration.getInstence();

            // Show full screen ad when its the last round.
            if (configObj.getRound().getRoundNumber() == 3) {
//                displayFullScreenAd();
            }
        }

        gameMode = configObj.getGameMode();
        gameId = configObj.getGame().getId();
        score = configObj.getScore(configObj.getWhoAmI());
        puzzel = configObj.getRoundConfiguration();

        questionsData = QuestionsData.getInstence();
        if (gameMode == WORD_GAME_VALUE) {
            correctWordsData = new ArrayList<CorrectWord>(
                    questionsData.getAllChosenWords());
            allWordsData = new ArrayList<CorrectWord>(
                    questionsData.getAllPossibleWords());

        } else {
            correctWordsData = new ArrayList<CorrectWord>(
                    questionsData.getAllSentenceWord());

            allWordsData = new ArrayList<CorrectWord>(
                    questionsData.getAllPossibleSentenceWords());
        }
        // add empty Object in front and end
        CorrectWord emptyCorrectWord = new CorrectWord(0, "", new int[0], 0, "");
        correctWordsData.add(0, emptyCorrectWord);
        correctWordsData.add(emptyCorrectWord);
        correctWordsData.add(emptyCorrectWord);
        correctWordsData.add(emptyCorrectWord);
        correctWordsData.add(emptyCorrectWord);
        correctWordsData.add(emptyCorrectWord);

        allWordsData.add(0, emptyCorrectWord);
        allWordsData.add(emptyCorrectWord);
        allWordsData.add(emptyCorrectWord);
        allWordsData.add(emptyCorrectWord);
        allWordsData.add(emptyCorrectWord);
        allWordsData.add(emptyCorrectWord);

        correctWordsListView.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem == 0
                        || firstVisibleItem != firstScrollVisibleItemCount) {

                    if (firstVisibleItem != firstScrollVisibleItemCount) {

                        buttonClick.play(0,context);
                        if (arrowDrower.isDoItOnce()) {
                            arrowDrower.initilizeBitmap();
                            arrowDrower.setDoItOnce(false);

                        }
                    }

                    firstScrollVisibleItemCount = firstVisibleItem;
                    if (!arrowDrower.isDoItOnce()) {
                        arrowDrower.clearDrawing();
                    }

                    if (isAllWords) {
                        setpuzzle(pointsArrayList, puzzel, allWordsData,
                                firstVisibleItem /* + 1 */);

                    } else {
                        setpuzzle(pointsArrayList, puzzel, correctWordsData,
                                firstVisibleItem /* + 1 */);

                    }

                }
            }
        });

    }

    // set the data to UI
    public void setDataToUI(ArrayList<CorrectWord> WordsData) {

        // set the list view items
        adapter = new CorrectWordsListAdapter(WordsData);
        correctWordsListView.setAdapter(adapter);
        adapter.setSelectedItem(0);

    }

    @Override
    public void onClick(View v) {

        if (arrowDrower.isDoItOnce()) {
            arrowDrower.initilizeBitmap();
            arrowDrower.setDoItOnce(false);
        }

        Intent toActivty;
        switch (v.getId()) {

            case R.id.foundWordsButtton:

                arrowDrower.clearDrawing();
                isAllWords = false;
                setDataToUI(correctWordsData);

                allWordsImageView
                        .setImageResource(R.drawable.all_kalemat_button_normal);
                foundWordsImageView
                        .setImageResource(R.drawable.kalemat_moktashafah_selected);

                break;
            case R.id.allWorrsButton:

                arrowDrower.clearDrawing();
                isAllWords = true;
                setDataToUI(allWordsData);

                allWordsImageView
                        .setImageResource(R.drawable.all_kalemat_button_selected);
                foundWordsImageView
                        .setImageResource(R.drawable.kalemat_moktashafah_normal);

                break;
            case R.id.doneButtonImageView:
                if (playWith != PRACTICE_PLAYING) {
                    toActivty = new Intent(getApplicationContext(),
                            RoundsViewActivity.class);
                    toActivty.putExtra(GAME_ID, gameId);

                } else {
                    toActivty = new Intent(getApplicationContext(),
                            MainMenuActivity.class);
                }

                toActivty.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
                // toActivty.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(toActivty);

                GameReportActivity.this.finish();
                break;

        }
    }

    // / private inner class
    private class UpdateGameInfoAsyncTask extends AsyncTask<Void, Void, String> {

        // private ProgressDialog dialog;
        private int whoAmI;

        public UpdateGameInfoAsyncTask() {
            // this.dialog = new ProgressDialog(context);
            this.whoAmI = 0;
        }

        @Override
        protected void onPreExecute() {

            // dialog.setMessage(getResources()
            // .getString(R.string.please_wait_msg));
            // dialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            Map<String, String> parameters = new HashMap<String, String>();
            // check if I am First User or Second User
            // get first UserId and compare it with my ID

            whoAmI = configObj.getWhoAmI();

            parameters.put(WHO_AM_I, whoAmI + "");
            parameters.put(ROUND_ID, configObj.getRound().getId() + "");
            parameters.put(ROUND_SCORE, configObj.getScore(whoAmI) + "");
            parameters.put(ROUND_CONFIG, configObj.getRound()
                    .getRoundConfigration());
            parameters.put(ROUND_SENTENCE, configObj.getSentenceIndex() + "");

            String parametersString = WebService
                    .createQueryStringForParameters(parameters);
            String url = getResources().getString(
                    R.string.web_service_http_address)
                    + getResources()
                    .getString(
                            R.string.update_round_game_info_web_service_address);

            String jsonString = WebService.requestWebService(url,
                    parametersString);

            // if return null ,, try again for more one time
            if (jsonString == null) {
                jsonString = WebService
                        .requestWebService(url, parametersString);
            }

            return jsonString;

        }

        protected void onPostExecute(String results) {
            if (results != null) {

                Log.e("JSON result :: ", results);
                boolean status = false;
                JSONObject jsonObject = null;
                // // dismiss the progress dialog
                // if (dialog.isShowing()) {
                // dialog.dismiss();
                // }

                try {

                    jsonObject = new JSONObject(results);
                    status = jsonObject.getBoolean(JSON_STATUS);
                    if (status) {
                        String roundDate = jsonObject.getString(ROUND_DATE);
                        configObj.getRound().setTimeString(roundDate);
                        updateLocalDataBase(configObj.getRound());
                        configObj.reSetGameConfiguration();
                        setReportMenuVisible();
                    } else {
                        String errorMessage = jsonObject
                                .getString(JSON_ERROR_MESSAGE);
                        Log.e("errormessage:", errorMessage);
                    }

                } catch (JSONException e) {
                    Log.e("at UpdateGameInfoAsTask",
                            "JSONException at onPostExecute ");
                    status = false;
                    setReportMenuVisible();
                }

            } else {
                // when response is null
                // // dismiss the progress dialog
                // if (dialog.isShowing()) {
                // dialog.dismiss();
                // }
                setReportMenuVisible();

            }

        }

        private void updateLocalDataBase(Round round) {

            // update is dirty , time
            round.setIsDirty(NO_VALUE);
            sqlLiteDB.updateRoundDateField(round);
            sqlLiteDB.updateRoundDirtyField(round);

        }

    }

    /**
     * this function in called after saving data the function set report menu
     * buttons to visible
     */
    public void setReportMenuVisible() {
        doneImageView.setVisibility(View.VISIBLE);
    }

}
