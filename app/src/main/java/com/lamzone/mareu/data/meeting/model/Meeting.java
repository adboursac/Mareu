package com.lamzone.mareu.data.meeting.model;

import android.content.res.Resources;

import androidx.annotation.NonNull;

import com.lamzone.mareu.data.meeting.MeetingDateTimeHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Meeting {
    private final long mId;
    private String mTitle;
    private Room mRoom;
    private LocalDateTime mStart;
    private LocalDateTime mEnd;

    private List<String> mMemberList;


    public Meeting(long id, String title, Room room, LocalDateTime start, LocalDateTime end, List<String> memberList) {
        mId = id;
        mTitle = title;
        mRoom = room;
        mStart = start;
        mEnd = end;
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

    public LocalDateTime getStart() {
        return mStart;
    }

    public LocalDateTime getEnd() {
        return mEnd;
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

    public void setStart(LocalDateTime start) {
        mStart = start;
    }

    public void setEnd(LocalDateTime end) {
        mEnd = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(mId, meeting.getId()) &&
                Objects.equals(mTitle, meeting.getTitle()) &&
                Objects.equals(mRoom, meeting.getRoom()) &&
                Objects.equals(mStart, meeting.getStart()) &&
                Objects.equals(mEnd, meeting.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTitle, mRoom, mStart, mEnd);
    }

    @NonNull
    @Override
    public Meeting clone() {
        return new Meeting(
                System.currentTimeMillis(),
                mTitle,
                mRoom,
                mStart,
                mEnd,
                new ArrayList<>(mMemberList)
        );
    }

    public static String memberListToString(List<String> list, String separator) {
        String text = "";
        for (int i = 0; i < list.size(); i++) {
            text += list.get(i);
            if (i + 1 < list.size()) text += separator;
        }
        return text;
    }

    public String shortDescription(Resources resources) {
        return getTitle() +
                " • " +
                MeetingDateTimeHelper.toString(mStart) +
                " • " +
                mRoom.getName(resources);
    }
}
