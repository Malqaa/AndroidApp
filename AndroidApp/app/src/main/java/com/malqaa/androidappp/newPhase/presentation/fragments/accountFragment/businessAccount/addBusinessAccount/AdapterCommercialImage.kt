package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.businessAccount.addBusinessAccount

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.databinding.RowImageCommercialBinding
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton

class AdapterCommercialImage(private var listBitMap: ArrayList<Uri>, val iRemoveImg: IRemoveImg) :
    RecyclerView.Adapter<AdapterCommercialImage.CommercialViewHolder>() {

    lateinit var context: Context

    class CommercialViewHolder(var viewBinding: RowImageCommercialBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommercialViewHolder {
        context = parent.context
        return CommercialViewHolder(
            RowImageCommercialBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = listBitMap.size


    override fun onBindViewHolder(holder: CommercialViewHolder, position: Int) {
        PicassoSingleton.getPicassoInstance().load(listBitMap[position])
            .into(holder.viewBinding.commercialImg)


        holder.viewBinding.imgClose.setOnClickListener {
            iRemoveImg.onClickClose(position)
        }
    }

    fun updateAdapter(listBitMap: ArrayList<Uri>) {
        this.listBitMap = listBitMap
        notifyDataSetChanged()
    }

    interface IRemoveImg {
        fun onClickClose(pos: Int)
    }
}