package com.palteam.shabbik.configuration;

import android.content.Context;
import android.util.Log;

import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Round;

public class TrainingGameConfig extends Configuration {

    // private static Configuration instence = null;
    // private Game game;
    // private Round round;
    private static TrainingGameConfig  instence2 = null;
    private TrainingGameConfig() {
        game = new Game();
        round = new Round();
    }

    public static TrainingGameConfig  getInstence() {
        if (instence2 == null) {
            instence2= new TrainingGameConfig();
        }
        return instence2;
    }

    @Override
    public void initializeGameConfiguration(int gameId, int currentRoundId, int currntUserTurnId, Context context) {
        Log.i("Im traning", "");
    }

    @Override
    public void reSetGameConfiguration() {

        game = new Game();
        round = new Round();
    }

    @Override
    public void updateRoundInfo(int score, String roundConfiguration,
                                int sentenceIndex, int whoAmI) {
        round.setFirstUserScore(score);
        if (sentenceIndex >= 0) {
            round.setRoundSentence(sentenceIndex);
        }
        round.setRoundConfigration(roundConfiguration);
    }
}
