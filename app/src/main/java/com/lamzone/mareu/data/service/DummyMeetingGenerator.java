package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

abstract class DummyMeetingGenerator {
    public static List<Meeting> generateMeetings() { return new ArrayList<Meeting>(Arrays.asList(new Meeting(0, "Réunion", Room.Luigi, LocalTime.now(), LocalTime.now().plusMinutes(30))));
    }
}
