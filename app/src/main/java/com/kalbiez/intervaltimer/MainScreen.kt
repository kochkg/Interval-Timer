package com.kalbiez.intervaltimer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kalbiez.intervaltimer.service.ServiceHelper
import com.kalbiez.intervaltimer.service.StopwatchService
import com.kalbiez.intervaltimer.service.StopwatchState
import com.kalbiez.intervaltimer.ui.theme.Light
import com.kalbiez.intervaltimer.util.Constants.ACTION_SERVICE_CANCEL
import com.kalbiez.intervaltimer.util.Constants.ACTION_SERVICE_START
import com.kalbiez.intervaltimer.util.Constants.ACTION_SERVICE_STOP

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(stopwatchService: StopwatchService) {
        val context = LocalContext.current
        val hours by stopwatchService.hours
        val minutes by stopwatchService.minutes
        val seconds by stopwatchService.seconds
        val currentState by stopwatchService.currentState

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Column(
                        modifier = Modifier.weight(weight = 9f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        AnimatedContent(label = "hoursAnim", targetState = hours, transitionSpec = { addAnimation() }) {
                                Text(
                                        text = hours,
                                        style = TextStyle(
                                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                color = if (hours == "00") Color.White else Blue
                                        )
                                )
                        }
                        AnimatedContent(label = "minutesAnim", targetState = minutes, transitionSpec = { addAnimation() }) {
                                Text(
                                        text = minutes, style = TextStyle(
                                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                color = if (minutes == "00") Color.White else Blue
                                        )
                                )
                        }
                        AnimatedContent(label = "secondsAnim", targetState = seconds, transitionSpec = { addAnimation() }) {
                                Text(
                                        text = seconds, style = TextStyle(
                                                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                                fontWeight = FontWeight.Bold,
                                                color = if (seconds == "00") Color.White else Blue
                                        )
                                )
                        }
                }
                Row(modifier = Modifier.weight(weight = 1f)) {
                        Button(
                                modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(0.8f),
                                onClick = {
                                        ServiceHelper.triggerForegroundService(
                                                context = context,
                                                action = if (currentState == StopwatchState.Started) ACTION_SERVICE_STOP
                                                else ACTION_SERVICE_START
                                        )
                                }, colors = ButtonDefaults.buttonColors(
                                        containerColor = if (currentState == StopwatchState.Started) Red else Blue,
                                        contentColor = Color.White
                                )
                        ) {
                                Text(
                                        text = if (currentState == StopwatchState.Started) "Stop"
                                        else if ((currentState == StopwatchState.Stopped)) "Resume"
                                        else "Start"
                                )
                        }
                        Spacer(modifier = Modifier.width(30.dp))
                        Button(
                                modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight(0.8f),
                                onClick = {
                                        ServiceHelper.triggerForegroundService(
                                                context = context, action = ACTION_SERVICE_CANCEL
                                        )
                                },
                                enabled = seconds != "00" && currentState != StopwatchState.Started,
                                colors = ButtonDefaults.buttonColors(disabledContainerColor = Light)
                        ) {
                                Text(text = "Cancel")
                        }
                }
        }
}

@ExperimentalAnimationApi
fun addAnimation(duration: Int = 600): ContentTransform {
        return (slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeIn(
                animationSpec = tween(durationMillis = duration)
        )).togetherWith(slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> height } + fadeOut(
                animationSpec = tween(durationMillis = duration)
        ))
}