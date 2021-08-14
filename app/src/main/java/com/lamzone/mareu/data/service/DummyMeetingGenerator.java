package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class DummyMeetingGenerator {
    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(Arrays.asList(
                new Meeting(
                        0,
                        "AAA Games",
                        Room.Luigi, LocalTime.now(),
                        LocalTime.now().plusMinutes(30),
                        new ArrayList<>(Arrays.asList(
                                "nathan.drake@naughtydog.com",
                                "sonic@sega.jp",
                                "didi.kong@nintendo.jp"
                        ))

                )
        ));
    }
}
