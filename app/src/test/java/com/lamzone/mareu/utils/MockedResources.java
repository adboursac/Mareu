package com.lamzone.mareu.utils;

import android.content.res.Resources;
import androidx.annotation.NonNull;

import com.lamzone.mareu.R;

public class MockedResources extends Resources {
    public MockedResources() {
        super(null, null, null);
    }

    @NonNull
    @Override
    public String getString(int id) {
        switch (id) {
            case R.string.invalidMeetingTimeSlot:
                return "Room is already reserved with:";
            default:
                return "mocked string";
        }
    }
}
