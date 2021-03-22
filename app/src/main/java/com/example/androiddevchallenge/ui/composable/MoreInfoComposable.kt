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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.cornerRadius
import com.example.androiddevchallenge.ui.theme.moreInfoSmallTextSize
import com.example.androiddevchallenge.util.Utils
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather

// not used
@Composable
fun MoreInfoCard(modifier: Modifier, currentWeather: CurrentWeather, color: Color) {
    Column(
        modifier = modifier
            .background(
                shape = RoundedCornerShape(cornerRadius),
                color = MaterialTheme.colors.background
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Thermostat,
                contentDescription = null,
                tint = color
            )
            Text(
                text = "Min: ${currentWeather.main.tempMin}°C\nMax: ${currentWeather.main.tempMax}°C",
                fontSize = moreInfoSmallTextSize,
                color = color
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.WbSunny,
                contentDescription = null,
                tint = color
            )
            Text(
                text = "Sunrise: ${
                Utils.getLocalTime(
                    currentWeather.sys.sunrise,
                    currentWeather.timezone
                )
                }\nSunset: ${
                Utils.getLocalTime(
                    currentWeather.sys.sunset,
                    currentWeather.timezone
                )
                }",
                fontSize = moreInfoSmallTextSize,
                color = color
            )
        }
    }
}
