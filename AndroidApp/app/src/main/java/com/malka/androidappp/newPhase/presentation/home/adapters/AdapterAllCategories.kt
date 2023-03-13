package com.malka.androidappp.newPhase.presentation.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.BaseViewHolder
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import kotlinx.android.synthetic.main.item_categories_card_in_home.view.*


class AdapterAllCategories(
    val allCategories: List<Category>,
    var context: Context,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_categories_card_in_home, parent, false)
        return BaseViewHolder(view)

    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.view.run {
            allCategories.get(position).run {
//                bgline.hide()
//                is_selectimage.hide()
                category_name_tv.text = name
                if(image.isNullOrEmpty()){
                    category_icon.setImageResource(R.drawable.product_attribute_bg2)
                }else{
                    HelpFunctions.loadCompanyImage(context,category_icon,image)
//                    Picasso.get()
//                        .load(image)
//                        .into(category_icon)
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