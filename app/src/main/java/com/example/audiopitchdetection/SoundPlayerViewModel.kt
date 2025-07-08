package com.example.audiopitchdetection

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class SoundPlayerViewModel() : ViewModel() {

    private var currentPlayer: MediaPlayer? = null
    private var currentPlayerFileId by mutableStateOf<Int?>(null)
    var isEnable by mutableStateOf(false)

    fun playSound(context: Context, audioFileId: Int) {
        currentPlayerFileId = audioFileId
        if (currentPlayerFileId != null) {
            val player = MediaPlayer.create(context, currentPlayerFileId!!)
            stopAudio()
            currentPlayer = player
            isEnable = true
            currentPlayer?.apply {
                start()
                setOnCompletionListener {
                    stopAudio()
                }
            }
        }
    }

    private fun stopAudio() {
        currentPlayerFileId = null
        isEnable = false
        currentPlayer?.apply {
            stop()
            release()
        }
        currentPlayer = null
    }
}