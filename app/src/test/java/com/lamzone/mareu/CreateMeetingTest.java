package com.lamzone.mareu;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.MeetingRepository;
import com.lamzone.mareu.data.meeting.MeetingTimeHelper;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.utils.MockedResources;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setStartTime(LocalTime.of(1,0));
        m.setEndTime(
                m.getStartTime().plusMinutes(
                        MeetingTimeHelper.MEETING_MIN_DURATION)
        );
        mMeetingRepository.addMeeting(m);
        assertTrue(mMeetingRepository.fetchMeetings().contains(m));
    }

    /**
     * Ensure viewModel add meeting correctly
     */
    @Test
    public void viewModelAddMeetingTest() {
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setStartTime(LocalTime.of(1,0));
        m.setEndTime(
                m.getStartTime().plusMinutes(
                        MeetingTimeHelper.MEETING_MIN_DURATION)
        );
        mMeetingViewModel.addMeeting(m);
        mMeetingViewModel.fetchMeetings();
        assertTrue(mMeetingViewModel.getMeetingsLiveData().getValue().contains(m));
    }

    /**
     * Ensure same room and overlapping Start Time is Denied
     */
    @Test
    public void sameRoomOverlappingStartTimeIsDenied() {
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setEndTime(m.getEndTime().plusMinutes(1));
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertNotEquals("", bookingMessage);
    }

    /**
     * Ensure same room and overlapping End Time is Denied
     */
    @Test
    public void sameRoomOverlappingEndTimeIsDenied() {
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setStartTime(m.getEndTime().minusMinutes(1));
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertNotEquals("", bookingMessage);
    }

    /**
     * Ensure same room and non overlapping time is accepted
     */
    @Test
    public void sameRoomNonOverlappingTimeIsAccepted() {
        Meeting m = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).clone();
        m.setStartTime(m.getEndTime());
        m.setEndTime(m.getEndTime().plusMinutes(MeetingTimeHelper.MEETING_MIN_DURATION));
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("", bookingMessage);
    }

    /**
     * Ensure different room and overlapping time is accepted
     */
    @Test
    public void differentRoomOverlappingTimeIsAccepted() {
        Meeting bookedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        Meeting m = bookedMeeting.clone();
        m.setRoom(Room.Kirby);
        
        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("", bookingMessage);
    }

    /**
     * Ensure meeting end after midnight is denied
     */
    @Test
    public void meetingEndAfterMidnightIsDenied() {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime midnightLatestStartTime = midnight
                .minusMinutes(MeetingTimeHelper.MEETING_MAX_DURATION);

        Meeting bookedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        Meeting m = bookedMeeting.clone();
        m.setStartTime(midnightLatestStartTime.plusMinutes(1));
        m.setEndTime(midnight.plusMinutes(1));

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingEndAfterMidnight", bookingMessage);
    }

    /**
     * Ensure end time before start time is denied
     */
    @Test
    public void meetingEndBeforeStartDenied() {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime midnightLatestStartTime = midnight
                .minusMinutes(MeetingTimeHelper.MEETING_MAX_DURATION);

        Meeting bookedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        Meeting m = bookedMeeting.clone();
        m.setStartTime(midnightLatestStartTime);
        m.setEndTime(midnight.plusMinutes(1));

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingEndsBeforeItStarts", bookingMessage);
    }

    /**
     * Ensure meeting duration above MEETING_MAX_DURATION is denied
     */
    @Test
    public void meetingExceedingMaxDurationIsDenied() {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime midnightLatestStartTime = midnight
                .minusMinutes(MeetingTimeHelper.MEETING_MAX_DURATION);

        Meeting bookedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        Meeting m = bookedMeeting.clone();
        m.setStartTime(midnightLatestStartTime.minusMinutes(1));
        m.setEndTime(midnight);

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingMaxDuration", bookingMessage);
    }

    /**
     * Ensure meeting duration below MEETING_MIN_DURATION is denied
     */
    @Test
    public void meetingBelowMinDurationIsDenied() {
        LocalTime midnight = LocalTime.of(0, 0);
        LocalTime smallestEndTime = midnight
                .plusMinutes(MeetingTimeHelper.MEETING_MIN_DURATION);

        Meeting bookedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        Meeting m = bookedMeeting.clone();
        m.setStartTime(midnight);
        m.setEndTime(smallestEndTime.minusMinutes(1));

        String bookingMessage = mMeetingViewModel.checkMeetingValidity(m, mMockedResources);
        assertEquals("id: invalidMeetingMinDuration", bookingMessage);
    }
}
