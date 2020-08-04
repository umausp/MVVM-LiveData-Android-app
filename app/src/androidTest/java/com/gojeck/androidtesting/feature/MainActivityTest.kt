package com.gojeck.androidtesting.feature

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.gojeck.R
import com.gojeck.androidtesting.BaseActivityTest
import com.gojeck.feature.model.TrendingRepositoryMainViewModel
import com.gojeck.feature.view.MainActivity
import com.gojeck.utils.ctx
import org.junit.Test

class MainActivityTest : BaseActivityTest<MainActivity>() {
    override val activityClass: Class<MainActivity>
        get() = MainActivity::class.java

    @Test
    fun open_activity_show_trending_title() {
        launchActivity()
        clickSortByName()
    }

    fun clickSortByName() {
        Espresso.openActionBarOverflowOrOptionsMenu(ctx)
        performClickByText(R.string.sort_by_name)
    }

    fun clickSortByStarts() {
        Espresso.openActionBarOverflowOrOptionsMenu(ctx)
        performClickByText(R.string.sort_by_stars)
    }

    fun hasTrendingTextShown() {
        assertTextDisplayed(R.string.trending_string)
    }

    fun scroll(index: Int) {
        scrollRecyclerView(R.id.rv_activity_main_trending_repository, index)
    }

    fun swipeDown() {
        Espresso.onView(withId(R.id.swipe_container)).perform(ViewActions.swipeDown())
    }

    fun isItemDisplayed(task: TrendingRepositoryMainViewModel) {
        assertTextDisplayed(task.name)
    }

    fun clickTaskItem(task: TrendingRepositoryMainViewModel) {
        performClickByText(task.name)
    }

    fun isItemNotDisplayed(task: TrendingRepositoryMainViewModel) {
        assertTextNotDisplayed(task.name)
    }


}