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
                return "id: invalidMeetingTimeSlot";
            case R.string.invalidMeetingEndAfterMidnight:
                return "id: invalidMeetingEndAfterMidnight";
            case R.string.invalidMeetingEndsBeforeItStarts:
                return "id: invalidMeetingEndsBeforeItStarts";
            case R.string.invalidMeetingMaxDuration:
                return "id: invalidMeetingMaxDuration";
            case R.string.invalidMeetingMinDuration:
                return "id: invalidMeetingMinDuration";
            default:
                return "mocked string";
        }
    }
}
