package com.malka.androidappp.botmnav_fragments.MyProducts

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.design.GenericProductAdapterNew
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.ModelSoldUnsold
import kotlinx.android.synthetic.main.fragment_sold_business.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyProductsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_business, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.MyProducts)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        getSoldItemsApi()
    }


    fun getSoldItemsApi() {
        HelpFunctions.startProgressBar(requireActivity())

        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelSoldUnsold> = malqaa.getsolditemsbyId(ConstantObjects.logged_userid)


        call.enqueue(object : Callback<ModelSoldUnsold> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<ModelSoldUnsold>,
                response: Response<ModelSoldUnsold>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        sold_business_recycler.adapter = GenericProductAdapter(response.body()!!.data.sellingitems,requireContext())
                        did_not_sale_rcv.adapter = GenericProductAdapter(response.body()!!.data.unsolditems,requireContext())
                        sold_out_rcv.adapter = GenericProductAdapterNew(response.body()!!.data.solditems,requireContext())

                        for_sale.setOnClickListener {
                            

                            did_not_Sell.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.edittext_bg))
                            sold_out.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.edittext_bg))
                            for_sale.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.round_btn))




                            for_sale.setTextColor(Color.parseColor("#FFFFFF"));
                            did_not_Sell.setTextColor(Color.parseColor("#45495E"));
                            sold_out.setTextColor(Color.parseColor("#45495E"));
                            did_not_sale_rcv.hide()
                            sold_out_rcv.hide()
                            sold_business_recycler.show()

                        }
                        for_sale.performClick()
                        did_not_Sell.setOnClickListener {



                            did_not_Sell.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.round_btn))
                            sold_out.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.edittext_bg))
                            for_sale.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.edittext_bg))

                            did_not_Sell.setTextColor(Color.parseColor("#FFFFFF"));
                            sold_out.setTextColor(Color.parseColor("#45495E"));
                            for_sale.setTextColor(Color.parseColor("#45495E"));

                            sold_business_recycler.hide()
                            sold_out_rcv.hide()
                            did_not_sale_rcv.show()

                        }
                        sold_out.setOnClickListener {


                            did_not_Sell.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.edittext_bg))
                            sold_out.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.round_btn))
                            for_sale.setBackground( ContextCompat.getDrawable(requireContext(), R.drawable.edittext_bg))



                            sold_out.setTextColor(Color.parseColor("#FFFFFF"));
                            did_not_Sell.setTextColor(Color.parseColor("#45495E"));
                            for_sale.setTextColor(Color.parseColor("#45495E"));
                            did_not_sale_rcv.hide()
                            sold_business_recycler.hide()
                            sold_out_rcv.show()


                        }
                    }

                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.ErrorOccur),
                        this@MyProductsFragment.context
                    )

                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<ModelSoldUnsold>, t: Throwable) {
                t.message?.let {
                    HelpFunctions.ShowLongToast(
                        it,
                        activity
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }



}