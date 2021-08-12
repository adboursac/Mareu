package com.lamzone.mareu.data.meeting;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class MeetingViewModel extends ViewModel {

    private MeetingRepository mMeetingRepository;
    private MutableLiveData<List<Meeting>> mMeetingsLiveData;
    private MutableLiveData<boolean[]>  mSelectedRoomsLiveData;
    private int mSelectedRoomsCounter = 0;
    private Room[] mRooms;
    private List<Meeting> mMeetingsFullList;


    public MeetingViewModel() {
        mMeetingRepository = new MeetingRepository();
        mMeetingsLiveData = new MutableLiveData<>();
        initRoomFilter();
    }

    public MutableLiveData<List<Meeting>> getMeetingsLiveData() {
        return mMeetingsLiveData;
    }
    public MutableLiveData<boolean[]> getSelectedRoomsLiveData() {
        return mSelectedRoomsLiveData;
    }
    public Room[] getRooms() { return mRooms; }

    public void fetchMeetings() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMeetingsFullList = meetings;
        mMeetingsLiveData.setValue(meetings);
    }

    public String addMeeting(Meeting meeting) {
        String validationMessage = meetingValidTest(meeting);
        if (validationMessage != null) return validationMessage;

        mMeetingRepository.addMeeting(meeting);
        updateData();
        return null;
    }

    private String meetingValidTest(Meeting meeting) {
        if (meeting.getTitle() == null || meeting.getTitle().length() < 2) return "@string:invalidTitle";
        if (meeting.getRoom() == null) return "@string:invalidSelectRoom";
        return null;
    }

    public void deleteMeeting(Meeting meeting) {
        mMeetingRepository.deleteMeeting(meeting);
        updateData();
    }

    private void updateData() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMeetingsFullList = meetings;
        mMeetingsLiveData.setValue(meetings);
        updateFilteredMeetings();
    }

    private void initRoomFilter() {
        mRooms = Room.values();
        boolean[] selectedRoomsList = new boolean[mRooms.length];
        Arrays.fill(selectedRoomsList, false);
        mSelectedRoomsLiveData = new MutableLiveData<>(selectedRoomsList);
    }

    private int getRoomPosition(Room room) {
        for (int i=0; i < mRooms.length; i++) {
            if (mRooms[i].getName() == room.getName()) return i;
        }
        return -1;
    }

    public void toggleRoomState(Room room) {
        boolean[] selectedRooms = mSelectedRoomsLiveData.getValue();
        int position = getRoomPosition(room);

        if (selectedRooms[position]) {
            mSelectedRoomsCounter--;
            selectedRooms[position] = false;
        }
        else {
            mSelectedRoomsCounter++;
            selectedRooms[position] = true;
        }

        Log.e("@@counter",""+mSelectedRoomsCounter);
        mSelectedRoomsLiveData.setValue(selectedRooms);
    }

    public void filterClear() {
        boolean[] selectedRoomsList = mSelectedRoomsLiveData.getValue();
        Arrays.fill(selectedRoomsList, false);
        mSelectedRoomsCounter = 0;
        mSelectedRoomsLiveData.setValue(selectedRoomsList);
    }

    public void filterSelectAll() {
        boolean[] selectedRoomsList = mSelectedRoomsLiveData.getValue();
        Arrays.fill(selectedRoomsList, true);
        mSelectedRoomsCounter = mRooms.length;
        mSelectedRoomsLiveData.setValue(selectedRoomsList);
    }

    public void updateFilteredMeetings() {
        if (mSelectedRoomsCounter == 0 || mSelectedRoomsCounter == mRooms.length) {
            mMeetingsLiveData.setValue(mMeetingsFullList);
            return;
        }

        ArrayList<Meeting> filteredList = new ArrayList<>();
        boolean[] selected = mSelectedRoomsLiveData.getValue();
        for (Meeting meeting : mMeetingsFullList) {
            int roomPosition = getRoomPosition(meeting.getRoom());
            if (selected[roomPosition]) filteredList.add(meeting);
        }

        mMeetingsLiveData.setValue(filteredList);
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @NonNull
    private String stringifyParticipants(@NonNull List<String> participants) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < participants.size(); i++) {
            String participant = participants.get(i);

            result.append(participant);

            if (i + 1 < participants.size()) {
                result.append(", ");
            }
        }

        return result.toString();
    }
}