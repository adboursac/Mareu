package com.lamzone.mareu.data.meeting;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;

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

    public Room[] getRooms() {
        return mRooms;
    }

    public boolean[] getRoomFilter() {
        return mRoomFilter;
    }

    public void setRoomFilter(boolean[] roomFilter) {
        setEmptyRoomFilter(false);
        mRoomFilter = roomFilter;
    }

    public boolean isEmptyRoomFilter() {
        return mEmptyRoomFilter;
    }

    public void setEmptyRoomFilter(boolean emptyRoomFilter) {
        mEmptyRoomFilter = emptyRoomFilter;
    }

    public void fetchMeetings() {
        List<Meeting> meetings = mMeetingRepository.fetchMeetings();
        mMeetingsLiveData.setValue(meetings);
    }

    public void addMeeting(Meeting meeting) {
        mMeetingRepository.addMeeting(meeting);
        fetchMeetings();
        applyFilters();
    }

    public void deleteMeeting(Meeting meeting) {
        mMeetingRepository.deleteMeeting(meeting);
        fetchMeetings();
        applyFilters();
    }

    public Meeting getMeeting(long id) {
        List<Meeting> meetings = mMeetingRepository.getCachedMeetings();
        for (Meeting meeting : meetings) {
            if (meeting.getId() == id) return meeting;
        }
        return null;
    }

    public String checkMeetingValidity(Meeting meeting, Resources resources) {
        int messageId = checkMeetingUncompletedFields(meeting);
        if (messageId != 0) return resources.getString(messageId);

        messageId = MeetingTimeHelper.checkEndTime(meeting);
        if (messageId != 0) return resources.getString(messageId);

        List<Meeting> updatedMeetingList = mMeetingRepository.fetchMeetings();
        Meeting overlappingMeeting = MeetingTimeHelper.findOverlappingMeetings(meeting, updatedMeetingList);
        if (overlappingMeeting != null)
            return generateOverlappingMeetingMessage(overlappingMeeting, resources);
        return "";
    }

    private String generateOverlappingMeetingMessage(Meeting overlappingMeeting, Resources resources) {
        return resources.getString(R.string.invalidMeetingTimeSlot) + "\n" +
                MeetingTimeHelper.toString(
                        overlappingMeeting.getStartTime(),
                        overlappingMeeting.getEndTime());
    }

    private int checkMeetingUncompletedFields(Meeting meeting) {
        if (meeting.getTitle() == null || meeting.getTitle().length() < 2)
            return R.string.invalidMeetingTitle;
        if (meeting.getRoom() == null) return R.string.invalidMeetingRoomEmpty;
        if (meeting.getStartTime() == null) return R.string.invalidMeetingStartTimeEmpty;
        if (meeting.getEndTime() == null) return R.string.invalidMeetingEndTimeEmpty;
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
            if (mRooms[i].getNameId() == room.getNameId()) return i;
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
        if (fromTime != null) return MeetingTimeHelper.toString(fromTime);
        else {
            return context.getResources().getString(R.string.from);
        }
    }

    public String getToTimeString(Context context) {
        LocalTime endTime = mTimeFilter[1];
        if (endTime != null) return MeetingTimeHelper.toString(endTime);
        else {
            return context.getResources().getString(R.string.to);
        }
    }

    public void setTimeFilter(String from, String to) {
        LocalTime fromTime = MeetingTimeHelper.toTime(from);
        LocalTime toTime = MeetingTimeHelper.toTime(to);
        mTimeFilter = new LocalTime[]{fromTime, toTime};
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
}
