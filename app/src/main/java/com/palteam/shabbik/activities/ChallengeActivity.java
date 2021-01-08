package com.palteam.shabbik.activities;

//import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.palteam.shabbik.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;



import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import com.palteam.shabbik.beans.ChallengeListItemModel;
import com.palteam.shabbik.beans.ChallengeListItemModel.ChallengeType;
import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.beans.User;
//import com.palteam.shabbik.provider.FacebookProvider;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.ui.fragments.ChallengeFragment;
import com.palteam.shabbik.utils.AppUtils;
import com.palteam.shabbik.utils.IConstants;

/**
 * Display user/user-friends challenges in pager layout.
 */
public class ChallengeActivity extends FragmentActivity implements IConstants,
        OnClickListener, ViewPager.OnPageChangeListener {

    private final String TAG = ChallengeActivity.class.getSimpleName();

    private ArrayList<Game> allGames;
    private ShabbikDAO sqlLiteDB;

    // private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    // private SharedPreferences sharedPreferences;
    private Context context;

    private ImageView inviteNewFriendsImageView;

    private ImageView leftToggle, middleToggle, rightToggle;

    private List<List<ChallengeListItemModel>> fragmentsData;

    private enum FRAGMENTS_INDEXES {
        DONE_CHALLENGES, FRIENDS_CHALLENGES, USER_CHALLENGES
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        AppUtils.logHeap(TAG);
        setContentView(R.layout.activity_challenge);
//        setContentView(R.layout.activity_challenges);

        init();

    }

    /**
     * initialize UI Components
     */
    private void init() {

        context = getApplicationContext();
        sqlLiteDB = ShabbikDAO.getInstance(context);
        // tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);

        leftToggle = (ImageView) findViewById(R.id.imageViewToggleLeft);
        leftToggle.setOnClickListener(this);

        middleToggle = (ImageView) findViewById(R.id.imageViewToggleMiddle);
        middleToggle.setOnClickListener(this);

        rightToggle = (ImageView) findViewById(R.id.imageViewToggleRight);
        rightToggle.setOnClickListener(this);

        pager.setOnPageChangeListener(this);

        // Initialize pager.
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(2);

        inviteNewFriendsImageView = (ImageView) findViewById(R.id.imageViewForInviteNewFriends);
        inviteNewFriendsImageView.setOnClickListener(this);

        allGames = new ArrayList<Game>();

        fragmentsData = new ArrayList<List<ChallengeListItemModel>>();

        for (int index = 0; index < FRAGMENTS_INDEXES.values().length; index++) {
            fragmentsData.add(new ArrayList<ChallengeListItemModel>());
        }

        // sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
        // MODE_PRIVATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateOnResume();
    }

    private void updateOnResume() {

        clearData();

        retrieveAllGameInfo();

        for (int index = 0; index < pager.getChildCount(); index++) {

            ChallengeFragment challengesFragment = (ChallengeFragment) pagerAdapter
                    .getItem(index);
            challengesFragment.refresh();
        }

    }

    private void clearData() {

        for (List<ChallengeListItemModel> fragmentData : fragmentsData) {
            fragmentData.clear();
        }

    }

    private void retrieveAllGameInfo() {

        // retrieve all Games from LocalDataBase
        allGames = sqlLiteDB.retrieveAllGamesInfo();

        getUserRoundsData(allGames);

        for (Game game : allGames) {
            // retrieve users info for each game
            User firstUser = sqlLiteDB.retrieveUserInfo(game.getFirstUserId());
            User secondUser = sqlLiteDB
                    .retrieveUserInfo(game.getSecondUserId());
            game.setFirstUser(firstUser);
            game.setSecondUser(secondUser);

            // retrieve rounds info for each game
            ArrayList<Round> rounds = sqlLiteDB.retrieveRoundsOfGame(game
                    .getId());
            game.setFirstRound(rounds.get(0));
            game.setSecondRound(rounds.get(1));
            game.setThirdRound(rounds.get(2));
        }

    }

    /**
     * @param allGames
     * @return
     */
    private List<ChallengeListItemModel> getUserRoundsData(List<Game> allGames) {

        int userId = sqlLiteDB.retrieveMainUserId();

        String waitingMessage = getResources().getString(
                R.string.waiting_message);
        String yourTurnMessage = getResources().getString(
                R.string.your_turn_message);

        String roundsTitles[] = getResources().getStringArray(
                R.array.rounds_titles);

        if (userId != -1) {

            for (Game game : allGames) {

                // retrieve users info for each game
                User firstUser = sqlLiteDB.retrieveUserInfo(game
                        .getFirstUserId());
                User secondUser = sqlLiteDB.retrieveUserInfo(game
                        .getSecondUserId());
                game.setFirstUser(firstUser);
                game.setSecondUser(secondUser);

                // retrieve rounds info for each game
                ArrayList<Round> rounds = sqlLiteDB.retrieveRoundsOfGame(game
                        .getId());
                game.setFirstRound(rounds.get(0));
                game.setSecondRound(rounds.get(1));
                game.setThirdRound(rounds.get(2));

                // Not finished.
                ChallengeListItemModel challengeListItem = new ChallengeListItemModel();

                // Game id;
                challengeListItem.setGameId(game.getId());

                User otherUser = getOtherUser(game);

                if (otherUser != null) {

                    // Name.
                    String name = otherUser.getUserFirstName() + " "
                            + otherUser.getUserLastName();
                    challengeListItem.setName(name);

                    // Image URI.
//                    challengeListItem
//                            .setImageUri(FacebookProvider
//                                    .getFacebookUserProfileURI(otherUser
//                                            .getUserFBId()));

                    if (game.getCurrentRoundId() == IConstants.FINISHED_GAME_CURRENT_ROUND_ID) {

                        // Game is finished.

                        // Challenge type.
                        challengeListItem
                                .setType(ChallengeType.DONE_CHALLENGES);

                        // Status message;
                        if (isUserWin(game, rounds)) {

                            challengeListItem
                                    .setStatusMessage(getString(R.string.you_win));

                        } else {

                            challengeListItem
                                    .setStatusMessage(getString(R.string.you_lose));

                        }

                        fragmentsData.get(
                                FRAGMENTS_INDEXES.DONE_CHALLENGES.ordinal())
                                .add(challengeListItem);

                    } else {

                        int lastRoundIndex = getLastRoundIndex(rounds,
                                game.getCurrentRoundId());

                        int contactTurnId = game.getUserTurnId();

                        // Round title
                        challengeListItem
                                .setRoundNumber(roundsTitles[lastRoundIndex]);

                        if (contactTurnId == userId) {

                            // User challenges.

                            // Challenge type.
                            challengeListItem
                                    .setType(ChallengeType.MY_CHALLENGE);

                            // Status message;
                            challengeListItem.setStatusMessage(yourTurnMessage);

                            // User challenge.
                            fragmentsData
                                    .get(FRAGMENTS_INDEXES.USER_CHALLENGES
                                            .ordinal()).add(challengeListItem);

                        } else {

                            // Friends challenges.

                            // Challenge type.
                            challengeListItem
                                    .setType(ChallengeType.FRIENDS_CHALLENGES);

                            // Status message;
                            challengeListItem.setStatusMessage(waitingMessage);

                            // Friend challenge.
                            fragmentsData.get(
                                    FRAGMENTS_INDEXES.FRIENDS_CHALLENGES
                                            .ordinal()).add(challengeListItem);
                        }

                    }

                }
            }

        } else {
            Log.i(TAG, "Failed to get user id from shared preferences");
        }

        return fragmentsData.get(FRAGMENTS_INDEXES.USER_CHALLENGES.ordinal());
    }

    private User getOtherUser(Game game) {

        int userId = sqlLiteDB.retrieveMainUserId();

        if (userId != -1) {

            if (game.getFirstUser().getId() == userId) {
                return game.getSecondUser();
            } else {
                return game.getFirstUser();
            }
        }

        return null;
    }

    private boolean isUserWin(Game game, List<Round> rounds) {

        int firstUserScore = 0, secondUserScore = 0;

        for (Round round : rounds) {

            firstUserScore += round.getFirstUserScore();
            secondUserScore += round.getSecondUserScore();
        }

        if (game.getSecondUser().getId() == getOtherUser(game).getId()) {

            // App user is the first user of the game.
            return firstUserScore > secondUserScore;

        } else {

            // App user is the second user of the game.
            return firstUserScore < secondUserScore;
        }
    }

    /**
     * @param rounds
     * @param currentRoundId
     * @return
     */
    private int getLastRoundIndex(List<Round> rounds, int currentRoundId) {

        for (int index = 0; index < rounds.size(); index++) {

            if (rounds.get(index).getId() == currentRoundId) {
                return index;
            }
        }

        return -1;
    }

    /**
     * Adapter for challenges pager.
     *
     * @author Ahmad Abu Rjeila.
     */
    public class PagerAdapter extends FragmentPagerAdapter {

        private String[] titles;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);

            titles = getResources().getStringArray(R.array.challenges_titles);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Fragment getItem(int position) {

            FRAGMENTS_INDEXES fragments_indexes[] = FRAGMENTS_INDEXES.values();
            FirebaseCrashlytics.getInstance().log("info::"+TAG+"::"+fragments_indexes[position] + " = " + fragmentsData.get(position).size());
            return ChallengeFragment.newInstance(position,
                    fragmentsData.get(position));
        }

    }

    public void startChalleng(int position, ChallengeListItemModel challengeItem) {

        Game selectedGame = getGameById(challengeItem.getGameId());

        if (selectedGame != null) {
            openGame(selectedGame);
        } else {
            Log.i(TAG, "Game is null");
        }
    }

    private Game getGameById(int gameId) {

        for (Game game : allGames) {
            if (game.getId() == gameId) {
                return game;
            }
        }

        return null;
    }

    private void openGame(Game game) {

        Log.i(TAG, "xxx Game is opened " + game.toString());

        Game newGame = sqlLiteDB.retrieveGameOfId(game.getId());

        // game has finished ,,, show report Page
        Intent toActivty = new Intent(getApplicationContext(),
                RoundsViewActivity.class);
        toActivty.putExtra(GAME_ID, newGame.getId());
        startActivity(toActivty);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageViewForInviteNewFriends:

                Log.i(TAG, "Invite new friends");

                break;

            case R.id.imageViewToggleLeft:
                pager.setCurrentItem(0, true);
                break;

            case R.id.imageViewToggleMiddle:
                pager.setCurrentItem(1, true);
                break;

            case R.id.imageViewToggleRight:
                pager.setCurrentItem(2, true);
                break;

        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

        Log.i(TAG, "xxx onPageScrollStateChanged " + state);

    }

    @Override
    public void onPageScrolled(int position, float offset, int offsetPixels) {

        // Log.i(TAG, "xxx onPageScrolled " + position + "/" + offset + "/"
        // + offsetPixels);

    }

    @Override
    public void onPageSelected(int currentPosition) {

        if (currentPosition == 0) {

            leftToggle.setImageResource(R.drawable.toggle_left_selected);
            middleToggle.setImageResource(R.drawable.middle_tab_selector);
            rightToggle.setImageResource(R.drawable.right_tab_selector);

        } else if (currentPosition == 2) {

            rightToggle.setImageResource(R.drawable.toggle_right_selected);
            leftToggle.setImageResource(R.drawable.left_tab_selector);
            middleToggle.setImageResource(R.drawable.middle_tab_selector);

        } else {

            middleToggle.setImageResource(R.drawable.toggle_middle_selected);
            leftToggle.setImageResource(R.drawable.left_tab_selector);
            rightToggle.setImageResource(R.drawable.right_tab_selector);
        }

        Log.i(TAG, "xxx onPageSelected " + currentPosition);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LinearLayout sentencelayout =   (LinearLayout) this
                .findViewById(R.id.challenges_layout);
        unbindDrawables(sentencelayout );
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

}
