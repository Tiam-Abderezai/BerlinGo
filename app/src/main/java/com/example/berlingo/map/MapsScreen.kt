package com.example.berlingo.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.berlingo.common.Dimensions
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.extensions.decodePolyline
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.JourneysState
import com.example.berlingo.journeys.legs.stops.StopsColumn
import com.example.berlingo.journeys.legs.stops.StopsEvent
import com.example.berlingo.journeys.legs.stops.StopsState
import com.example.berlingo.map.MapsState.Error
import com.example.berlingo.map.MapsState.Initial
import com.example.berlingo.map.MapsState.Loading
import com.example.berlingo.map.MapsState.Success
import com.example.berlingo.map.columns.MapsJourneysColumn
import com.example.berlingo.map.network.responses.Route
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("MapsScreen()")
private const val testTag = "MapsScreen()"

@Composable
fun MapsScreen(
    mapsState: MapsState = Initial,
    mapsEvent: suspend (MapsEvent) -> Unit = {},
    journeysState: JourneysState = JourneysState.Initial,
    journeysEvent: suspend (JourneysEvent) -> Unit = {},
    stopsState: StopsState = StopsState.Initial,
    stopsEvent: suspend (StopsEvent) -> Unit = {},
) {
    val backgroundColor = if (isDarkMode()) DarkGray else LightGray
    Surface(
        modifier = Modifier
            .testTag("$testTag: Surface()")
            .fillMaxSize(),
        color = backgroundColor
    ) {
        Box(modifier = Modifier.testTag("$testTag: Surface(): Box()")) {
            Column(modifier = Modifier.testTag("$testTag: Surface(): Box(): Column()")) {
                StopsColumn(
                    journeysEvent,
                    stopsState,
                    stopsEvent,
                )
                MapsJourneysColumn(
                    journeysState,
                    mapsEvent,
                )
                when (mapsState) {
                    is Initial -> MapComponent(emptyList())
                    is Loading -> LoadingScreen()
                    is Error -> ErrorScreen(message = mapsState.message)
                    is Success -> MapComponent(directions = mapsState.directions)
                }
            }
        }
    }
}

@Composable
private fun MapComponent(directions: List<List<Route>>) {
    Box(
        modifier = Modifier
            .testTag("$testTag: MapComponent(): Box()")
            .height(300.dp),
        contentAlignment = Alignment.Center,
    ) {
        val berlinLat = 52.5200
        val berlinLng = 13.4050
        val berlinCameraPosition = CameraPosition.fromLatLngZoom(LatLng(berlinLat, berlinLng), 10f)
        GoogleMap(
            modifier = Modifier
                .testTag("$testTag: MapComponent(): Box(): GoogleMap()")
                .height(Dimensions.mapHeight),
            cameraPositionState = rememberCameraPositionState { position = berlinCameraPosition },
        ) {
            if (directions.isNotEmpty()) {
                directions.map { routes ->
                    routes.map { route ->
                        val points = route.polyline?.points ?: ""
                        val polyLine = points.decodePolyline()
                        Polyline(
                            points = polyLine,
                            color = Color.Blue,
                            width = 5f,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MapsPreview() {
    MapsScreen()
}
