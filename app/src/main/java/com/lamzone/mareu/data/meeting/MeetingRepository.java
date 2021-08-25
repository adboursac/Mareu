package com.lamzone.mareu.data.meeting;


import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.service.MeetingApiService;

import java.util.ArrayList;
import java.util.List;

public class MeetingRepository {

    private final MeetingApiService mService;
    private List<Meeting> mMeetingsCache;

    public MeetingRepository(MeetingApiService service) {
        mService = service;
        mMeetingsCache = new ArrayList<>();
    }

    public List<Meeting> getCachedMeetings() {
        return mMeetingsCache;
    }

    public List<Meeting> fetchMeetings() {
        mMeetingsCache = mService.getMeetings();
        return mMeetingsCache;
    }

    public void deleteMeeting(Meeting meeting) {
        mService.deleteMeeting(meeting);
    }

    public void addMeeting(Meeting meeting) {
        mService.addMeeting(meeting);
    }
}
