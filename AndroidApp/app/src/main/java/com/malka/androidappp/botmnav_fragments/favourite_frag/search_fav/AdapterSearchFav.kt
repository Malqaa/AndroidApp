package com.malka.androidappp.botmnav_fragments.favourite_frag.search_fav

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

class AdapterSearchFav(
    val favsearchposts: ArrayList<ModelSearchFav>,
    var context: SearchFav
) : RecyclerView.Adapter<AdapterSearchFav.AdapterSearchFavViewHolder>() {

    var onItemClick: ((ModelSearchFav) -> Unit)? = null

    inner class AdapterSearchFavViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val searchtitle: TextView = itemview.textView10
        val searchpath: TextView = itemview.textView13
        val btn_remove_fav_search: Button = itemview.btn_remove_fav_search

        init {
            itemview.setOnClickListener {
                onItemClick?.invoke(favsearchposts[adapterPosition])
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSearchFavViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.favcard_searches_categories, parent, false)
        return AdapterSearchFavViewHolder(view)
    }

    override fun getItemCount() = favsearchposts.size

    override fun onBindViewHolder(holder: AdapterSearchFavViewHolder, position: Int) {
        holder.searchtitle.text = favsearchposts[position].titlesearchfav
        holder.searchpath.text = favsearchposts[position].searchdestinationpath
        holder.btn_remove_fav_search.setOnClickListener(View.OnClickListener {
            val resp = HelpFunctions.DeleteFromFavourite(
                sellerid = "",
                category = "",
                query = favsearchposts[position].titlesearchfav.toString(),
                context = context
            )
            if (resp)
                ConstantObjects.userfavourite!!.data.searchQue.removeAt(position);
            context.RefreshScreen()
        })
    }
}