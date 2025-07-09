package com.example.audiopitchdetection

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GuitarTunerScreen(
    context: Context,
    viewModel: PitchViewModel,
    onBackPress: () -> Unit
) {
    /*
    6	E₂	E2	82.41
    5	A₂	A2	110.00
    4	D₃	D3	146.83
    3	G₃	G3	196.00
    2	B₃	B3	246.94
    1	E₄	E4	329.63
    */
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
        val soundPlayer = SoundPlayerViewModel()
        var selectedTuning by remember { mutableStateOf<Int?>(null) }

        LaunchedEffect(selectedTuning) {
            if (selectedTuning != null) {
                viewModel.startListening(context = context)
            } else {
                viewModel.stopListening()
            }
        }

        if (selectedTuning != null) {
            Text(
                "Pitch: ${"%.2f".format(viewModel.pitch).toFloat()} Hz",
                style = MaterialTheme.typography.headlineMedium,
                color = if (selectedTuning != null) {
                    when (selectedTuning) {
                        1 -> {
                            if ("%.2f".format(viewModel.pitch).toFloat() == 990.53f) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        2 -> {
                            if ("%.2f".format(viewModel.pitch).toFloat() == 732.13f) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        3 -> {
                            if ("%.2f".format(viewModel.pitch).toFloat() == 129.2f) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        4 -> {
                            if ("%.2f".format(viewModel.pitch).toFloat() == 581.4f) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        5 -> {
                            if ("%.2f".format(viewModel.pitch).toFloat() == 129.2f) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        6 -> {
                            if ("%.2f".format(viewModel.pitch).toFloat() == 667.53f) {
                                Color.Green
                            } else {
                                Color.Red
                            }
                        }

                        else -> {
                            Color.Black
                        }
                    }
                } else {
                    Color.Black
                }
            )
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.acoustic_tuner),
                contentDescription = "Guitar Tuner",
                modifier = Modifier.fillMaxSize().padding(horizontal = 64.dp)
            )
            Column(
                modifier = Modifier.padding(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button({
                        selectedTuning = 1
                        soundPlayer.playSound(
                            context,
                            R.raw.string1_e4
                        ) {
                            selectedTuning = null
                        }
                    }) {
                        Text("E4")
                    }
                    Button({
                        selectedTuning = 2
                        soundPlayer.playSound(
                            context,
                            R.raw.string2_b3
                        ) {
                            selectedTuning = null
                        }
                    }) {
                        Text("B3")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button({
                        selectedTuning = 3
                        soundPlayer.playSound(
                            context,
                            R.raw.string3_g3
                        ) {
                            selectedTuning = null
                        }
                    }) {
                        Text("G3")
                    }
                    Button({
                        selectedTuning = 4
                        soundPlayer.playSound(
                            context,
                            R.raw.string4_d3
                        ) {
                            selectedTuning = null
                        }
                    }) {
                        Text("D3")
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button({
                        selectedTuning = 5
                        soundPlayer.playSound(
                            context,
                            R.raw.string5_a2
                        ) {
                            selectedTuning = null
                        }
                    }) {
                        Text("A2")
                    }
                    Button({
                        selectedTuning = 6
                        soundPlayer.playSound(
                            context,
                            R.raw.string6_e2
                        ) {
                            selectedTuning = null
                        }
                    }) {
                        Text("E2")
                    }
                }
            }
        }
    }
}
