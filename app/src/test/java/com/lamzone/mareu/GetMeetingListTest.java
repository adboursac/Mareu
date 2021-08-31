package com.lamzone.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.MeetingRepository;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.utils.TestPrinter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

/**
 * Get Meeting List feature unit test
 */
public class GetMeetingListTest {
    private MeetingRepository mMeetingRepository;
    private MeetingViewModel mMeetingViewModel;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        DI.generateNewApiService();
    }

    /**
     * fetch meetings from API and ensure it contains the full list
     */
    @Test
    public void repositoryFetchMeetingsTest() {
        mMeetingRepository = new MeetingRepository(DI.getMeetingApiService());
        List<Meeting> testedList = mMeetingRepository.fetchMeetings();
        List<Meeting> expectedList = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(testedList, containsInAnyOrder(expectedList.toArray()));
    }

    /**
     * fetch meetings from API and ensure that cached List contains full list
     */
    @Test
    public void repositoryGetCachedMeetingsTest() {
        mMeetingRepository = new MeetingRepository(DI.getMeetingApiService());
        mMeetingRepository.fetchMeetings();
        List<Meeting> testedList = mMeetingRepository.getCachedMeetings();
        List<Meeting> expectedList = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(testedList, containsInAnyOrder(expectedList.toArray()));
    }

    /**
     * fetch meetings from repository and ensure it contains the full list
     */
    @Test
    public void viewModelFetchMeetingsTest() {
        mMeetingViewModel = new MeetingViewModel();
        mMeetingViewModel.fetchMeetings();
        List<Meeting> testedList = mMeetingViewModel.getMeetingsLiveData().getValue();
        List<Meeting> expectedList = DummyMeetingGenerator.DUMMY_MEETINGS;
        assertThat(testedList, containsInAnyOrder(expectedList.toArray()));
    }
}
