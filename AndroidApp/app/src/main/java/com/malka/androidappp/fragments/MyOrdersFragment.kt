package com.malka.androidappp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.GenericOrderAdapter
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.getCartModel
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrdersFragment : Fragment(R.layout.fragment_my_orders) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListenser()
        myRequestsItemApi()
    }

    private fun initView() {
        toolbar_title.text = getString(R.string.my_orders)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }


    }


    fun myRequestsItemApi() {
        HelpFunctions.startProgressBar(requireActivity())

        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<getCartModel> = malqaa.getMyRequest(ConstantObjects.logged_userid)


        call.enqueue(object : Callback<getCartModel> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<getCartModel>,
                response: Response<getCartModel>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {

                        current_recycler.adapter =
                            GenericOrderAdapter(response.body()!!.data, requireContext())

                        current.setOnClickListener {
                            current_recycler.adapter =
                                GenericOrderAdapter(response.body()!!.data, requireContext())
                            received_recycler.hide()
                            current_recycler.show()

                            current.setBackground(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.round_btn
                                )
                            )

                            received.setBackground(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.product_attribute_bg3
                                )
                            )

                            current.setTextColor(Color.parseColor("#FFFFFF"));
                            received.setTextColor(Color.parseColor("#45495E"));

                        }

                        received.setOnClickListener {

                            received_recycler.adapter =
                                GenericOrderAdapter(response.body()!!.data, requireContext(), false)
                            current_recycler.hide()
                            received_recycler.show()
                            current.setBackground(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.product_attribute_bg3
                                )
                            )

                            received.setBackground(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.round_btn
                                )
                            )

                            received.setTextColor(Color.parseColor("#FFFFFF"));
                            current.setTextColor(Color.parseColor("#45495E"));

                        }


                    }

                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.ErrorOccur),
                        requireContext()
                    )

                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<getCartModel>, t: Throwable) {
                t.message?.let {
                    HelpFunctions.ShowLongToast(
                        it,
                        requireContext()
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }

}