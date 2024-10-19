package com.kosmas.tecprin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kosmas.tecprin.ui.details.DetailsBottomSheetFragment
import com.kosmas.tecprin.ui.main.MainActivity
import com.kosmas.tecprin.ui.main.ProductAdapter
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyAppTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun tapRecyclerViewItem_opensBottomSheetDialogFragment() {
        // 1. Wait for the RecyclerView to be displayed
        onView(withId(R.id.productsRecyclerView)).check(matches(isDisplayed()))

        runBlocking {
            delay(2000)
        }

        // 2. Perform a click on the desired item (e.g., the first item)
        onView(withId(R.id.productsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ViewHolder>(
                    0,
                    click()
                )
            )

        // 3. Assert that the BottomSheetDialogFragment is displayed
        val activityScenario = activityScenarioRule.scenario
        activityScenario.onActivity { activity ->
            val fragment =
                activity.supportFragmentManager.findFragmentByTag(DetailsBottomSheetFragment.TAG)
            assertTrue(fragment is DetailsBottomSheetFragment && fragment.isVisible)
        }
    }

}
