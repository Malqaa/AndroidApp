package com.malka.androidappp.activities_main.product_detail

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.BaseViewHolder
import com.malka.androidappp.helper.Extension
import kotlinx.android.synthetic.main.product_review_design.view.*

class RateAdapter(val context: Context, val rateData: List<RateResponse.RateReview>) :
    RecyclerView.Adapter<BaseViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.product_review_design, parent, false)
        return BaseViewHolder(view)
    }

    override fun getItemCount(): Int {
        return rateData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val Rate = rateData[position]
        Rate.run {
            holder.view.run {
                review_name.text = userName
                review_comment.text = comment
                review_rating.text = rate.toString()
                review_date.text = dateTimeFormated
                Extension.loadThumbnail(
                    context,
                    Rate.userImage,
                    review_profile_pic
                )
            }

        }


    }

}