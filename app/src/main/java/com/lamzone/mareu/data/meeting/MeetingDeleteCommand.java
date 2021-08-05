package com.lamzone.mareu.data.meeting;

import com.lamzone.mareu.data.meeting.model.Meeting;

public interface MeetingDeleteCommand {
    void deleteMeeting(Meeting meeting);
}
