package com.github.cesar1287.flexchallenge

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

class TimerViewTest {

    @Mock
    lateinit var timerView: TimerView

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun `verify if initial value is setup`() {
        Mockito.`when`(timerView.setup()).then {
            val initialValue = 0.0001f * 360
            assertEquals(initialValue, timerView.mCircleSweepAngle)
        }
    }

    @Test
    fun `verify if the timer start`() {
        Mockito.`when`(timerView.start(5L)).then {
            assertEquals(timerView.mTimerAnimator?.isRunning, true)
        }
    }

    @Test
    fun `verify if the timer is canceled`() {
        Mockito.`when`(timerView.stop()).then {
            assertEquals(timerView.mTimerAnimator, null)
        }
    }

    @Test
    fun `verify if the timer is setup correctly`() {
        val seconds = 10L
        Mockito.`when`(timerView.setupTimerAnimation(seconds)).then {
            assertEquals(timerView.mTimerAnimator?.duration, seconds)
        }
    }
}