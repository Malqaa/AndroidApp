package com.malka.androidappp.botmnav_fragments.question_ans_comnt.adapter_ques_ans

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.adapter_sub_comments.AdapterComments
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.Question
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.card_ques_ans.view.*
import retrofit2.Callback
import retrofit2.Response


class AdapterQuesAns(
    val quesAnsposts: List<Question>,
    var clickedListener: OnQAPostItemClickLisentener
) : RecyclerView.Adapter<AdapterQuesAns.AdapterQuesAnsViewHolder>() {

    class AdapterQuesAnsViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {


        val questext: TextView = itemview.ques_text
        val ansicon:ImageView = itemview.ans_icon
        val buyername: TextView = itemview.buyer_name
        val anstext: TextView = itemview.ans_text
        val sellername: TextView = itemview.seller_name
        var btn_ans:TextView = itemview.buttonans
        val btn_comment:TextView = itemview.buttoncomment
        val comnt_recycler:RecyclerView = itemview.commentRecycler

        fun intailaizeAnsOnClick(item:Question,action: OnQAPostItemClickLisentener){
            btn_ans.setOnClickListener(){action.OnQAItemClick(item, adapterPosition)}
        }

        fun intializeCommentOnClick(item: Question,action: OnQAPostItemClickLisentener){
            btn_comment.setOnClickListener(){action.onCommentItemClick(item, adapterPosition)}
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterQuesAnsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(
            R.layout.card_ques_ans,
            parent,
            false
        )
        return AdapterQuesAnsViewHolder(view)


    }

    override fun getItemCount() = quesAnsposts.size



    override fun onBindViewHolder(holder: AdapterQuesAnsViewHolder, position: Int) {
        //val item = quesAnsposts.get(holder.adapterPosition)

            if (quesAnsposts != null && quesAnsposts.size > 0 && quesAnsposts[position] != null) {

                //////////////Hide Comment and Anser buttons for buyers////////////////////////////
                if(ConstantObjects.logged_userid!=SharedPreferencesStaticClass.ad_userid)
                {
                    holder.btn_ans.setVisibility(View.GONE)
                    holder.btn_comment.setVisibility(View.GONE)
                }

             //////////////////////////////settext to question view//////////////////////////////////
                holder.questext.text =
                if (quesAnsposts[position].question != null) quesAnsposts[position].question
                else ""
                //holder.buyername.text = quesAnsposts[position].buyerName

                //////////////////////////////settext to question view///////////////////////////////
                holder.anstext.text =
                if (quesAnsposts[position].answer != null && quesAnsposts[position].answer.description != null)
                    quesAnsposts[position].answer.description

                else ""

                ///////////////////////hide ansicon if answer text is empty/////////////////////////
                //holder.ansicon.setTag(position)
                holder.ansicon.visibility =
                    if(quesAnsposts[position].answer != null && quesAnsposts[position].answer.description != null)
                        View.VISIBLE
                    else
                        View.GONE
                ///////////////////////hide sellername if answer text is empty/////////////////////////
                holder.sellername.visibility = if(quesAnsposts[position].answer != null && quesAnsposts[position].answer.description != null)
                    View.VISIBLE
                else
                    View.GONE
            ///////////////////////////Calling CommentRecyclerview////////////////////////////////////
                holder.comnt_recycler.layoutManager = LinearLayoutManager(holder.comnt_recycler.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                holder.comnt_recycler.adapter =
                    if(quesAnsposts[position].answer != null && quesAnsposts[position].answer.description != null && quesAnsposts[position].comment != null)
                    AdapterComments(quesAnsposts[position].comment)
                else null
            //////////////////////////////////////////////////////////////////////////////////////////
            }


///////////////////////////////////Post Answer APi on send Button /////////////////////////////////////////////////////////////////////
        holder.intailaizeAnsOnClick(quesAnsposts.get(position),clickedListener)
        holder.intializeCommentOnClick(quesAnsposts.get(position),clickedListener)
    }

    interface OnQAPostItemClickLisentener{
        fun OnQAItemClick(item: Question, position: Int)

        fun onCommentItemClick(item: Question,position: Int)
    }

}