package com.lamzone.mareu.data.meeting;

import android.content.Context;
import android.content.res.Resources;
import android.util.Patterns;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeetingViewModel extends ViewModel {

    private MeetingRepository mMeetingRepository;
    private MutableLiveData<List<Meeting>> mMeetingsLiveData;

    private LocalDate mDateFilter;
    private LocalTime mTimeFilter;

    private boolean[] mRoomFilter;
    private Room[] mRooms;
    private boolean mEmptyRoomFilter;

    public MeetingViewModel() {
        mMeetingRepository = new MeetingRepository(DI.getMeetingApiService());
        mMeetingsLiveData = new MutableLiveData<>();
        fetchMeetings();
        initRoomFilter();
        initDateAndTimeFilter();
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

    public LocalDate getDateFilter() { return mDateFilter; }

    public LocalTime getTimeFilter() { return mTimeFilter; }

    public boolean isEmptyRoomFilter() {
        return mEmptyRoomFilter;
    }

    public void setRoomFilter(boolean[] roomFilter) {
        setEmptyRoomFilter(false);
        mRoomFilter = roomFilter;
    }

    public void setEmptyRoomFilter(boolean emptyRoomFilter) {
        mEmptyRoomFilter = emptyRoomFilter;
    }

    public void setDateFilter(String dateString) {
        mDateFilter = MeetingDateTimeHelper.stringToDate(dateString, null);
    }

    public void setTimeFilter(String timeString) {
        mTimeFilter = MeetingDateTimeHelper.stringToTime(timeString, null);
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

    /**
     * get a meeting by id
     * @param id
     * @return
     */
    public Meeting getMeeting(long id) {
        List<Meeting> meetings = mMeetingRepository.getCachedMeetings();
        for (Meeting meeting : meetings) {
            if (meeting.getId() == id) return meeting;
        }
        return null;
    }

    /**
     * Check if given meeting can be added according to business rules :
     * - fields can't be empty
     * - must contain at least two member emails
     * - meeting can't be set before today
     * - end time must respect business rules
     * - meeting can't overlap another booked meeting
     * @param meeting
     * @param resources
     * @return empty String if meeting is valid, or message description of what is wrong.
     */
    public String checkMeetingValidity(Meeting meeting, Resources resources) {
        int messageId = checkMeetingUncompletedFields(meeting);
        if (messageId != 0) return resources.getString(messageId);

        messageId = MeetingDateTimeHelper.checkBeforeToday(meeting);
        if (messageId != 0) return resources.getString(messageId);

        messageId = MeetingDateTimeHelper.checkEndTime(meeting);
        if (messageId != 0) return resources.getString(messageId);

        List<Meeting> updatedMeetingList = mMeetingRepository.fetchMeetings();
        Meeting overlappingMeeting = MeetingDateTimeHelper.findOverlappingMeetings(meeting, updatedMeetingList);
        if (overlappingMeeting != null)
            return generateOverlappingMeetingMessage(overlappingMeeting, resources);
        return "";
    }

    private String generateOverlappingMeetingMessage(Meeting overlappingMeeting, Resources resources) {
        return resources.getString(R.string.invalidMeetingTimeSlot) + "\n" +
                MeetingDateTimeHelper.timeSlotToString(
                        overlappingMeeting.getStart(),
                        overlappingMeeting.getEnd());
    }

    private int checkMeetingUncompletedFields(Meeting meeting) {
        if (meeting.getTitle() == null || meeting.getTitle().length() < 2)
            return R.string.invalidMeetingTitle;
        if (meeting == null) return R.string.invalidMeetingDateEmpty;
            if (meeting.getRoom() == null) return R.string.invalidMeetingRoomEmpty;
        if (meeting.getStart() == null) return R.string.invalidMeetingStartTimeEmpty;
        if (meeting.getEnd() == null) return R.string.invalidMeetingEndTimeEmpty;
        if (meeting.getMemberList().size() < 2) return R.string.invalidMeetingMiniumTwoMembers;
        return 0;
    }

    private void initRoomFilter() {
        mRooms = Room.values();
        mRoomFilter = new boolean[mRooms.length];
        mEmptyRoomFilter = true;
        Arrays.fill(mRoomFilter, false);
    }

    /**
     * get Room item position in existing Rooms.
     * @param room
     * @return
     */
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

    private void initDateAndTimeFilter() {
        mDateFilter = LocalDate.now();
        mTimeFilter = null;
    }

    /**
     * generate String from Date filter for UI display.
     * @param context
     * @return
     */
    public String getDateFilterString(Context context) {
        if (mDateFilter != null) return MeetingDateTimeHelper.dateToString(mDateFilter);
        else return context.getResources().getString(R.string.meetingDate);
    }

    /**
     * generate String from Time filter for UI display.
     * @param context
     * @return
     */
    public String getTimeFilterString(Context context) {
        if (mTimeFilter != null) return MeetingDateTimeHelper.timeToString(mTimeFilter);
        else return context.getResources().getString(R.string.meetingTime);
    }

    public void applyFilters() {
        List<Meeting> filteredMeetings = mMeetingRepository.getCachedMeetings();
        filteredMeetings = applyRoomFilter(filteredMeetings);
        filteredMeetings = MeetingDateTimeHelper.filterMeetings(filteredMeetings, mDateFilter, mTimeFilter);
        mMeetingsLiveData.setValue(filteredMeetings);
    }

    public boolean isEmailValid(String text) {
        if (!text.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(text).matches()) return true;
        else return false;
    }
}
