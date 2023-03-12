package com.malka.androidappp.fragments.my_product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.my_product_cardview.view.*


class AdapterMyProduct(
    val myProductPost: ArrayList<ModelMyProduct>,
    var context: MyProduct,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterMyProduct.AdapterMyProductViewHolder>() {


    inner class AdapterMyProductViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {

        val myproducticon: ImageView = itemview.myproduct_imgg
        val myProductname: TextView = itemview.myproduct_title
        val myProductcode: TextView = itemview.code
        val myProductSKU: TextView = itemview.sku
        val myProductstatus: TextView = itemview.status
        val myProductduration: TextView = itemview.duration
        val myProductstock: TextView = itemview.stock
        val myProductstart: TextView = itemview.start
        val myProductreserve: TextView = itemview.reserve
        val myProductbuynow: TextView = itemview.myproduct_buynowprice
        val myProductedit: ImageView = itemview.editicon

        init {
            itemview.setOnClickListener(this)
            myProductedit.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.OnItemClick(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterMyProductViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.my_product_cardview, parent, false)
        return AdapterMyProductViewHolder(view)

    }

    override fun getItemCount() = myProductPost.size

    override fun onBindViewHolder(holder: AdapterMyProductViewHolder, position: Int) {

        if (myProductPost[position].images != null)
            Picasso.get()
                .load(Constants.IMAGE_URL + myProductPost[position].images!!.get(0))
                .into(holder.myproducticon) else holder.myproducticon.setImageResource(R.drawable.cam)

        holder.myProductname.text = myProductPost[position].title
        holder.myProductcode.text = myProductPost[position].code
        holder.myProductSKU.text = myProductPost[position].sKU
        holder.myProductstatus.text = myProductPost[position].id
        holder.myProductduration.text = myProductPost[position].listingDuration
        holder.myProductstock.text = myProductPost[position].stock
        holder.myProductstart.text = myProductPost[position].startPrice
        holder.myProductreserve.text = myProductPost[position].specifyReserve
        holder.myProductbuynow.text = myProductPost[position].buyNow
        holder.myProductedit.setImageResource(R.drawable.edit)


        holder.myProductedit.setOnClickListener(View.OnClickListener {
            listener.onEdit(position)
        })

    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int) {}
        fun onEdit(position: Int) {}
    }


}

