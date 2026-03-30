package com.example.weatherapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.db.WeatherEntity
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.data.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    var locationText by mutableStateOf("")
    var forecastList by mutableStateOf<List<WeatherEntity>>(emptyList())
    var isLoading by mutableStateOf(false)
    var messageResourceId by mutableStateOf<Int?>(null)

    fun fetchForecast(context: Context) {
        if (locationText.isBlank()) return

        viewModelScope.launch {
            isLoading = true
            messageResourceId = null
            forecastList = emptyList()

            val hasNetwork = NetworkUtils.isNetworkAvailable(context)
            val (data, isCachedData) = repository.getForecast(locationText, context)

            forecastList = data

            messageResourceId = when {
                hasNetwork -> null
                !hasNetwork && isCachedData -> com.example.weatherapp.R.string.show_cached_data
                !hasNetwork && data.isEmpty() ->  com.example.weatherapp.R.string.no_internet_no_cache_data
                else -> null
            }

            isLoading = false
        }
    }
}
