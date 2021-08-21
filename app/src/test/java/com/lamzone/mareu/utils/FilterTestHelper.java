package com.lamzone.mareu.utils;

import com.lamzone.mareu.data.meeting.model.Room;

import java.time.LocalTime;

public class FilterTestHelper {
    /**
     * Generate a room filter with room entry only
     * @param room the room to filter with
     */
    public static boolean[] generateRoomFilter(Room room) {
        boolean[] roomFilter = new boolean[Room.values().length];
        Room[] rooms = Room.values();
        for (int i = 0; i < rooms.length; i++)
            roomFilter[i] = rooms[i].equals(room);
        return roomFilter;
    }
}
