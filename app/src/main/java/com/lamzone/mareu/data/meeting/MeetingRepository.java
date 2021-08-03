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

    public List<Meeting> getMeetings() { return service.getMeetings(); }

    public void deleteMeeting(Meeting meeting) { service.deleteMeeting(meeting); }

    public void createMeeting(Meeting meeting) { service.createMeeting(meeting); }
}
