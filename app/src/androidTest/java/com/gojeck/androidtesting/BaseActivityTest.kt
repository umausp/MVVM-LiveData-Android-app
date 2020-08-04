package com.gojeck.androidtesting

import androidx.appcompat.widget.Toolbar
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.gojeck.base.BaseActivity

abstract class BaseActivityTest<A : BaseActivity> : BaseAndroidTest() {
    abstract val activityClass: Class<A>

    private var activity: A? = null

    fun getActivity(): A = activity!!

    fun launchActivity(action: (A) -> Unit = {}): ActivityScenario<A> =
        ActivityScenario.launch(activityClass).onActivity {
            activity = it
            action(it)
        }

    private fun getToolbarNavigationContentDescription(id: Int) =
        getActivity().findViewById<Toolbar>(id).navigationContentDescription as String

    fun clickToolbarNavigationIcon(id: Int) {
        Espresso.onView(
            ViewMatchers.withContentDescription(
                getToolbarNavigationContentDescription(id)
            )
        ).perform(ViewActions.click())
    }
}
