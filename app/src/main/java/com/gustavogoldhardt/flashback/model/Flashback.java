package com.gustavogoldhardt.flashback.model;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by gustavogoldhardt on 12/22/14.
 */
public class Flashback {
    private String mId;
    private int mIndex;
    private Date mDate;
    private String mPath;
    private Bitmap mThumbnail;

    public Flashback(String id, int index, Date date, String path, Bitmap thumbnail) {
        this.mId = id;
        this.mIndex = index;
        this.mDate = date;
        this.mPath = path;
        this.mThumbnail = thumbnail;
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

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap mThumbnail) {
        this.mThumbnail = mThumbnail;
    }
}
