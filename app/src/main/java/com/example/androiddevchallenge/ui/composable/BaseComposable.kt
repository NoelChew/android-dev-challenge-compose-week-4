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

import android.os.Build
import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.androiddevchallenge.ui.theme.comparisonBarLabelIconSize
import com.example.androiddevchallenge.util.Utils
import com.joanzapata.iconify.widget.IconTextView
import com.kwabenaberko.openweathermaplib.model.common.Weather

@Composable
fun CardLabel(modifier: Modifier, shape: Shape, color: Color, text: String, icon: ImageVector) {
    Card(modifier = modifier, shape = shape) {
        Surface(color = color) {
            Row(
                modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(4.dp)
                )

                Icon(
                    modifier = Modifier
                        .size(comparisonBarLabelIconSize)
                        .padding(4.dp),
                    imageVector = icon,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun WeatherIcon(modifier: Modifier, weather: Weather, iconTextSize: Float, color: Color) {
    val iconTextColor =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.graphics.Color.valueOf(
                color.red,
                color.green,
                color.blue,
                color.alpha
            ).toArgb()
        } else {
            Color.Black.toArgb()
        }

    AndroidView(
        modifier = modifier, // Occupy the max size in the Compose UI tree
        factory = { context ->
            // Creates custom view
            IconTextView(context).apply {
                textSize = iconTextSize // pixel
                gravity = Gravity.CENTER
            }
        },
        update = { view ->
            // View's been inflated or state read in this block has been updated
            // Add logic here if necessary

            // As selectedItem is read here, AndroidView will recompose
            // whenever the state changes
            // Example of Compose -> View communication
            view.text = Utils.getIconifyAssetFromWeatherId(
                weather.id,
                weather.icon
            )
            view.setTextColor(iconTextColor)
        }
    )
}

@Composable
fun CircleButton(
    modifier: Modifier,
    iconImageVector: ImageVector,
    iconTint: Color,
    iconContentDescription: String,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier
            .background(shape = CircleShape, color = Color.Transparent)
            .clip(shape = CircleShape),
        onClick = onClick,
    ) {
        Icon(
            modifier = Modifier
                .fillMaxSize(),
            imageVector = iconImageVector,
            tint = iconTint,
            contentDescription = iconContentDescription
        )
    }

}
