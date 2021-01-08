package com.palteam.shabbik.beans;

import com.palteam.shabbik.beans.FacebookUser.FacebookFriendType;

public class FacebookFriendsListItem implements FacebookFriendsListItemInt {
    private String name;
    private String id;
    private FacebookFriendType type;

    /**
     * @return Facebook friend name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Facebook friend name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Facebook friend id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Facebook friend id.
     */
    public void setId(String id) {
        this.id = id;
    }


    /**
     * @return Facebook friend type (player, not player).
     */
    public FacebookFriendType getType() {
        return type;
    }

    /**
     * @param type Facebook friend type (player, not player).
     */
    public void setType(FacebookFriendType type) {
        this.type = type;
    }

    @Override
    public boolean isSection() {
        return false;
    }

}
