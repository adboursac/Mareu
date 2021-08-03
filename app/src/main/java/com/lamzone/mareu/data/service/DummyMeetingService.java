package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;

import java.util.List;

public class DummyMeetingService implements MeetingApiService {

    public static final List<Meeting> mMeetings = DummyMeetingGenerator.generateMeetings();

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<Meeting> getMeetings() {
        return mMeetings;
    }

    /**
     * {@inheritDoc}
     * @param meeting
     */
    @Override
    public void deleteMeeting(Meeting meeting) {

    }

    /**
     * {@inheritDoc}
     * @param meeting
     */
    @Override
    public void createMeeting(Meeting meeting) {

    }
}
