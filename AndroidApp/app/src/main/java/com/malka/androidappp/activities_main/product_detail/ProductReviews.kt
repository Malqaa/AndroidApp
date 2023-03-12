package com.malka.androidappp.activities_main.product_detail

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.servicemodels.Reviewmodel
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.malka.androidappp.activities_main.product_detail.RateResponse as RateResponse1

class ProductReviews : BaseActivity() {

    lateinit var adapter: RateAdapter

    val list: ArrayList<Reviewmodel> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_reviews1)

        toolbar_title.text = getString(R.string.reviews)
        back_btn.setOnClickListener {
            finish()
        }


        getRates()


        //        list.add(Reviewmodel("Ahmed1", "15/12/2022","Very good and fast delivery", "4.7", R.drawable.profile_pic ))
//        list.add(Reviewmodel("Ahmed2", "17/12/2022","Great and fast delivery","4.5", R.drawable.profiledp ))
//        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience","5.0", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed5", "10/12/2022","Excelent fast delivery","4.6", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed6", "5/12/2022","Amazing and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed3", "12/12/2022","Good and fast delivery", "4.9", R.drawable.car ))
//        list.add(Reviewmodel("Ahmed4", "16/12/2022","Great Experience ","5.0", R.drawable.car ))
//
//        setCategoryAdaptor()
//    }
//
//
//
//    private fun setCategoryAdaptor(list: ArrayList<RateResponse1.RateReview>) {
//        category_rcv.adapter = object : GenericListAdapter<RateResponse1.RateReview>(
//            R.layout.product_review_design,
//            bind = { element, holder, itemCount, position ->
//                holder.view.run {
//                    element.run {
//                        review_name.text = userName
//                        review_date.text=createdAt
//                        review_rating.text=rate
//                        review_comment.text=comment
//                        review_profile_pic.setImageResource(image)
//
//                    }
//                }
//            }
//        ) {
//            override fun getFilter(): Filter {
//                TODO("Not yet implemented")
//            }
//
//        }.apply {
//            submitList(
//                list
//            )
//        }
//    }
//
//
//    }
////
    }
        private fun getRates() {
            val apiBuilder = RetrofitBuilder.GetRetrofitBuilder()
            val Rates = apiBuilder.getRates()
            Rates.enqueue(object : Callback<RateResponse1> {
                override fun onResponse(
                    call: Call<RateResponse1>,
                    response: Response<RateResponse1>
                ) {
                    val rates = response.body()
                    if (rates != null) {
                        Log.d("Api", rates.toString())
                        val reviewList = findViewById<RecyclerView>(R.id.category_rcv)
                        adapter = RateAdapter(this@ProductReviews, rates.data)
                        reviewList.adapter = adapter
                        reviewList.layoutManager = LinearLayoutManager(this@ProductReviews)


                    }

                }

                override fun onFailure(call: Call<RateResponse1>, t: Throwable) {
                    Log.d("Api", "Error in fetching rates", t)
                }

            })

        }
    }


