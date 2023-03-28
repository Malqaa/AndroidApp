package com.malka.androidappp.newPhase.presentation.addProduct.activity2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.BaseViewHolder
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.all_categories_cardview.view.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.category_icon
import kotlinx.android.synthetic.main.all_categories_cardview.view.category_name_tv
import kotlinx.android.synthetic.main.item_categories_card_in_home.view.*

class AdapterAllCategoriesAdapter(
    private val allCategories: List<Category>,
    var onItemClick: (position:Int) -> Unit
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
                if (allCategories.get(position).is_select) {
                   // bgline.show()
                    is_selectimage.show()
                    category_icon.borderColor=  ContextCompat.getColor(
                        context,
                        R.color.bg
                    )

                }
                else {
                    bgline.hide()
                    is_selectimage.hide()
                    category_icon.borderColor=  ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                }



                if(image.isNullOrEmpty()){
                    category_icon.setImageResource(R.mipmap.malqa_iconn)
                }else{
                    HelpFunctions.loadCompanyImage(context,category_icon,image)
//                    Picasso.get()
//                        .load(Constants.IMAGE_URL + image)
//                        .into(category_icon)
                }
                holder.itemView.setOnClickListener{
                    allCategories.forEach {
                        it.is_select=false
                    }
                    allCategories.get(position).is_select=true
                    notifyDataSetChanged()
                    onItemClick.invoke(position)
                }
            }
        }

    }



}