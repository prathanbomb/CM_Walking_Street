package com.test.material.supitsara.materialnavigationtest;

import android.graphics.drawable.Drawable;

/**
 * Created by supitsara on 19/9/2558.
 */
public class CategoryItem {
    private String mText;
    private int mDrawable;

    public CategoryItem(String text, int drawable) {
        mText = text;
        mDrawable = drawable;
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

    public void setDrawable(int drawable) {
        mDrawable = drawable;
    }
}
