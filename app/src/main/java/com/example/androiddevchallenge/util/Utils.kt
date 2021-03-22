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
package com.example.androiddevchallenge.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone

fun Context.hasPermission(permission: String): Boolean {

    // Background permissions didn't exit prior to Q, so it's approved by default.
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q
    ) {
        return true
    }

    return ActivityCompat.checkSelfPermission(this, permission) ==
        PackageManager.PERMISSION_GRANTED
}

/**
 * Requests permission and if the user denied a previous request, but didn't check
 * "Don't ask again", we provide additional rationale.
 *
 * Note: The [Snackbar] should have an action to request the permission.
 */
fun Activity.requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackbar: Snackbar
) {
    val provideRationale = shouldShowRequestPermissionRationale(permission)

    if (provideRationale) {
        snackbar.show()
    } else {
        requestPermissions(arrayOf(permission), requestCode)
    }
}

fun Modifier.horizontalGradientBackground(
    colors: List<Color>
) = gradientBackground(colors) { gradientColors, size ->
    Brush.horizontalGradient(
        colors = gradientColors,
        startX = 0f,
        endX = size.width
    )
}

fun Modifier.verticalGradientBackground(
    colors: List<Color>
) = gradientBackground(colors) { gradientColors, size ->
    Brush.verticalGradient(
        colors = gradientColors,
        startY = 0f,
        endY = size.width
    )
}

fun Modifier.gradientBackground(
    colors: List<Color>,
    brushProvider: (List<Color>, Size) -> Brush
): Modifier = composed {
    var size by remember { mutableStateOf(Size.Zero) }
    val gradient = remember(colors, size) { brushProvider(colors, size) }
    drawWithContent {
        size = this.size
        drawRect(brush = gradient)
        drawContent()
    }
}

fun String.capitalizeFirstLetter() = this.split(" ").joinToString(" ") { it.capitalize() }.trimEnd()

class Utils {
    companion object {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val df = SimpleDateFormat("HH:mm")
        fun getCurrentTimeWithTimeZoneInfo(timeDifferenceInSeconds: Long): String {
            cal.timeInMillis = System.currentTimeMillis() + timeDifferenceInSeconds * 1000
            df.timeZone = cal.timeZone
            val timeText = df.format(cal.time)
            val timeZoneValue = timeDifferenceInSeconds / 3600
            val timeZoneText = if (timeZoneValue >= 0) "+$timeZoneValue" else "$timeZoneValue"
            return "$timeText (UTC $timeZoneText)"
        }

        fun getLocalTime(unixTime: Long, timeDifferenceInSeconds: Long, showTimeZone: Boolean = false): String {
            cal.timeInMillis = (unixTime + timeDifferenceInSeconds) * 1000
            df.timeZone = cal.timeZone
            val timeText = df.format(cal.time)
            if (!showTimeZone) {
                return "$timeText"
            } else {
                val timeZoneValue = timeDifferenceInSeconds / 3600
                val timeZoneText = if (timeZoneValue >= 0) "+$timeZoneValue" else "$timeZoneValue"
                return "$timeText (UTC $timeZoneText)"
            }
        }

        // https://openweathermap.org/weather-conditions
        fun getIconifyAssetFromWeatherId(weatherId: Long, icon: String): String {
            when (weatherId.toInt()) {
                in 200..299 -> { // Group 2xx: Thunderstorm
                    return "{mdi-weather-lightning}"
                }
                800 -> { // Group 800: Clear
                    if (icon.contains("n")) {
                        return "{mdi-weather-night}"
                    } else {
                        return "{mdi-weather-sunny}"
                    }
                }
                in 801..802 -> { // Group 80x: Clouds
                    return "{mdi-weather-partlycloudy}"
                }
                in 803..804 -> { // Group 80x: Clouds
                    return "{mdi-weather-cloudy}"
                }
                in 600..699 -> { // Group 6xx: Snow
                    return "{mdi-weather-snowy}"
                }
                in 500..599 -> { // Group 5xx: Rain
                    return "{mdi-weather-pouring}"
                }
                in 300..399 -> { // Group 3xx: Drizzle
                    return "{mdi-weather-rainy}"
                }
                701 -> { // Mist
                    return "{mdi-weather-fog}"
                }
                741 -> { // Fog
                    return "{mdi-weather-fog}"
                }
                else -> return "{mdi-help}"
            }
        }
    }
}
