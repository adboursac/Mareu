package com.lamzone.mareu.data.meeting;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MeetingDateTimeHelper {

    public static final int MEETING_MIN_DURATION = 15;//15 minutes
    public static final int MEETING_MAX_DURATION = 240;//4 hours

    /**
     * Return the first occurrence of meeting, in given list, overlapping with given meeting
     *
     * @param meeting          search overlapping meetings with this meeting
     * @param meetingsFullList search overlapping meetings in this list
     * @return
     */
    public static Meeting findOverlappingMeetings(Meeting meeting, List<Meeting> meetingsFullList) {
        for (Meeting m : meetingsFullList) {
            if (meeting.getRoom() == m.getRoom()
                    && meeting.getStart().toLocalDate().equals(m.getStart().toLocalDate())
                    && (meeting.getStart().equals(m.getStart()) || (
                    meeting.getStart().isBefore(m.getEnd()) &&
                            meeting.getEnd().isAfter(m.getStart())))
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
     *
     * @param meeting
     * @return 0 if end time is correct, id of invalidity message ID if it isn't.
     */
    public static int checkEndTime(Meeting meeting) {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime midnightLatestStartTime = midnight
                .minusMinutes(MEETING_MAX_DURATION);
        LocalTime startTime = meeting.getStart().toLocalTime();
        LocalTime endTime = meeting.getEnd().toLocalTime();

        if (endTime.equals(midnight)) {
            if (startTime.isBefore(midnightLatestStartTime))
                return R.string.invalidMeetingMaxDuration;
            if (midnight.minusMinutes(MEETING_MIN_DURATION).isBefore(startTime))
                return R.string.invalidMeetingMinDuration;
        } else {
            if (startTime.isAfter(midnightLatestStartTime) && endTime.isBefore(startTime))
                return R.string.invalidMeetingEndAfterMidnight;
            if (startTime.isAfter(endTime))
                return R.string.invalidMeetingEndsBeforeItStarts;
            if (startTime.plusMinutes(MEETING_MIN_DURATION).isAfter(endTime))
                return R.string.invalidMeetingMinDuration;
            if (startTime.plusMinutes(MEETING_MAX_DURATION).isBefore(endTime)
                    && startTime.isBefore(midnightLatestStartTime))
                return R.string.invalidMeetingMaxDuration;
        }

        return 0;
    }

    /**
     * Check if the given meeting is set before today
     * @param meeting
     * @return 0 or invalidity message ID if it's before today
     */
    public static int checkBeforeToday(Meeting meeting) {
        LocalDate today = LocalDate.now();
        if (meeting.getStart().toLocalDate().isBefore(today)) return R.string.invalidMeetingBeforeToday;
        else return 0;
    }

    /**
     * return a filtered list from both given date and time filter
     *
     * @param meetings
     * @param dateFilter
     * @param timeFilter
     * @return
     */
    public static List<Meeting> filterMeetings(List<Meeting> meetings,
                                               LocalDate dateFilter,
                                               LocalTime timeFilter) {
        List<Meeting> filteredList = filterByDate(meetings, dateFilter);
        filteredList = filterByTime(filteredList, timeFilter);
        return filteredList;
    }

    /**
     * return a filtered list from given date filter
     *
     * @param fullList
     * @param date
     * @return
     */
    public static List<Meeting> filterByDate(List<Meeting> fullList, LocalDate date) {
        if (date == null) return fullList;
        ArrayList<Meeting> filteredList = new ArrayList<>();
        for (Meeting m : fullList) {
            if (m.getStart().toLocalDate().equals(date)) filteredList.add(m);
        }
        return filteredList;
    }

    /**
     * returns meetings with start time in range [start time, end time[
     * (end time excluded)
     *
     * @param fullList
     * @param startTime
     */
    private static List<Meeting> filterByTime(List<Meeting> fullList, LocalTime startTime) {
        if (startTime == null) return fullList;
        ArrayList<Meeting> filteredList = new ArrayList<>();
        for (Meeting m : fullList) {
            boolean upperEqualMinRange = !startTime.isBefore(m.getStart().toLocalTime());
            boolean underMaxRange = m.getEnd().toLocalTime().isAfter(startTime);
            if (upperEqualMinRange && underMaxRange) filteredList.add(m);
        }
        return filteredList;
    }

    public static DateTimeFormatter getTimeFormatter() {
        return DateTimeFormatter.ofPattern("HH:mm");
    }

    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern("d/MM/yyyy");
    }

    public static String dateToString(LocalDate date) {
        return date.format(getDateFormatter());
    }

    public static String timeToString(LocalTime time) {
        return time.format(getTimeFormatter());
    }

    public static String dateTimeToTimeString(LocalDateTime dateTime) {
        return dateTime.format(getTimeFormatter());
    }

    public static String timeSlotToString(LocalDateTime start, LocalDateTime end) {
        return dateTimeToTimeString(start) + " - " + dateTimeToTimeString(end);
    }

    public static LocalDate stringToDate(String dateString, LocalDate defaultDate) {
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, getDateFormatter());
        } catch (Exception e) {
            date = defaultDate;
        }
        return date;
    }

    public static LocalTime stringToTime(String timeString, LocalTime defaultTime) {
        LocalTime time;
        try {
            time = LocalTime.parse(timeString, getTimeFormatter());
        } catch (Exception e) {
            time = defaultTime;
        }
        return time;
    }

    public static LocalDateTime stringToDateTime(String dateString, LocalDate defaultDate,
                                                 String timeString, LocalTime defaultTime) {
        LocalDate date = stringToDate(dateString, defaultDate);
        LocalTime time = stringToTime(timeString, defaultTime);
        if (date == null || time == null) return null;
        return LocalDateTime.of(date, time);
    }
}
