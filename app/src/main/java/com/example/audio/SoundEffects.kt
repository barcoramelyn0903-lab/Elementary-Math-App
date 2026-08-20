package com.example.audio

import android.media.AudioManager
import android.media.ToneGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SoundEffects(private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)) {

    private var toneGen: ToneGenerator? = null
    var isSoundEnabled: Boolean = true

    init {
        try {
            toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 80)
        } catch (e: Exception) {
            toneGen = null
        }
    }

    fun playCorrect() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 120)
                delay(120)
                toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 180)
            } catch (_: Exception) {}
        }
    }

    fun playWrong() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_NACK, 200)
            } catch (_: Exception) {}
        }
    }

    fun playTap() {
        if (!isSoundEnabled) return
        try {
            toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP2, 50)
        } catch (_: Exception) {}
    }

    fun playVictoryFanfare() {
        if (!isSoundEnabled) return
        scope.launch {
            try {
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(120)
                toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
                delay(120)
                toneGen?.startTone(ToneGenerator.TONE_PROP_ACK, 300)
            } catch (_: Exception) {}
        }
    }

    fun release() {
        toneGen?.release()
        toneGen = null
    }
}
