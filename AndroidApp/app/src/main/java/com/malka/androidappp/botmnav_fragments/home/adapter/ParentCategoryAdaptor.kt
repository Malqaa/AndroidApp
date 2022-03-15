package com.malka.androidappp.botmnav_fragments.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.DynamicList
import com.malka.androidappp.botmnav_fragments.home.model.Generaladvetisement
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show


class ParentCategoryAdaptor(
    val listCar: List<DynamicList>?,val currentfragment: Fragment
) :
    RecyclerView.Adapter<ParentCategoryAdaptor.CarViewHolder>() {

    var onItemClick: ((Generaladvetisement) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.parenet_category_item, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val list: DynamicList = listCar!![position]
        holder.run {
            list.run {
                when(typeName){
                    "category"->{
                        category_type!!.show()
                        product_list_layout!!.hide()
                    }
                    "list"->{
                        category_type!!.hide()
                        product_list_layout!!.show()
                    }
                }
                detail_tv!!.text=detail
                category_name_tv!!.text=category_name
                category_name_tv_2!!.text=category_name
                product_rcv!!.adapter =  GeneralAdvertisementAdapter(product)
                category_icon_iv!!.setImageResource(category_icon)
            }
        }

    }



    override fun getItemCount(): Int {
        return if(listCar!=null) listCar!!.size else 0
    }

    inner class CarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var category_icon_iv: ImageView? = null
        var category_name_tv: TextView? = null
        var category_name_tv_2: TextView? = null
        var detail_tv: TextView? = null
        var product_rcv: RecyclerView? = null
        var category_type: LinearLayout? = null
        var product_list_layout: LinearLayout? = null


        init {
            category_icon_iv = itemView.findViewById(R.id.category_icon_iv)
            category_name_tv = itemView.findViewById(R.id.category_name_tv)
            category_name_tv_2 = itemView.findViewById(R.id.category_name_tv_2)
            product_rcv = itemView.findViewById(R.id.product_rcv)
            detail_tv = itemView.findViewById(R.id.detail_tv)
            category_type = itemView.findViewById(R.id.category_type)
            product_list_layout = itemView.findViewById(R.id.product_list_layout)

        }



    }
}