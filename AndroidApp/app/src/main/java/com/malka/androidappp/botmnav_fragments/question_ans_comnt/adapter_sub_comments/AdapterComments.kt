package com.malka.androidappp.botmnav_fragments.question_ans_comnt.adapter_sub_comments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.Comment
import kotlinx.android.synthetic.main.card_comment.view.*


class AdapterComments
    (val commentpost: List<Comment>
) : RecyclerView.Adapter<AdapterComments.AdapterCommentsViewHolder>()
{


    class AdapterCommentsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val commenticon: ImageView = itemview.comment_icon
        val comment_text : TextView = itemview.comment_text
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterCommentsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_comment,parent,false)
        return AdapterCommentsViewHolder(view)

    }

    override fun getItemCount() = commentpost.size

    override fun onBindViewHolder(holder: AdapterCommentsViewHolder, position: Int) {

        //holder.commenticon.setImageResource(R.drawable.comments)
        ////////////////////////////////////////////////////////////////////////////////
        holder.comment_text.text  =
            if (commentpost[position].description!=null)
            commentpost[position].description
            else ""



    }




}