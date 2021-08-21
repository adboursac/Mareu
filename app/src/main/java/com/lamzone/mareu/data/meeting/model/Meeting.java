package com.lamzone.mareu.data.meeting.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meeting {
    private final long mId;
    private String mTitle;
    private Room mRoom;
    private LocalTime mStartTime;
    private LocalTime mEndTime;

    private List<String> mMemberList;


    public Meeting(long id, String title, Room room, LocalTime startTime, LocalTime endTime, List<String> memberList) {
        mId = id;
        mTitle = title;
        mRoom = room;
        mStartTime = startTime;
        mEndTime = endTime;
        mMemberList = memberList;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public Room getRoom() {
        return mRoom;
    }

    public LocalTime getStartTime() {
        return mStartTime;
    }

    public LocalTime getEndTime() {
        return mEndTime;
    }

    public List<String> getMemberList() {
        return mMemberList;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setRoom(Room room) {
        mRoom = room;
    }

    public void setStartTime(LocalTime startTime) {
        mStartTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        mEndTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(mId, meeting.getId()) &&
                Objects.equals(mTitle, meeting.getTitle()) &&
                Objects.equals(mRoom, meeting.getRoom()) &&
                Objects.equals(mStartTime, meeting.getStartTime()) &&
                Objects.equals(mEndTime, meeting.getEndTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTitle, mRoom, mStartTime, mEndTime);
    }

    @NonNull
    @Override
    public Meeting clone() {
        return new Meeting(
                System.currentTimeMillis(),
                mTitle,
                mRoom,
                mStartTime,
                mEndTime,
                new ArrayList<>(mMemberList)
        );
    }
}
