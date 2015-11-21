package com.test.material.supitsara.materialnavigationtest;

import android.graphics.drawable.Drawable;

/**
 * Created by supitsara on 19/9/2558.
 */
public class CategoryItem {
    private String mText;
    private int mDrawable;
    private int booth_count;

    public CategoryItem(String text, int drawable, int count) {
        mText = text;
        mDrawable = drawable;
        booth_count = count;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public int getDrawable() {
        return mDrawable;
    }

    public int getCount() {
        return booth_count;
    }

    public void setDrawable(int drawable) {
        mDrawable = drawable;
    }
}
