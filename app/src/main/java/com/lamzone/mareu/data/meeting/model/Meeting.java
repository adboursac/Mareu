package com.lamzone.mareu.data.meeting.model;

import androidx.annotation.Nullable;

import java.util.Objects;

public class Meeting {
    private final int id;
    private String title;
    private Room room;

    public Meeting(int id, String title, Room room) {
        this.id = id;
        this.title = title;
        this.room = room;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(id, meeting.id) &&
                Objects.equals(title, meeting.title) &&
                Objects.equals(room, meeting.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, room);
    }
}
