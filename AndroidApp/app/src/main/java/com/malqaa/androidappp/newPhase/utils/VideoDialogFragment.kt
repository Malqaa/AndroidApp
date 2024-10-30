package com.malqaa.androidappp.newPhase.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.malqaa.androidappp.R

class VideoDialogFragment : DialogFragment() {

    private lateinit var playerView: PlayerView
    private var isPlaying = false
    private var player: ExoPlayer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.player_view)
        initializePlayer()
    }

    private fun initializePlayer() {
        val videoUrl = arguments?.getString("video_url") ?: return
        player = ExoPlayer.Builder(requireContext()).build()
        playerView.player = player
        val mediaItem = MediaItem.fromUri(videoUrl)
        player?.setMediaItem(mediaItem)
        player?.prepare()
        player?.playWhenReady = true
        isPlaying = true
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.release()
        player = null
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