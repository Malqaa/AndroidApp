package com.malka.androidappp.botmnav_fragments.home.view.viewpager_adapter_piechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.home.GetAllAds
import kotlinx.android.synthetic.main.fragment_pie_chart_frag4.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PieChartFrag4 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart_frag4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllAdsData()
    }


    // Count of listing
    fun getAllAdsData() {

        val malqa: MalqaApiService = RetrofitBuilder.GetAllAds()
        val call: Call<GetAllAds> = malqa.GetAllAds(ConstantObjects.logged_userid)

        call.enqueue(object : Callback<GetAllAds> {
            override fun onResponse(
                call: Call<GetAllAds>,
                response: Response<GetAllAds>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val car: List<AdDetailModel> = response.body()!!.data.generaladvetisement
                        val general: List<AdDetailModel> =
                            response.body()!!.data.generaladvetisement
                        val totatlAd: Int = car.size + general.size
                        number_of_totallisting.text = "$totatlAd"

                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.Error), context)
//                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                    }
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.Error), context)
//                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT)
                }
            }

            override fun onFailure(call: Call<GetAllAds>, t: Throwable) {
                HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
            }
        })

    }

}
