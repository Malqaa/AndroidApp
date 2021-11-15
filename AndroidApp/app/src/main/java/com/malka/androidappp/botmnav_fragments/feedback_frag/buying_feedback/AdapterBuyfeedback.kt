package com.malka.androidappp.botmnav_fragments.feedback_frag.buying_feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback.AdapterAllFeedback
import com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback.AllFeedback
import com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback.ModelAllFeedback
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import kotlinx.android.synthetic.main.feedback_card.view.*

class AdapterBuyfeedback(
    val buyfeedbackposts: ArrayList<ModelBuyfeedback>,
    var context: BuyingFeedback
) : RecyclerView.Adapter<AdapterBuyfeedback.AdapterBuyfeedbackViewHolder>()
{


    class AdapterBuyfeedbackViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val smileicon: ImageView = itemview.image_feedbackcard
        val firstname : TextView = itemview.text1st
        val feedbackdate: TextView = itemview.feedback_date
        val feedbackcomment: TextView = itemview.textv2nd
        val feedbackusername: TextView = itemview.text3rd
        val feedbackrespondbtn: TextView = itemview.amount2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterBuyfeedbackViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.feedback_card,parent,false)
        return AdapterBuyfeedbackViewHolder(view)

    }

    override fun getItemCount() = buyfeedbackposts.size

    override fun onBindViewHolder(holder: AdapterBuyfeedbackViewHolder, position: Int) {

        holder.smileicon.setImageResource(buyfeedbackposts[position].imgicon)
        holder.firstname.text  = buyfeedbackposts[position].fname
        holder.feedbackdate.text = buyfeedbackposts[position].date
        holder.feedbackcomment.text  = buyfeedbackposts[position].comment
        holder.feedbackusername.text = buyfeedbackposts[position].username
        holder.feedbackrespondbtn.text = buyfeedbackposts[position].respondbtn

        if(!SharedPreferencesStaticClass.myFeedback){
            holder.feedbackrespondbtn.visibility = View.GONE
        }else{
            holder.feedbackrespondbtn.text = buyfeedbackposts[position].respondbtn
        }
    }




}