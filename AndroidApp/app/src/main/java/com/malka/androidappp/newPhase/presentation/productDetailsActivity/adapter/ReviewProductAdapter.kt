package com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.databinding.ItemQuestionAnswerDesignBinding
import com.malka.androidappp.databinding.ItemReviewProductBinding

class ReviewProductAdapter :
    RecyclerView.Adapter<ReviewProductAdapter.ReviewViewHolder>() {
    lateinit var context: Context

    class ReviewViewHolder(var viewBinding: ItemReviewProductBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        context = parent.context
        return ReviewViewHolder(
                ItemReviewProductBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = 3

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {

    }
}