package com.example.audiopitchdetection

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlin.math.pow

class PitchViewModel : ViewModel() {
    private val sampleRate = 44100
    private val bufferSize = 2048
    private var isRecording = false
    private var audioRecord: AudioRecord? = null
    var pitch by mutableStateOf(0f)
        private set

    fun startListening(context: Context) {
        if (isRecording) return
        isRecording = true

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            viewModelScope.launch(Dispatchers.IO) {
                val buffer = ShortArray(bufferSize)
                audioRecord?.startRecording()

                while (isRecording) {
                    val read = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                    if (read > 0) {
                        pitch = computePitch(buffer)
                    }
                }
            }
        }
    }

    fun stopListening() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }

    private fun computePitch(buffer: ShortArray): Float {
        val n = buffer.size
        val real = FloatArray(n)
        val imag = FloatArray(n)

        // Apply Hamming window and convert to float
        for (i in buffer.indices) {
            val window = 0.54 - 0.46 * kotlin.math.cos(2.0 * Math.PI * i / (n - 1))
            real[i] = buffer[i] * window.toFloat()
            imag[i] = 0f
        }

        // Perform FFT
        fft(real, imag)

        // Calculate magnitude and find peak
        var maxIndex = 0
        var maxMag = 0f
        for (i in 1 until n / 2) {
            val mag = real[i] * real[i] + imag[i] * imag[i]
            if (mag > maxMag) {
                maxMag = mag
                maxIndex = i
            }
        }

        return maxIndex * sampleRate / n.toFloat()
    }
}

fun fft(real: FloatArray, imag: FloatArray) {
    val n = real.size
    val levels = (kotlin.math.log2(n.toFloat())).toInt()

    // Bit reversal
    for (i in 0 until n) {
        val j = Integer.reverse(i).ushr(32 - levels)
        if (j > i) {
            real[i] = real[j].also { real[j] = real[i] }
            imag[i] = imag[j].also { imag[j] = imag[i] }
        }
    }

    var size = 2
    while (size <= n) {
        val halfSize = size / 2
        val tableStep = n / size
        for (i in 0 until n step size) {
            for (j in 0 until halfSize) {
                val k = j * tableStep
                val angle = -2.0 * Math.PI * k / n
                val wReal = kotlin.math.cos(angle).toFloat()
                val wImag = kotlin.math.sin(angle).toFloat()
                val tReal = wReal * real[i + j + halfSize] - wImag * imag[i + j + halfSize]
                val tImag = wReal * imag[i + j + halfSize] + wImag * real[i + j + halfSize]
                real[i + j + halfSize] = real[i + j] - tReal
                imag[i + j + halfSize] = imag[i + j] - tImag
                real[i + j] += tReal
                imag[i + j] += tImag
            }
        }
        size *= 2
    }
}

// Helper function for FFT calculation
fun exp(x: Double): Float = (Math.E.pow(x)).toFloat()