package com.lamzone.mareu;

import android.content.res.Resources;

import androidx.appcompat.widget.MenuPopupWindow;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.MeetingDateTimeHelper;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.ui.MainActivity;
import com.lamzone.mareu.utils.GetElementFromMatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;

public class MeetingFiltersInstrumentedTest {

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        DI.generateNewApiService();
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }

    /**
     * Ensure filter by room returns required rooms only
     */
    @Test
    public void roomFilterTest() {
        Meeting expectedMeeting = DummyMeetingGenerator.DUMMY_MEETINGS.get(0);
        String expectedItemTitle = expectedMeeting.shortDescription(mActivity.getResources());
        // Perform click on filter drop menu
        onView(withId(R.id.meeting_filter_drop_menu)).perform(click());
        // Perform click on filter by Room
        onView(GetElementFromMatch.atPosition(
                allOf(withId(R.id.title)),
                1)
        ).perform(click());
        // click on Luigi item
        onView(allOf(withId(R.id.recyclerView), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(4, click()));
        // click apply filter
        onView(withId(R.id.applyButton)).perform(click());

        // filter result to only 1 result
        onView(ViewMatchers.withId(R.id.recyclerView))
                .check(matches(hasChildCount(1)));
        // filter result to right meeting
        onView(GetElementFromMatch.atPosition(
                allOf(withId(R.id.item_title)), 0))
                .check(matches(withText(expectedItemTitle)));
    }

    /**
     * Ensure filter with filled start time and null end time returns meetings
     * with range [start time, end time[
     */
    @Test
    public void dateFilterTest() throws InterruptedException {
        Resources resources = mActivity.getResources();
        List<Meeting> expectedList = new ArrayList<>(Arrays.asList(
                DummyMeetingGenerator.DUMMY_MEETINGS.get(6),
                DummyMeetingGenerator.DUMMY_MEETINGS.get(7)
        ));
        LocalDate filterDate = LocalDate.now().plusDays(1);
        // Perform click on filter drop menu
        onView(withId(R.id.meeting_filter_drop_menu)).perform(click());
        // Perform click on filter by date
        onView(GetElementFromMatch.atPosition(
                allOf(withId(R.id.title)),
                0)
        ).perform(click());
        //set date filter
        TextInputEditText dateInput = mActivity.findViewById(R.id.dateInput);
        mActivity.runOnUiThread(() -> {
            dateInput.setText(MeetingDateTimeHelper
                    .dateToString(expectedList.get(0).getStart().toLocalDate()));
        });
        // click apply filter
        onView(withId(R.id.applyButton)).perform(click());

        //check if Meeting Count is correct
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(2));
        //check if meeting at position 0 is correct
        onView(GetElementFromMatch.atPosition(
                allOf(withId(R.id.item_title)), 0))
                .check(matches(withText(expectedList.get(0).shortDescription(resources))));
        //check if meeting at position 1 is correct
        onView(GetElementFromMatch.atPosition(
                allOf(withId(R.id.item_title)), 1))
                .check(matches(withText(expectedList.get(1).shortDescription(resources))));
    }
}
