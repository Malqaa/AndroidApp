package com.malka.androidappp.newPhase.presentation.homeScreen.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.BaseViewHolder
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_categories_card_in_home.view.*
import java.lang.Exception


class AdapterAllCategories(
    val allCategories: List<Category>,
    val listener: OnItemClickListener
) : RecyclerView.Adapter<BaseViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        context = parent.context
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
                if (image.isNullOrEmpty()) {
                    category_icon.setImageResource(R.drawable.product_attribute_bg2)
                } else {
//                    println("hhhhh "+image)
//                    Glide.with(context)
//                        .load(image).into(category_icon)
                    Extension.loadThumbnail(
                        context,
                        image,
                        category_icon, progressBar
                    )
                  // HelpFunctions.loadCompanyImage(context, category_icon, image)
//                    Glide.with(context)
//                        .load(image).into(category_icon)
//                    Picasso.get()
//                        .load(image)
//                        .into(category_icon,object :Callback{
//                            override fun onSuccess() {
//                                progressBar.hide()
//                            }
//
//                            override fun onError(e: Exception?) {
//                            }

                   //     })
                }
                setOnClickListener {
                    listener.pnCategorySelected(position)
                }
            }
        }

    }

    interface OnItemClickListener {
        fun pnCategorySelected(position: Int) {}
    }

}