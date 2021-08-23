package com.lamzone.mareu.data.meeting.model;

import android.content.res.Resources;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;

import com.lamzone.mareu.R;

import static android.text.TextUtils.substring;

public enum Room {
    Bowser(R.string.bowser, R.color.bowser),
    Donkey(R.string.donkey, R.color.donkey),
    Goomba(R.string.goomba, R.color.goomba),
    Kirby(R.string.kirby, R.color.kirby),
    Luigi(R.string.luigi, R.color.luigi),
    Mario(R.string.mario, R.color.mario),
    Peach(R.string.peach, R.color.peach),
    Toad(R.string.toad, R.color.toad),
    Wario(R.string.wario, R.color.wario),
    Yoshi(R.string.yoshi, R.color.yoshi);

    @StringRes
    private final int mName;

    @ColorRes
    private final int mColor;

    Room(@StringRes int name, @ColorRes int color) {
        mName = name;
        mColor = color;
    }

    @StringRes
    public int getNameId() {
        return mName;
    }

    @ColorRes
    public int getColorId() {
        return mColor;
    }

    public String getName(Resources resources) {
        return resources.getString(mName);
    }

    public String getLetter(Resources resources) {
        return substring(getName(resources),0,1);
    }

    public int getColor(Resources resources) {
        return resources.getColor(mColor);
    }

}

