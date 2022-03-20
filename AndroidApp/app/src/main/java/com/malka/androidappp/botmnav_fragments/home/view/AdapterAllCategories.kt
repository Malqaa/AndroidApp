package com.malka.androidappp.botmnav_fragments.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.hide
import com.malka.androidappp.network.constants.ApiConstants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.all_categories_cardview.view.*

class AdapterAllCategories(
    val allCategories: List<AllCategoriesModel>,
    var context: Context,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_categories_cardview, parent, false)
        return BaseViewHolder(view)

    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {

        holder.view.run {
            allCategories.get(position).run {
//                val params: ViewGroup.LayoutParams = fullview.layoutParams
//                params.width = resources.getDimension(R.dimen._90sdp).toInt()
//                params.height = params.height
//                fullview.layoutParams = params
                bgline.hide()
                is_selectimage.hide()
                category_name_tv.text = categoryName
                if(imagePath.isNullOrEmpty()){
                    category_icon.setImageResource(R.drawable.product_attribute_bg2)
                }else{
                    Picasso.get()
                        .load(ApiConstants.IMAGE_URL + imagePath)
                        .into(category_icon)
                }

                setOnClickListener {
                    listener.OnItemClick(position)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun OnItemClick(position: Int) {}
    }

}