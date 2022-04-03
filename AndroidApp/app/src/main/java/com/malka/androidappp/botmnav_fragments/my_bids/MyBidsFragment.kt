package com.malka.androidappp.botmnav_fragments.my_bids

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.ModelSoldUnsold
import kotlinx.android.synthetic.main.activity_my_bids_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBidsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_my_bids_fragment, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.my_bids)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        myBidsItemApi()
    }


    fun myBidsItemApi() {
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
                        my_bids_recycler.adapter = GenericProductAdapter(response.body()!!.data.sellingitems!!,requireContext())

                    }

                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.ErrorOccur),
                        requireContext()
                    )

                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<ModelSoldUnsold>, t: Throwable) {
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