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

    public void addMeeting(Meeting meeting) {
        mMeetingRepository.addMeeting(meeting);
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