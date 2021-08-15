package com.lamzone.mareu.data.meeting;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingViewModel extends ViewModel {

    public static final int FILTER_PRECISION = 6;
    public static final int MEETING_MIN_DURATION = 15;
    public static final int MEETING_MAX_DURATION = 120;

    private MeetingRepository mMeetingRepository;
    private MutableLiveData<List<Meeting>> mMeetingsLiveData;
    private MutableLiveData<LocalTime[]> mHoursFilterLiveData;
    private MutableLiveData<boolean[]> mSelectedRoomsLiveData;
    private int mSelectedRoomsCounter = 0;
    private Room[] mRooms;

    public MeetingViewModel() {
        mMeetingRepository = new MeetingRepository();
        mMeetingsLiveData = new MutableLiveData<>();
        mHoursFilterLiveData = new MutableLiveData<>();
        fetchMeetings();
        initRoomFilter();
        initHourFilter();
    }

    public MutableLiveData<List<Meeting>> getMeetingsLiveData() {
        return mMeetingsLiveData;
    }

    public MutableLiveData<boolean[]> getSelectedRoomsLiveData() {
        return mSelectedRoomsLiveData;
    }

    public MutableLiveData<LocalTime[]> getHoursFilterLiveData() {
        return mHoursFilterLiveData;
    }

    public Room[] getRooms() {
        return mRooms;
    }

    public void fetchMeetings() {
        List<Meeting> meetings = mMeetingRepository.fetchMeetings();
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
        List<Meeting> meetings = mMeetingRepository.getCachedMeetings();
        mMeetingsLiveData.setValue(meetings);
        applyFilters();
    }

    private int meetingValidTest(Meeting meeting) {
        if (meeting.getTitle() == null || meeting.getTitle().length() < 2)
            return R.string.invalidMeetingTitle;
        if (meeting.getRoom() == null) return R.string.invalidMeetingRoomEmpty;
        if (meeting.getStartTime() == null) return R.string.invalidMeetingStartTimeEmpty;
        if (meeting.getEndTime() == null) return R.string.invalidMeetingEndTimeEmpty;
        if (!validateHourRange(meeting)) return R.string.invalidMeetingTimeSlot;
        if (!validateMinimumDuration(meeting)) return R.string.invalidMeetingMinDuration;
        if (!validateMaximumDuration(meeting)) return R.string.invalidMeetingMaxDuration;
        if (meeting.getMemberList().size() < 2) return R.string.invalidMeetingMiniumTwoMembers;
        return 0;
    }

    private void initRoomFilter() {
        mRooms = Room.values();
        boolean[] selectedRoomsList = new boolean[mRooms.length];
        Arrays.fill(selectedRoomsList, false);
        mSelectedRoomsLiveData = new MutableLiveData<>(selectedRoomsList);
    }

    private int getRoomPosition(Room room) {
        for (int i = 0; i < mRooms.length; i++) {
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
        } else {
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

    private void initHourFilter() {
        LocalTime[] hourRange = new LocalTime[2];
        Arrays.fill(hourRange, null);
        mHoursFilterLiveData = new MutableLiveData<>(hourRange);
    }

    public String getFromTimeString(Context context) {
        LocalTime fromTime = mHoursFilterLiveData.getValue()[0];
        if (fromTime != null) return fromTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        else {
            return context.getResources().getString(R.string.from);
        }
    }

    public String getToTimeString(Context context) {
        LocalTime endTime = mHoursFilterLiveData.getValue()[1];
        if (endTime != null) return endTime.format(DateTimeFormatter.ofPattern("HH:mm"));
        else {
            return context.getResources().getString(R.string.to);
        }
    }

    public void setHourRange(String from, String to) {
        LocalTime fromTime = stringToLocalTime(from);
        LocalTime toTime = stringToLocalTime(to);
        LocalTime[] hourRange = new LocalTime[]{fromTime, toTime};
        mHoursFilterLiveData.setValue(hourRange);
    }

    public LocalTime stringToLocalTime(String timeString) {
        LocalTime time;
        try {
            time = LocalTime.parse(timeString, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            time = null;
        }
        return time;
    }

    public List<Meeting> applyRoomFilter(List<Meeting> meetings) {
        if (mSelectedRoomsCounter == 0 || mSelectedRoomsCounter == mRooms.length) {
            return meetings;
        }

        ArrayList<Meeting> filteredList = new ArrayList<>();

        boolean[] selected = mSelectedRoomsLiveData.getValue();
        for (Meeting m : meetings) {
            int roomPosition = getRoomPosition(m.getRoom());
            if (selected[roomPosition]) filteredList.add(m);
        }

        return filteredList;
    }

    public List<Meeting> applyHourFilter(List<Meeting> meetings) {
        LocalTime[] hourFilter = mHoursFilterLiveData.getValue();
        if (hourFilter[0] == null && hourFilter[1] == null) {
            return meetings;
        }

        ArrayList<Meeting> filteredList = new ArrayList<>();

        if (hourFilter[0] == null) {
            for (Meeting m : meetings) {
                LocalTime endTimeMin = hourFilter[1].minusMinutes(FILTER_PRECISION);
                LocalTime endTimeMax = hourFilter[1].plusMinutes(FILTER_PRECISION);
                boolean upperMinRange = m.getEndTime().isAfter(endTimeMin);
                boolean underMaxRange = m.getEndTime().isBefore(endTimeMax);
                if (upperMinRange && underMaxRange) filteredList.add(m);
            }
        } else if (hourFilter[1] == null) {
            for (Meeting m : meetings) {
                LocalTime startTimeMin = hourFilter[0].minusMinutes(FILTER_PRECISION);
                LocalTime startTimeMax = hourFilter[0].plusMinutes(FILTER_PRECISION);
                boolean upperMinRange = m.getStartTime().isAfter(startTimeMin);
                boolean underMaxRange = m.getStartTime().isBefore(startTimeMax);
                if (upperMinRange && underMaxRange) filteredList.add(m);
            }
        } else {
            for (Meeting m : meetings) {
                LocalTime startTimeMin = hourFilter[0].minusMinutes(1);
                LocalTime startTimeMax = hourFilter[1].plusMinutes(1);
                boolean upperMinRange = m.getStartTime().isAfter(startTimeMin);
                boolean underMaxRange = m.getStartTime().isBefore(startTimeMax);
                if (upperMinRange && underMaxRange) filteredList.add(m);
            }
        }

        return filteredList;
    }

    public void applyFilters() {
        List<Meeting> filteredMeetings = mMeetingRepository.getCachedMeetings();
        filteredMeetings = applyRoomFilter(filteredMeetings);
        filteredMeetings = applyHourFilter(filteredMeetings);
        mMeetingsLiveData.setValue(filteredMeetings);
    }

    public boolean validateEmail(String text) {
        if (!text.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(text).matches()) return true;
        else return false;
    }

    public boolean validateHourRange(Meeting meeting) {
        List<Meeting> meetings = mMeetingRepository.getCachedMeetings();
        for (Meeting m : meetings) {
            if (meeting.getRoom() == m.getRoom() &&
                    (meeting.getStartTime().equals(m.getStartTime()) ||
                            (meeting.getStartTime().isBefore(m.getEndTime()) &&
                                    meeting.getEndTime().isAfter(m.getStartTime())
                            )
                    )
            ) return false;
        }
        return true;
    }

    public boolean validateMinimumDuration(Meeting meeting) {
        LocalTime minimumEndTime = meeting.getStartTime().plusMinutes(MEETING_MIN_DURATION);
        return ! meeting.getEndTime().isBefore(minimumEndTime);
    }

    public boolean validateMaximumDuration(Meeting meeting) {
        LocalTime maximumEndTime = meeting.getStartTime().plusMinutes(MEETING_MAX_DURATION);
        return ! meeting.getEndTime().isAfter(maximumEndTime);
    }

    public static String listToString(List<String> list, String separator) {
        String text = "";
        for (int i = 0; i < list.size(); i++) {
            text += list.get(i);
            if (i + 1 < list.size()) text += separator;
        }
        return text;
    }
}