package com.example.berlingo.main
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.berlingo.data.network.NetworkApiImpl
import com.example.berlingo.repositories.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel@Inject constructor(
    private val networkApiImpl: NetworkApiImpl,
) : ViewModel() {
//    private val _state = MutableStateFlow(MainViewState())
//    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val locations = networkApiImpl.getLocations()

            Log.d("dev-log", "data ${locations.data}")
            Log.d("dev-log", "message ${locations.message}")
            Log.d("dev-log", "status ${locations.status}")
//            _state.update { it.copy(isLoading = true) }
//            _state.update {
//                it.copy(
//                    repositories = appRepository.getLocations(""),
//                    isLoading = true,
//                )
//            }
        }
    }
}
