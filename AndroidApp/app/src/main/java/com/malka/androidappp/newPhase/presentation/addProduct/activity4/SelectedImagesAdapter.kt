package com.malka.androidappp.newPhase.presentation.addProduct.activity4

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemAddProductImgsBinding
import com.malka.androidappp.newPhase.domain.models.ImageSelectModel
import kotlinx.android.synthetic.main.activity_add_photo.*

class SelectedImagesAdapter(var data: List<ImageSelectModel>,var setOnSelectedMainImage:SetOnSelectedMainImage) :
    RecyclerView.Adapter<SelectedImagesAdapter.SelectedImagesViewHolder>() {

    lateinit var context: Context

    class SelectedImagesViewHolder(var viewBinding: ItemAddProductImgsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImagesViewHolder {
        context = parent.context
        return SelectedImagesViewHolder(
            ItemAddProductImgsBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: SelectedImagesViewHolder, position: Int) {
        holder.viewBinding.addImgIc.setImageURI(data[position].uri)
        if(data[position].is_main){
            holder.viewBinding.isSelectCb.setImageResource(R.drawable.checkbox_selected)
        }else{
            holder.viewBinding.isSelectCb.setImageResource(R.drawable.checkbox_un_selected_2)
        }
        holder.viewBinding.isSelectCb.setOnClickListener {
            setOnSelectedMainImage.onSelectedMainImage(position)
        }

    }

    interface SetOnSelectedMainImage{
        fun onSelectedMainImage(position: Int)
    }
}