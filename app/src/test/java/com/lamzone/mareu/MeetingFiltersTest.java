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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class MeetingFiltersTest {

    private MeetingViewModel mMeetingViewModel;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        mMeetingViewModel = new MeetingViewModel();
    }

    @Test
    public void RoomFilterTest() {
        Meeting expectedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        boolean[] roomFilter = FilterTestHelper.generateRoomFilter(expectedMeeting.getRoom());
        mMeetingViewModel.setRoomFilter(roomFilter);
        mMeetingViewModel.applyFilters();

        assertEquals(1, mMeetingViewModel.getMeetingsLiveData().getValue().size());
        assertEquals(expectedMeeting, mMeetingViewModel.getMeetingsLiveData().getValue().get(0));
    }

    @Test
    public void FiltersWithStatTimeTest() {
        Meeting expectedMeeting0 = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        Meeting expectedMeeting5 = DummyMeetingGenerator.DUMMY_MEETINGS.get(5);

        mMeetingViewModel.setTimeFilter(mMeetingViewModel.formatTime(LocalTime.of(9,45)), null);
        mMeetingViewModel.applyFilters();

        assertEquals(2, mMeetingViewModel.getMeetingsLiveData().getValue().size());
        assertEquals(expectedMeeting0, mMeetingViewModel.getMeetingsLiveData().getValue().get(0));
        assertEquals(expectedMeeting5, mMeetingViewModel.getMeetingsLiveData().getValue().get(1));
    }
}
