package com.malka.androidappp.botmnav_fragments.home.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.helper.BaseViewHolder
import kotlinx.android.synthetic.main.all_categories_cardview.view.*

class AdapterAllCategories(
    val allCategories: ArrayList<AllCategoriesModel>,
    var context: HomeFragment,
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
                category_name_tv.text = categoryName
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