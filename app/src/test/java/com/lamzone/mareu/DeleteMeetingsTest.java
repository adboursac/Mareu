package com.lamzone.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lamzone.mareu.data.meeting.MeetingRepository;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class DeleteMeetingsTest {
    private MeetingRepository mMeetingRepository;
    private MeetingViewModel mMeetingViewModel;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        mMeetingRepository = new MeetingRepository();
        mMeetingViewModel = new MeetingViewModel();
    }

    /**
     * delete Meeting and ensure that it doesn't contains this Meeting anymore
     */
    @Test
    public void repositoryDeleteMeetingTest() {
        Meeting MeetingToDelete = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        mMeetingRepository.deleteMeeting(MeetingToDelete);
        assertFalse(mMeetingRepository.fetchMeetings().contains(MeetingToDelete));
    }


    /**
     * delete Meeting and ensure that it doesn't contains this Meeting anymore
     */
    @Test
    public void viewModelDeleteMeetingTest() {
        Meeting MeetingToDelete = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        mMeetingViewModel.deleteMeeting(MeetingToDelete);
        assertFalse(mMeetingViewModel.getMeetingsLiveData().getValue().contains(MeetingToDelete));
    }
}
