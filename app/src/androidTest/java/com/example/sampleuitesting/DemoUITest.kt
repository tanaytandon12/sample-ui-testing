package com.example.sampleuitesting

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import java.util.UUID

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DemoUITest {

    lateinit var mScenario: FragmentScenario<DemoFragment>

    private val mMutableStateFlow: MutableStateFlow<DemoDataStatus> by lazy {
        MutableStateFlow(DemoDataStatus.Loading)
    }

    private val mViewModel: DemoViewModel by lazy {
        object : DemoViewModel() {
            override fun fetchInfo(id: Long): Flow<DemoDataStatus> {
                return mMutableStateFlow
            }

        }
    }

    @Before
    fun setup() {
        DemoViewModel.Factory.INSTANCE = mViewModel
        mScenario =
            launchFragmentInContainer(themeResId = R.style.Theme_SampleUITesting)
    }

    @Test
    fun verifyLoadingState() {
        mScenario.moveToState(Lifecycle.State.STARTED)
        mMutableStateFlow.value = DemoDataStatus.Loading
        isViewVisible(R.id.pbDemo)
        isViewNotVisible(R.id.tvError)
        isViewNotVisible(R.id.tvDescription)
        isViewNotVisible(R.id.tvTitle)
        isViewNotVisible(R.id.btnRetry)
        mScenario.moveToState(Lifecycle.State.DESTROYED)
    }

    @Test
    fun verifySuccessState() {
        mScenario.moveToState(Lifecycle.State.STARTED)
        val demo = DemoData(UUID.randomUUID().toString(), UUID.randomUUID().toString())
        mMutableStateFlow.value = DemoDataStatus.Success(demo)
        isViewNotVisible(R.id.pbDemo)
        isViewVisible(R.id.tvDescription)
        isViewVisible(R.id.tvTitle)
        isViewNotVisible(R.id.tvError)
        isViewNotVisible(R.id.btnRetry)
        verifyText(demo.title, R.id.tvTitle)
        verifyText(demo.description, R.id.tvDescription)
        mScenario.moveToState(Lifecycle.State.DESTROYED)
    }
}