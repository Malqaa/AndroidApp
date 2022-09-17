package com.malka.androidappp.fragments.feedback_frag.selling_feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import kotlinx.android.synthetic.main.feedback_card.view.*

class AdapterSellingFeedback(
    val sellfeedbackposts: ArrayList<ModelSellingFeedback>,
    var context: SellingFeedback
) : RecyclerView.Adapter<AdapterSellingFeedback.AdapterSellingFeedbackViewHolder>()
{


    class AdapterSellingFeedbackViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val smileicon: ImageView = itemview.image_feedbackcard
        val firstname : TextView = itemview.text1st
        val feedbackdate: TextView = itemview.feedback_date
        val feedbackcomment: TextView = itemview.textv2nd
        val feedbackusername: TextView = itemview.text3rd
        val feedbackrespondbtn: TextView = itemview.amount2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSellingFeedbackViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.feedback_card,parent,false)
        return AdapterSellingFeedbackViewHolder(view)

    }

    override fun getItemCount() = sellfeedbackposts.size

    override fun onBindViewHolder(holder: AdapterSellingFeedbackViewHolder, position: Int) {

        holder.smileicon.setImageResource(sellfeedbackposts[position].imgicon)
        holder.firstname.text  = sellfeedbackposts[position].fname
        holder.feedbackdate.text = sellfeedbackposts[position].date
        holder.feedbackcomment.text  = sellfeedbackposts[position].comment
        holder.feedbackusername.text = sellfeedbackposts[position].username
        holder.feedbackrespondbtn.text = sellfeedbackposts[position].respondbtn

        if(!SharedPreferencesStaticClass.myFeedback){
            holder.feedbackrespondbtn.visibility = View.GONE
        }else{
            holder.feedbackrespondbtn.text = sellfeedbackposts[position].respondbtn
        }
    }




}