package com.lamzone.mareu.data.meeting;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingTimeHelper {

    public static final int FILTER_PRECISION = 15;
    public static final int MEETING_MIN_DURATION = 15;
    public static final int MEETING_MAX_DURATION = 240;

    public static Meeting checkTimeSlot(Meeting meeting, List<Meeting> meetingsFullList) {
        for (Meeting m : meetingsFullList) {
            if (meeting.getRoom() == m.getRoom() &&
                    (meeting.getStartTime().equals(m.getStartTime()) ||
                            (meeting.getStartTime().isBefore(m.getEndTime()) &&
                                    meeting.getEndTime().isAfter(m.getStartTime())
                            )
                    )
            ) return m;
        }
        return null;
    }

    public static int checkEndTime(Meeting meeting) {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime midnightLatestStartTime = midnight
                .minusMinutes(MEETING_MAX_DURATION);

        if (meeting.getEndTime().equals(midnight)) {
            if (meeting.getStartTime().isBefore(midnightLatestStartTime))
                return R.string.invalidMeetingMaxDuration;
            if (midnight.minusMinutes(MEETING_MIN_DURATION).isBefore(meeting.getStartTime()))
                return R.string.invalidMeetingMinDuration;
        } else {
            if (meeting.getStartTime().isAfter(midnightLatestStartTime)
                    && meeting.getEndTime().isBefore(meeting.getStartTime()))
                return R.string.invalidMeetingEndAfterMidnight;
            if (meeting.getStartTime().isAfter(meeting.getEndTime()))
                return R.string.invalidMeetingEndsBeforeItStarts;
            if (meeting.getStartTime().plusMinutes(MEETING_MIN_DURATION).isAfter(meeting.getEndTime()))
                return R.string.invalidMeetingMinDuration;
            if (meeting.getStartTime().plusMinutes(MEETING_MAX_DURATION).isBefore(meeting.getEndTime())
                    && meeting.getStartTime().isBefore(midnightLatestStartTime))
                return R.string.invalidMeetingMaxDuration;
        }

        return 0;
    }

    public static List<Meeting> filterMeetings(List<Meeting> meetings, LocalTime[] hourFilter) {
        if (hourFilter[0] == null && hourFilter[1] == null) {
            return meetings;
        }

        ArrayList<Meeting> filteredList = new ArrayList<>();
        if (hourFilter[0] == null) {
            for (Meeting m : meetings) {
                LocalTime endTimeMin = hourFilter[1].minusMinutes(FILTER_PRECISION);
                LocalTime endTimeMax = hourFilter[1].plusMinutes(FILTER_PRECISION);
                boolean upperMinRange = m.getEndTime().isAfter(endTimeMin);
                boolean underMaxRange = m.getEndTime().isBefore(endTimeMax);
                if (upperMinRange && underMaxRange) filteredList.add(m);
            }
        } else if (hourFilter[1] == null) {
            for (Meeting m : meetings) {
                LocalTime startTimeMin = hourFilter[0].minusMinutes(FILTER_PRECISION);
                LocalTime startTimeMax = hourFilter[0].plusMinutes(FILTER_PRECISION);
                boolean upperMinRange = m.getStartTime().isAfter(startTimeMin);
                boolean underMaxRange = m.getStartTime().isBefore(startTimeMax);
                if (upperMinRange && underMaxRange) filteredList.add(m);
            }
        } else {
            for (Meeting m : meetings) {
                LocalTime startTimeMin = hourFilter[0].minusMinutes(1);
                LocalTime startTimeMax = hourFilter[1].plusMinutes(1);
                boolean upperMinRange = m.getStartTime().isAfter(startTimeMin);
                boolean underMaxRange = m.getStartTime().isBefore(startTimeMax);
                if (upperMinRange && underMaxRange) filteredList.add(m);
            }
        }

        return filteredList;
    }
}
