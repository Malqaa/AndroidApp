package com.malka.androidappp.botmnav_fragments.sold_business

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_fixed_price_offer.*
import kotlinx.android.synthetic.main.fragment_sold_business.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SoldBusiness : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_business, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_sold_business.title = getString(R.string.Sold)
        toolbar_sold_business.setTitleTextColor(Color.WHITE)
        toolbar_sold_business.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_sold_business.navigationIcon?.isAutoMirrored = true
        toolbar_sold_business.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }

        getSoldItemsApi()
    }


    fun getSoldItemsApi() {
        val malqaa: MalqaApiService = RetrofitBuilder.getSolditembyId(ConstantObjects.logged_userid)
        val call: Call<ModelSoldUnsold> = malqaa.getsolditemsbyId(ConstantObjects.logged_userid)
        val soldBusinessRecycler: RecyclerView =
            requireActivity().findViewById(R.id.sold_business_recycler)

        call.enqueue(object : Callback<ModelSoldUnsold> {
            override fun onResponse(
                call: Call<ModelSoldUnsold>,
                response: Response<ModelSoldUnsold>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null && response.body()!!.data != null && response.body()!!.data.solditems != null) {
                        soldBusinessRecycler.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        soldBusinessRecycler.adapter =
                            AdapterSoldBusiness(response.body()!!.data.solditems)
                    } else {
                        HelpFunctions.ShowAlert(
                            this@SoldBusiness.context,
                            getString(R.string.Information),
                            getString(R.string.NoRecordFound)
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.ErrorOccur),
                        this@SoldBusiness.context
                    )
                }
            }

            override fun onFailure(call: Call<ModelSoldUnsold>, t: Throwable) {
                t.message?.let {
                    HelpFunctions.ShowLongToast(
                        it,
                        activity
                    )
                }
            }
        })

    }
}