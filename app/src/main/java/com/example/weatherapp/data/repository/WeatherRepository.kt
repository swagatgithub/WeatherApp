package com.example.weatherapp.data.repository

import android.content.Context
import com.example.weatherapp.data.api.WeatherApiService
import com.example.weatherapp.data.db.WeatherDao
import com.example.weatherapp.data.db.WeatherEntity
import com.example.weatherapp.data.util.NetworkUtils
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApiService,
    private val dao: WeatherDao
) {
    private val apiKey = "c0633580993f4d08b33164925222406"

    suspend fun getForecast(location: String, context: Context): Pair<List<WeatherEntity>, Boolean> {
        val hasNetwork = NetworkUtils.isNetworkAvailable(context)
        var isCachedData = false

        val data = if (hasNetwork) {
            try {
                val response = api.getForecast(apiKey, location)
                val entities = response.forecast.forecastday.map {
                    WeatherEntity(
                        location = response.location.name.lowercase(),
                        date = it.date,
                        temperature = it.day.avgTempC,
                        condition = it.day.condition.text,
                        iconUrl = if (it.day.condition.icon.startsWith("//"))
                            "https:${it.day.condition.icon}"
                        else
                            it.day.condition.icon
                    )
                }
                dao.insertAll(entities)
                entities
            } catch (e: Exception) {
                // API failure but possibly offline
                val cached = dao.getForecast(location)
                isCachedData = cached.isNotEmpty()
                cached
            }
        } else {
            // Fully offline
            val cached = dao.getForecast(location)
            isCachedData = cached.isNotEmpty()
            cached
        }

        return Pair(data, isCachedData)
    }
}



