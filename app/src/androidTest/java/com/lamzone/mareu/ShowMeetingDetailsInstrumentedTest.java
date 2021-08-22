package com.lamzone.mareu;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.ui.MainActivity;
import com.lamzone.mareu.ui.MeetingDetailsFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ShowMeetingDetailsInstrumentedTest {

    private MainActivity mActivity;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
    }


    @Test
    public void testNavigationToMeetingDetails() {
        // Create a TestNavHostController
        TestNavHostController navController = new TestNavHostController(
                ApplicationProvider.getApplicationContext());
        // Create a graphical FragmentScenario for the TitleScreen
        FragmentScenario<MeetingDetailsFragment> detailScenario = FragmentScenario.launchInContainer(MeetingDetailsFragment.class);
        detailScenario.onFragment(fragment -> {
                    // Set the graph on the TestNavHostController
                    navController.setGraph(R.navigation.nav_graph);
                    // Make the NavController available via the findNavController() APIs
                    Navigation.setViewNavController(fragment.requireView(), navController);
                }
        );
        // Verify that performing a click changes the NavController’s state
        onView(ViewMatchers.withId(R.id.addButton)).perform(ViewActions.click());
        assertEquals(R.id.meetingDetailsFragment, navController.getCurrentDestination().getId());
    }


    /**
     * When click on item, detail screen is displayed
     * and showing the right meeting details
     */
    @Test
    public void clickMeetingItemShowDetailsViewAndMeetingTitle() {
        String expectedTitle = DummyMeetingGenerator.DUMMY_MEETINGS.get(0).getTitle();
        // Perform click on the element at position 0
        onView(allOf(withId(R.id.recyclerView), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        /*
        // Check if the detail View has been displayed
        onView(withId(R.id.meetingDetailsFragment)).check(matches(isDisplayed()));
         */
        // Check if the detail View is showing the right name
        onView(withId(R.id.titleInput))
                .check(matches(withText(expectedTitle)));
    }
}
