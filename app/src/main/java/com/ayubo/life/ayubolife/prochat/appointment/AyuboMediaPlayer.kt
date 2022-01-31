package com.ayubo.life.ayubolife.prochat.appointment

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.TextView
import com.ayubo.life.ayubolife.prochat.util.CommonUtils
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.TransferListener
import com.google.android.exoplayer2.util.Util

class AyuboMediaPlayer(
    val context: Context,
    val playButton: CompoundButton,
    val seekBar: SeekBar,
    val txtDurationSend: TextView,
    val url: String
) {
    private lateinit var playerControl: PlayerControl

    private var player: SimpleExoPlayer?

    private var dragging: Boolean = false

    private var events: PlayerEventListener

    init {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)

        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val mediaDataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "mediaPlayerSample"),
            bandwidthMeter as TransferListener<in DataSource>
        )
        val mediaSource =
            ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(Uri.parse(url))

        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        playerControl = PlayerControl(player) // we init player
        events = PlayerEventListener(playButton, seekBar)
        player!!.addListener(events)
        player!!.playWhenReady = false
        player!!.prepare(mediaSource, false, false)
    }

    fun pause() {
        playerControl.pause()
    }

    fun release() {
        if (handler != null) {
            handler.removeCallbacks(updateProgressAction)
        }
        if (player != null) {
            player!!.removeListener(events)
            player!!.stop()
            player!!.release()
            player = null
        }
    }

    fun playAudio() {
        updateProgressBar()
        playerControl.start()
    }

    private val handler: Handler = Handler()

    private fun updateProgressBar() {
        val position = if (player == null) 0 else player!!.currentPosition
        if (!dragging) {
            if (player != null) {
                seekBar.progress = player!!.currentPosition.toInt()
            } else {
                return
            }
        }
        val bufferedPosition = if (player == null) 0 else player!!.bufferedPosition
        seekBar.setSecondaryProgress(bufferedPosition.toInt())
        // Remove scheduled updates.
        handler.removeCallbacks(updateProgressAction)
        // Schedule an update if necessary.
        val playbackState = if (player == null) Player.STATE_IDLE else player!!.playbackState
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            var delayMs: Long
            if (player!!.playWhenReady && playbackState == Player.STATE_READY) {
                delayMs = 1000 - position % 1000
                if (delayMs < 200) {
                    delayMs += 1000
                }
            } else {
                delayMs = 1000
            }
            handler.postDelayed(updateProgressAction, delayMs)
        }
    }

    private val updateProgressAction = { updateProgressBar() }

    private inner class PlayerEventListener(val playButton: CompoundButton, val seekBar: SeekBar) :
        Player.DefaultEventListener() {

        override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
            when (playbackState) {
                Player.STATE_IDLE       // The player does not have any media to play yet.
                -> {
                }
                Player.STATE_BUFFERING  // The player is buffering (loading the content)
                -> {
                }
                Player.STATE_READY      // The player is able to immediately play
                -> {
                    seekBar.max = playerControl.duration
//                    txtDurationSend.text = CommonUtils.milliSecondsToTimer(playerControl.duration.toLong())

                    seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                            if (player != null) {
                                txtDurationSend.text =
                                    CommonUtils.milliSecondsToTimer(playerControl.currentPosition.toLong())
                                if (dragging) {
                                    val playbackState = player!!.playbackState
                                    if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
                                        if (playbackState == Player.STATE_READY) {
                                            playerControl.seekTo(i)
                                        }
                                    }
                                }
                            }

                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar) {
                            dragging = true
                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar) {
                            dragging = false
                        }
                    })
                }
                Player.STATE_ENDED      // The player has finished playing the media
                -> {
                    playerControl.seekToDefaultPosition()
                    playerControl.pause()
                    playButton.isChecked = false
                }
            }
        }
    }


}