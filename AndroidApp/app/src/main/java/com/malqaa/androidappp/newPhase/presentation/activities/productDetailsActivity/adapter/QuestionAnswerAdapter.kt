package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.questionResp.QuestionItem

class QuestionAnswerAdapter(var questionList: List<QuestionItem>,var setonSelectedQuestion:SetonSelectedQuestion) :
    RecyclerView.Adapter<QuestionAnswerAdapter.QuestionAnswerViewHolder>() {
    lateinit var context: Context

    class QuestionAnswerViewHolder(var viewBinding: ItemQuestionAnswerDesignBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAnswerViewHolder {
        context = parent.context
        return QuestionAnswerViewHolder(
            ItemQuestionAnswerDesignBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = questionList.size

    override fun onBindViewHolder(holder: QuestionAnswerViewHolder, position: Int) {
        holder.viewBinding.tvQuestion.text = questionList[position].question.toString()
        holder.viewBinding.questionTime.text = HelpFunctions.getViewFormatForDateTrack(questionList[position].createdAt,"dd/MM/yyyy HH:mm:ss")
//        Extension.loadThumbnail(
//            context,
//            questionList[position].clientImage,
//            holder.viewBinding.ivQuestionUser,
//           null
//        )
        Glide.with(context).load( questionList[position].clientImage).error(R.mipmap.malqa_iconn_round).into(    holder.viewBinding.ivQuestionUser)

        if (questionList[position].answer!=null && questionList[position].answer !="") {
            holder.viewBinding.questionResponseYet.hide()
            holder.viewBinding.answerView.show()
            holder.viewBinding.answerTv.text = questionList[position].answer
            holder.viewBinding.answerTime.text =  HelpFunctions.getViewFormatForDateTrack(questionList[position].answeredAt,"dd/MM/yyyy HH:mm:ss")
            holder.viewBinding.answerTime.show()
        } else {
            holder.viewBinding.questionResponseYet.show()
            holder.viewBinding.answerView.hide()
            holder.viewBinding.answerTime.hide()
        }
        holder.viewBinding.containerQuestion.setOnClickListener {
            setonSelectedQuestion.onSelectQuestion(position)
        }
    }
    interface SetonSelectedQuestion{
        fun onSelectQuestion(position: Int)
    }
}