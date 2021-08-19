package com.lamzone.mareu;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.MeetingRepository;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.data.service.MeetingApiService;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Meeting Repository
 */
public class MeetingRepositoryTest {
    
    private MeetingApiService service;
    private MeetingRepository mMeetingRepository;

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        mMeetingRepository = new MeetingRepository();
    }

    /**
     * fetch meeting from API and ensure it contains the full list
     */
    @Test
    public void fetchMeetingsTest() {
        List<Meeting> meetingsActual = mMeetingRepository.fetchMeetings();
        List<Meeting> meetingsExpected = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(meetingsActual, containsInAnyOrder(meetingsExpected.toArray()));
    }

    /**
     * fetch meeting from API and ensure that cached List contains full list
     */
    @Test
    public void getCachedMeetingsTest() {
        mMeetingRepository.fetchMeetings();
        List<Meeting> meetingsActual = mMeetingRepository.getCachedMeetings();
        List<Meeting> meetingsExpected = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(meetingsActual, containsInAnyOrder(meetingsExpected.toArray()));
    }

    /**
     * add Meeting from Meeting list and ensure it doesn't contains this Meeting anymore
     */
    @Test
    public void addMeetingWithSuccess() {
        Meeting meetingToAdd = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        mMeetingRepository.addMeeting(meetingToAdd);
        assertTrue(mMeetingRepository.fetchMeetings().contains(meetingToAdd));
    }

    /**
     * delete Meeting from Meeting list and ensure it doesn't contains this Meeting anymore
     */
    @Test
    public void deleteMeetingWithSuccess() {
        Meeting MeetingToDelete = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        mMeetingRepository.deleteMeeting(MeetingToDelete);
        assertFalse(mMeetingRepository.fetchMeetings().contains(MeetingToDelete));
    }
}