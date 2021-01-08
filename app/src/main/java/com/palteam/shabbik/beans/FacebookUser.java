package com.palteam.shabbik.beans;

public class FacebookUser {

    public enum FacebookFriendType {
        PLAYER, NOT_PLAYER
    }

    ;

    private String id;
    private String firstName;
    private String lastName;

    /**
     * @return Facebook id for user.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Facebook id for user.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return first name of Facebook user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName first name of Facebook user.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return last name of Facebook user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName last name of Facebook user.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
