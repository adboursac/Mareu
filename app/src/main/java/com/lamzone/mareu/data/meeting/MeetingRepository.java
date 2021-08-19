package com.lamzone.mareu.data.meeting;

import androidx.lifecycle.MutableLiveData;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.data.service.MeetingApiService;

import java.util.ArrayList;
import java.util.List;

public class MeetingRepository {

    private static final MeetingApiService service = DI.getMeetingApiService();
    private List<Meeting> mMeetingsCache = new ArrayList<>();

    public List<Meeting> getCachedMeetings() {
        return mMeetingsCache;
    }

    public List<Meeting> fetchMeetings() {
        mMeetingsCache = service.getMeetings();
        return mMeetingsCache;
    }

    public void deleteMeeting(Meeting meeting) {
        service.deleteMeeting(meeting);
    }

    public void addMeeting(Meeting meeting) {
        service.addMeeting(meeting);
    }
}
