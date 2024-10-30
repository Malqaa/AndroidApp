package com.malqaa.androidappp.newPhase.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.malqaa.androidappp.databinding.FragmentYoutubePlayerDialogBinding

class YouTubePlayerDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentYoutubePlayerDialogBinding
    private var videoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoId = it.getString(ARG_VIDEO_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentYoutubePlayerDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupWebViewYouTubePlayer(webView = binding.webView, videoId = videoId)
    }

    companion object {
        private const val ARG_VIDEO_ID = "video_id"

        fun newInstance(videoId: String): YouTubePlayerDialogFragment {
            val fragment = YouTubePlayerDialogFragment()
            val args = Bundle().apply {
                putString(ARG_VIDEO_ID, videoId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webView.destroy()
    }
}
