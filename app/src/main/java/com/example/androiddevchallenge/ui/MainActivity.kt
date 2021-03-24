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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.bermudaBay
import com.example.androiddevchallenge.ui.theme.bermudaBayVariant
import com.example.androiddevchallenge.ui.theme.bermudaBayVariant_dark
import com.example.androiddevchallenge.ui.theme.bermudaBay_dark

class MainActivity : AppCompatActivity() {
    private val mainViewModel by viewModels<MainViewModel>()

    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme() {
                MyApp(mainViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (mainViewModel.weatherA.name == null) {
            mainViewModel.changeCityA(false)
        }
        if (mainViewModel.weatherB.name == null) {
            mainViewModel.changeCityB(false)
        }
    }
}

private enum class DisplayState {
    Single,
    Comparison
}

// Start building your app here!
@ExperimentalAnimationApi
@Composable
fun MyApp(viewModel: MainViewModel) {
    var displayState by remember { mutableStateOf(DisplayState.Single) }

    val actionIcon = when (displayState) {
        DisplayState.Single -> Icons.Default.AddCircle
        DisplayState.Comparison -> Icons.Default.RemoveCircle
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = { Text("Compose (& Compare!) Weather App") },
                actions = {
                    // RowScope here, so these icons will be placed horizontally
                    IconButton(
                        onClick = {
                            displayState = when (displayState) {
                                DisplayState.Single -> DisplayState.Comparison
                                DisplayState.Comparison -> DisplayState.Single
                            }
                        }
                    ) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = stringResource(R.string.content_description_toggle_comparison_city)
                        )
                    }
                }
            )
        }
    ) {
        Surface() {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                if (MaterialTheme.colors.isLight) bermudaBayVariant else bermudaBayVariant_dark,
                                if (MaterialTheme.colors.isLight) bermudaBay else bermudaBay_dark,
                                if (MaterialTheme.colors.isLight) bermudaBayVariant else bermudaBayVariant_dark
                            )
                        )
                    )
            ) {
                val (bottomPortion, topPortion, leftArrow, rightArrow) = createRefs()
                Column(
                    modifier = Modifier
                        .wrapContentHeight()
                        .constrainAs(bottomPortion) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(bottom = 8.dp)
                ) {
                    val modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                    ComparisonBar(
                        modifier = modifier,
                        label = "Temperature",
                        unit = "°C",
                        valueA = if (viewModel.weatherA.name != null) viewModel.weatherA.main.temp else 0.0,
                        valueB = if (viewModel.weatherB.name != null) viewModel.weatherB.main.temp else 0.0,
                        weightA = if (viewModel.weatherA.name != null) ((viewModel.weatherA.main.temp + 20) / 70).toFloat() else 0f,
                        weightB = if (viewModel.weatherB.name != null) ((viewModel.weatherB.main.temp + 20) / 70).toFloat() else 0f,
                        icon = Icons.Default.Thermostat,
                        isComparisonVisible = displayState == DisplayState.Comparison
                    )
                    ComparisonBar(
                        modifier = modifier,
                        label = "Humidity",
                        unit = "%",
                        valueA = if (viewModel.weatherA.name != null) viewModel.weatherA.main.humidity else 0.0,
                        valueB = if (viewModel.weatherB.name != null) viewModel.weatherB.main.humidity else 0.0,
                        weightA = if (viewModel.weatherA.name != null && viewModel.weatherB.name != null) {
                            if (viewModel.weatherA.main.humidity == 0.0 && viewModel.weatherB.main.humidity == 0.0) {
                                1f
                            } else {
                                (viewModel.weatherA.main.humidity / (viewModel.weatherA.main.humidity + viewModel.weatherB.main.humidity)).toFloat()
                            }
                        } else {
                            0f
                        },
                        weightB = if (viewModel.weatherA.name != null && viewModel.weatherB.name != null) {
                            if (viewModel.weatherA.main.humidity == 0.0 && viewModel.weatherB.main.humidity == 0.0) {
                                1f
                            } else {
                                (viewModel.weatherB.main.humidity / (viewModel.weatherA.main.humidity + viewModel.weatherB.main.humidity)).toFloat()
                            }
                        } else {
                            0f
                        },
                        icon = Icons.Default.Water,
                        isComparisonVisible = displayState == DisplayState.Comparison
                    )
                    ComparisonBar(
                        modifier = modifier,
                        label = "Clouds",
                        unit = "%",
                        valueA = if (viewModel.weatherA.name != null) viewModel.weatherA.clouds.all else 0.0,
                        valueB = if (viewModel.weatherB.name != null) viewModel.weatherB.clouds.all else 0.0,
                        weightA = if (viewModel.weatherA.name != null && viewModel.weatherB.name != null) {
                            if (viewModel.weatherA.clouds.all == 0.0 && viewModel.weatherB.clouds.all == 0.0) {
                                1f
                            } else {
                                (viewModel.weatherA.clouds.all / (viewModel.weatherA.clouds.all + viewModel.weatherB.clouds.all)).toFloat()
                            }
                        } else {
                            0f
                        },
                        weightB = if (viewModel.weatherA.name != null && viewModel.weatherB.name != null) {
                            if (viewModel.weatherA.clouds.all == 0.0 && viewModel.weatherB.clouds.all == 0.0) {
                                1f
                            } else {
                                (viewModel.weatherB.clouds.all / (viewModel.weatherA.clouds.all + viewModel.weatherB.clouds.all)).toFloat()
                            }
                        } else {
                            0f
                        },
                        icon = Icons.Default.WbCloudy,
                        isComparisonVisible = displayState == DisplayState.Comparison
                    )
                    ComparisonBar(
                        modifier = modifier,
                        label = "Wind",
                        unit = "m/s",
                        valueA = if (viewModel.weatherA.name != null) viewModel.weatherA.wind.speed else 0.0,
                        valueB = if (viewModel.weatherB.name != null) viewModel.weatherB.wind.speed else 0.0,
                        weightA = if (viewModel.weatherA.name != null && viewModel.weatherB.name != null) {
                            if (viewModel.weatherA.wind.speed == 0.0 && viewModel.weatherB.wind.speed == 0.0) {
                                1f
                            } else {
                                (viewModel.weatherA.wind.speed / (viewModel.weatherA.wind.speed + viewModel.weatherB.wind.speed)).toFloat()
                            }
                        } else {
                            0f
                        },
                        weightB = if (viewModel.weatherA.name != null && viewModel.weatherB.name != null) {
                            if (viewModel.weatherA.wind.speed == 0.0 && viewModel.weatherB.wind.speed == 0.0) {
                                50f
                            } else {
                                (viewModel.weatherB.wind.speed / (viewModel.weatherA.wind.speed + viewModel.weatherB.wind.speed)).toFloat()
                            }
                        } else {
                            0f
                        },
                        icon = Icons.Default.Air,
                        isComparisonVisible = displayState == DisplayState.Comparison
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        if (viewModel.threeHourForecastWeatherA.list != null) {
                            ForecastInfo(
                                Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                                    .padding(vertical = 4.dp),
                                viewModel.threeHourForecastWeatherA,
                                if (displayState == DisplayState.Single) 4 else 2,
                                viewModel.weatherA.timezone,
                                MaterialTheme.colors.primaryVariant
                            )
                        }

                        if (displayState == DisplayState.Comparison && viewModel.threeHourForecastWeatherB.list != null) {
                            ForecastInfo(
                                Modifier
                                    .weight(1f)
                                    .wrapContentHeight()
                                    .padding(vertical = 4.dp),
                                viewModel.threeHourForecastWeatherB,
                                2,
                                viewModel.weatherB.timezone,
                                MaterialTheme.colors.secondaryVariant
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .constrainAs(topPortion) {
                            top.linkTo(parent.top)
                            bottom.linkTo(bottomPortion.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Top
                ) {
                    val modifier = Modifier
                        .width(180.dp)
                        .wrapContentHeight()

                    WeatherCityCard(
                        modifier,
                        viewModel.weatherA,
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.primaryVariant,
                        (displayState == DisplayState.Comparison),
                        { viewModel.changeCityA(true) },
                        { viewModel.changeCityA(false) }
                    )
                    AnimatedVisibility(
                        visible = displayState == DisplayState.Comparison,
                        enter = expandHorizontally(
                            animationSpec = spring(
                                stiffness = Spring.StiffnessMedium,
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                visibilityThreshold = null
                            )
                        ),
                        exit = shrinkHorizontally()
                    ) {
                        WeatherCityCard(
                            modifier,
                            viewModel.weatherB,
                            MaterialTheme.colors.secondary,
                            MaterialTheme.colors.secondaryVariant,
                            (displayState == DisplayState.Comparison),
                            { viewModel.changeCityB(true) },
                            { viewModel.changeCityB(false) }
                        )
                    }
                }
                CircleButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                        .constrainAs(leftArrow) {
                            end.linkTo(topPortion.start)
                            top.linkTo(topPortion.top)
                            bottom.linkTo(topPortion.bottom)
                        },
                    iconImageVector = Icons.Default.ChevronLeft,
                    iconTint = MaterialTheme.colors.primary,
                    iconContentDescription = stringResource(R.string.content_description_change_city),
                    onClick = { viewModel.changeCityA(true) },
                )
                CircleButton(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(48.dp)
                        .constrainAs(rightArrow) {
                            start.linkTo(topPortion.end)
                            top.linkTo(topPortion.top)
                            bottom.linkTo(topPortion.bottom)
                        },
                    iconImageVector = Icons.Default.ChevronRight,
                    iconTint = MaterialTheme.colors.primary,
                    iconContentDescription = stringResource(R.string.content_description_change_city),
                    onClick = { viewModel.changeCityA(false) },
                )
            }
        }
    }
}

// @Preview("Light Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun LightPreview() {
//    MyTheme {
//        MyApp()
//    }
// }
//
// @Preview("Dark Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun DarkPreview() {
//    MyTheme(darkTheme = true) {
//        MyApp()
//    }
// }
