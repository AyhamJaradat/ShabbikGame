package com.palteam.shabbik.beans;

public class BaseBean {
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BaseBean [id=" + id + "]";
    }
}
