package com.malka.androidappp.botmnav_fragments.won_n_loss.won_frag

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.won_n_loss.model_wonloss.ModelWonLost
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_won.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WonFrag : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_won, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_won.title = getString(R.string.Won)
        toolbar_won.setTitleTextColor(Color.WHITE)
        toolbar_won.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_won.navigationIcon?.isAutoMirrored = true
        toolbar_won.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        WonApiCall()
    }

    fun WonApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelWonLost> = malqaa.getWonLost(ConstantObjects.logged_userid)
        val wonRecyclerr: RecyclerView = requireActivity().findViewById(R.id.recyclerViewWon)

        call.enqueue(object : Callback<ModelWonLost> {
            override fun onResponse(call: Call<ModelWonLost>, response: Response<ModelWonLost>) {
                if (response.isSuccessful) {
                    if (response.body()!!.data != null && response.body()!!.data.wonAuctions != null && response.body()!!.data.wonAuctions.size > 0) {
                        wonRecyclerr.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        wonRecyclerr.adapter = AdapterWon(response.body()!!.data.wonAuctions)
                    } else {
                        HelpFunctions.ShowAlert(
                            this@WonFrag.context,
                            getString(R.string.Information),
                            getString(R.string.NoRecordFound)
                        )
                    }
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.ErrorOccur), activity)
//                    Toast.makeText(activity, "Error Occur", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ModelWonLost>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }

//                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }
}