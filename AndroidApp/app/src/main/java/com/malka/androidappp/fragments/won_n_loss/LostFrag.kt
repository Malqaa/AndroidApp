package com.malka.androidappp.fragments.won_n_loss

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.model_wonloss.ModelWonLost
import kotlinx.android.synthetic.main.fragment_lost.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LostFrag : Fragment(R.layout.fragment_lost) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.Loser)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        lostApiCall()

    }

    fun lostApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelWonLost> = malqaa.getWonLost(ConstantObjects.logged_userid)

        call.enqueue(object : Callback<ModelWonLost> {
            override fun onResponse(call: Call<ModelWonLost>, response: Response<ModelWonLost>) {
                if (response.isSuccessful) {
                    recyclerViewLost.adapter = GenericProductAdapter(response.body()!!.data.lostAuctions,requireContext())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.ErrorOccur), activity)
                }
            }

            override fun onFailure(call: Call<ModelWonLost>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }

            }
        })
    }
}