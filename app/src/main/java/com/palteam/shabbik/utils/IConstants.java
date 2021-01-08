package com.palteam.shabbik.utils;

public interface IConstants {
    // shared preferences
    String SHARED_PREFERENCES_NAME = "ShabbikSharedPreferences";
    String IS_ENTERED_BEFORE = "IS_ENTERED_BEFORE";
    String IS_USER_REGESTERED = "Is user registered";

    String IS_DEFAULT_PUZZL_DIRTY = "IsdefaultPuzzleDirty";
    int YES_ENTERED_BEFORE = 1;
    int NOT_ENTERED_BEFORE = 0;
    /*
     * USER
     */

    String USER_EMAIL = "userEmail";
    String USER_F_NAME = "userFirstName";
    String USER_L_NAME = "userLastName";
    String USER_ID = "userId";
    String USER_FACEBOOK_ID = "userFBId";
    String USER_KEY = "userKey";
    String USER_NICKNAME = "userNickName";

    String LIST_OF_USER_FB_ID = "listOfFaceBookIds";

    /*
     * Game
     */
    String GAME_ID = "gameId";
    String GAME_CURRENT_ROUND_ID = "gameCurrentRoundId";
    String GAME_USER_TURN_ID = "gameUserTurnId";

    String USER_OBJECT = "userObject";
    String GAME_OBJECT = "gameObject";
    String ROUND_OBJECT = "roundObject";

    /*
     * Round
     */
    String ROUND_ID = "roundId";
    String ROUND_SCORE = "roundScore";
    String ROUND_DATE = "roundDate";
    String ROUND_CONFIG = "roundConfig";
    String ROUND_SENTENCE = "roundSentence";
    String ROUND_NUMBER = "roundNumber";

    /*
     * notification
     */
    String MY_NOTIFICATION_ID = "notificationId";

    /*
     * Info Used for logic
     */
    String WHO_AM_I = "whichUserAmI";
    int I_AM_FIRST_USER = 1;
    int I_AM_SECOND_USER = 2;

    int FINISHED_GAME_CURRENT_ROUND_ID = -1;

    /*
     * JSON
     */
    String JSON_STATUS = "status";
    String JSON_ERROR_MESSAGE = "errorMsg";
    String JSON_ARRAY_SIZE = "arraySize";
    String AUTHENTICATION_STATUS = "authenticationStatus";
    String JSON_OBJECT_KEY = "gsonObject";

    int NUMBER_OF_DATA_FILES = 4;

    /*
     * Notification data from server
     */
    String NOTIFICATION_DATA_KEY = "notificationData";
    /*
     * Used by Android Web service
     */
    public static final int POST_TASK = 1;
    public static final int GET_TASK = 2;

    /**
     * Used in place of null for unused parameters.
     */
    public final static String NULL_VALUE = "nullValue";

    // for intent attributes
    public static final String LIST_OF_DONE_WORDS = "DoneWords";
    public static final String GAME_SCORE = "score";
    public static final String PUZZEL = "strpuzzel";
    public final static String GAME_MODE = "wordSentenceGameMode";
    public static final String WORDS_SCORES = "wordScore";
    public static final String PLAY_WITH = "userWillPlayWith";
    public static final String POWER_UPS = "powerups";

    public final static int WORD_GAME_VALUE = 1;
    public final static int SENTENCE_GAME_VALUE = 2;
    public final static int PLAY_WITH_FACEBOOK_USER = 3;
    public final static int PLAY_WITH_RANDOM_USER = 4;
    public final static int PRACTICE_PLAYING = 5;
    public static final int NUMBER_LETTERS_IN_ALPHABET = 42;
    // public static final char LETTER_A ;
    public static final int MINIMUM_WORD_LENGTH = 2;
    public static final int DEFAULT_PUZZLE_SIZE = 4;
    // public static final String alphapt = "";

    public static final String FILENAME = "arabic_statment.txt";

    public static final String DICTIONARY_FILENAME_1 = "data1.txt";
    public static final String DICTIONARY_FILENAME_2 = "data2.txt";
    public static final String DICTIONARY_FILENAME_3 = "data3.txt";
    public static final String DICTIONARY_FILENAME_4 = "data4.txt";

    public static final String DEFAULT_PUZZEL_FILENAME = "default_puzzel.txt";
    public static final String REVERT_PUZZEL_FILENAME = "revert_puzzel.txt";

    public static final int POWERUPS_SCORE_VALUE = 5;
    /**
     * Needed for GCM functionality.
     */
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public String REGISTRATION_TYPE_EXTRA = "registration_type";

    String IS_USER_VOTE = "IsUserVote";

    public enum RegistrationType {
        FACEBOOK, MANUAL
    }

    ;

    /**
     * Inner interface for fonts names.
     *
     * @author Ahamd Abu Rjeila.
     */
    public interface Fonts {

        /**
         * Fonts names
         */
        String ROBOTO_CONDENSED_BOLD = "fonts/RobotoCondensed-Bold.ttf";
        String ROBOTO_CONDENSED_REGULAR = "fonts/RobotoCondensed-Bold.ttf";

    }

    /**
     * Inner interface for Shared preferences data.
     *
     * @author Ahmad Abu Rjeila.
     */
    public interface Preferences {

        /**
         * Id for notification, incremented for each notification in order not
         * to replace each other.
         */
        String NOTIFICAION_ID = "notification_id";
    }

}
