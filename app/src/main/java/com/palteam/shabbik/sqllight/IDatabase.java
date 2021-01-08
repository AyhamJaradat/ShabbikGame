package com.palteam.shabbik.sqllight;

public interface IDatabase {

    String YES_VALUE = "true";
    String NO_VALUE = "false";

    String GAME_MODE_WORDS = "WordsGame";
    String GAME_MODE_SENTENCE = "SemtemceGame";

    String TABLE_USER = "User";
    String TABLE_GAME = "Game";
    String TABLE_ROUND = "Round";
    String TABLE_SHARED = "Shared";

    // Puzzles
    String TABLE_PUZZLE = "Puzzle";

    String DATABASE_NAME = "shabbik_db";
    int DATABASE_VERSION = 8;

    /*
     * Columns Name as in dataBase
     */

    enum user_column {
        idUsers, fName, lName, email, facebookId, gcmKey, isDirty, highestScore, latestScore, numberOfSentences, numberOfWords, coins
    }

    enum game_column {
        idGame, startDate, gameMode, isFinished, firstUserId, secondUserId, isDirty, currentRoundId, userTurnId
    }

    enum round_column {
        idRound, roundNumber, firstUserScore, secondUserScore, startDate, gameConfiguration, roundSentence, isFinished, gameId, isDirty
    }

    enum puzzle_column {
        idRound, puzzle, allWords, revertedPuzzle, revertedAllWords
    }

    enum shared_column {
        idShared, userId, userKey, facebookId, isRegister
    }

    // Database creation sql statement
    String USER_TABLE_CREATE = "create table " + TABLE_USER + "("
            + user_column.idUsers + " integer primary key , "
            + user_column.fName + " TEXT , " + user_column.lName + " TEXT , "
            + user_column.email + " TEXT , " + user_column.facebookId
            + " TEXT , " + user_column.gcmKey + " TEXT , "
            + user_column.isDirty + " TEXT , " + user_column.highestScore
            + " INTEGER , " + user_column.latestScore + " INTEGER , "
            + user_column.numberOfSentences + " INTEGER , "
            + user_column.numberOfWords + " INTEGER , " + user_column.coins
            + " INTEGER );";

    String GAME_TABLE_CREATE = " create table " + TABLE_GAME + " ( "
            + game_column.idGame + " integer primary key , "
            + game_column.startDate + " TEXT , " + game_column.gameMode
            + " INTEGER , " + game_column.isFinished + " TEXT , "
            + game_column.firstUserId + " INTEGER ," + game_column.secondUserId
            + " INTEGER , " + game_column.isDirty + " TEXT , "
            + game_column.currentRoundId + " INTEGER , "
            + game_column.userTurnId + " INTEGER );";

    String ROUND_TABLE_CREATE = " create table " + TABLE_ROUND + " ( "
            + round_column.idRound + " integer primary key , "
            + round_column.roundNumber + " INTEGER , "
            + round_column.firstUserScore + " INTEGER , "
            + round_column.secondUserScore + " INTEGER , "
            + round_column.startDate + " TEXT , "
            + round_column.gameConfiguration + " TEXT , "
            + round_column.roundSentence + " INTEGER , "
            + round_column.isFinished + " TEXT , " + round_column.gameId
            + " INTEGER , " + round_column.isDirty + " TEXT );";

    String PUZZLE_TABLE_CREATE = " create table " + TABLE_PUZZLE + " ( "
            + puzzle_column.idRound + " integer primary key , "
            + puzzle_column.puzzle + " TEXT , " + puzzle_column.allWords
            + " TEXT , " + puzzle_column.revertedPuzzle + " TEXT , "
            + puzzle_column.revertedAllWords + " TEXT );";

    String SHARED_TABLE_CREATE = " create table " + TABLE_SHARED + " ( "
            + shared_column.idShared + " integer primary key , "
            + shared_column.userId + " INTEGER , " + shared_column.userKey
            + " TEXT , " + shared_column.facebookId + " TEXT , "
            + shared_column.isRegister + " INTEGER );";
}
