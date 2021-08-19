package com.lamzone.mareu.data.meeting;

import android.content.Context;
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

    private MeetingRepository mMeetingRepository;
    private MutableLiveData<List<Meeting>> mMeetingsLiveData;

    private LocalTime[] mTimeFilter;

    private boolean[] mRoomFilter;
    private Room[] mRooms;
    private boolean mEmptyRoomFilter;

    public MeetingViewModel() {
        mMeetingRepository = new MeetingRepository();
        mMeetingsLiveData = new MutableLiveData<>();
        fetchMeetings();
        initRoomFilter();
        initTimeFilter();
    }

    public MutableLiveData<List<Meeting>> getMeetingsLiveData() {
        return mMeetingsLiveData;
    }
    public LocalTime[] getTimeFilter() {
        return mTimeFilter;
    }

    public Room[] getRooms() {
        return mRooms;
    }
    public boolean[] getRoomFilter() { return mRoomFilter; }
    public void setRoomFilter(boolean[] roomFilter) {
        setEmptyRoomFilter(false);
        mRoomFilter = roomFilter;
    }
    public boolean isEmptyRoomFilter() { return mEmptyRoomFilter; }
    public void setEmptyRoomFilter(boolean emptyRoomFilter) { mEmptyRoomFilter = emptyRoomFilter; }

    public void fetchMeetings() {
        List<Meeting> meetings = mMeetingRepository.fetchMeetings();
        mMeetingsLiveData.setValue(meetings);
    }

    public void addMeeting(Meeting meeting) {
        mMeetingRepository.addMeeting(meeting);
        updateViewModelData();
    }

    public void deleteMeeting(Meeting meeting) {
        mMeetingRepository.deleteMeeting(meeting);
        updateViewModelData();
    }

    public Meeting getMeeting(long id) {
        List<Meeting> meetings = mMeetingRepository.getCachedMeetings();
        for (Meeting meeting : meetings) {
            if (meeting.getId() == id) return meeting;
        }
        return null;
    }

    private void updateViewModelData() {
        List<Meeting> meetings = mMeetingRepository.getCachedMeetings();
        mMeetingsLiveData.setValue(meetings);
        applyFilters();
    }

    public String checkMeetingValidity(Meeting meeting, Context context) {
        int messageId = checkRoomRelatedFields(meeting);
        if (messageId != 0) return context.getResources().getString(messageId);

        List<Meeting> updatedMeetingList = mMeetingRepository.fetchMeetings();
        Meeting overlappingMeeting = MeetingTimeHelper.checkTimeSlot(meeting, updatedMeetingList);
        if (overlappingMeeting != null) return
                context.getResources().getString(R.string.invalidMeetingTimeSlot)
                        + "\n" + meetingTimeSlotToString(overlappingMeeting);

        messageId = checkNonRoomRelatedFields(meeting);
        if (messageId != 0) return context.getResources().getString(messageId);

        return "";
    }

    private int checkRoomRelatedFields(Meeting meeting) {
        if (meeting.getRoom() == null) return R.string.invalidMeetingRoomEmpty;
        if (meeting.getStartTime() == null) return R.string.invalidMeetingStartTimeEmpty;
        if (meeting.getEndTime() == null) return R.string.invalidMeetingEndTimeEmpty;
        return MeetingTimeHelper.checkEndTime(meeting);
    }

    private int checkNonRoomRelatedFields(Meeting meeting) {
        if (meeting.getTitle() == null || meeting.getTitle().length() < 2)
            return R.string.invalidMeetingTitle;
        if (meeting.getMemberList().size() < 2) return R.string.invalidMeetingMiniumTwoMembers;
        return 0;
    }

    private void initRoomFilter() {
        mRooms = Room.values();
        mRoomFilter = new boolean[mRooms.length];
        mEmptyRoomFilter = true;
        Arrays.fill(mRoomFilter, false);
    }

    private int getRoomPosition(Room room) {
        for (int i = 0; i < mRooms.length; i++) {
            if (mRooms[i].getName() == room.getName()) return i;
        }
        return -1;
    }

    public List<Meeting> applyRoomFilter(List<Meeting> meetings) {
        if (mEmptyRoomFilter) return meetings;

        ArrayList<Meeting> filteredList = new ArrayList<>();
        for (Meeting m : meetings) {
            int roomPosition = getRoomPosition(m.getRoom());
            if (mRoomFilter[roomPosition]) {
                filteredList.add(m);
            }
        }
        return filteredList;
    }

    private void initTimeFilter() {
        mTimeFilter = new LocalTime[2];
        Arrays.fill(mTimeFilter, null);
    }

    public String getFromTimeString(Context context) {
        LocalTime fromTime = mTimeFilter[0];
        if (fromTime != null) return formatTime(fromTime);
        else {
            return context.getResources().getString(R.string.from);
        }
    }

    public String getToTimeString(Context context) {
        LocalTime endTime = mTimeFilter[1];
        if (endTime != null) return formatTime(endTime);
        else {
            return context.getResources().getString(R.string.to);
        }
    }

    private String meetingTimeSlotToString(Meeting meeting) {
        return formatTime(meeting.getStartTime()) +
                " - " + formatTime(meeting.getEndTime());
    }

    public void setTimeFilter(String from, String to) {
        LocalTime fromTime = stringToLocalTime(from);
        LocalTime toTime = stringToLocalTime(to);
        mTimeFilter = new LocalTime[]{fromTime, toTime};
    }

    public String formatTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
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

    public void applyFilters() {
        List<Meeting> filteredMeetings = mMeetingRepository.getCachedMeetings();
        filteredMeetings = applyRoomFilter(filteredMeetings);
        filteredMeetings = MeetingTimeHelper.filterMeetings(filteredMeetings, mTimeFilter);
        mMeetingsLiveData.setValue(filteredMeetings);
    }

    public boolean isEmailValid(String text) {
        if (!text.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(text).matches()) return true;
        else return false;
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