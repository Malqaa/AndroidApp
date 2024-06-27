package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity4

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemAddProductImgsBinding
import com.malqaa.androidappp.newPhase.domain.models.ImageSelectModel
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton

class SelectedImagesAdapter(
    var data: ArrayList<ImageSelectModel>,
    var setOnSelectedMainImage: SetOnSelectedMainImage
) :
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
        if (data[position].isEdit) {
//            data[position].is_main = true
            if (!data[position].url.isNullOrEmpty() && data[position].type == 1) {
                PicassoSingleton.getPicassoInstance()
                    .load(data[position].url.replace("http", "https"))
                    .placeholder(R.drawable.splash_logo).error(R.drawable.splash_logo)
                    .into(holder.viewBinding.addImgIc)
                data[position].is_main = data[position].isMainMadia || data[position].is_main
            } else {
                if (data[position].uri != null)
                    holder.viewBinding.addImgIc.setImageURI(data[position].uri)
            }
        } else
            holder.viewBinding.addImgIc.setImageURI(data[position].uri)

        if (data[position].is_main) {
            holder.viewBinding.isSelectCb.setImageResource(R.drawable.checkbox_selected)
        } else {
            holder.viewBinding.isSelectCb.setImageResource(R.drawable.checkbox_un_selected_2)
        }

        holder.viewBinding.isSelectCb.setOnClickListener {

            if (data[position].is_main) {
                data[position].is_main = false
                holder.viewBinding.isSelectCb.setImageResource(R.drawable.checkbox_un_selected_2)
            } else {
                holder.viewBinding.isSelectCb.setImageResource(R.drawable.checkbox_selected)
                setOtherFalse(data[position])
                data[position].is_main = true
                data[position].isMainMadia = true
            }
            setOnSelectedMainImage.onSelectedMainImage(data, position)
        }

        holder.viewBinding.imgRemove.setOnClickListener {
            setOnSelectedMainImage.onRemoveImage(data, position)
        }



    }

    private fun setOtherFalse(itemSelect : ImageSelectModel ){
        for(i in data.indices){
            data[i].isMainMadia = false
            data[i].is_main =false
        }
    }
    fun updateData(listMedia: ArrayList<ImageSelectModel>) {
        this.data = listMedia
        notifyDataSetChanged()
    }

    interface SetOnSelectedMainImage {
        fun onRemoveImage(data: ArrayList<ImageSelectModel>, position: Int)
        fun onSelectedMainImage(data: ArrayList<ImageSelectModel>, position: Int)
    }
}