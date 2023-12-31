package com.example.berlingo.main

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @Test
    fun launchMainActivity() {
        // Launch the Main Activity
        ActivityScenario.launch(MainActivity::class.java)

        // Now you can add Espresso tests to interact with your UI
    }
}