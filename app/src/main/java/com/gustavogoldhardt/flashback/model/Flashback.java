package com.gustavogoldhardt.flashback.model;

import java.util.Date;

/**
 * Created by gustavogoldhardt on 12/22/14.
 */
public class Flashback {
    private String mId;
    private int mIndex;
    private Date mDate;

    public Flashback(String id, int index, Date date) {
        this.mId = id;
        this.mIndex = index;
        this.mDate = date;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public int getIndex() {
        return mIndex;
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }
}
