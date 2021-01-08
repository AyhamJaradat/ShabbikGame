package com.palteam.shabbik.configuration;

import android.content.Context;
import android.util.Log;

import com.palteam.shabbik.beans.User;
import com.palteam.shabbik.sqllight.IDatabase;
import com.palteam.shabbik.sqllight.ShabbikDAO;
import com.palteam.shabbik.utils.IConstants;

public class GameConfiguration extends Configuration implements IDatabase,
        IConstants {
    private User firstUser, secondUser;
    private int currentUserTurnId, currentRoundId, gameId;
    private Context context;
    private ShabbikDAO sqlLiteDB;
    private static GameConfiguration instence1 = null;
    private GameConfiguration() {

    }

    public static GameConfiguration getInstence() {

        if (instence1 == null) {

            instence1 = new GameConfiguration();
        }

        return instence1;

    }

    @Override
    public void initializeGameConfiguration(int gameId, int currentRoundId,
                                            int currntUserTurnId, Context context) {
        Log.i("inside iniGameConfig", "");
        this.context = context;
        this.gameId = gameId;
        this.currentRoundId = currentRoundId;
        this.currentUserTurnId = currntUserTurnId;
        this.context = context;
        this.sqlLiteDB = ShabbikDAO.getInstance(context);
        retrieveGameConfigurationInfoFromLocalDatabase();

    }

    private void retrieveGameConfigurationInfoFromLocalDatabase() {
        // retrieve game object

        this.game = sqlLiteDB.retrieveGameOfId(gameId);
        // retrieve round object
        this.round = sqlLiteDB.retrieveRoundOfId(currentRoundId);
        // retrieve Users object
        this.firstUser = sqlLiteDB.retrieveUserInfo(game.getFirstUserId());
        this.secondUser = sqlLiteDB.retrieveUserInfo(game.getSecondUserId());

    }

    @Override
    public void updateRoundConfiguration(String roundConfigration) {
        round.setRoundConfigration(roundConfigration);
        // TODO: update Local DataBase
    }

    /**
     * get the user which is his turn now
     *
     * @return currentPlayingUser
     */
    public User getCurrentPlayingUser() {
        if (firstUser.getId() == currentUserTurnId) {
            return firstUser;
        } else {
            return secondUser;
        }
    }

    /**
     * get the user which will play next will be used to send him notification
     *
     * @return user
     */
    public User getNextPlayingUser() {
        if (firstUser.getId() == currentUserTurnId) {
            return secondUser;
        } else {
            return firstUser;
        }
    }

    public int getCurrentRoundId() {
        return currentRoundId;
    }

    public void setCurrentRoundId(int currentRoundId) {
        this.currentRoundId = currentRoundId;
    }

    public User getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(User firstUser) {
        this.firstUser = firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(User secondUser) {
        this.secondUser = secondUser;
    }

    public int getCurrentUserTurnId() {
        return currentUserTurnId;
    }

    public void setCurrentUserTurnId(int currentUserTurnId) {
        this.currentUserTurnId = currentUserTurnId;
    }

    @Override
    public int getSentenceIndex() {

        int sentenceIndex = round.getRoundSentence();
        return sentenceIndex;
    }

    @Override
    public void updateRoundInfo(int score, String roundConfiguration,
                                int sentenceIndex, int whoAmI) {

        if (whoAmI == I_AM_FIRST_USER) {
            round.setFirstUserScore(score);
        } else {
            round.setSecondUserScore(score);
        }
        if (sentenceIndex >= 0) {
            // sentence Game
            round.setRoundSentence(sentenceIndex);
        }
        round.setRoundConfigration(roundConfiguration);
        round.setIsDirty(YES_VALUE);
        sqlLiteDB.updateRoundInfo(round);

        // Change userTurn Id ,, change currentRoundId

        int nextUserTurnId;
        if (whoAmI == I_AM_FIRST_USER) {
            nextUserTurnId = game.getSecondUserId();
            boolean isSaved = sqlLiteDB.updateGameCurrentUserTurnId(
                    game.getId(), nextUserTurnId);

        } else {

            nextUserTurnId = game.getFirstUserId();
            boolean isSaved = sqlLiteDB.updateGameCurrentUserTurnId(
                    game.getId(), nextUserTurnId);
            // Am second User , I finished a round
            // update Current Round ID
            int currentRoundNumber = round.getRoundNumber();
            int nextRoundNumber;
            if (currentRoundNumber < 3) {
                nextRoundNumber = currentRoundNumber + 1;
                int nextCurrentRoundId = sqlLiteDB.getRoundId(game.getId(),
                        nextRoundNumber);
                if (nextCurrentRoundId > 0) {
                    sqlLiteDB.updateGameCurrentRoundId(game.getId(),
                            nextCurrentRoundId);
                }

            } else {
                // last round already played
                sqlLiteDB.updateGameCurrentRoundId(game.getId(),
                        FINISHED_GAME_CURRENT_ROUND_ID);

            }

        }

    }


}


