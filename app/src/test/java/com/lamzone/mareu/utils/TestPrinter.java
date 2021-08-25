package com.lamzone.mareu.utils;

import android.content.res.Resources;

import com.lamzone.mareu.data.meeting.model.Meeting;

import java.util.List;

public class TestPrinter {
    public static void meetingList(List<Meeting> list) {
        Resources res = new MockedResources();
        System.out.println("DEBUG> "+list.size());
        for (Meeting m : list) {
            System.out.println("DEBUG>[] "+m.shortDescription(res));
        }
    }
}
