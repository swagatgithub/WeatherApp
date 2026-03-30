package com.example.weatherapp.data.db

import androidx.room.Entity

@Entity(
    tableName = "weather_table",
    primaryKeys = ["location", "date"]
)
data class WeatherEntity(
    val location: String,
    val date: String,
    val temperature: Double,
    val condition: String,
    val iconUrl: String
)

