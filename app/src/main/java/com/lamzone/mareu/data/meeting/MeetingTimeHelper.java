package com.lamzone.mareu.data.meeting;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MeetingTimeHelper {

    public static final int MEETING_MIN_DURATION = 15;
    public static final int MEETING_MAX_DURATION = 240;

    /**
     * Return the first occurrence of meeting, in given list, overlapping with given meeting
     * @param meeting search overlapping meetings with this meeting
     * @param meetingsFullList search overlapping meetings in this list
     * @return
     */
    public static Meeting findOverlappingMeetings(Meeting meeting, List<Meeting> meetingsFullList) {
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

    /**
     * check if end time of given meeting respects business rules :
     * - minimum meeting duration
     * - maximum meeting duration
     * - a meeting can't end before its start time
     * - a meeting can't end after midnight
     * Return a message id corresponding to each cases.
     * @param meeting
     * @return 0 if end time is correct, id of invalidity message if it isn't.
     */
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

    /**
     * return a filtered list from given meeting list applying a given hour filter with from
     * Filter acts differently whether the filter values ​​are filled or null
     * @param meetings
     * @param hourFilter Array that contain two LocalTime values: fromTime and toTime
     * @return - unfiltered list, when both filter values are null.
     * - meetings with start time in range [fromTime,toTime[ excluded, when toTime value is null.
     * - meetings with end time before or equal to parameter toTime, when fromTime is null.
     * - meetings with time slot in inclusive range [fromTime, toTime]
     */
    public static List<Meeting> filterMeetings(List<Meeting> meetings, LocalTime[] hourFilter) {
        if (hourFilter[0] == null && hourFilter[1] == null) return meetings;

        ArrayList<Meeting> filteredList = new ArrayList<>();
        if (hourFilter[0] == null) includeEndedMeetings(meetings, filteredList, hourFilter[1]);
        else if (hourFilter[1] == null) includeOverlappingMeetings(meetings, filteredList, hourFilter[0]);
        else includeContainedMeetings(meetings, filteredList, hourFilter[0], hourFilter[1]);

        return filteredList;
    }

    /**
     * returns meetings with start time in range [start time, end time[
     * (end time excluded)
     * @param fullList
     * @param filteredList
     * @param time
     */
    private static void includeOverlappingMeetings(List<Meeting> fullList, List<Meeting> filteredList, LocalTime time) {
        for (Meeting m : fullList) {
            boolean upperEqualMinRange = !time.isBefore(m.getStartTime());
            boolean underMaxRange = m.getEndTime().isAfter(time);
            if (upperEqualMinRange && underMaxRange) filteredList.add(m);
        }
    }

    /**
     * returns meetings with end time before or equal to parameter time
     * @param fullList
     * @param filteredList
     * @param time
     */
    private static void includeEndedMeetings(List<Meeting> fullList, List<Meeting> filteredList, LocalTime time) {
        for (Meeting m : fullList) {
            if (!m.getEndTime().isAfter(time)) filteredList.add(m);
        }
    }

    /**
     * add meetings according to inclusive range [start time, end time]
     * @param fullList
     * @param filteredList
     * @param start
     * @param end
     */
    private static void includeContainedMeetings(List<Meeting> fullList, List<Meeting> filteredList, LocalTime start, LocalTime end) {
        for (Meeting m : fullList) {
            boolean upperEqualMinRange = !m.getStartTime().isBefore(start);
            boolean underEqualMaxRange = !m.getEndTime().isAfter(end);
            if (upperEqualMinRange && underEqualMaxRange) filteredList.add(m);
        }
    }

    private static DateTimeFormatter getTimeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    public static String toString(LocalTime time) {
        return time.format(getTimeFormatter());
    }

    public static String toString(LocalTime start, LocalTime end) {
        return toString(start) + " - " + toString(end);
    }

    public static LocalTime toTime(String timeString) {
        return toTime(timeString, null);
    }

    public static LocalTime toTime(String timeString, LocalTime defaultTime) {
        LocalTime time;
        try {
            time = LocalTime.parse(timeString, getTimeFormatter());
        } catch (Exception e) {
            time = defaultTime;
        }
        return time;
    }
}
