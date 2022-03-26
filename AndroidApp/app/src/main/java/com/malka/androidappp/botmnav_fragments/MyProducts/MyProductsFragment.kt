package com.malka.androidappp.botmnav_fragments.MyProducts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.BrowseMarketXLAdap
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
            override fun onResponse(
                call: Call<ModelSoldUnsold>,
                response: Response<ModelSoldUnsold>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        sold_business_recycler.adapter = BrowseMarketXLAdap(response.body()!!.data.sellingitems,requireContext())

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