package pl.edu.pwr.drozd.lab1;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.StringStartsWith.startsWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final String validMass = "100";
    private final String validHeight = "195";
    private final String invalidMass = "5";
    private final String invalidHeight = "400";

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void BMIInvisibleUntilClick() {
        onView(withId(R.id.tv_bmi_result)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tv_bmi_opinion)).check(matches(not(isDisplayed())));
        onView(withId(R.id.et_mass)).perform(typeText(validMass));
        onView(withId(R.id.et_height)).perform(typeText(validHeight));
        onView(withId(R.id.btn_count)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.tv_bmi_result)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_bmi_opinion)).check(matches(isDisplayed()));
        onView(withId(R.id.et_mass)).perform(clearText(), typeText(invalidMass));
        onView(withId(R.id.et_height)).perform(clearText(), typeText(invalidHeight));
    }

    @Test
    public void BMIInvisibleAfterWrongInput() {
        onView(withId(R.id.et_mass)).perform(typeText(validMass));
        onView(withId(R.id.et_height)).perform(typeText(validHeight));
        onView(withId(R.id.btn_count)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.tv_bmi_result)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_bmi_opinion)).check(matches(isDisplayed()));
        onView(withId(R.id.et_mass)).perform(clearText(), typeText(invalidMass));
        onView(withId(R.id.et_height)).perform(clearText(), typeText(invalidHeight));
        onView(withId(R.id.btn_count)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.tv_bmi_result)).check(matches(not(isDisplayed())));
        onView(withId(R.id.tv_bmi_opinion)).check(matches(not(isDisplayed())));
    }
    @Test
    public void displaysValidBMI() {
        onView(withId(R.id.et_mass))
                .perform(typeText(validMass));
        onView(withId(R.id.et_height))
                .perform(typeText(validHeight));
        onView(withId(R.id.btn_count)).perform(click());

        onView(withId(R.id.tv_bmi_result)).check(matches(withText("26.3"))).check(matches(isDisplayed()));
        onView(withId(R.id.tv_bmi_opinion)).check(matches(withText("Overweight"))).check(matches(isDisplayed()));
    }

    @Test
    public void displaysToastWhenWrongInput() {
        onView(withId(R.id.btn_count)).perform(click());
        onView(withText(startsWith("Wrong"))).
                inRoot(withDecorView(
                        not(is(mActivityRule.getActivity().
                                getWindow().getDecorView())))).
                check(matches(isDisplayed()));
    }

    @Test
    public void spinnerChangesUnits()   {
        onView(allOf(withId(R.id.mode_spinner), isDisplayed())).perform(click());
        onView(allOf(withText("Kg/Cm"), isDisplayed()));
        onView(allOf(withText("Lbs/In"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.tv_mass_mode), isDisplayed())).check(matches(withText("[Lbs]")));
        onView(allOf(withId(R.id.tv_height_mode), isDisplayed())).check(matches(withText("[In]")));
        onView(allOf(withId(R.id.mode_spinner), isDisplayed())).perform(click());
        onView(allOf(withText("Lbs/In"), isDisplayed()));
        onView(allOf(withText("Kg/Cm"), isDisplayed())).perform(click());
        onView(allOf(withId(R.id.tv_mass_mode), isDisplayed())).check(matches(withText("[Kg]")));
        onView(allOf(withId(R.id.tv_height_mode), isDisplayed())).check(matches(withText("[Cm]")));
    }

}
