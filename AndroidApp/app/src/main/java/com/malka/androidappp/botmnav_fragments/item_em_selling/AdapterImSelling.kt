package com.malka.androidappp.botmnav_fragments.item_em_selling

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.item_detail.all_categories.AdapterSubCategories
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.all_categories_card.view.*
import kotlinx.android.synthetic.main.item_im_selling_card.view.*
import kotlinx.android.synthetic.main.unsold_business_card.view.*

class AdapterImSelling(
    val itemImSellingPost: ArrayList<ModelImSelling>,
    var context: ItemImSelling,
    val listener: AdapterImSelling.OnItemClickListener

) : RecyclerView.Adapter<AdapterImSelling.AdapterImSellingViewHolder>() {

    inner class AdapterImSellingViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val itemMsellingicon: ImageView = itemview.item_imselling_imgg
        val itemMsellingtitle: TextView = itemview.item_imselling_title
        val itemMsellingdescrip: TextView = itemview.item_imselling_desc
        val itemMsellingreview: TextView = itemview.item_imselling_reserveprice
        val itemMsellingbuyprice: TextView = itemview.item_imselling_buynowprice

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClickHandler(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterImSellingViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_im_selling_card, parent, false)
        return AdapterImSellingViewHolder(view)

    }

    override fun getItemCount() = itemImSellingPost.size

    override fun onBindViewHolder(holder: AdapterImSellingViewHolder, position: Int) {

        if (itemImSellingPost[position].prodimg != null)
            Picasso.get()
                .load(ApiConstants.IMAGE_URL + itemImSellingPost[position].prodimg)
                .into(holder.itemMsellingicon) else holder.itemMsellingicon.setImageResource(R.drawable.cam)
        holder.itemMsellingtitle.text = itemImSellingPost[position].prodtitle
        holder.itemMsellingdescrip.text = itemImSellingPost[position].proddes
        holder.itemMsellingreview.text = itemImSellingPost[position].review
        holder.itemMsellingbuyprice.text = itemImSellingPost[position].buynowprice
    }

    interface OnItemClickListener {
        fun OnItemClickHandler(position: Int) {}
    }


}