package com.lamzone.mareu.data.meeting.model;

import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.Objects;

public class Meeting {
    private final long id;
    private String title;
    private Room room;
    private LocalTime time;


    public Meeting(long id, String title, Room room, LocalTime time) {
        this.id = id;
        this.title = title;
        this.room = room;
        this.time = time;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public Room getRoom() {
        return room;
    }
    public LocalTime getTime() { return time; }


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
