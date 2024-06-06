package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity2

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.utils.helper.BaseViewHolder
import com.malqaa.androidappp.newPhase.utils.Extension
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Category
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.all_categories_cardview.view.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.category_icon
import kotlinx.android.synthetic.main.all_categories_cardview.view.category_name_tv

class AdapterAllCategoriesAdapter(
    private var allCategories: List<Category>,
    var onItemClick: (position: Int) -> Unit
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
            allCategories[position].run {
                category_name_tv.text = name
                if (allCategories[position].is_select) {
                    AddProductObjectData.selectedCategoryId = allCategories[position].id
                    // bgline.show()
                    is_selectimage.show()
                    category_icon.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.bg
                        )
                    )


                } else {
                    bgline.hide()
                    is_selectimage.hide()
                    category_icon.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
//                    category_icon.borderColor=  ContextCompat.getColor(
//                        context,
//                        R.color.white
//                    )
                }



                if (image.isNullOrEmpty()) {
                    category_icon.setImageResource(R.mipmap.ic_launcher)
                } else {
                    Glide.with(context).load(image).error(R.mipmap.ic_launcher_round).into(category_icon)

//                    Extension.loadThumbnail(
//                        context,
//                        image,
//                        category_icon,
//                        null
//                    )

//                    Picasso.get()
//                        .load(Constants.IMAGE_URL + image)
//                        .into(category_icon)
                }
                holder.itemView.setOnClickListener {
                    allCategories.forEach {
                        it.is_select = false
                    }
                    allCategories.get(position).is_select = true
                    notifyDataSetChanged()
                    onItemClick.invoke(position)
                }
            }
        }

    }


}