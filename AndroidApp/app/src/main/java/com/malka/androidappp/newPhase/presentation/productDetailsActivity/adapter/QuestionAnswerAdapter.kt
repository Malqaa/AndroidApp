package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.questionResp.QuestionItem
import kotlinx.android.synthetic.main.dialog_answer_question.*
import kotlinx.android.synthetic.main.item_question_answer_design.view.*

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
        holder.viewBinding.questionTime.text = HelpFunctions.getViewFormatForDateTrack(questionList[position].createdAt)
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
            holder.viewBinding.answerTime.text =  HelpFunctions.getViewFormatForDateTrack(questionList[position].answeredAt)
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