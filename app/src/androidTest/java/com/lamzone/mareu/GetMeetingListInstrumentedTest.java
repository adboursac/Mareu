package com.lamzone.mareu;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.lamzone.mareu.data.service.DummyMeetingGenerator.DUMMY_MEETINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;

/**
 * Get Meeting List feature unit test
 */
public class GetMeetingListInstrumentedTest {

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
     * Ensure that non filtered meeting recyclerview is displaying right amount of meetings
     */
    @Test
    public void meetingListDisplaysRightAmount() {
        int expectedMeetingAmount = DUMMY_MEETINGS.size();
        onView(ViewMatchers.withId(R.id.recyclerView))
                .check(matches(hasChildCount(expectedMeetingAmount)));
    }

    /**
     * Ensure that non filtered meeting recyclerview is displaying meetings correctly
     */
    @Test
    public void meetingListDisplaysMeetingsCorrectly() {
        int expectedMeetingAmount = DUMMY_MEETINGS.size();
        for (int i = 0; i < expectedMeetingAmount; i++) {
            String expectedItemTitle = DUMMY_MEETINGS.get(i).shortDescription(mActivity.getResources());
            onView(withId(R.id.recyclerView))
                    .perform(RecyclerViewActions.actionOnItem(
                            hasDescendant(withText(expectedItemTitle)),
                            click()));
        }
    }
}