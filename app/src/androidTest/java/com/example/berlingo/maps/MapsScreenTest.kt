package com.example.berlingo.maps

import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ViewRootForTest
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
        rule.setContent {
            ViewRootForTest
            MapsScreen(
                mapsState = mapsViewModel.state.collectAsState().value,
                mapsEvent = mapsViewModel::handleEvent,
                journeysState = journeysViewModel.state.collectAsState().value,
                journeysEvent = journeysViewModel::handleEvent,
                stopsState = stopsViewModel.state.collectAsState().value,
                stopsEvent = stopsViewModel::handleEvent,
            )
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
//            StopsColumn(
//                journeysEvent = journeysViewModel::handleEvent,
//                stopsState = stopsViewModel.state.collectAsState().value,
//                stopsEvent = stopsViewModel::handleEvent,
//            )
//            MapsJourneysColumn(
//                journeysState = journeysViewModel.state.collectAsState().value,
//                mapsEvent = mapsViewModel::handleEvent,
//            )
//        }
//        rule.apply {
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box()").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn()").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row()").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row(): Column()").assertExists()
//
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row(): Column()").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row(): Column() - plannedDeparture").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row(): Column() - departure").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row(): Column() - departureDelay").assertExists()
//            onNodeWithTag("MapsJourneysColumn(): MapsJourneys(): Box(): LazyColumn(): Row(): Text() - plannedDeparture").assertExists()
//
//        }
//    }

    @Test
    fun assert_StopsColumn_AND_MapComponent_exist() {
        rule.apply {
            onNodeWithTag("StopsColumn(): Column()").assertExists()
            onNodeWithTag("StopsColumn(): OriginTextField(): TextField()").assertExists()
                .performTextInput("Lichterfelde")
            onNodeWithTag("StopsColumn(): OriginTrailingIcons(): Row(): Icon() - Clear TextField").assertExists()
            onNodeWithTag("StopsColumn(): OriginTrailingIcons(): Row(): Icon() - Get Current Location").assertExists()
            onNodeWithTag("StopsColumn(): DestinationTextField(): TextField()").assertExists()
                .performTextInput("Hauptbahnhof")
            onNodeWithTag("StopsColumn(): DestinationTrailingIcons(): Row(): Icon()").assertExists()
            onNodeWithTag("StopsColumn(): SearchJourneysButton(): Box()").assertExists()
            onNodeWithTag("StopsColumn(): SearchJourneysButton(): Box(): Button()").assertExists()
                .performClick()
            onNodeWithTag("MapsScreen(): MapComponent(): Box()").assertExists()
            onNodeWithTag("MapsScreen(): MapComponent(): Box(): GoogleMap()").assertExists()
        }
    }



}