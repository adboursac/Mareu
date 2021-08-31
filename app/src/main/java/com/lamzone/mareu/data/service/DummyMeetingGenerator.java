package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingGenerator {
    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(
                    0,
                    "Lame Keynote",
                    Room.Luigi,
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(9, 30)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(11, 30)),
                    new ArrayList<>(Arrays.asList(
                            "amandine@lamzone.com",
                            "luc@lamzone.com",
                            "olvier@lamzone.com"
                    ))
            ),
            new Meeting(
                    1,
                    "AAA Games",
                    Room.Mario,
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(11, 15)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(13, 0)),
                    new ArrayList<>(Arrays.asList(
                            "miyamoto@nintendo.jp",
                            "sonic@sega.jp",
                            "didi.kong@nintendo.jp"
                    ))

            ),
            new Meeting(
                    2,
                    "Unity 2021",
                    Room.Peach,
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(13, 0)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(14, 0)),
                    new ArrayList<>(Arrays.asList(
                            "jenna@unity.com",
                            "eric@unity.com",
                            "elsa@unity.com"
                    ))

            ),
            new Meeting(
                    3,
                    "Web Design",
                    Room.Kirby,
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(13, 0)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(15, 0)),
                    new ArrayList<>(Arrays.asList(
                            "helene@lamzone.com",
                            "marc@lamzone.com",
                            "mitch@lamzone.com"
                    ))

            ),
            new Meeting(
                    4,
                    "Android Jetpack",
                    Room.Donkey,
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(14, 20)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(15, 00)),
                    new ArrayList<>(Arrays.asList(
                            "julia@android.com",
                            "mitch@android.com",
                            "mike@android.com"
                    ))

            ),
            new Meeting(
                    5,
                    "ViewModel",
                    Room.Yoshi,
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(9, 36)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(10, 36)),
                    new ArrayList<>(Arrays.asList(
                            "florence@openclassroom.com",
                            "yacine@openclassroom.com",
                            "virgile@openclassroom.com"
                    ))

            ),
            new Meeting(
                    6,
                    "Navigation",
                    Room.Peach,
                    LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.of(10, 00)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(11, 00)),
                    new ArrayList<>(Arrays.asList(
                            "florence@openclassroom.com",
                            "mitch@openclassroom.com",
                            "tiffany@openclassroom.com"
                    ))

            ),
            new Meeting(
                    7,
                    "ViewModel",
                    Room.Toad,
                    LocalDateTime.of(
                            LocalDate.now().plusDays(1),
                            LocalTime.of(10, 30)),
                    LocalDateTime.of(
                            LocalDate.now(),
                            LocalTime.of(12, 00)),
                    new ArrayList<>(Arrays.asList(
                            "amadine@lamzone.com",
                            "luc@lamzone.com",
                            "virgile@lamzone.com"
                    ))

            )
    );

    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
}
