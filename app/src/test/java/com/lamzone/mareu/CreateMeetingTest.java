package com.lamzone.mareu;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.MeetingRepository;
import com.lamzone.mareu.data.meeting.MeetingDateTimeHelper;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.utils.MockedResources;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Create Meeting feature unit test
 */
public class CreateMeetingTest {
    private MeetingRepository mMeetingRepository;
    private MeetingViewModel mMeetingViewModel;
    private Resources mMockedResources;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        DI.generateNewApiService();
        mMeetingRepository = new MeetingRepository(DI.getMeetingApiService());
        mMeetingViewModel = new MeetingViewModel();
        mMockedResources = new MockedResources();
    }

    /**
     * Ensure repository add meeting correctly
     */
    @Test
    public void repositoryAddMeetingTest() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        //move meeting to a free room on this interval
        m.setRoom(Room.Goomba);
        mMeetingRepository.addMeeting(m);
        assertTrue(mMeetingRepository.fetchMeetings().contains(m));
    }

    /**
     * Ensure viewModel add meeting correctly
     */
    @Test
    public void viewModelAddMeetingTest() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setRoom(Room.Goomba);
        //move meeting to a free room on this interval
        mMeetingViewModel.addMeeting(m);
        mMeetingViewModel.fetchMeetings();
        assertTrue(mMeetingViewModel.getMeetingsLiveData().getValue().contains(m));
    }

    /**
     * Ensure same room and overlapping Start Time is Denied
     */
    @Test
    public void sameRoomOverlappingStartTimeIsDenied() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        //shift its time slot to 10:00 - 12:00
        m.setStart(m.getStart().plusMinutes(30));
        m.setEnd(m.getEnd().plusMinutes(30));
        // expect meeting to be denied
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertNotEquals("", bookingMessage);
    }

    /**
     * Ensure same room and overlapping End Time is Denied
     */
    @Test
    public void sameRoomOverlappingEndTimeIsDenied() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        //shift its time slot to 9:00 - 11:00
        m.setStart(m.getStart().minusMinutes(30));
        m.setEnd(m.getEnd().minusMinutes(30));
        // expect meeting to be denied
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertNotEquals("", bookingMessage);
    }

    /**
     * Ensure same room and non overlapping time is accepted
     */
    @Test
    public void sameRoomNonOverlappingTimeIsAccepted() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        //shift to a free time slot 17:30 - 19:30
        m.setStart(m.getStart().plusHours(8));
        m.setEnd(m.getEnd().plusHours(8));
        // expect meeting to be accepted
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("", bookingMessage);
    }

    /**
     * Ensure same room and non overlapping Date is accepted
     */
    @Test
    public void sameRoomNonOverlappingDateIsAccepted() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        //shift date to next day
        m.setStart(m.getStart().plusDays(1));
        m.setEnd(m.getEnd().plusDays(1));
        // expect meeting to be accepted
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("", bookingMessage);
    }

    /**
     * Ensure Book meeting in the past is Denied
     */
    @Test
    public void bookMeetingBeforeTodayIsDenied() {
        //cloning meeting at Room.Luigi with date is today from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        //shift date to next day
        m.setStart(m.getStart().minusDays(1));
        m.setEnd(m.getEnd().minusDays(1));
        // expect meeting to be denied
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertNotEquals("", bookingMessage);
    }

    /**
     * Ensure different room and overlapping time is accepted
     */
    @Test
    public void differentRoomOverlappingTimeIsAccepted() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setRoom(Room.Goomba);
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("", bookingMessage);
    }

    /**
     * Ensure meeting end after midnight is denied
     */
    @Test
    public void meetingEndAfterMidnightIsDenied() {
        LocalDateTime midnight = LocalDateTime.of(
                LocalDate.now().plusDays(1),
                LocalTime.of(0, 0));
        LocalDateTime midnightLatestStartTime = midnight
                .minusMinutes(MeetingDateTimeHelper.MEETING_MAX_DURATION);
        //set time slot to [midnight +1minute - MEETING_MAX_DURATION , midnight + 1minute]
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setStart(midnightLatestStartTime.plusMinutes(1));
        m.setEnd(midnight.plusMinutes(1));

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingEndAfterMidnight", bookingMessage);
    }

    /**
     * Ensure end time before start time is denied
     */
    @Test
    public void meetingEndBeforeStartDenied() {
        //cloning meeting at Room.Luigi from 9:30 to 11:30
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setRoom(Room.Goomba);
        //set time slot to 11:30 to 9:30
        m.setStart(m.getEnd());
        m.setEnd(DummyMeetingGenerator.DUMMY_MEETINGS.get(0).getStart());
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingEndsBeforeItStarts", bookingMessage);
    }

    /**
     * Ensure meeting duration above MEETING_MAX_DURATION is denied
     */
    @Test
    public void meetingExceedingMaxDurationIsDenied() {
        LocalDateTime start = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(12, 0));
        LocalDateTime end = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(12, 1)
                .plusMinutes(MeetingDateTimeHelper.MEETING_MAX_DURATION));

        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setRoom(Room.Goomba);
        m.setStart(start);
        m.setEnd(end);

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingMaxDuration", bookingMessage);
    }

    /**
     * Ensure meeting duration below MEETING_MIN_DURATION is denied
     */
    @Test
    public void meetingBelowMinDurationIsDenied() {
        LocalDateTime start = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(12, 30));
        LocalDateTime end = LocalDateTime.of(
                LocalDate.now(),
                LocalTime.of(12, 29)
                        .plusMinutes(MeetingDateTimeHelper.MEETING_MIN_DURATION));

        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setRoom(Room.Goomba);
        m.setStart(start);
        m.setEnd(end);

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingMinDuration", bookingMessage);
    }
}
