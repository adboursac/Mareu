package com.lamzone.mareu;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.lamzone.mareu.data.meeting.MeetingTimeHelper;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.utils.FilterTestHelper;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

/**
 * Meeting filter feature unit test
 */
public class MeetingFiltersTest {

    private MeetingViewModel mMeetingViewModel;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        mMeetingViewModel = new MeetingViewModel();
    }

    /**
     * Ensure filter by room returns required rooms only
     */
    @Test
    public void roomFilterTest() {
        Meeting expectedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        boolean[] roomFilter = FilterTestHelper.generateRoomFilter(expectedMeeting.getRoom());
        mMeetingViewModel.setRoomFilter(roomFilter);
        mMeetingViewModel.applyFilters();

        assertEquals(1, mMeetingViewModel.getMeetingsLiveData().getValue().size());
        assertEquals(expectedMeeting, mMeetingViewModel.getMeetingsLiveData().getValue().get(0));
    }

    /**
     * Ensure filter with filled start time and null end time returns meetings
     * with range [start time, end time[
     */
    @Test
    public void filtersWithStartTimeTest() {
        List<Meeting> expectedList = new ArrayList<>(Arrays.asList(
                DummyMeetingGenerator.DUMMY_MEETINGS.get(2),
                DummyMeetingGenerator.DUMMY_MEETINGS.get(3)
        ));
        mMeetingViewModel.setTimeFilter(MeetingTimeHelper.toString(LocalTime.of(13, 0)), null);
        mMeetingViewModel.applyFilters();
        List<Meeting> testedList = mMeetingViewModel.getMeetingsLiveData().getValue();

        assertEquals(2, testedList.size());
        assertThat(testedList, containsInAnyOrder(expectedList.toArray()));
    }

    @Test

    /**
     * Ensure filter with null start time and filled end time returns meetings
     * with end time before or equal to parameter time
     */
    public void filterWithEndTimeTest() {
        List<Meeting> expectedList = new ArrayList<>(Arrays.asList(
                DummyMeetingGenerator.DUMMY_MEETINGS.get(0),
                DummyMeetingGenerator.DUMMY_MEETINGS.get(1),
                DummyMeetingGenerator.DUMMY_MEETINGS.get(5)
        ));
        mMeetingViewModel.setTimeFilter(null, MeetingTimeHelper.toString(LocalTime.of(13, 0)));
        mMeetingViewModel.applyFilters();
        List<Meeting> testedList = mMeetingViewModel.getMeetingsLiveData().getValue();

        assertEquals(3, testedList.size());
        assertThat(testedList, containsInAnyOrder(expectedList.toArray()));
    }

    /**
     * Ensure filter with both filled start time and end time returns meetings
     * according to inclusive range [start time, end time]
     */
    @Test
    public void filterWithTimeRangeTest() {
        List<Meeting> expectedList = new ArrayList<>(Arrays.asList(
                DummyMeetingGenerator.DUMMY_MEETINGS.get(0),
                DummyMeetingGenerator.DUMMY_MEETINGS.get(5)
        ));
        mMeetingViewModel.setTimeFilter(
                MeetingTimeHelper.toString(LocalTime.of(9, 30)),
                MeetingTimeHelper.toString(LocalTime.of(11, 30)));
        mMeetingViewModel.applyFilters();
        List<Meeting> testedList = mMeetingViewModel.getMeetingsLiveData().getValue();

        assertEquals(2, testedList.size());
        assertThat(testedList, containsInAnyOrder(expectedList.toArray()));
    }
}
