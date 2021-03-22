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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.theme.comparisonBarValueTextSize
import com.example.androiddevchallenge.ui.theme.cornerRadius

@ExperimentalAnimationApi
@Composable
fun ComparisonBar(
    modifier: Modifier,
    label: String,
    unit: String,
    valueA: Double,
    valueB: Double,
    weightA: Float,
    weightB: Float,
    icon: ImageVector,
    isComparisonVisible: Boolean
) {
    val _weightA by animateFloatAsState(targetValue = weightA)
    val _weightB by animateFloatAsState(targetValue = weightB)

    val cardColor by animateColorAsState(targetValue = if (!isComparisonVisible || _weightA > _weightB) MaterialTheme.colors.secondary else (if (_weightA == _weightB) MaterialTheme.colors.surface else MaterialTheme.colors.secondaryVariant))
    val valueWidth = 80.dp
    val barWidth = 240.dp
    Column(
        modifier = modifier.padding(bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CardLabel(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .wrapContentHeight(),
            shape = if (!isComparisonVisible) RoundedCornerShape(
                topStart = 8.dp,
                topEnd = 8.dp
            ) else RoundedCornerShape(cornerRadius),
            color = cardColor,
            text = label,
            icon = icon
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            if (!isComparisonVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(28.dp)
                        .background(
                            shape = RoundedCornerShape(
                                bottomStart = cornerRadius,
                                bottomEnd = cornerRadius
                            ),
                            color = MaterialTheme.colors.background
                        )
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .width(valueWidth)
                        .wrapContentHeight()
                        .padding(2.dp),
                    text = "$valueA $unit",
                    textAlign = if (isComparisonVisible) TextAlign.End else TextAlign.Center,
                    fontSize = comparisonBarValueTextSize,
                    color = if (isComparisonVisible) Color.White else cardColor
                )

                AnimatedVisibility(
                    visible = isComparisonVisible,
                    enter = expandHorizontally(),
                    exit = shrinkHorizontally()
                ) {
                    Row(
                        modifier = Modifier
                            .width(barWidth)
                            .wrapContentHeight()
                            .padding(vertical = 6.dp),
//                        .clip(shape = RoundedCornerShape(cornerRadius)),
                        verticalAlignment = Alignment.CenterVertically

                    ) {

                        Box(
                            modifier = Modifier
                                .weight(if (_weightA == 0f) 0.001f else _weightA)
                                .height(4.dp)
                                .background(color = MaterialTheme.colors.secondary)
                        )
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color = cardColor, shape = CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .weight(if (_weightB == 0f) 0.001f else _weightB)
                                .height(4.dp)
                                .background(color = MaterialTheme.colors.secondaryVariant)
                        )
                    }
                }
                if (isComparisonVisible) {
                    Text(
                        modifier = Modifier
                            .width(valueWidth)
                            .wrapContentHeight()
                            .padding(2.dp),
                        text = "$valueB $unit",
                        textAlign = TextAlign.Start,
                        fontSize = comparisonBarValueTextSize
                    )
                }
            }
        }
    }
}
