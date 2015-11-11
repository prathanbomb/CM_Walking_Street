package com.test.material.supitsara.materialnavigationtest;

import android.graphics.drawable.Drawable;

/**
 * Created by supitsara on 26/9/2558.
 */
public class BoothListItem {

    private String mName, mDescription, mUrl;

    public BoothListItem(String boothName, String boothDescription, String thumbnailUrl) {
        mName = boothName;
        mDescription = boothDescription;
        mUrl = thumbnailUrl;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}
