package com.malka.androidappp.design

import android.os.Bundle
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.reviewmodel
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.product_review_design.view.*
import kotlinx.android.synthetic.main.product_reviews1.*

class product_reviews1 : AppCompatActivity() {

    val list : ArrayList<reviewmodel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_reviews1)

        list.add(reviewmodel("Ahmed1", "15/12/2022","Very good and fast delivery", "4.7", R.drawable.profile_pic ))
        list.add(reviewmodel("Ahmed2", "17/12/2022","Great and fast delivery","4.5", R.drawable.profiledp ))
        list.add(reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
        list.add(reviewmodel("Ahmed4", "16/12/2022","Great Experience","5.0", R.drawable.car2 ))
        list.add(reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car4 ))
        list.add(reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car5 ))
        list.add(reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
        list.add(reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car2 ))
        list.add(reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car4 ))
        list.add(reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car5 ))
        list.add(reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
        list.add(reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car2 ))

        setCategoryAdaptor(list)
    }



    private fun setCategoryAdaptor(list: ArrayList<reviewmodel>) {
        category_rcv.adapter = object : GenericListAdapter<reviewmodel>(
            R.layout.product_review_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        review_name.text=name
                        review_date.text=date
                        review_rating.text=rating
                        review_comment.text=comment
                        review_profile_pic.setImageResource(image)
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }
}

