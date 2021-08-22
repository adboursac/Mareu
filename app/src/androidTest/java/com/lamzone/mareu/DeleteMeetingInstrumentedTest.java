package com.lamzone.mareu;

import com.lamzone.mareu.ui.MainActivity;
import com.lamzone.mareu.utils.DeleteButtonAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static com.lamzone.mareu.data.service.DummyMeetingGenerator.DUMMY_MEETINGS;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;


public class DeleteMeetingInstrumentedTest {

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
     * When we delete an item, the item is no more shown
     */
    @Test
    public void deleteMeetingTest() {
        int initialMeetingCount = DUMMY_MEETINGS.size();
        // item number should be equal to DUMMY_MEETINGS_COUNT
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(initialMeetingCount));
        // perform a click on a delete icon
        onView(ViewMatchers.withId(R.id.recyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteButtonAction()));
        // should one meeting less
        onView(ViewMatchers.withId(R.id.recyclerView)).check(withItemCount(initialMeetingCount - 1));
    }
}
