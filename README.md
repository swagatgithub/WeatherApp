# WeatherApp – 3‑Day Forecast (Jetpack Compose + Room)

WeatherApp is a simple Android application that shows a 3‑day weather forecast for a selected location.  
It uses an open‑source weather API for remote data, caches responses locally using Room, and displays the UI with Jetpack Compose.

## Features

- 3‑day weather forecast for a city/location
- Uses a free open‑source Weather API ([Weather API] (https://www.weatherapi.com/) ) 
- Caches latest forecast data locally using Room
- Offline support: when the internet is not available, shows the last cached forecast
- Modern UI built entirely with Jetpack Compose
- Kotlin Coroutines + Flow for async and reactive data handling

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM + Repository, clean-ish layering (data / domain / ui)
- **Networking:** Retrofit to call the Weather API
- **Local Storage:** Room (SQLite)
- **Async:** Coroutines, Flow
- **DI:** Hilt
- **Others:** ViewModel, Coil / Gson

## Data Flow

1. UI layer (Compose screens) interacts with a ViewModel.
2. ViewModel calls a Repository to request the 3‑day forecast.
3. Repository:
   - Tries to fetch fresh data from the Weather API.
   - On success, saves it into Room and emits the updated data.
   - On network failure, falls back to the last cached data from Room.
4. UI observes a `State` and renders accordingly.

## Offline Caching Behaviour

- On first launch with internet:
  - Fetch forecast from remote API and cache in Room.
- On subsequent launches:
  - If internet is available, refresh data from API and update cache.
  - If internet is not available, show the most recent forecast stored in Room.

## Weather API

This project uses the open‑source [Weather API Forecast Endpoint](https://www.weatherapi.com/) .

Example request:

```https
GET https://api.weatherapi.com/v1/forecast.json?key=givenAPIKey&q=Delhi&days=3&aqi=no&alerts=no
