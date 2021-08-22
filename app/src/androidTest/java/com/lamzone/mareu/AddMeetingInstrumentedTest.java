package com.lamzone.mareu;

import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.data.service.DummyMeetingGenerator;
import com.lamzone.mareu.ui.MainActivity;
import com.lamzone.mareu.utils.GetElementFromMatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.rule.ActivityTestRule;


public class AddMeetingInstrumentedTest {

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
     * add
     */
    @Test
    public void addMeetingTest() throws InterruptedException {
        String testTitle = "TestTitle";
        String startTimeText = "18:00";
        String endTimeText = "19:00";
        String expectedItemTitle = testTitle + " • " + startTimeText + " • Luigi" ;
        int initialMeetingCount = DummyMeetingGenerator.DUMMY_MEETINGS.size();
        // Perform click on the element at position 0
        onView(withId(R.id.addButton)).perform(click());
        //enter meeting title
        onView(withId(R.id.titleInput)).perform(typeText(testTitle));
        //press back button
        onView(isRoot()).perform(pressBack());
        //pick a room
        onView(withId(R.id.roomLayout)).perform(click());
        onData(instanceOf(Room.class))
                .inRoot(RootMatchers.withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .atPosition(4) //Room: Luigi
                .perform(click());
        //set time slot
        TextInputEditText startTimeInput = mActivity.findViewById(R.id.startTimeInput);
        TextInputEditText endTimeInput = mActivity.findViewById(R.id.endTimeInput);
        mActivity.runOnUiThread(() -> {
            startTimeInput.setText(startTimeText);
            endTimeInput.setText(endTimeText);
        });
        //Enter two member
        onView(withId(R.id.memberMailInput)).perform(typeText("email1@lamzone.com"));
        onView(GetElementFromMatch.atPosition(allOf(withId(R.id.text_input_end_icon)),4)).perform(click());
        onView(withId(R.id.memberMailInput)).perform(typeText("email2@lamzone.com"));
        onView(GetElementFromMatch.atPosition(allOf(withId(R.id.text_input_end_icon)),4)).perform(click());
        Espresso.closeSoftKeyboard();
        // validate
        onView(withId(R.id.addButton)).perform(click());
        onView(GetElementFromMatch.atPosition(
                allOf(withId(R.id.item_title)),
                initialMeetingCount)
        ).check(matches(withText(expectedItemTitle)));
    }
}
