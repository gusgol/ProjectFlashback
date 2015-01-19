package com.gustavogoldhardt.flashback.model;

import java.util.Date;

/**
 * Created by gustavogoldhardt on 12/22/14.
 */
public class Flashback {
    private String mId;
    private int mIndex;
    private Date mDate;
    private String mPath;

    public Flashback(String id, int index, Date date, String path) {
        this.mId = id;
        this.mIndex = index;
        this.mDate = date;
        this.mPath = path;
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

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }
}
