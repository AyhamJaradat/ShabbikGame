package com.palteam.shabbik.beans;

public class FacebookFriendsListSection implements FacebookFriendsListItemInt {
    private String title;

    /**
     * @return section title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title section title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isSection() {
        return true;
    }

}
