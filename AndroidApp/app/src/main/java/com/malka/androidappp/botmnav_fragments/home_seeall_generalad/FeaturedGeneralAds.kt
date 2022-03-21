package com.malka.androidappp.botmnav_fragments.home_seeall_generalad

import android.graphics.Color
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
import com.malka.androidappp.servicemodels.home.generalads
import kotlinx.android.synthetic.main.fragment_featured_general_ads.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeaturedGeneralAds : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_featured_general_ads, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_featured_general.title = getString(R.string.FeaturedGeneralAds)
        toolbar_featured_general.setTitleTextColor(Color.WHITE)
        toolbar_featured_general.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_featured_general.navigationIcon?.isAutoMirrored = true
        toolbar_featured_general.setNavigationOnClickListener() { requireActivity().onBackPressed() }
        //
        BindGeneralAdsData()
    }

    fun BindGeneralAdsData() {
        var reyclerviewgeneral: RecyclerView =
            requireActivity().findViewById(R.id.recylerview_featured_general)
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<generalads> = malqa.GetGeneralAds()
            try {
                call.enqueue(object : Callback<generalads> {
                    override fun onResponse(
                        call: Call<generalads>,
                        response: Response<generalads>
                    ) {
                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                val _generals: generalads = response.body()!!;
                                if (_generals != null && _generals.data != null && _generals.data.size > 0) {
                                    var genadpt: AdapterFeaturedGeneralAds =
                                        AdapterFeaturedGeneralAds(_generals.data)

                                    reyclerviewgeneral.layoutManager =
                                        LinearLayoutManager(
                                            activity,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    reyclerviewgeneral.adapter = genadpt
                                    //genadpt.onItemClick = { indobj ->
                                    //HelpFunctions.ViewAdvertismentDetail(
                                    //indobj.referenceId,
                                    //indobj.template,
                                    //this@FeaturedGeneralAds
                                    //)
                                    //}
                                }
                            } else {
                                reyclerviewgeneral.adapter = null
                            }
                        } else {
                            reyclerviewgeneral.adapter = null
                        }

                    }

                    override fun onFailure(call: Call<generalads>, t: Throwable) {
                        reyclerviewgeneral.adapter = null
                        //HelpFunctions.ShowLongToast(t.message.toString(), requireActivity())
                    }
                })
            } catch (e: Exception) {
                reyclerviewgeneral.adapter = null
                HelpFunctions.ShowLongToast(
                    getString(R.string.Somethingwentwrong),
                    requireActivity()
                )
            }

        } catch (ex: Exception) {
            reyclerviewgeneral.adapter = null
            HelpFunctions.ReportError(ex)
        }
    }

}