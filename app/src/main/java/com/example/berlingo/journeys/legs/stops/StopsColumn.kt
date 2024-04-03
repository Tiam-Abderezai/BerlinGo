package com.example.berlingo.journeys.legs.stops

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.berlingo.MainActivity
import com.example.berlingo.R
import com.example.berlingo.common.Dimensions.large
import com.example.berlingo.common.Dimensions.medium
import com.example.berlingo.common.Dimensions.smallXX
import com.example.berlingo.common.Dimensions.smallXXX
import com.example.berlingo.common.components.ErrorScreen
import com.example.berlingo.common.components.LoadingScreen
import com.example.berlingo.common.logger.BaseLogger
import com.example.berlingo.common.logger.FactoryLogger
import com.example.berlingo.journeys.JourneysEvent
import com.example.berlingo.journeys.legs.stops.network.responses.Stop
import com.example.berlingo.locationPermissionGranted
import com.example.berlingo.ui.theme.DarkGray
import com.example.berlingo.ui.theme.LightGray
import com.example.berlingo.ui.theme.isDarkMode
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val logger: BaseLogger = FactoryLogger.getLoggerCompose("StopsColumn()")
private const val testTag = "StopsColumn()"

var textFieldOriginFocused by mutableStateOf(false)
var textFieldDestinationFocused by mutableStateOf(false)
var originStop by mutableStateOf(Stop())
var destinStop by mutableStateOf(Stop())
var originStopName by mutableStateOf("")
var destinStopName by mutableStateOf("")

@SuppressLint("CoroutineCreationDuringComposition", "MissingPermission")
@Composable
fun StopsColumn(
    journeysEvent: suspend (JourneysEvent) -> Unit = {},
    stopsState: StopsState = StopsState.Initial,
    stopsEvent: suspend (StopsEvent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .testTag("$testTag: Column()")
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        val backgroundColor = if (isDarkMode()) DarkGray else LightGray
        val textColor = if (isDarkMode()) LightGray else DarkGray
        val context = LocalContext.current
        OriginTextField(textColor, backgroundColor, stopsEvent, context)
        DestinationTextField(textColor, backgroundColor, stopsEvent)
        SearchJourneysButton(journeysEvent, textColor)
    }
    HandleStopsState(stopsState, stopsEvent)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun OriginTextField(
    textColor: Color,
    backgroundColor: Color,
    stopsEvent: suspend (StopsEvent) -> Unit,
    context: Context,
) {
    TextField(
        modifier = Modifier
            .testTag("$testTag: OriginTextField(): TextField()")
            .background(color = backgroundColor)
            .fillMaxWidth()
            .padding(smallXX)
            .onFocusChanged { focusState ->
                textFieldOriginFocused = focusState.isFocused
            },
        label = { Text(text = "A", color = textColor, fontWeight = FontWeight.SemiBold) },
        value = originStopName,
        onValueChange = { query ->
            originStopName = query
            getStops(stopsEvent, query)
        },
        trailingIcon = { OriginTrailingIcons(stopsEvent, context) },
    )
}

@Composable
private fun OriginTrailingIcons(
    stopsEvent: suspend (StopsEvent) -> Unit,
    context: Context,
) {
    Row(modifier = Modifier.testTag("$testTag: OriginTrailingIcons(): Row()")) {
        if (originStopName.isNotEmpty()) {
            Icon(
                modifier = Modifier
                    .testTag("$testTag: OriginTrailingIcons(): Row(): Icon() - Clear TextField")
                    .clickable {
                        originStopName = ""
                        CoroutineScope(Dispatchers.IO).launch { stopsEvent.invoke(StopsEvent.EmptyStops()) }
                    },
                imageVector = Icons.Filled.Clear,
                contentDescription = stringResource(R.string.clear_textfield),
            )
        }
        Spacer(modifier = Modifier.width(smallXX))
        Icon(
            modifier = Modifier
                .testTag("$testTag: OriginTrailingIcons(): Row(): Icon() - Get Current Location")
                .clickable { getUserLocation(context, stopsEvent) },
            imageVector = Icons.Filled.LocationOn,
            contentDescription = stringResource(R.string.get_current_location),
        )
        Spacer(modifier = Modifier.width(smallXXX))
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DestinationTextField(
    textColor: Color,
    backgroundColor: Color,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
    TextField(
        modifier = Modifier
            .testTag("$testTag: DestinationTextField(): TextField()")
            .background(color = backgroundColor)
            .fillMaxWidth()
            .padding(smallXX)
            .onFocusChanged { focusState ->
                textFieldDestinationFocused = focusState.isFocused
            },
        label = { Text("B", color = textColor, fontWeight = FontWeight.SemiBold) },
        value = destinStopName,
        onValueChange = { query ->
            destinStopName = query
            getStops(stopsEvent, query)
        },
        trailingIcon = { DestinationTrailingIcons() },
    )
}

@Composable
private fun DestinationTrailingIcons() {
    Row(modifier = Modifier.testTag("$testTag: DestinationTrailingIcons(): Row()")) {
        if (destinStopName.isNotEmpty()) {
            Icon(
                Icons.Filled.Clear,
                contentDescription = stringResource(R.string.clear_textfield),
                modifier = Modifier.clickable { destinStopName = "" },
            )
        }
        Spacer(modifier = Modifier.width(smallXX))
        Icon(
            modifier = Modifier
                .testTag("$testTag: DestinationTrailingIcons(): Row(): Icon()")
                .clickable { swapStops() },
            painter = painterResource(id = R.drawable.icon_swap),
            contentDescription = stringResource(R.string.swap_stops),
        )
        Spacer(modifier = Modifier.width(smallXXX))
    }
}

@Composable
private fun SearchJourneysButton(
    journeysEvent: suspend (JourneysEvent) -> Unit,
    textColor: Color,
) {
    Box(
        modifier = Modifier
            .testTag("$testTag: SearchJourneysButton(): Box()")
            .fillMaxWidth()
            .padding(smallXX),
    ) {
        Button(
            modifier = Modifier
                .testTag("$testTag: SearchJourneysButton(): Box(): Button()")
                .fillMaxWidth()
                .height(large)
                .align(Alignment.Center),
            onClick = { getJourneys(journeysEvent) },
            shape = RoundedCornerShape(smallXXX),
        ) {
            Icon(
                modifier = Modifier
                    .testTag("$testTag: SearchJourneysButton(): Box(): Button(): Icon()")
                    .size(large),
                painter = painterResource(id = R.drawable.icon_search),
                tint = textColor,
                contentDescription = null,
            )
        }
    }
}

private fun getStops(
    stopsEvent: suspend (StopsEvent) -> Unit,
    query: String,
) {
    CoroutineScope(Dispatchers.IO).launch {
        stopsEvent.invoke(
            StopsEvent.GetStops(query),
        )
    }
}

private fun getJourneys(journeysEvent: suspend (JourneysEvent) -> Unit) {
    val originLocationId = originStop.location?.id.toString()
    val destinLocationId = destinStop.location?.id.toString()
    val destinLocationLat = destinStop.location?.latitude ?: ""
    val destinLocationLng = destinStop.location?.longitude ?: ""
    if (originStopName.isNotBlank() && destinStopName.isNotBlank()) {
        CoroutineScope(Dispatchers.IO).launch {
            journeysEvent.invoke(
                JourneysEvent.GetJourneys(
                    from = originLocationId,
                    to = destinLocationId,
                    toLatitude = destinLocationLat,
                    toLongitude = destinLocationLng,
                ),
            )
            // ------------------------------ //
            // Hardcoded Values for Debugging //
//                        }
//                        journeysEvent.invoke(
//                            JourneysEvent.JourneyQueryEvent(
//                                "900064301",
//                                "900003200",
//                                52.525607,
//                                13.369072,
//                            ),
//                        )
        }
    }
}

@SuppressLint("MissingPermission")
private fun getUserLocation(
    context: Context,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    if (locationPermissionGranted.value) {
        val taskLocation = fusedLocationProviderClient.lastLocation
        taskLocation.addOnCompleteListener(context as MainActivity) { task ->
            if (task.isComplete) {
                CoroutineScope(Dispatchers.IO).launch {
                    stopsEvent.invoke(
                        StopsEvent.GetNearestStops(
                            task.result,
                        ),
                    )
                }
            }
        }
    }
}

private fun swapStops() {
    val tempStop = originStop
    originStop = destinStop
    destinStop = tempStop

    val tempStopName = originStopName
    originStopName = destinStopName
    destinStopName = tempStopName
}

@Composable
private fun HandleStopsState(
    stopsState: StopsState,
    stopsEvent: suspend (StopsEvent) -> Unit,
) {
    when (stopsState) {
        is StopsState.Initial -> {}
        is StopsState.Loading -> LoadingScreen()
        is StopsState.Error -> ErrorScreen(message = stopsState.message)
        is StopsState.Success -> {
            DisplayStops(stopsState.stops, stopsEvent)
            if (stopsState.nearestStop.name?.isNotEmpty() == true) {
                // When user clicks on the current user location icon, it
                // fills the origin text field "A" with user location name
                originStop = stopsState.nearestStop
                originStopName = stopsState.nearestStop.name
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DisplayStops(stopsState: List<Stop>, stopsEvent: suspend (StopsEvent) -> Unit) {
    val textColor = if (isDarkMode()) Color.White else Color.Black
    LazyColumn(modifier = Modifier.testTag("$testTag: DisplayStops(): LazyColumn()")) {
        items(stopsState) { stop ->
            val keyboardController = LocalSoftwareKeyboardController.current
            Row(
                modifier = Modifier
                    .testTag("$testTag: DisplayStops(): LazyColumn(): Row()")
                    .height(medium)
                    .fillMaxWidth()
                    .clickable {
                        if (textFieldOriginFocused) {
                            originStop = stop
                            originStopName = stop.name ?: ""
                        }
                        if (textFieldDestinationFocused) {
                            destinStop = stop
                            destinStopName = stop.name ?: ""
                        }
                        keyboardController?.hide()
                        // Used to clear the list of Locations/Stops
                        // after focusing on either A or B TextField
                        clearStopsColumn(stopsEvent)
                    },
            ) {
                Text(
                    modifier = Modifier.testTag("$testTag: DisplayStops(): LazyColumn(): Row(): Text()"),
                    text = stop.name ?: "",
                    color = textColor,
                )
            }
        }
    }
}

private fun clearStopsColumn(stopsEvent: suspend (StopsEvent) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        stopsEvent.invoke(
            StopsEvent.EmptyStops(
                emptyList(),
            ),
        )
    }
}

 @Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
 @Composable
 fun StopsColumnPreview() {
     StopsColumn()
 }
