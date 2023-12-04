package com.malka.androidappp.newPhase.presentation.addProduct.activity4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.databinding.ItemVideoAddBinding

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
        var currntposition = position
        holder.viewBinding.etAddVideo.setText(videoLinks[position])
        holder.viewBinding.etAddVideo.isEnabled = false
//        holder.viewBinding.etAddVideo.addTextChangedListener(object :
//            TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//
//            }
//            override fun afterTextChanged(s: Editable) {
//                if (s.isNullOrBlank()) {
//                    videoLinks[currntposition] = holder.viewBinding.etAddVideo.text.toString().trim()
//                    setOnTypingVideoLinkTypingVideoLinks.onTypeTypingVideoLink(
//                        holder.viewBinding.etAddVideo.text.toString().trim(), currntposition
//                    )
//                }
//            }
//        })
        holder.viewBinding.ivDelete.setOnClickListener {
            setOnTypingVideoLinkTypingVideoLinks.onDeleteItem(position)
        }
    }

    interface SetOnTypingVideoLinkTypingVideoLinks {
        fun onTypeTypingVideoLink(value: String, position: Int)
        fun onDeleteItem(position: Int)
    }
}