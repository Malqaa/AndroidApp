package com.malka.androidappp.fragments.home.view.viewpager_adapter_piechart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.total_online_users.ModelGetTotalOnlineUsers
import kotlinx.android.synthetic.main.fragment_pie_chart_frag2.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PieChartFrag2 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart_frag2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTotalOnlineUsersApi()
    }



    fun getTotalOnlineUsersApi(){
    val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
    val call:Call<ModelGetTotalOnlineUsers> = malqaa.GetTotalOnlineUers()

    call.enqueue(object : Callback<ModelGetTotalOnlineUsers>{
        override fun onResponse(call: Call<ModelGetTotalOnlineUsers>, response: Response<ModelGetTotalOnlineUsers>) {

            if (response.isSuccessful)
            {
                if (response.body() != null)
                {
                    val totalOnlineUserData = response.body()!!.data.toString()
                    number_of_totalonlineusers.text = totalOnlineUserData
                }

                else{
                    HelpFunctions.ShowLongToast(getString(R.string.Nullbackresponse),context)
//                    Toast.makeText(activity,"Null-back response",Toast.LENGTH_LONG).show()
                }
            }
            else
            {
                HelpFunctions.ShowLongToast(getString(R.string.FailedResponse),context)
//                Toast.makeText(activity,"Failed Response",Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<ModelGetTotalOnlineUsers>, t: Throwable) {
            t.message?.let { HelpFunctions.ShowLongToast(it,context) }

//            Toast.makeText(activity,t.message,Toast.LENGTH_LONG).show()
        }
    })
    }

}