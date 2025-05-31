package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.all_categories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.ChooseCateFragment
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import kotlinx.android.synthetic.main.all_categories_card.view.*

class AdapterAllCategories(
    private val allCategories: ArrayList<AllCategoriesModel>,
    var context: ChooseCateFragment,
    val listener: AdapterAllCategories.OnItemClickListener
) : RecyclerView.Adapter<AdapterAllCategories.AdapterAllCategoriesViewHolder>() {

    inner class AdapterAllCategoriesViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val categoryName: TextView = itemview.categoryName
        val categoryIcon: ImageView = itemview.arrowimgmobile

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


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterAllCategoriesViewHolder {
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.all_categories_card, parent, false)
        return AdapterAllCategoriesViewHolder(view)

    }

    override fun getItemCount() = allCategories.size

    override fun onBindViewHolder(holder: AdapterAllCategoriesViewHolder, position: Int) {

        holder.categoryName.text = allCategories[position].categoryName

        if (!allCategories[position].isCategory) {
            holder.categoryIcon.setImageResource(R.drawable.ic_block)
        } else {
            holder.categoryIcon.setImageResource(R.drawable.arrow_right_black_24dp)
        }


    }

    interface OnItemClickListener {
        fun OnItemClickHandler(position: Int) {}
    }

}