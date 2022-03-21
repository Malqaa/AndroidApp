package com.malka.androidappp.botmnav_fragments.unsold_business

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.sold_business.ModelSoldUnsold
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_unsold_business.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UnsoldBusiness : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_unsold_business, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_unsold_business.title = getString(R.string.UnSold)
        toolbar_unsold_business.setTitleTextColor(Color.WHITE)
        toolbar_unsold_business.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_unsold_business.navigationIcon?.isAutoMirrored = true
        toolbar_unsold_business.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }
        getUnsoldItemApi()
    }

    fun getUnsoldItemApi() {

        val malqaa: MalqaApiService =
            RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelSoldUnsold> = malqaa.getunsolditemsbyId(ConstantObjects.logged_userid)
        val unsoldBusinessRecycler: RecyclerView =
            requireActivity().findViewById(R.id.unsold_business_recycler)

        call.enqueue(object : Callback<ModelSoldUnsold> {
            override fun onResponse(
                call: Call<ModelSoldUnsold>,
                response: Response<ModelSoldUnsold>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null && response.body()!!.data != null && response.body()!!.data.unsolditems != null) {
                        unsoldBusinessRecycler.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        unsoldBusinessRecycler.adapter =
                            AdapterUnsoldBusiness(response.body()!!.data.unsolditems)
                    } else {
                        HelpFunctions.ShowAlert(
                            this@UnsoldBusiness.context,
                            getString(R.string.Information),
                            getString(R.string.NoRecordFound)
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.ErrorOccur), activity)
                }

            }

            override fun onFailure(call: Call<ModelSoldUnsold>, t: Throwable) {

                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }

            }
        })


    }

}