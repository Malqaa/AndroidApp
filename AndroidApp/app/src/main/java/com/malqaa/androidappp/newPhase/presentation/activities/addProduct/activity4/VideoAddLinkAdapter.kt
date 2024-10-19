package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malqaa.androidappp.databinding.ItemVideoAddBinding

class VideoAddLinkAdapter(
    private var videoLinks: ArrayList<String>,
    private var setOnTypingVideoLinkTypingVideoLinks: SetOnTypingVideoLinkTypingVideoLinks
) :
    Adapter<VideoAddLinkAdapter.VideoAddLinkViewHolder>() {

    class VideoAddLinkViewHolder(var viewBinding: ItemVideoAddBinding) :
        ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAddLinkViewHolder {
        return VideoAddLinkViewHolder(
            ItemVideoAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = videoLinks.size

    override fun onBindViewHolder(
        holder: VideoAddLinkViewHolder,
        position: Int
    ) {
        holder.viewBinding.etAddVideo.setText(videoLinks[position])
        holder.viewBinding.etAddVideo.isEnabled = false
        holder.viewBinding.ivDelete.setOnClickListener {
            setOnTypingVideoLinkTypingVideoLinks.onDeleteItem(position)
        }
    }

    fun updateAdapter(videoLinks: ArrayList<String>) {
        this.videoLinks = videoLinks
        notifyDataSetChanged()
    }

    interface SetOnTypingVideoLinkTypingVideoLinks {
        fun onTypeTypingVideoLink(value: String, position: Int)
        fun onDeleteItem(position: Int)
    }
}