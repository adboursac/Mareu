package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.util.Arrays;
import java.util.List;

abstract class DummyMeetingGenerator {
    static List<Meeting> generateMeetings() { return Arrays.asList(new Meeting(0, "New Game", Room.Donkey));
    }
}
