package com.palteam.shabbik.configuration;

import android.content.Context;

import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.utils.IConstants;

public abstract class Configuration implements IConstants {

    public static Configuration instence = null;

    public Game game;
    public Round round;
    public int whoAmI;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public int getGameMode() {
        return game.getGameMode();
    }

    public void setGameMode(int gameMode) {
        game.setGameMode(gameMode);
    }

    public void increamentScore(int scoreValue, int whoAmI) {
        int score;
        if (whoAmI == 0 || whoAmI == I_AM_FIRST_USER) {
            score = round.getFirstUserScore();
            score += scoreValue;
            round.setFirstUserScore(score);
        } else {
            score = round.getSecondUserScore();
            score += scoreValue;
            round.setSecondUserScore(score);
        }
    }

    public int getScore(int whoAmI) {
        if (whoAmI == 0 || whoAmI == I_AM_FIRST_USER)
            return round.getFirstUserScore();
        else
            return round.getSecondUserScore();
    }

    public String getRoundConfiguration() {
        return round.getRoundConfigration();
    }

    public void setSentenceIndex(int sentenceIndex) {
        round.setRoundSentence(sentenceIndex);

    }

    public abstract void initializeGameConfiguration(int gameId, int currentRoundId,
                                                     int currntUserTurnId, Context context) ;

    public void reSetGameConfiguration() {

    }

    public int getSentenceIndex() {

        return 0;
    }

    public abstract void updateRoundInfo(int score, String roundConfiguration,
                                         int sentenceIndex, int whoAmI);

    public void updateRoundConfiguration(String roundConfigration) {
        round.setRoundConfigration(roundConfigration);

    }

    public int getWhoAmI() {
        return whoAmI;
    }

    public void setWhoAmI(int whoAmI) {
        this.whoAmI = whoAmI;
    }

}
