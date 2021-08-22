package com.lamzone.mareu;

import com.lamzone.mareu.databinding.FragmentMeetingListBinding;
import com.lamzone.mareu.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static com.lamzone.mareu.data.service.DummyMeetingGenerator.DUMMY_MEETINGS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
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
                .check(matches(hasMinimumChildCount(expectedMeetingAmount)));
    }
}