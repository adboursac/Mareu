package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingGenerator {
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(
                    0,
                    "LamApp Keynote",
                    Room.Luigi, LocalTime.of(9, 30),
                    LocalTime.of(11, 30),
                    new ArrayList<>(Arrays.asList(
                            "jenna@lamzone.com",
                            "kad@lamzone.com",
                            "olvier@lamzone.com"
                    ))

            ),
            new Meeting(
                    1,
                    "AAA Games",
                    Room.Mario, LocalTime.of(11, 15),
                    LocalTime.of(12, 0),
                    new ArrayList<>(Arrays.asList(
                            "nathan.drake@naughtydog.com",
                            "sonic@sega.jp",
                            "didi.kong@nintendo.jp"
                    ))

            ),
            new Meeting(
                    2,
                    "Unity 2021",
                    Room.Wario, LocalTime.of(13, 0),
                    LocalTime.of(14, 0),
                    new ArrayList<>(Arrays.asList(
                            "jenna@unity.com",
                            "eric@unity.com",
                            "elsa@unity.com"
                    ))

            ),
            new Meeting(
                    3,
                    "Web Design",
                    Room.Kirby, LocalTime.of(13, 0),
                    LocalTime.of(14, 30),
                    new ArrayList<>(Arrays.asList(
                            "helene@lamzone.com",
                            "marc@lamzone.com",
                            "mitch@lamzone.com"
                    ))

            ),
            new Meeting(
                    4,
                    "Android Jetpack",
                    Room.Donkey, LocalTime.of(14, 20),
                    LocalTime.of(15, 00),
                    new ArrayList<>(Arrays.asList(
                            "julia@android.com",
                            "mitch@android.com",
                            "mike@android.com"
                    ))

            ),
            new Meeting(
                    5,
                    "ViewModel",
                    Room.Yoshi, LocalTime.of(9, 36),
                    LocalTime.of(10, 36),
                    new ArrayList<>(Arrays.asList(
                            "yumei@openclassroom.com",
                            "sammy@openclassroom.com",
                            "adam@openclassroom.com"
                    ))

            )
    );

    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
