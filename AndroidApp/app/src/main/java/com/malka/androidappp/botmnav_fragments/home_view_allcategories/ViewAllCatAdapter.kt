package com.malka.androidappp.botmnav_fragments.home_view_allcategories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.my_product.AdapterMyProduct


class ViewAllCatAdapter(
    val categories: List<ModelViewCategories>,
    var clickListener: onViewAllcateClickListener
) : RecyclerView.Adapter<ViewAllCatAdapter.ViewAllCatAdapterViewHolder>() {

    inner class ViewAllCatAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview),
        View.OnClickListener {
        val categoryName: TextView = itemview.findViewById(R.id.cate_name)
        val categoryImage: ImageView = itemview.findViewById(R.id.catimgg)
        val categoryCard: RelativeLayout = itemview.findViewById(R.id.viewall_cate_relative)
        val addToFav: ImageButton = itemview.findViewById(R.id.add_cat_fav)

        fun initialize(item: ModelViewCategories, action: onViewAllcateClickListener) {
            categoryCard.setOnClickListener() { action.onItemClick(item, adapterPosition) }
            addToFav.setOnClickListener(){action.addCatfav(item,adapterPosition)}
        }

        init {

            addToFav.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {

            }
        }

    }
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewAllCatAdapterViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_allcategories_cardview, parent, false)
            return ViewAllCatAdapterViewHolder(view)
        }


        override fun getItemCount() = categories.size

        override fun onBindViewHolder(holder: ViewAllCatAdapterViewHolder, position: Int) {

            holder.categoryImage.setImageResource(categories[position].imageallcate!!)
            holder.categoryName.text = categories[position].textallcate
            holder.addToFav.setImageResource(R.drawable.favorite)


            holder.initialize(categories.get(position), clickListener)

        }

        interface onViewAllcateClickListener {
            fun onItemClick(item: ModelViewCategories, position: Int)
            fun addCatfav(item: ModelViewCategories, position: Int)
        }

    }