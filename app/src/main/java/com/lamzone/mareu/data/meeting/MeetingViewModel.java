package com.lamzone.mareu.data.meeting;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingViewModel extends ViewModel {

    private MeetingRepository mMeetingRepository;
    private MutableLiveData<List<Meeting>> mMeetingsLiveData;
    private List<Meeting> mMeetingsFullList;
    private MutableLiveData<LocalTime[]>  mHoursFilterLiveData;
    private MutableLiveData<boolean[]>  mSelectedRoomsLiveData;
    private int mSelectedRoomsCounter = 0;
    private Room[] mRooms;

    public MeetingViewModel() {
        mMeetingRepository = new MeetingRepository();
        mMeetingsLiveData = new MutableLiveData<>();
        mHoursFilterLiveData = new MutableLiveData<>();
        initRoomFilter();
    }

    public MutableLiveData<List<Meeting>> getMeetingsLiveData() {
        return mMeetingsLiveData;
    }
    public MutableLiveData<boolean[]> getSelectedRoomsLiveData() {
        return mSelectedRoomsLiveData;
    }
    public MutableLiveData<LocalTime[]> getHoursFilterLiveData() { return mHoursFilterLiveData; }
    public Room[] getRooms() { return mRooms; }

    public void getMeetings() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMeetingsFullList = meetings;
        mMeetingsLiveData.setValue(meetings);
    }

    public int addMeeting(Meeting meeting) {
        int validationMessage = meetingValidTest(meeting);
        if (validationMessage != 0) return validationMessage;

        mMeetingRepository.addMeeting(meeting);
        updateViewModelData();
        return 0;
    }

    public void deleteMeeting(Meeting meeting) {
        mMeetingRepository.deleteMeeting(meeting);
        updateViewModelData();
    }

    private void updateViewModelData() {
        List<Meeting> meetings = mMeetingRepository.getMeetings();
        mMeetingsFullList = meetings;
        mMeetingsLiveData.setValue(meetings);
        updateFilteredMeetings();
    }

    private int meetingValidTest(Meeting meeting) {
        if (meeting.getTitle() == null || meeting.getTitle().length() < 2) return R.string.invalidMeetingTitle;
        if (meeting.getRoom() == null) return R.string.invalidMeetingRoomEmpty;
        if (meeting.getStartTime() == null) return R.string.invalidMeetingStartTimeEmpty;
        if (meeting.getEndTime() == null) return R.string.invalidMeetingEndTimeEmpty;
        return 0;
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

        mSelectedRoomsLiveData.setValue(selectedRooms);
    }

    public void roomFilterClear() {
        boolean[] selectedRoomsList = mSelectedRoomsLiveData.getValue();
        Arrays.fill(selectedRoomsList, false);
        mSelectedRoomsCounter = 0;
        mSelectedRoomsLiveData.setValue(selectedRoomsList);
    }

    public void roomFilterSelectAll() {
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