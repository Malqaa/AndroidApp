package com.malka.androidappp.botmnav_fragments.favourite_frag.category_fav

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.favcard_searches_categories.view.*

class AdapterFavCat(
    val favcatposts: ArrayList<ModelFavCat>,
    var context: CategoriesFav
) : RecyclerView.Adapter<AdapterFavCat.AdapterFavCatViewHolder>() {

    var onItemClick: ((ModelFavCat) -> Unit)? = null

    inner class AdapterFavCatViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val cattitle: TextView = itemview.textView10
        val catpath: TextView = itemview.textView13
        val btn_remove_fav_search: Button = itemview.btn_remove_fav_search

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(favcatposts[adapterPosition])
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterFavCatViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favcard_searches_categories, parent, false)
        return AdapterFavCatViewHolder(view)
    }

    override fun getItemCount() = favcatposts.size

    override fun onBindViewHolder(holder: AdapterFavCatViewHolder, position: Int) {
        holder.cattitle.text = favcatposts[position].titlecatfav
        holder.catpath.text = favcatposts[position].catdestinationpath
        holder.btn_remove_fav_search.setOnClickListener(View.OnClickListener {
            val resp = HelpFunctions.DeleteFromFavourite(
                sellerid = "",
                category = favcatposts[position].titlecatfav.toString(),
                query = "",
                context = context
            )
            if(resp)
                ConstantObjects.userfavourite!!.data.category.removeAt(position);
            context.RefreshScreen()
        })
    }
}