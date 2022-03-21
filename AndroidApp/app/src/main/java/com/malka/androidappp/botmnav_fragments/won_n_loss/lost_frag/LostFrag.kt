package com.malka.androidappp.botmnav_fragments.won_n_loss.lost_frag

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.won_n_loss.model_wonloss.ModelWonLost
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_lost.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LostFrag : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lost, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_lost.title = getString(R.string.Lost)
        toolbar_lost.setTitleTextColor(Color.WHITE)
        toolbar_lost.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_lost.navigationIcon?.isAutoMirrored = true
        toolbar_lost.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        lostApiCall()

    }

    fun lostApiCall() {
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelWonLost> = malqaa.getWonLost(ConstantObjects.logged_userid)
        val lostRecyclerr: RecyclerView = requireActivity().findViewById(R.id.recyclerViewLost)

        call.enqueue(object : Callback<ModelWonLost> {
            override fun onResponse(call: Call<ModelWonLost>, response: Response<ModelWonLost>) {
                if (response.isSuccessful) {
                    if (response.body()!!.data != null && response.body()!!.data.lostAuctions != null && response.body()!!.data.lostAuctions.size > 0) {
                        lostRecyclerr.layoutManager =
                            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                        lostRecyclerr.adapter = AdapterLost(response.body()!!.data.lostAuctions)
                    }
                    HelpFunctions.ShowAlert(
                        this@LostFrag.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                } else {
//                    Toast.makeText(activity, "Error Occur", Toast.LENGTH_LONG).show()
                    HelpFunctions.ShowLongToast(getString(R.string.ErrorOccur), activity)
                }
            }

            override fun onFailure(call: Call<ModelWonLost>, t: Throwable) {
//                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }

            }
        })
    }
}