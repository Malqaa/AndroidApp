package com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import kotlinx.android.synthetic.main.feedback_card.view.*


class AdapterAllFeedback(
    val allfeedbackposts: ArrayList<ModelAllFeedback>,
    var context: AllFeedback
) : RecyclerView.Adapter<AdapterAllFeedback.AdapterAllFeedbackViewHolder>() {


    class AdapterAllFeedbackViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

        val smileicon: ImageView = itemview.image_feedbackcard
        val firstname: TextView = itemview.text1st
        val feedbackdate: TextView = itemview.feedback_date
        val feedbackcomment: TextView = itemview.textv2nd
        val feedbackusername: TextView = itemview.text3rd
        val feedbackrespondbtn: TextView = itemview.amount2
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterAllFeedbackViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.feedback_card, parent, false)
        return AdapterAllFeedbackViewHolder(view)

    }

    override fun getItemCount() = allfeedbackposts.size

    override fun onBindViewHolder(holder: AdapterAllFeedbackViewHolder, position: Int) {

        holder.smileicon.setImageResource(allfeedbackposts[position].imgicon)
        holder.firstname.text = allfeedbackposts[position].fname
        holder.feedbackdate.text = allfeedbackposts[position].date
        holder.feedbackcomment.text = allfeedbackposts[position].comment
        holder.feedbackusername.text = allfeedbackposts[position].username

        if(!SharedPreferencesStaticClass.myFeedback){
            holder.feedbackrespondbtn.visibility = View.GONE
        }else{
            holder.feedbackrespondbtn.text = allfeedbackposts[position].respondbtn
        }


    }


}