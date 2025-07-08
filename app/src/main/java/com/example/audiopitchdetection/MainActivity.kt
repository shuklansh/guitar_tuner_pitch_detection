package com.example.audiopitchdetection

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.audiopitchdetection.ui.theme.AudioPitchDetectionTheme
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AudioPitchDetectionTheme {
                val context = LocalContext.current.applicationContext
                var screenSelected by remember { mutableStateOf(0) }
                Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this@MainActivity,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            123
                        )
                    }
                    val viewModel: PitchViewModel by viewModels()
                    if (screenSelected == 1) {
                        PitchDetectorScreen(context, viewModel) {
                            screenSelected = 0
                        }
                    } else if (screenSelected == 2) {
                        GuitarTunerScreen(context, viewModel) {
                            screenSelected = 0
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(
                                24.dp,
                                Alignment.CenterVertically
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                {
                                    screenSelected = 1
                                }
                            ) {
                                Text("Open Pitch Detector", color = Color.White)
                            }
                            Button(
                                {
                                    screenSelected = 2
                                }
                            ) {
                                Text("Open Guitar tuner", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}