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

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.implementation.callback.CurrentWeatherCallback
import com.kwabenaberko.openweathermaplib.implementation.callback.ThreeHourForecastCallback
import com.kwabenaberko.openweathermaplib.model.currentweather.CurrentWeather
import com.kwabenaberko.openweathermaplib.model.threehourforecast.ThreeHourForecast

class MainViewModel : AndroidViewModel {
    val helper = OpenWeatherMapHelper("1b035b39e3d589246b59d5c35c114028")

    constructor(application: Application) : super(application) {
        helper.setUnits("metric")
    }

    var weatherA by mutableStateOf(CurrentWeather())
    var weatherB by mutableStateOf(CurrentWeather())
    var threeHourForecastWeatherA by mutableStateOf(ThreeHourForecast())
    var threeHourForecastWeatherB by mutableStateOf(ThreeHourForecast())

    val cities = listOf(
        "Amsterdam",
        "Bangkok",
        "Beijing",
        "Berlin",
        "Brussels",
        "Cairo",
        "Cape Town",
        "Delhi",
        "Doha",
        "Hanoi",
        "Hong Kong",
        "Jakarta",
        "Kuala Lumpur",
        "London",
        "Madrid",
        "Manila",
        "Mexico City",
        "Milan",
        "Moscow",
        "New York",
        "Paris",
        "San Francisco",
        "Seoul",
        "Shanghai",
        "Singapore",
        "Sydney",
        "Tokyo",
        "Vancouver"
    )

    var cityIndexA = 5
    var cityIndexB = 11

    fun changeCityA(isLeftArrowClick: Boolean = false) {
        if (isLeftArrowClick) {
            cityIndexA--
        } else {
            cityIndexA++
        }
        if (cityIndexA > cities.size - 1) {
            cityIndexA = 0
        } else if (cityIndexA < 0) {
            cityIndexA = cities.size - 1
        }
        updateWeatherForCityA(cities[cityIndexA])
    }

    fun changeCityB(isLeftArrowClick: Boolean = false) {
        if (isLeftArrowClick) {
            cityIndexB--
        } else {
            cityIndexB++
        }
        if (cityIndexB > cities.size - 1) {
            cityIndexB = 0
        } else if (cityIndexB < 0) {
            cityIndexB = cities.size - 1
        }
        updateWeatherForCityB(cities[cityIndexB])
    }

    fun updateWeatherForCityA(cityName: String) {
        helper.getCurrentWeatherByCityName(
            cityName,
            object : CurrentWeatherCallback {
                override fun onSuccess(currentWeather: CurrentWeather) {
                    Log.d(
                        "MainViewModel",
                        "OpenWeather result: " + currentWeather.name + " " + currentWeather.weather[0].main + " " + "http://openweathermap.org/img/wn/" + currentWeather.weather[0].icon + "@2x.png"
                    )
                    weatherA = currentWeather
                    getForecastWeatherForCityA()
                }

                override fun onFailure(throwable: Throwable) {
                }
            }
        )
    }

    fun updateWeatherForCityB(cityName: String) {
        helper.getCurrentWeatherByCityName(
            cityName,
            object : CurrentWeatherCallback {
                override fun onSuccess(currentWeather: CurrentWeather) {
                    weatherB = currentWeather
                    getForecastWeatherForCityB()
                }

                override fun onFailure(throwable: Throwable) {
                }
            }
        )
    }

    fun getForecastWeatherForCityA(cityName: String = cities[cityIndexA]) {
        helper.getThreeHourForecastByCityName(
            cityName,
            object : ThreeHourForecastCallback {
                override fun onSuccess(threeHourForecast: ThreeHourForecast?) {
                    if (threeHourForecast != null) {
                        Log.d(
                            "MainViewModel",
                            "OpenWeather ThreeHourForecastWeather result: size: " + threeHourForecast.list[0].dt + " " + threeHourForecast.list[0].weather[0].main + " " + "http://openweathermap.org/img/wn/" + threeHourForecast.list[0].weather[0].icon + "@2x.png"
                        )
                        threeHourForecastWeatherA = threeHourForecast
                    }
                }

                override fun onFailure(throwable: Throwable) {
                }
            }
        )
    }

    fun getForecastWeatherForCityB(cityName: String = cities[cityIndexB]) {
        helper.getThreeHourForecastByCityName(
            cityName,
            object : ThreeHourForecastCallback {
                override fun onSuccess(threeHourForecast: ThreeHourForecast?) {
                    if (threeHourForecast != null) {
                        threeHourForecastWeatherB = threeHourForecast
                    }
                }

                override fun onFailure(throwable: Throwable) {
                }
            }
        )
    }
}
