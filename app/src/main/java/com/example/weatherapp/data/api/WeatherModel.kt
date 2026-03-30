package com.example.weatherapp.data.api

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val location: Location,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    @SerializedName("avgtemp_c") val avgTempC: Double,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String
)
