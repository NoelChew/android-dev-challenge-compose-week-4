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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.cornerRadius
import com.example.androiddevchallenge.ui.theme.forecastSmallTextSize
import com.example.androiddevchallenge.ui.theme.forecastTextSize
import com.example.androiddevchallenge.util.Utils
import com.example.androiddevchallenge.util.capitalizeFirstLetter
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast

@Composable
fun ForecastInfo(
    modifier: Modifier,
    threeHourForecast: ThreeHourForecast,
    numberOfItems: Int,
    timeDifferenceInSeconds: Long,
    color: Color
) {
    Row(
        modifier = modifier,
    ) {
        for (i in 0 until numberOfItems) {
            val it = threeHourForecast.list[i]
            Column(
                modifier = Modifier
                    .padding(2.dp)
                    .weight(1f)
                    .wrapContentHeight()
                    .background(
                        shape = RoundedCornerShape(cornerRadius),
                        color = MaterialTheme.colors.background
                    )
                    .padding(4.dp)
                    .semantics(mergeDescendants = true) {}
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
                        Text(
                            text = Utils.getLocalTime(it.dt, timeDifferenceInSeconds),
                            fontSize = forecastTextSize,
                            color = color
                        )
                        Text(
                            text = "${it.main.temp}°C",
                            fontSize = forecastTextSize,
                            color = color
                        )
                    }
                    WeatherIcon(
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        it.weather[0],
                        24f,
                        color
                    )
                }
                Text(
                    text = "${it.weather[0].description.capitalizeFirstLetter()}",
                    fontSize = forecastSmallTextSize,
                    color = color
                )
            }
        }
    }
}
