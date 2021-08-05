package com.lamzone.mareu.data.meeting;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.util.ArrayList;
import java.util.List;

public class MeetingViewModel extends ViewModel {

    MeetingRepository mMeetingRepository;
    MutableLiveData<List<Meeting>> mMutableLiveData;

    public MeetingViewModel() {
        mMeetingRepository = new MeetingRepository();
        mMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<Meeting>> getMutableLiveData() {
        return mMutableLiveData;
    }

    public void fetchMeetings() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMutableLiveData.setValue(meetings);
    }

    public String addMeeting(Meeting meeting) {
        String validationMessage = meetingValidTest(meeting);
        if (validationMessage != null) return validationMessage;

        mMeetingRepository.addMeeting(meeting);
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMutableLiveData.setValue(meetings);

        return null;
    }

    private String meetingValidTest(Meeting meeting) {
        if (meeting.getRoom() == null) return "@string:invalidSelectRoom";
        return null;
    }

    public void deleteMeeting(Meeting meeting) {
        mMeetingRepository.deleteMeeting(meeting);
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMutableLiveData.setValue(meetings);
    }

    public void filterByRoom(Room room) {
        List<Meeting> meetings = (List<Meeting>) getMutableLiveData().getValue();
        if (meetings == null) return;

        ArrayList<Meeting> filteredList = new ArrayList<>();
        for ( Meeting meeting : meetings) {
            if (meeting.getRoom() == room) filteredList.add(meeting);
        }

        mMutableLiveData.setValue(filteredList);
    }
}