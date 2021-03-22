/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.arrowIconSize
import com.example.androiddevchallenge.ui.theme.cornerRadius
import com.example.androiddevchallenge.ui.theme.weatherInfoTemperatureTextSize
import com.example.androiddevchallenge.util.Utils
import com.example.androiddevchallenge.util.capitalizeFirstLetter
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather

@Composable
fun WeatherCityCard(
    modifier: Modifier,
    currentWeather: CurrentWeather,
    color: Color,
    showArrows: Boolean,
    onChangeCityLeftClick: () -> Unit,
    onChangeCityRightClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .background(color = MaterialTheme.colors.background, shape = RoundedCornerShape(8.dp))
            .padding(bottom = 8.dp)
            .semantics(mergeDescendants = true) {},
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (currentWeather.name != null) {
            WeatherCityLabel(
                modifier,
                currentWeather.name,
                color,
                showArrows,
                onChangeCityLeftClick,
                onChangeCityRightClick
            )

            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = Utils.getCurrentTimeWithTimeZoneInfo(currentWeather.timezone),
                color = color
            )

            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = "${currentWeather.main.temp}°C",
                color = color,
                fontSize = weatherInfoTemperatureTextSize
            )

            WeatherIcon(
                Modifier
                    .padding(16.dp)
                    .wrapContentWidth()
                    .height(88.dp),
                currentWeather.weather[0],
                96f,
                color
            )

            Text(
                text = currentWeather.weather[0].description.capitalizeFirstLetter(),
                color = color
            )
        } else {
            Button(onChangeCityRightClick) {
                Text("Press here to load city")
            }
        }
    }
}

@Composable
fun WeatherCityLabel(
    modifier: Modifier,
    text: String,
    color: Color,
    showArrows: Boolean = false,
    onChangeCityLeftClick: () -> Unit,
    onChangeCityRightClick: () -> Unit
) {
    Card(
        modifier = modifier,
        backgroundColor = color,
        shape = RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (showArrows) {
                Icon(
                    modifier = Modifier
                        .clickable(onClick = onChangeCityLeftClick)
                        .padding(8.dp)
                        .size(arrowIconSize),
                    imageVector = Icons.Default.ChevronLeft,
                    contentDescription = "Previous Country"
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(arrowIconSize)
                )
            }

            Text(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                text = text
            )

            if (showArrows) {
                Icon(
                    modifier = Modifier
                        .clickable(onClick = onChangeCityRightClick)
                        .padding(8.dp)
                        .size(arrowIconSize),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Next Country"
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(arrowIconSize)
                )
            }
        }
    }
}
