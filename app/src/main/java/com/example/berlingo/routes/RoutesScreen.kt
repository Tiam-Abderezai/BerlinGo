package com.example.berlingo.routes

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.berlingo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutesScreen(routesViewModel: RoutesViewModel) {
    Surface(color = Color.Gray, modifier = Modifier.fillMaxSize()) {
        // State for the two text fields

        var searchText by remember { mutableStateOf("") }
        val searchLocations = routesViewModel.searchLocations.collectAsState()
        val selectedLocation = routesViewModel.selectedLocation.collectAsState()
        var searchedJourneys by remember { mutableStateOf(listOf("")) }

//        val viewModel = hiltViewModel<RoutesViewModel>()
        val state = routesViewModel.state.collectAsState()
        var textFieldOriginFocused by remember { mutableStateOf(false) }
        var textFieldDestinationFocused by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        var stateOriginTextField by remember { mutableStateOf("") }
        var stateDestinationTextField by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Top part with TextFields and Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Adjust the weight to control the space distribution
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                TextField(
                    value = stateOriginTextField,
                    onValueChange = {
//                        state.value.isLoading
                        searchText = it
                        stateOriginTextField = it
                        runBlocking { routesViewModel.queryLocation(it) }
//                    viewModel
                    },
                    label = { Text("A") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp).onFocusChanged { focusState ->
                        textFieldOriginFocused = focusState.isFocused
//                        textFieldDestinationClicked = false
                    },
                )
                TextField(
                    value = stateDestinationTextField,
                    onValueChange = {
//                        searchText = it
                        stateDestinationTextField = it
                        runBlocking { routesViewModel.queryLocation(it) }
                    },
                    label = { Text("B") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp).onFocusChanged { focusState ->
                        textFieldDestinationFocused = focusState.isFocused
//                        textFieldOriginClicked = false
                    },
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Button(
                        onClick = {
                            Log.d("dev-log", "$stateOriginTextField $stateDestinationTextField")
//                            runBlocking { routesViewModel.queryJourneys(stateOriginTextField, "", stateDestinationTextField, 1.1, 1.1) }
                            CoroutineScope(Dispatchers.IO).launch {
                                routesViewModel.queryJourneys("900023201", "900980720", "ATZE+Musiktheater", 52.54333, 13.35167)
                                routesViewModel.queryJourneys("900023201", "900980720", "ATZE+Musiktheater", 52.54333, 13.35167)
                                searchedJourneys = routesViewModel.searchedJourneys.value
                            }
                        },
                        modifier = Modifier.padding(8.dp)
                            .align(Alignment.CenterEnd),

                    ) {
                        // Icon on the left of the text
                        Icon(
                            painter = painterResource(id = R.drawable.icon_search),
                            contentDescription = null, // Provide a proper description for accessibility
//                        modifier = Modifier.size(24.dp) // Set the size of the icon as needed
                        )
                    }
                }
            }
            if (searchLocations.value.isNotEmpty()) {
                LazyColumn {
                    items(searchLocations.value) { result ->
                        Text(
                            modifier = Modifier.clickable {
                                if (textFieldOriginFocused) {
                                    stateOriginTextField = result.name
                                    runBlocking { routesViewModel.queryLocation("") }
                                }
                                if (textFieldDestinationFocused) {
                                    stateDestinationTextField = result.name
                                    runBlocking { routesViewModel.queryLocation("") }
                                }
                            },
                            text = result.name,
                            color = Color.Yellow,
                        )
                    }
                }
            }

            // Middle and bottom part with LazyColumn
            LazyColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(), // Adjust the weight to control the space distribution
            ) {
                items(searchedJourneys) {journey ->
                    if (searchedJourneys.isNotEmpty()) {
                        Text(
                            text = journey,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }
}
