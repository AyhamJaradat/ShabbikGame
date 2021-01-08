package com.palteam.shabbik.sqllight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.palteam.shabbik.beans.Game;
import com.palteam.shabbik.beans.Puzzle;
import com.palteam.shabbik.beans.Round;
import com.palteam.shabbik.beans.User;
import com.palteam.shabbik.utils.IConstants;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ShabbikDAO implements IDatabase, IConstants {

    private static ShabbikDAO instence;
    private AtomicInteger mOpenCounter = new AtomicInteger();
    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private ShabbikDAO(Context context) {
        dbHelper = new MySQLiteHelper(context, DATABASE_NAME, null,
                DATABASE_VERSION);
    }

    public static synchronized ShabbikDAO getInstance(Context context) {

        if (instence == null)
            instence = new ShabbikDAO(context);

        return instence;
    }
    public synchronized SQLiteDatabase openDatabase() {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            database = dbHelper.getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            database.close();

        }
    }
    /*
     * Database methods
     * Insert New User to sqlLight DB
     */

    public boolean insertNewUser(User user) {

        // open DataBase
        long insertId = -1;
        try {
            openDatabase();
            synchronized (database) {
                ContentValues values = new ContentValues();
                values.put(user_column.idUsers + "", user.getId());
                values.put(user_column.fName + "", user.getUserFirstName());
                values.put(user_column.lName + "", user.getUserLastName());
                values.put(user_column.gcmKey + "", user.getUserKey());

                if (user.getUserEmail() != null && user.getUserEmail() != ""
                        || user.getUserEmail() != NULL_VALUE) {
                    values.put(user_column.email + "", user.getUserEmail());
                }

                if (user.getUserFBId() != null && user.getUserFBId() != ""
                        || user.getUserFBId() != NULL_VALUE) {
                    values.put(user_column.facebookId + "", user.getUserFBId());
                }

                insertId = database.insert(TABLE_USER, null, values);
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        // close database


        if (insertId <= 0)
            return false;
        else
            return true;
    }

    public boolean isUserExist(User user) {

        // open DataBase
        boolean isExist = false;
        try {
            openDatabase();
            synchronized (database) {
                String[] retainedColumns = {user_column.idUsers + ""};
                String[] whereArgs = {"" + user.getId()};

                Cursor cursor = database.query(TABLE_USER, retainedColumns,
                        user_column.idUsers + " = ? ", whereArgs, null, null, null);
                isExist = cursor.moveToFirst();
                Log.i(" isExist  ", ""+isExist );
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }



        return isExist;

    }

    public boolean incrementUserNumberOfWords(int userId, int numOfWords) {

        // open DataBase
        int effectedRows = 0;
        try {
            openDatabase();
            synchronized (database) {
                int previousValue = 0;

                ContentValues values = new ContentValues();

                String[] retainedColumns = {user_column.numberOfWords + ""};
                String[] whereArgs = {"" + userId};

                Cursor cursor = database.query(TABLE_USER, retainedColumns,
                        user_column.idUsers + " = ? ", whereArgs, null, null, null);
                if (cursor.moveToFirst()) {
                    previousValue = cursor.getInt(0);

                    previousValue = +numOfWords;
                    values.put(user_column.numberOfWords + "", previousValue);
                    effectedRows = database.update(TABLE_USER, values,
                            user_column.idUsers + " = ? ", whereArgs);

                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean incrementUserNumberOfSentences(int userId, int numOfSentences) {

        // open DataBase
        int effectedRows = 0;
        try {
            openDatabase();
            synchronized (database) {
                int previousValue = 0;

                ContentValues values = new ContentValues();

                String[] retainedColumns = {user_column.numberOfSentences + ""};
                String[] whereArgs = {"" + userId};

                Cursor cursor = database.query(TABLE_USER, retainedColumns,
                        user_column.idUsers + " = ? ", whereArgs, null, null, null);
                if (cursor.moveToFirst()) {
                    previousValue = cursor.getInt(0);

                    previousValue = +numOfSentences;
                    values.put(user_column.numberOfSentences + "", previousValue);
                    effectedRows = database.update(TABLE_USER, values,
                            user_column.idUsers + " = ? ", whereArgs);

                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateUserLastScore ( int userId, int score){
        // open DataBase
        int effectedRows=-1;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(user_column.latestScore + "", score);
                String[] whereArgs = {"" + userId};

                effectedRows = database.update(TABLE_USER, values,
                        user_column.idUsers + " = ? ", whereArgs);

                // update highest score if needed
                int previousHighestScore = selectUserHighestScore(userId);
                if (score > previousHighestScore) {
                    updateUserHighsetScore(userId, score);
                }
                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateUserHighsetScore ( int userId, int score){

        // open DataBase
        int effectedRows = 0;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(user_column.highestScore + "", score);

                String[] whereArgs = {"" + userId};

                effectedRows = database.update(TABLE_USER, values,
                        user_column.idUsers + " = ? ", whereArgs);
                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public int selectUserHighestScore ( int userId){

        // open DataBase
        int highestScore = 0;
        try {
            openDatabase();
            synchronized (database) {

                String[] retainedColumns = {user_column.highestScore + ""};
                String[] whereArgs = {"" + userId};

                Cursor cursor = database.query(TABLE_USER, retainedColumns,
                        user_column.idUsers + " = ? ", whereArgs, null, null, null);
                if (cursor.moveToFirst()) {
                    highestScore = cursor.getInt(0);
                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        return highestScore;

    }

    /**
     * function to retrieve profile information values
     *
     * @param user
     * @return user with highest, latest score and number os sentences and words
     */
    public User getUserProfileInfo (User user){

        // open DataBase
        try {
            openDatabase();
            synchronized (database) {

                String[] retainedColumns = {user_column.highestScore + "",
                        user_column.latestScore + "",
                        user_column.numberOfSentences + "",
                        user_column.numberOfWords + ""};
                String[] whereArgs = {"" + user.getId()};

                Cursor cursor = database.query(TABLE_USER, retainedColumns,
                        user_column.idUsers + " = ? ", whereArgs, null, null, null);
                if (cursor.moveToFirst()) {
                    user.setHighestScore(cursor.getInt(0));
                    user.setLatestScore(cursor.getInt(1));
                    user.setNumberOfSentences(cursor.getInt(2));
                    user.setNumberOfWords(cursor.getInt(3));
                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        return user;
    }

    public boolean isGameExist (Game game){

        // open DataBase
        boolean isExist = false;
        try {
            openDatabase();
            synchronized (database) {

                String[] retainedColumns = {game_column.idGame + ""};
                String[] whereArgs = {"" + game.getId()};

                Cursor cursor = database.query(TABLE_GAME, retainedColumns,
                        game_column.idGame + " = ? ", whereArgs, null, null, null);
                isExist = cursor.moveToFirst();
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (isExist)
            return true;
        else
            return false;

    }

    public boolean insertNewGame (Game game){

        // open DataBase
        long insertId = -1;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(game_column.idGame + "", game.getId());
                values.put(game_column.startDate + "", game.getTimeString());
                values.put(game_column.gameMode + "", game.getGameMode());
                values.put(game_column.isFinished + "", game.getIsFinished());
                values.put(game_column.firstUserId + "", game.getFirstUserId());
                values.put(game_column.secondUserId + "", game.getSecondUserId());
                values.put(game_column.isDirty + "", game.getIsDirty());
                values.put(game_column.currentRoundId + "", game.getCurrentRoundId());
                values.put(game_column.userTurnId + "", game.getUserTurnId());

                insertId = database.insert(TABLE_GAME, null, values);
                // close database
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (insertId <= 0)
            return false;
        else
            return true;

    }

    public boolean insertNewRounds (ArrayList<Round> rounds) {

        // open DataBase
        long insertId = -1;
        try {
            openDatabase();
            synchronized (database) {
                ContentValues values;

                for (Round round : rounds) {
                    values = new ContentValues();
                    values.put(round_column.idRound + "", round.getId());
                    values.put(round_column.roundNumber + "", round.getRoundNumber());
                    values.put(round_column.firstUserScore + "",
                            round.getFirstUserScore());
                    values.put(round_column.secondUserScore + "",
                            round.getSecondUserScore());
                    values.put(round_column.gameConfiguration + "",
                            round.getRoundConfigration());
                    values.put(round_column.roundSentence + "",
                            round.getRoundSentence());
                    values.put(round_column.startDate + "", round.getTimeString());
                    if (round.isFinished()) {
                        values.put(round_column.isFinished + "", YES_VALUE);
                    } else {
                        values.put(round_column.isFinished + "", NO_VALUE);
                    }
                    values.put(round_column.gameId + "", round.getGameId());
                    values.put(round_column.isDirty + "", round.getIsDirty());

                    insertId = database.insert(TABLE_ROUND, null, values);
                    // close database

                }
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (insertId <= 0)
            return false;
        else
            return true;

    }

    public ArrayList<Game> retrieveAllGamesInfo () {
        // open DataBase
        ArrayList<Game> games = new ArrayList<Game>();
        try {
            openDatabase();
            synchronized (database) {


                // Select All Query
                String selectQuery = "SELECT  * FROM " + TABLE_GAME;

                Cursor cursor = database.rawQuery(selectQuery, null);

                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    do {
                        Game game = new Game();

                        game.setId(cursor.getInt(0));
                        game.setTimeString(cursor.getString(1));// need to be changed to
                        // object
                        game.setGameMode(cursor.getInt(2));
                        game.setIsFinished(cursor.getString(3));
                        game.setFirstUserId(cursor.getInt(4));
                        game.setSecondUserId(cursor.getInt(5));
                        game.setIsDirty(cursor.getString(6));
                        game.setCurrentRoundId(cursor.getInt(7));
                        game.setUserTurnId(cursor.getInt(8));

                        // Adding post to list
                        games.add(game);
                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return games;
    }

    public User retrieveUserInfo ( int userId){

        // open DataBase
        User user = null;
        try {
            openDatabase();

            synchronized (database) {

                // Select All Query
                String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                        + user_column.idUsers + " = ? ";
                String[] whereArgs = {"" + userId};

                Cursor cursor = database.rawQuery(selectQuery, whereArgs);


                if (cursor.moveToFirst()) {
                    user = new User();
                    user.setId(cursor.getInt(0));
                    user.setUserFirstName(cursor.getString(1));
                    user.setUserLastName(cursor.getString(2));
                    // index 3 is email column
                    user.setUserFBId(cursor.getString(4));
                    user.setUserKey(cursor.getString(5));
                    // index 6 is isDirty column // we don't need it
                }

                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return user;
    }

    /**
     * retrieve three rounds for game with gameId , sorted by round number
     *
     * @param gameId
     * @return
     */
    public ArrayList<Round> retrieveRoundsOfGame ( int gameId){
        // open DataBase
        ArrayList<Round> rounds = new ArrayList<Round>();
        try {
            openDatabase();
            synchronized (database) {


                // Select All Query

                // String selectQuery = "SELECT  * FROM " + TABLE_ROUND + " WHERE "
                // + round_column.gameId + " = ?";
                // Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                String[] retainedColumns = {round_column.idRound + "",
                        round_column.roundNumber + "",
                        round_column.firstUserScore + "",
                        round_column.secondUserScore + "", round_column.startDate + "",
                        round_column.gameConfiguration + "",
                        round_column.roundSentence + "", round_column.isFinished + "",
                        round_column.gameId + "", round_column.isDirty + ""};

                String[] whereArgs = {"" + gameId};

                Cursor cursor = database.query(TABLE_ROUND, retainedColumns,
                        round_column.gameId + " = ? ", whereArgs, null, null,
                        round_column.roundNumber + " ASC ");

                if (cursor.moveToFirst()) {
                    do {
                        Round round = new Round();

                        round.setId(cursor.getInt(0));
                        round.setRoundNumber(cursor.getInt(1));
                        round.setFirstUserScore(cursor.getInt(2));
                        round.setSecondUserScore(cursor.getInt(3));
                        round.setTimeString(cursor.getString(4));
                        round.setRoundConfigration(cursor.getString(5));
                        round.setRoundSentence(cursor.getInt(6));
                        String isFinished = cursor.getString(7);
                        if (isFinished.equalsIgnoreCase(YES_VALUE)) {
                            round.setFinished(true);
                        } else {
                            round.setFinished(false);
                        }

                        round.setGameId(cursor.getInt(8));
                        round.setIsDirty(cursor.getString(9));

                        // Adding post to list
                        rounds.add(round);
                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return rounds;
    }

    public boolean updateUserInfo (User user){

        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(user_column.fName + "", user.getUserFirstName());
                values.put(user_column.lName + "", user.getUserLastName());
                values.put(user_column.facebookId + "", user.getUserFBId());

                String[] whereArgs = {"" + user.getId()};

                effectedRows = database.update(TABLE_USER, values,
                        user_column.idUsers + " = ? ", whereArgs);
                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Game retrieveGameOfId ( int gameId){

        // open DataBase

        Game game = null;
        try {
            openDatabase();
            synchronized (database) {

                // Select All Query
                String selectQuery = "SELECT  * FROM " + TABLE_GAME + " WHERE "
                        + game_column.idGame + " = ? ";
                String[] whereArgs = {"" + gameId};

                Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                if (cursor.moveToFirst()) {
                    game = new Game();

                    game.setId(cursor.getInt(0));
                    game.setTimeString(cursor.getString(1));
                    game.setGameMode(cursor.getInt(2));
                    game.setIsFinished(cursor.getString(3));
                    game.setFirstUserId(cursor.getInt(4));
                    game.setSecondUserId(cursor.getInt(5));
                    game.setIsDirty(cursor.getString(6));
                    game.setCurrentRoundId(cursor.getInt(7));
                    game.setUserTurnId(cursor.getInt(8));
                }

                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return game;
    }

    public Round retrieveRoundOfId ( int roundId){
        // open DataBase

        Round round = null;
        try {
            openDatabase();
            synchronized (database) {
                // Select All Query
                String selectQuery = "SELECT  * FROM " + TABLE_ROUND + " WHERE "
                        + round_column.idRound + " = ? ";
                String[] whereArgs = {"" + roundId};

                Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                if (cursor.moveToFirst()) {

                    round = new Round();

                    round.setId(cursor.getInt(0));
                    round.setRoundNumber(cursor.getInt(1));
                    round.setFirstUserScore(cursor.getInt(2));
                    round.setSecondUserScore(cursor.getInt(3));
                    round.setTimeString(cursor.getString(4));
                    round.setRoundConfigration(cursor.getString(5));
                    round.setRoundSentence(cursor.getInt(6));
                    String isFinished = cursor.getString(7);
                    if (isFinished.equalsIgnoreCase(YES_VALUE)) {
                        round.setFinished(true);
                    } else {
                        round.setFinished(false);
                    }

                    round.setGameId(cursor.getInt(8));
                    round.setIsDirty(cursor.getString(9));
                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return round;
    }

    public boolean updateRoundInfo (Round round){

        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(round_column.firstUserScore + "", round.getFirstUserScore());
                values.put(round_column.secondUserScore + "",
                        round.getSecondUserScore());
                values.put(round_column.roundSentence + "", round.getRoundSentence());
                values.put(round_column.gameConfiguration + "",
                        round.getRoundConfigration());
                values.put(round_column.isDirty + "", round.getIsDirty());


                String[] whereArgs = {"" + round.getId()};

                effectedRows = database.update(TABLE_ROUND, values,
                        round_column.idRound + " = ? ", whereArgs);
                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateRoundDirtyField (Round round){
        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {
                // update Round
                ContentValues values = new ContentValues();
                values.put(round_column.isDirty + "", round.getIsDirty());

                String[] whereArgs = {"" + round.getId()};

                effectedRows = database.update(TABLE_ROUND, values,
                        round_column.idRound + " = ? ", whereArgs);

                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateRoundDateField (Round round){

        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {
                // update Round
                ContentValues values = new ContentValues();
                values.put(round_column.startDate + "", round.getTimeString());

                String[] whereArgs = {"" + round.getId()};

                effectedRows = database.update(TABLE_ROUND, values,
                        round_column.idRound + " = ? ", whereArgs);

                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public int getRoundId ( int gameId, int roundNumber){
        // open DataBase
        int roundId = -1;
        try {
            openDatabase();
            synchronized (database) {


                // Select All Query
                String selectQuery = "SELECT " + round_column.idRound + " FROM "
                        + TABLE_ROUND + " WHERE " + round_column.gameId + " = ? AND "
                        + round_column.roundNumber + " = ? ";
                String[] whereArgs = {"" + gameId, "" + roundNumber};

                Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                if (cursor.moveToFirst()) {
                    roundId = cursor.getInt(0);
                }

                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return roundId;
    }

    public boolean updateGameCurrentRoundId ( int gameId, int currentRoundId){

        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(game_column.currentRoundId + "", currentRoundId);

                String[] whereArgs = {"" + gameId};

                effectedRows = database.update(TABLE_GAME, values,
                        game_column.idGame + " = ? ", whereArgs);
                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean updateGameCurrentUserTurnId ( int gameId, int currentUserTurnId){

        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {

                // update Game
                ContentValues values = new ContentValues();
                values.put(game_column.userTurnId + "", currentUserTurnId);

                String[] whereArgs = {"" + gameId};

                effectedRows = database.update(TABLE_GAME, values,
                        game_column.idGame + " = ? ", whereArgs);

                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Check user status new/registered and apply needed action insert/update.
     *
     * @param user user to be checked.
     * @return database transaction result.
     */
    public boolean registerUser (User user){

        if (isUserExist(user)) {

            // Already found, update record.
            return updateUserInfo(user);

        } else {

            // New user, add to database.
            return insertNewUser(user);
        }
    }

    public String getFullUserName ( int gameId, int whoAmI){

        // open DataBase
        String fullUserName = "";
        try {
            openDatabase();
            synchronized (database) {


                // Select All Query
                String selectQuery = "";
                if (whoAmI == I_AM_FIRST_USER) {
                    selectQuery = "SELECT " + game_column.firstUserId + " FROM "
                            + TABLE_GAME + " WHERE " + game_column.idGame + " = ? ";
                } else {
                    selectQuery = "SELECT " + game_column.secondUserId + " FROM "
                            + TABLE_GAME + " WHERE " + game_column.idGame + " = ? ";
                }
                String[] whereArgs = {"" + gameId};

                Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                if (cursor.moveToFirst()) {

                    int userId = cursor.getInt(0);
                    fullUserName = getFullUserName(userId);
                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        return fullUserName;
    }

    private String getFullUserName ( int userId){

        // open DataBase
        String userFullName = "";
        try {
            openDatabase();
            synchronized (database) {

                // Select All Query
                String selectQuery = "SELECT " + user_column.fName + " , "
                        + user_column.lName + " FROM " + TABLE_USER + " WHERE "
                        + user_column.idUsers + " = ? ";
                String[] whereArgs = {"" + userId};

                Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                if (cursor.moveToFirst()) {

                    userFullName += cursor.getString(0);
                    userFullName += " ";
                    userFullName += cursor.getString(1);

                }

                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return userFullName;
    }

    public ArrayList<Round> getAllDirtyRounds () {
        Log.i("ShabbikDAO", "getAllDirtyRounds");
        // open DataBase
        ArrayList<Round> rounds = new ArrayList<Round>();
        try {
            openDatabase();
            synchronized (database) {


                // Select All Query

                // String selectQuery = "SELECT  * FROM " + TABLE_ROUND + " WHERE "
                // + round_column.isDirty + " = ?";
                // Cursor cursor = database.rawQuery(selectQuery, whereArgs);

                String[] retainedColumns = {round_column.idRound + "",
                        round_column.roundNumber + "",
                        round_column.firstUserScore + "",
                        round_column.secondUserScore + "", round_column.startDate + "",
                        round_column.gameConfiguration + "",
                        round_column.roundSentence + "", round_column.isFinished + "",
                        round_column.gameId + "", round_column.isDirty + ""};

                String[] whereArgs = {"" + YES_VALUE};

                Cursor cursor = database.query(TABLE_ROUND, retainedColumns,
                        round_column.isDirty + " = ? ", whereArgs, null, null, null);

                if (cursor.moveToFirst()) {
                    do {
                        Round round = new Round();

                        round.setId(cursor.getInt(0));
                        round.setRoundNumber(cursor.getInt(1));
                        round.setFirstUserScore(cursor.getInt(2));
                        round.setSecondUserScore(cursor.getInt(3));
                        round.setTimeString(cursor.getString(4));
                        round.setRoundConfigration(cursor.getString(5));
                        round.setRoundSentence(cursor.getInt(6));
                        String isFinished = cursor.getString(7);
                        if (isFinished.equalsIgnoreCase(YES_VALUE)) {
                            round.setFinished(true);
                        } else {
                            round.setFinished(false);
                        }

                        round.setGameId(cursor.getInt(8));
                        round.setIsDirty(cursor.getString(9));

                        // Adding post to list
                        rounds.add(round);
                    } while (cursor.moveToNext());

                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }
        return rounds;

    }

    public boolean isBothScoreExists ( int currentRoundId){
        // open DataBase
        boolean isExist = false;
        try {
            openDatabase();
            synchronized (database) {

                String[] retainedColumns = {round_column.firstUserScore + "",
                        round_column.secondUserScore + ""};
                String[] whereArgs = {"" + currentRoundId};

                Cursor cursor = database.query(TABLE_ROUND, retainedColumns,
                        round_column.idRound + " = ? ", whereArgs, null, null, null);

                if (cursor.moveToFirst()) {
                    if (cursor.getInt(0) != 0 && cursor.getInt(1) != 0)
                        isExist = true;
                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        return isExist;
    }

    public boolean isFirstUserScoreExists ( int currentRoundId){
        // open DataBase
        boolean isExist = false;
        try {
            openDatabase();
            synchronized (database) {

                String[] retainedColumns = {round_column.firstUserScore + ""};
                String[] whereArgs = {"" + currentRoundId};

                Cursor cursor = database.query(TABLE_ROUND, retainedColumns,
                        round_column.idRound + " = ? ", whereArgs, null, null, null);

                if (cursor.moveToFirst()) {
                    if (cursor.getInt(0) != 0)
                        isExist = true;
                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        return isExist;
    }

    public int getUserCoins ( int userId){
        // open DataBase
        int coins = 0;
        try {
            openDatabase();
            synchronized (database) {


                String[] retainedColumns = {user_column.coins + ""};
                String[] whereArgs = {"" + userId};

                Cursor cursor = database.query(TABLE_USER, retainedColumns,
                        user_column.idUsers + " = ? ", whereArgs, null, null, null);
                if (cursor.moveToFirst()) {
                    coins = cursor.getInt(0);

                }
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        return coins;

    }

    public boolean setUserCoins ( int userId, int value){
        // open DataBase
        int effectedRows = -1;
        try {
            openDatabase();
            synchronized (database) {

                ContentValues values = new ContentValues();
                values.put(user_column.coins + "", value);
                String[] whereArgs = {"" + userId};

                effectedRows = database.update(TABLE_USER, values,
                        user_column.idUsers + " = ? ", whereArgs);

                // close
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (effectedRows > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean insertNewPuzzle (Puzzle puzzle){

        // open DataBase
        long insertId = -1;
        try {
            openDatabase();
            synchronized (database) {
                ContentValues values = new ContentValues();
                values.put(puzzle_column.idRound + "", puzzle.getId());
                values.put(puzzle_column.puzzle + "", puzzle.getPuzzle());
                values.put(puzzle_column.allWords + "", puzzle.getAllWords());
                values.put(puzzle_column.revertedPuzzle + "",
                        puzzle.getRevertedPuzzle());
                values.put(puzzle_column.revertedAllWords + "",
                        puzzle.getRevertedAllWords());

                insertId = database.insert(TABLE_PUZZLE, null, values);

                // close database
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (insertId <= 0)
            return false;
        else
            return true;
    }

    public Puzzle retrievePuzzles (int roundId){
        // open DataBase
        Puzzle puzzle = null;
        try {
            openDatabase();
            synchronized (database) {
                // Select All Query
                String selectQuery = "SELECT  * FROM " + TABLE_PUZZLE + " WHERE "
                        + puzzle_column.idRound + " = ? ";
                String[] whereArgs = {"" + roundId};
                Cursor cursor = database.rawQuery(selectQuery, whereArgs);


                if (cursor.moveToFirst()) {
                    puzzle = new Puzzle();
                    puzzle.setId(cursor.getInt(0));
                    puzzle.setPuzzle(cursor.getString(1));
                    puzzle.setAllWords(cursor.getString(2));
                    puzzle.setRevertedPuzzle(cursor.getString(3));
                    puzzle.setRevertedAllWords(cursor.getString(4));
                }

                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        return puzzle;
    }

    public int retrieveMainUserId () {
        int userId = -1;
        // open DataBase
        try {
            openDatabase();
            synchronized (database) {


                String[] retainedColumns = {shared_column.userId + ""};
                String[] whereArgs = {"" + 1};

                Cursor cursor = database.query(TABLE_SHARED, retainedColumns,
                        shared_column.idShared + " = ? ", whereArgs, null, null, null);

                if (cursor.moveToFirst()) {

                    userId = cursor.getInt(0);

                }
                Log.i("UserId",""+userId);
                cursor.close();
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        return userId;
    }

    public boolean isMainUserRegistor () {

        // open DataBase
        int userId = -1;
        // open DataBase
        try {
            openDatabase();
            synchronized (database) {
                String[] retainedColumns = {shared_column.isRegister + ""};
                String[] whereArgs = {"" + 1};

                Cursor cursor = database.query(TABLE_SHARED, retainedColumns,
                        shared_column.idShared + " = ? ", whereArgs, null, null, null);

                if (cursor.moveToFirst()) {

                    userId = cursor.getInt(0);

                }

                cursor.close();

            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }

        if (userId > 0)
            return true;
        else
            return false;
    }

    public boolean removePuzzle ( int roundId){

        // open DataBase
        long deletedRows = -1;
        // open DataBase
        try {
            openDatabase();
            synchronized (database) {

                deletedRows = database.delete(TABLE_PUZZLE, puzzle_column.idRound
                        + " = ? ", new String[]{roundId + ""});

                // close database
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (deletedRows <= 0)
            return false;
        else
            return true;

    }

    public boolean saveMainUser (User user){

        // open DataBase
        long insertId = -1;
        // open DataBase
        try {
            openDatabase();
            synchronized (database) {
                if (user.getId() > 0) {
                    // remove main user before you insert new one
                    long deletedRows = database.delete(TABLE_SHARED,
                            shared_column.idShared + " = ? ", new String[]{1 + ""});

                    ContentValues values = new ContentValues();
                    values.put(shared_column.idShared + "", 1);
                    values.put(shared_column.userId + "", user.getId());
                    values.put(shared_column.userKey + "", user.getUserKey());
                    values.put(shared_column.isRegister + "", 1);

                    insertId = database.insert(TABLE_SHARED, null, values);
                }
                // close database
            }
        }
        catch(android.database.sqlite.SQLiteConstraintException ex){
            Log.i("dataops_", "Error Constraint"); //Print
        }

        finally{
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (insertId <= 0)
            return false;
        else
            return true;

    }

    public boolean removeMainUser () {

        // open DataBase
        long deletedRows = -1;
        // open DataBase
        try {
            openDatabase();
            synchronized (database) {

                deletedRows = database.delete(TABLE_SHARED, shared_column.idShared
                        + " = ? ", new String[]{1 + ""});

                // close database
            }
        } catch (android.database.sqlite.SQLiteConstraintException ex) {
            Log.i("dataops_", "Error Constraint"); //Print
        } finally {
            if (database != null && database.isOpen()) {
                closeDatabase();
            }
        }


        if (deletedRows <= 0)
            return false;
        else
            return true;

    }
}
