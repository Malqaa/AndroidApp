package com.malka.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.row_layout.view.*


class PostsAdapter(
    val posts: ArrayList<Model>,
    var clickListener: OnPostItemClickListener) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>()
{

    class PostsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {
        val ttname : TextView = itemview.titlenamee
        val minutess: TextView = itemview.minutes
        val description: TextView = itemview.descrip
        val pricee: TextView = itemview.pricee
        val myimg: ImageView = itemview.myimagee

        fun initialize(item: Model, action:OnPostItemClickListener){
            ttname.text = item.tname
            minutess.text = item.mins
            description.text = item.des
            pricee.text = item.price
            myimg.setImageResource(item.imagee)

            itemView.setOnClickListener(){action.onItemClick(item, adapterPosition)}
            //myimg.setOnClickListener(){action.onItemClick(item, adapterPosition)} for single item clicking
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.row_layout,parent,false)
        return PostsViewHolder(view)
    }


    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {

//        holder.ttname.text = posts[position].tname
//      holder.minutess.text  = posts[position].mins //count.toString()  (for int count)
//        holder.description.text  = posts[position].des
//        holder.pricee.text  = posts[position].price
//        holder.myimg.setImageResource(posts[position].imagee)
        holder.initialize(posts.get(position),clickListener)
    }

    interface OnPostItemClickListener{
        fun onItemClick(item: Model, position: Int)
    }



}