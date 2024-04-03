package com.example.berlingo.maps

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.berlingo.journeys.JourneysViewModel
import com.example.berlingo.journeys.legs.stops.StopsViewModel
import com.example.berlingo.journeys.legs.stops.network.StopsRepository
import com.example.berlingo.journeys.network.JourneysRepository
import com.example.berlingo.map.MapsRepository
import com.example.berlingo.map.MapsScreen
import com.example.berlingo.map.MapsViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MapsScreenTest {
    @get:Rule
    val rule = createComposeRule()

    private lateinit var journeysViewModel: JourneysViewModel
    private lateinit var stopsViewModel: StopsViewModel
    private lateinit var mapsViewModel: MapsViewModel
    private val mockJourneysRepository by lazy { mockk<JourneysRepository>() }
    private val mockStopsRepository by lazy { mockk<StopsRepository>() }
    private val mockMapsRepository by lazy { mockk<MapsRepository>() }


    @Before
    fun setUp() {
        journeysViewModel = JourneysViewModel(mockJourneysRepository)
        stopsViewModel = StopsViewModel(mockStopsRepository)
        mapsViewModel = MapsViewModel(mockMapsRepository)
    }

//    @Test
//    fun testMapComponent() {
//        rule.setContent {
//            MapComponent(
//                mapsState = mapsViewModel.state.collectAsState().value,
//                mapsEvent = mapsViewModel::handleEvent,
//                journeysState = journeysViewModel.state.collectAsState().value,
//                journeysEvent = journeysViewModel::handleEvent,
//                stopsState = stopsViewModel.state.collectAsState().value,
//                stopsEvent = stopsViewModel::handleEvent,
//            )
//        }
//        rule.apply{
////            onNodeWithTag("JourneysColumn(): Box()").assertExists()
//            onNodeWithTag("JourneysColumn(): Box(): Column()").assertExists()
//            onNodeWithTag("JourneysColumn(): DisplayJourneys(): Box()").assertExists()
//        }
//    }

    @Test
    fun testStopsColumn() {
        rule.setContent {
            MapsScreen(
                mapsState = mapsViewModel.state.collectAsState().value,
                mapsEvent = mapsViewModel::handleEvent,
                journeysState = journeysViewModel.state.collectAsState().value,
                journeysEvent = journeysViewModel::handleEvent,
                stopsState = stopsViewModel.state.collectAsState().value,
                stopsEvent = stopsViewModel::handleEvent,
            )
        }
        rule.apply{
            onNodeWithTag("StopsColumn(): Column()").assertExists()
            onNodeWithTag("StopsColumn(): OriginTextField(): TextField()").assertExists().performTextInput("Lichterfelde")
//            onNodeWithTag("StopsColumn(): OriginTrailingIcons(): Row()").assertExists()
            onNodeWithTag("StopsColumn(): OriginTrailingIcons(): Row(): Icon() - Clear TextField").assertExists()
            onNodeWithTag("StopsColumn(): OriginTrailingIcons(): Row(): Icon() - Get Current Location").assertExists()
            onNodeWithTag("StopsColumn(): DestinationTextField(): TextField()").assertExists().performTextInput("Hauptbahnhof")
//            onNodeWithTag("StopsColumn(): DestinationTrailingIcons(): Row()").assertExists()
            onNodeWithTag("StopsColumn(): DestinationTrailingIcons(): Row(): Icon()").assertExists()
            onNodeWithTag("StopsColumn(): SearchJourneysButton(): Box()").assertExists()
            onNodeWithTag("StopsColumn(): SearchJourneysButton(): Box(): Button()").assertExists().performClick()
//            onNodeWithTag("StopsColumn(): SearchJourneysButton(): Box(): Button(): Icon()").assertExists()
//            runBlocking { delay(2000) }
//            onNodeWithTag("StopsColumn(): DisplayStops(): LazyColumn()").assertExists()
//            onNodeWithTag("StopsColumn(): DisplayStops(): LazyColumn(): Row()").assertExists()
//            onNodeWithTag("StopsColumn(): DisplayStops(): LazyColumn(): Row(): Text()").assertExists()
//                .performClick()
        }
    }

//    @Test
//    fun testMapsJourneysColumn() {
//        rule.setContent {
//            MapsScreen(
//                mapsState = mapsViewModel.state.collectAsState().value,
//                mapsEvent = mapsViewModel::handleEvent,
//                journeysState = journeysViewModel.state.collectAsState().value,
//                journeysEvent = journeysViewModel::handleEvent,
//                stopsState = stopsViewModel.state.collectAsState().value,
//                stopsEvent = stopsViewModel::handleEvent,
//            )
//        }
//        rule.apply{
////            onNodeWithTag("JourneysColumn(): Box()").assertExists()
//            onNodeWithTag("JourneysColumn(): Box(): Column()").assertExists()
//            onNodeWithTag("JourneysColumn(): DisplayJourneys(): Box()").assertExists()
//        }
//    }
}