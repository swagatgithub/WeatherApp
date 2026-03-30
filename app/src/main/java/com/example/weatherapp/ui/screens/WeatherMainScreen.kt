package com.example.weatherapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.weatherapp.R
import com.example.weatherapp.data.db.WeatherEntity
import com.example.weatherapp.ui.viewmodel.WeatherViewModel

@Composable
fun WeatherMainScreen(viewModel: WeatherViewModel, paddingValues: PaddingValues) {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(paddingValues)) {
        OutlinedTextField(
            value = viewModel.locationText,
            onValueChange = { viewModel.locationText = it },
            label = { Text(stringResource(R.string.enter_location)) },
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = { viewModel.fetchForecast(context) },
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            Text(stringResource(R.string.forecast_weather))
        }

        Spacer(Modifier.height(16.dp))

        when {

            viewModel.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            viewModel.forecastList.isNotEmpty() -> {
                viewModel.messageResourceId?.let { resId ->
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = resId),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                ForecastList(viewModel.forecastList)
            }

            viewModel.forecastList.isEmpty() -> {
                viewModel.messageResourceId?.let { resId ->
                    Text(
                        text = stringResource(id = resId),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } ?: run {
                    Text(
                        text = stringResource(R.string.no_forecast_yet),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

    }
}

@Composable
private fun ForecastList(items: List<WeatherEntity>) {
    println("Inside ForecastList $items")
    LazyColumn {
        items(items) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(R.string.date_label, item.date))
                        Text(stringResource(R.string.temperature_label, item.temperature))
                        Text(stringResource(R.string.condition_label, item.condition))
                    }

                    Log.d("WeatherMainScreen", "Raw icon URL: ${item.iconUrl}")

                    AsyncImage(
                        model = item.iconUrl,
                        contentDescription = item.condition,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
        }
    }
}


