package com.malqaa.androidappp.newPhase.utils

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.malqaa.androidappp.R

class VideoDialogFragment : DialogFragment() {

    private lateinit var videoView: VideoView
    private lateinit var playPauseButton: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var seekBar: SeekBar
    private lateinit var currentTimeText: TextView
    private lateinit var durationText: TextView
    private val handler = Handler()
    private var isPlaying = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoView = view.findViewById(R.id.videoView)
        playPauseButton = view.findViewById(R.id.playPauseButton)
        progressBar = view.findViewById(R.id.progressBar)
        seekBar = view.findViewById(R.id.seekBar)
        currentTimeText = view.findViewById(R.id.currentTimeText)
        durationText = view.findViewById(R.id.durationText)

        initializePlayer()
        setupPlayPauseButton()
        setupSeekBar()
    }

    private fun initializePlayer() {
        val videoUrl = arguments?.getString("video_url") ?: return
        val uri: Uri = Uri.parse(videoUrl)
        videoView.setVideoURI(uri)

        // Show progress bar while loading
        videoView.setOnPreparedListener {
            progressBar.visibility = View.GONE
            playPauseButton.visibility = View.VISIBLE
            durationText.text = formatTime(videoView.duration)
            seekBar.max = videoView.duration
            updateSeekBar()
        }

        // Show progress bar on buffering
        videoView.setOnInfoListener { _, what, _ ->
            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                progressBar.visibility = View.VISIBLE
            } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                progressBar.visibility = View.GONE
            }
            false
        }

        videoView.setOnCompletionListener {
            playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            isPlaying = false
        }

        videoView.start()
        isPlaying = true
    }

    private fun setupPlayPauseButton() {
        playPauseButton.setOnClickListener {
            if (isPlaying) {
                videoView.pause()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            } else {
                videoView.start()
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            }
            isPlaying = !isPlaying
        }
    }

    private fun setupSeekBar() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    videoView.seekTo(progress)
                }
                currentTimeText.text = formatTime(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // Optional: Pause video playback when user starts seeking
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // Optional: Resume video playback when user stops seeking
            }
        })
    }

    private fun updateSeekBar() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                seekBar.progress = videoView.currentPosition
                currentTimeText.text = formatTime(videoView.currentPosition)
                handler.postDelayed(this, 1000)
            }
        }, 0)
    }

    private fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    companion object {
        fun newInstance(videoUrl: String): VideoDialogFragment {
            val fragment = VideoDialogFragment()
            val args = Bundle()
            args.putString("video_url", videoUrl)
            fragment.arguments = args
            return fragment
        }
    }
}

