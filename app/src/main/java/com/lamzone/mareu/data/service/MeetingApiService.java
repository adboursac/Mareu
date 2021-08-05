package com.lamzone.mareu.data.service;

import com.lamzone.mareu.data.meeting.model.Meeting;

import java.util.List;

/**
 * Meeting API Client
 */
public interface MeetingApiService {

    /**
     * Get every meetings
     * @return
     */
    List<Meeting> getMeetings();

    /**
     * Delete a meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Create a meeting
     */
     void addMeeting(Meeting meeting);
}
