//package com.example.berlingo.settings
//
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.onNodeWithTag
//import androidx.navigation.testing.TestNavHostController
//import io.mockk.mockk
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test

// TODO Fix error: io.mockk.MockKException: no answer found for: SettingsRepository(#1).getDarkModeState()
//class SettingsScreenTest {
//    @get:Rule
//    val rule = createComposeRule()
//
//    private lateinit var settingsViewModel: SettingsViewModel
//    private val mockSettingsRepository by lazy { mockk<SettingsRepository>() }
//    private lateinit var navController: TestNavHostController
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Before
//    fun setUp() {
//        rule.setContent {
//            runTest { mockSettingsRepository.darkModeState }
//            settingsViewModel = SettingsViewModel(mockSettingsRepository)
//            navController = TestNavHostController(LocalContext.current)
//        }
//    }
//
//
//    @Test
//    fun testSettingsScreen() {
//        rule.setContent {
//            SettingsScreen(
//                settingsViewModel = settingsViewModel,
//                navController = navController
//            )
//        }
//        rule.apply {
//            onNodeWithTag("SettingsScreen(): Surface(): Column").assertExists()
//        }
//    }
//
//}