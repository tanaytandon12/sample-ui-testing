package com.example.sampleuitesting

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.CoreMatchers

fun verifyText(stringResId: Int, textTvId: Int) {
    val text =
        ApplicationProvider.getApplicationContext<Application>().applicationContext.getString(
            stringResId
        )
    Espresso.onView(ViewMatchers.withId(textTvId)).check(ViewAssertions.matches(withText(text)))
}

fun verifyText(text: String, textTvId: Int) {
    Espresso.onView(ViewMatchers.withId(textTvId))
        .check(ViewAssertions.matches(ViewMatchers.withText(text)))
}

fun isViewEnabled(id: Int) {
    Espresso.onView(ViewMatchers.withId(id)).check(ViewAssertions.matches(ViewMatchers.isEnabled()))
}

fun isViewDisabled(id: Int) {
    Espresso.onView(ViewMatchers.withId(id))
        .check(ViewAssertions.matches(CoreMatchers.not(ViewMatchers.isEnabled())))
}


fun isViewVisible(id: Int): ViewInteraction {
    return Espresso.onView(ViewMatchers.withId(id))
        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
}

fun isViewNotVisible(id: Int): ViewInteraction {
    return Espresso.onView(ViewMatchers.withId(id))
        .check(ViewAssertions.matches((CoreMatchers.not(ViewMatchers.isDisplayed()))))
}


fun performClick(resId: Int) {
    Espresso.onView(ViewMatchers.withId(resId)).perform(ViewActions.click())
}
