//package com.palteam.shabbik.activities;
//
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ListView;
//import android.widget.Toast;
//
////import com.facebook.Session;
////import com.facebook.model.GraphUser;
//import com.google.gson.Gson;
//import com.palteam.shabbik.R;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.palteam.shabbik.beans.FacebookFriendsListItem;
//import com.palteam.shabbik.beans.FacebookFriendsListItemInt;
//import com.palteam.shabbik.beans.FacebookFriendsListSection;
//import com.palteam.shabbik.beans.FacebookUser.FacebookFriendType;
//import com.palteam.shabbik.beans.Game;
//import com.palteam.shabbik.beans.Round;
//import com.palteam.shabbik.beans.User;
//import com.palteam.shabbik.provider.FacebookProvider;
//import com.palteam.shabbik.provider.FacebookProvider.OnFacebookFriendsLoaded;
//import com.palteam.shabbik.social.facebook.FacebookManager;
//import com.palteam.shabbik.sqllight.IDatabase;
//import com.palteam.shabbik.sqllight.ShabbikDAO;
//import com.palteam.shabbik.ui.adapters.FacebookFriendsAdapter;
//import com.palteam.shabbik.ui.adapters.FacebookFriendsAdapter.OnFriendSelected;
//import com.palteam.shabbik.utils.AppUtils;
//import com.palteam.shabbik.utils.IConstants;
//import com.palteam.shabbik.webservice.WebService;
//
//public class FriendsActivity extends Activity implements
//        OnFacebookFriendsLoaded, IConstants, IDatabase, OnFriendSelected {
//
//    private final String TAG = FriendsActivity.class.getSimpleName();
//
//    private ProgressDialog dialog;
//
//    private List<FacebookFriendsListItemInt> facebookFriendsListData;
//    private List<User> convertedFriends;
//
//    private ListView friendsListView;
//    private Context context;
//    private int gameMode;
//    private ShabbikDAO sqlLiteDB;
//    // private SharedPreferences sharedPreferences;
//
//    private List<User> facebookPlayers;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        AppUtils.logHeap(TAG);
//
//        setContentView(R.layout.activity_friends);
//
//        init();
//    }
//    /**
//     * Initialize page components.
//     */
//    private void init() {
//        context = this;
//
//        gameMode = getIntent().getExtras().getInt(GAME_MODE);
//        // sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME,
//        // MODE_PRIVATE);
//        sqlLiteDB = ShabbikDAO.getInstance(context);
//        friendsListView = (ListView) findViewById(R.id.listViewForFacebookFriends);
//
//        facebookFriendsListData = new ArrayList<FacebookFriendsListItemInt>();
//
////        Session session = Session.getActiveSession();
//
//        dialog = new ProgressDialog(FriendsActivity.this);
//        dialog.setMessage(getResources().getString(R.string.please_wait_msg));
//
////        if (session != null) {
////
////            if (session.isOpened()) {
////
////                dialog.show();
////                new FacebookProvider(this).getFacebookFriends(session);
////
////            } else {
////
////                FacebookManager facebookManager = new FacebookManager(this);
////                boolean isSessionRefreshed = facebookManager
////                        .refreshConnection();
////
////                if (isSessionRefreshed) {
////
////                    dialog.show();
////                    new FacebookProvider(this).getFacebookFriends(session);
////
////                } else {
////
////                    Toast.makeText(getApplicationContext(),
////                            getString(R.string.facebook_failer),
////                            Toast.LENGTH_SHORT).show();
////                }
////            }
////
////        } else {
////
////            Toast.makeText(getApplicationContext(),
////                    getString(R.string.facebook_failer), Toast.LENGTH_SHORT)
////                    .show();
////
////            Log.i(TAG, "Session is null");
////
////            finish();
////        }
//    }
//
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//
//        closeLoadingDialog();
//
//    }
//
//    private void closeLoadingDialog() {
//
//        // dismiss the progress dialog
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
//
//    }
//
//    /**
//     * Find Facebook friend with the given Facebook id;
//     *
//     * @param facebookId friend facebook id.
//     * @return Facebook friend with the given id.
//     */
//    private User findFriendById(String facebookId) {
//
//        for (User user : facebookPlayers) {
//
//            if (user.getUserFBId().equals(facebookId)) {
//
//                return user;
//
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * Start game with the given user.
//     *
//     * @param user Facebook friend to start game with.
//     */
//    private void startGameWith(User user) {
//
//        Log.i(TAG, "xxx start game with : " + user.toString());
////crashlati
//        handleRetrievedUser(user);
//    }
//
//    /**
//     * this function will be called when facebook user is retrieved
//     *
//     * @param user
//     */
//    public void handleRetrievedUser(User user) {
//        // bring selected user information and add them to local
//        // database
//
//        // Add user Record To localDatabase if not existed
//        boolean isUserExist = sqlLiteDB.isUserExist(user);
//        if (!isUserExist) {
//            if (user.getUserFBId() == null || user.getUserFBId() == "")
//                user.setUserFBId(NULL_VALUE);
//
//            // user.setUserEmail(NULL_VALUE);
//            sqlLiteDB.insertNewUser(user);
//        }
//
//        // add game row and three round rows to globalDB
//        Game game = new Game();
//        int firstUserId = sqlLiteDB.retrieveMainUserId();
//        Log.i("first UserID", "" + firstUserId);
//        game.setFirstUserId(firstUserId);
//        game.setSecondUserId(user.getId());
//
//        game.setGameMode(gameMode);
//
//        new AddNewGameInfoAsyncTask(game).execute();
//
//    }
//
//    private void setFriendsListAdapter() {
//
//        FacebookFriendsAdapter friendsAdapter = new FacebookFriendsAdapter(
//                this, facebookFriendsListData);
//
//        friendsListView.setAdapter(friendsAdapter);
//    }
//
////    @Override
////    public void onFacebookFriendsLoaded(List<GraphUser> friends) {
////
////        convertedFriends = convertGraphUserToUser(friends);
////        getPlayersFriends(convertedFriends);
////
////    }
//
//    /**
//     * Get list of players friends from all Facebook friends.
//     *
//     * @param friends all friends.
//     */
//    private void getPlayersFriends(List<User> friends) {
//
//        // call asyncTansk to talk with server
//        new GetPlayersFriendsAsyncTask(friends).execute();
//
//    }
//
//    /**
//     * Convert list of Facebook graph users to list of user model.
//     *
//     * @param users Facebook graph users.
//     * @return list of user model.
//     */
////    private List<User> convertGraphUserToUser(List<GraphUser> users) {
////
////        List<User> convertedUsers = new ArrayList<User>();
////
////        for (GraphUser user : users) {
////
////            User convertedUser = new User();
////            convertedUser.setUserFBId(user.getId());
////            convertedUser.setUserFirstName(user.getFirstName());
////            convertedUser.setUserLastName(user.getLastName());
////
////            convertedUsers.add(convertedUser);
////        }
////
////        return convertedUsers;
////    }
//
//    /**
//     * Get list of not Facebook players friends.
//     *
//     * @param friends all Facebook friends.
//     * @param players players friends.
//     * @return list of not players friends.
//     */
//    private List<FacebookFriendsListItemInt> getNotPlayers(List<User> friends,
//                                                           List<User> players) {
//
//        List<FacebookFriendsListItemInt> notPlayers = new ArrayList<FacebookFriendsListItemInt>();
//
//        for (User friend : friends) {
//
//            boolean isFriend = false;
//
//            for (User player : players) {
//
//                if (friend.getUserFBId().equals(player.getUserFBId())) {
//
//                    isFriend = true;
//                    break;
//                }
//            }
//
//            if (!isFriend) {
//
//                FacebookFriendsListItem notPlayer = new FacebookFriendsListItem();
//                notPlayer.setId(friend.getUserFBId());
//                notPlayer.setName(friend.getUserFirstName() + " "
//                        + friend.getUserLastName());
//                notPlayer.setType(FacebookFriendType.NOT_PLAYER);
//                notPlayers.add(notPlayer);
//
//            }
//
//        }
//
//        return notPlayers;
//
//    }
//
//    /**
//     * Convert list of users to list of friends list item used in the adapter to
//     * display Facebook friends.
//     *
//     * @param users list of users to be converted.
//     * @return converted users.
//     */
//    private List<FacebookFriendsListItemInt> convertUsersToListItems(
//            List<User> users) {
//
//        List<FacebookFriendsListItemInt> convertedUsers = new ArrayList<FacebookFriendsListItemInt>();
//
//        for (User user : users) {
//
//            FacebookFriendsListItem listItem = new FacebookFriendsListItem();
//            listItem.setId(user.getUserFBId());
//            listItem.setName(user.getUserFirstName() + " "
//                    + user.getUserLastName());
//            listItem.setType(FacebookFriendType.PLAYER);
//
//            convertedUsers.add(listItem);
//
//        }
//
//        return convertedUsers;
//    }
//
//    /**
//     * * Fill adapter list with categorized Facebook friends (Players/Not
//     * players).
//     * <p/>
//     * method will be called after getting response from server it takes as
//     * parameters all friends and friends who played this game
//     *
//     * @param allFriends
//     * @param players
//     */
//
//    public void viewFriendsAndPLayers(List<User> allFriends, List<User> players) {
//
//        if (players == null) {
//            // there are NO friends have played this game
//            players = new ArrayList<User>();
//        }
//
//        // Sort list of friends before start processing them, in order for results list
//        // of processing to be sorted.
//        Collections.sort(allFriends);
//        Collections.sort(players);
//
//        List<FacebookFriendsListItemInt> playersFriends = convertUsersToListItems(players);
//        List<FacebookFriendsListItemInt> notPlayersFriends = getNotPlayers(
//                allFriends, players);
//
//        // Combine friends together
//        // Players section.
//        String playersFriendsSectionTitle = String.format(
//                getString(R.string.facebook_friends_players_section_title),
//                getString(R.string.app_name));
//
//        FacebookFriendsListSection playersSection = new FacebookFriendsListSection();
//        playersSection.setTitle(playersFriendsSectionTitle);
//        facebookFriendsListData.add(playersSection);
//
//
//        for (FacebookFriendsListItemInt playerFacebookFriend : playersFriends) {
//
//            facebookFriendsListData.add(playerFacebookFriend);
//
//        }
//
//        // Not players section
//        String notPlayersFriendsSectionTitle = getString(R.string.facebook_friends_not_players_section_title);
//
//        FacebookFriendsListSection notPlayersSection = new FacebookFriendsListSection();
//        notPlayersSection.setTitle(notPlayersFriendsSectionTitle);
//        facebookFriendsListData.add(notPlayersSection);
//
//        for (FacebookFriendsListItemInt notPlayerFacebookFriend : notPlayersFriends) {
//
//            facebookFriendsListData.add(notPlayerFacebookFriend);
//
//        }
//
//        setFriendsListAdapter();
//
//    }
//
//    // / private inner class
//    private class GetPlayersFriendsAsyncTask extends
//            AsyncTask<Void, Void, String> {
//
//        private List<User> usersList;
//
//        public GetPlayersFriendsAsyncTask(List<User> usersList) {
//
//            this.usersList = usersList;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            // generate facebookIDds json String
//            String facebookId_jsonList = "";
//            User user;
//            for (int i = 0; i < usersList.size(); i++) {
//                user = usersList.get(i);
//                facebookId_jsonList += user.getUserFBId();
//                if (i != (usersList.size() - 1))
//                    facebookId_jsonList += "+";
//            }
//
//            Map<String, String> parameters = new HashMap<String, String>();
//
//            parameters.put(LIST_OF_USER_FB_ID, facebookId_jsonList);
//
//            String parametersString = WebService
//                    .createQueryStringForParameters(parameters);
//            String url = getResources().getString(
//                    R.string.web_service_http_address)
//                    + getResources().getString(
//                    R.string.check_facebook_users_web_service_address);
//
//            String jsonString = WebService.requestWebService(url,
//                    parametersString);
//
//            // if return null ,, try again for more one time
//            if (jsonString == null) {
//                jsonString = WebService
//                        .requestWebService(url, parametersString);
//            }
//
//            return jsonString;
//
//        }
//
//        protected void onPostExecute(String results) {
//            if (results != null) {
//
//                Log.e("JSON result :: ", results);
//                boolean status = false;
//                JSONObject jsonObject = null;
//                List<User> players = null;
//                int size = 0;
//
//                try {
//
//                    jsonObject = new JSONObject(results);
//                    status = jsonObject.getBoolean(JSON_STATUS);
//                    if (status) {
//                        size = jsonObject.getInt(JSON_ARRAY_SIZE);
//                        if (size > 0) {
//                            players = new ArrayList<User>();
//                            for (int i = 1; i < size + 1; i++) {
//                                String strKey = JSON_OBJECT_KEY + i;
//
//                                String json = jsonObject.getString(strKey);
//                                User gsonUser = new Gson().fromJson(json,
//                                        User.class);
//
//                                players.add(gsonUser);
//                            }
//
//                        }
//
//                        facebookPlayers = players;
//
//                        closeLoadingDialog();
//
//                        viewFriendsAndPLayers(usersList, players);
//
//                    }
//
//                } catch (JSONException e) {
//                    Log.e("at SignUpAsyncTask",
//                            "JSONException at onPostExecute ");
//                    status = false;
//                }
//
//            } else {
//
//                // when response is null
//                // dismiss the progress dialog
//                closeLoadingDialog();
//
//                Log.i(TAG, "Data could Not be seved to DataBase");
//                Toast.makeText(context, getString(R.string.failer_message),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//    }
//
//    // / private inner class
//    private class AddNewGameInfoAsyncTask extends AsyncTask<Void, Void, String> {
//
//        private Game game;
//
//        public AddNewGameInfoAsyncTask(Game game) {
//            this.game = game;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            String requestParameters = "/" + game.getFirstUserId() + "/"
//                    + game.getSecondUserId() + "/" + gameMode;
//
//            String url = getResources().getString(
//                    R.string.web_service_http_address)
//                    + getResources().getString(
//                    R.string.add_new_game_info_web_service_address)
//                    + requestParameters;
//
//            String jsonString = WebService.requestWebService(url, null);
//
//            // if return null ,, try again for more one time
//            if (jsonString == null) {
//                jsonString = WebService.requestWebService(url, null);
//            }
//            if (isCancelled()) {
//
//                return null;
//
//            }
//
//            return jsonString;
//
//        }
//
//        protected void onPostExecute(String results) {
//            if (results != null) {
//
//                Log.e("JSON result :: ", results);
//                boolean status = false;
//                JSONObject jsonObject = null;
//                ArrayList<Round> rounds = null;
//                int size = 0;
//                try {
//
//                    jsonObject = new JSONObject(results);
//                    status = jsonObject.getBoolean(JSON_STATUS);
//                    if (status) {
//                        // i received one game object and three round objects
//                        String gameObject = jsonObject.getString(GAME_OBJECT);
//                        Game gsonGame = new Gson().fromJson(gameObject,
//                                Game.class);
//
//                        size = jsonObject.getInt(JSON_ARRAY_SIZE);
//                        if (size > 0) {
//                            rounds = new ArrayList<Round>();
//                            for (int i = 1; i < size + 1; i++) {
//                                String strKey = JSON_OBJECT_KEY + i;
//
//                                String json = jsonObject.getString(strKey);
//                                Round gsonRound = new Gson().fromJson(json,
//                                        Round.class);
//
//                                rounds.add(gsonRound);
//
//                            }
//                        }
//                        // dismiss the progress dialog
//                        closeLoadingDialog();
//                        addGameInfoToLocalDBAndStartGame(gsonGame, rounds);
//                    } else {
//                        closeLoadingDialog();
//                        Toast.makeText(context,
//                                getString(R.string.failer_message),
//                                Toast.LENGTH_SHORT).show();
//
//                    }
//
//                } catch (JSONException e) {
//                    Log.e("at AddNwGamInfoAscTask",
//                            "JSONException at onPostExecute ");
//                    status = false;
//                    closeLoadingDialog();
//                    Toast.makeText(context, getString(R.string.failer_message),
//                            Toast.LENGTH_SHORT).show();
//                }
//
//            } else {
//
//                // when response is null
//                // dismiss the progress dialog
//                closeLoadingDialog();
//
//                Log.i(TAG, "could not Add new game to dataBase");
//                Toast.makeText(context, getString(R.string.failer_message),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        }
//
//    }
//
//    /**
//     * this function will be called when game info is added to global database
//     *
//     * @param game
//     * @param rounds
//     */
//    public void addGameInfoToLocalDBAndStartGame(Game game,
//                                                 ArrayList<Round> rounds) {
//
//        game.setUserTurnId(game.getFirstUserId());
//        game.setCurrentRoundId(rounds.get(0).getId());
//        game.setIsDirty(NO_VALUE);
//        game.setIsFinished(NO_VALUE);
//
//        for (Round round : rounds) {
//            round.setIsDirty(NO_VALUE);
//        }
//        // and LOcal DB
//        // and start playing - use gameMode variable to know where to go
//        boolean isGameExist = sqlLiteDB.isGameExist(game);
//        if (!isGameExist) {
//            sqlLiteDB.insertNewGame(game);
//            sqlLiteDB.insertNewRounds(rounds);
//        }
//
//        // gameMode here should be equal to retrieved gameMode
//        // (game.getGameMode)
//        Intent toActivty;
//        toActivty = new Intent(getApplicationContext(),
//                RoundsViewActivity.class);
//        // if (game.getGameMode() == WORD_GAME_VALUE) {
//        // toActivty = new Intent(getApplicationContext(),
//        // WordGameActivity.class);
//        // } else {
//        // toActivty = new Intent(getApplicationContext(),
//        // SentenceGameActivity.class);
//        // }
//
//        toActivty.putExtra(GAME_ID, game.getId());
//        // toActivty.putExtra(PLAY_WITH, PLAY_WITH_FACEBOOK_USER);
//        // toActivty.putExtra(GAME_CURRENT_ROUND_ID, rounds.get(0).getId());
//        // toActivty.putExtra(GAME_USER_TURN_ID, game.getUserTurnId());
//
//        startActivity(toActivty);
//        FriendsActivity.this.finish();
//
//    }
//
//    @Override
//    public void onFriendSelected(int position) {
//
//        if (!facebookFriendsListData.get(position).isSection()) {
//
//            FacebookFriendsListItem selectedItem = (FacebookFriendsListItem) facebookFriendsListData
//                    .get(position);
//
//            switch (selectedItem.getType()) {
//
//                case NOT_PLAYER:
//
//                    // Share on user wall.
//                    FacebookManager.shareOnFacebook(this);
//
//                    break;
//
//                case PLAYER:
//                    // Start game with player.
//                    startGameWith(findFriendById(selectedItem.getId()));
//                    break;
//
//                default:
//                    // Shouldn't work here.
//                    break;
//
//            }
//        }
//
//    }
//
//
//}
