package com.malka.androidappp.fragments.favourite_frag.seller_fav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.favcard_seller.view.*


class AdapterFavSeller(
    val favsellerposts: ArrayList<ModelFavSeller>,
    var context: SellerFav
) : RecyclerView.Adapter<AdapterFavSeller.AdapterFavSellerViewHolder>() {

    var onItemClick: ((ModelFavSeller) -> Unit)? = null

    inner class AdapterFavSellerViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val sellericon: ImageView = itemview.circle_image
        val sellername: TextView = itemview.textView10
        val listingitem: TextView = itemview.textView13
        val btn_remove_fav_seller: Button = itemview.btn_remove_fav_seller

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(favsellerposts[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFavSellerViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.favcard_seller, parent, false)
        return AdapterFavSellerViewHolder(view)
    }

    override fun getItemCount() = favsellerposts.size

    override fun onBindViewHolder(holder: AdapterFavSellerViewHolder, position: Int) {

        holder.sellericon.setImageResource(favsellerposts[position].imgseller)
        holder.sellername.text = favsellerposts[position].favsellername
        holder.listingitem.text = favsellerposts[position].sellercurrentlisting
        holder.btn_remove_fav_seller.setOnClickListener(View.OnClickListener {
            val resp = HelpFunctions.DeleteFromFavourite(
                sellerid = favsellerposts[position].sellerid,
                category = "",
                query = "",
                context = context
            )
            if(resp)
                ConstantObjects.userfavourite!!.data.seller.removeAt(position);
            context.RefreshScreen()
        })
    }
}