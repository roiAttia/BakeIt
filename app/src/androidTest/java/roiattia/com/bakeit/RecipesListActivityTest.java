package roiattia.com.bakeit;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import roiattia.com.bakeit.ui.RecipeActivity;
import roiattia.com.bakeit.ui.RecipesListActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class RecipesListActivityTest {

    @Rule
    public ActivityTestRule<RecipesListActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipesListActivity.class);

    private IdlingResource mIdlingResource;
    private IdlingRegistry mIdlingRegistry = IdlingRegistry.getInstance();

    // Registers IdlingResource before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        mIdlingRegistry.register(mIdlingResource);
    }

    @Test
    public void clickRecipeItem_OpensRecipeActivity() {

        // Click item at position 1
        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Checks that the Ingredients recyclerview is shown
        onView(withId(R.id.rv_ingredients))
                .check(matches(isDisplayed()));

    }

    // Unregisters IdlingResource
    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            mIdlingRegistry.unregister(mIdlingResource);
        }
    }
}
