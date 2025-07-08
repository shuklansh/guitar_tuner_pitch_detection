package com.example.audiopitchdetection

import android.content.Context
import androidx.compose.runtime.Composable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import java.nio.file.WatchEvent

@Composable
fun PitchDetectorScreen(
    context: Context,
    viewModel: PitchViewModel,
    onBackPress: () -> Unit
) {
    var isListening by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Back", color = Color.Black, modifier = Modifier.clickable {
                onBackPress()
            })
        }
        Text("Pitch: ${viewModel.pitch} Hz", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .height(250.dp)
                .width(120.dp)
                .background(Color.LightGray, RoundedCornerShape(12.dp)),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .background(Color.Blue, RoundedCornerShape(12.dp))
                    .height(
                        viewModel.pitch.coerceIn(0f, 1000f).toInt().dp
                    )
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            isListening = !isListening
            if (isListening) {
                viewModel.startListening(context = context)
            } else {
                viewModel.stopListening()
            }
        }) {
            Text(if (isListening) "Stop Listening" else "Start Listening")
        }
    }
}