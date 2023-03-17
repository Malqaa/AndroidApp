package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.AtrributeItemBinding
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding

class QuestionAnswerAdapter  :
    RecyclerView.Adapter<QuestionAnswerAdapter.QuestionAnswerViewHolder>(){
    lateinit var context: Context
    class QuestionAnswerViewHolder(var viewBinding: ItemQuestionAnswerDesignBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionAnswerViewHolder {
        context = parent.context
        return  QuestionAnswerViewHolder(
            ItemQuestionAnswerDesignBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: QuestionAnswerViewHolder, position: Int) {

    }
}