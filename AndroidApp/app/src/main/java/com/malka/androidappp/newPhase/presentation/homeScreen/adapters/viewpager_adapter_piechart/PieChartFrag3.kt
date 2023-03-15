package com.malka.androidappp.newPhase.presentation.homeScreen.adapters.viewpager_adapter_piechart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.total_members.ModelGetTotalMembers
import kotlinx.android.synthetic.main.fragment_pie_chart_frag3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PieChartFrag3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart_frag3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTotalMembersApi()
    }

    fun getTotalMembersApi() {
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelGetTotalMembers> = malqaa.getTotalMembers()
        call.enqueue(object : Callback<ModelGetTotalMembers> {
            override fun onResponse(
                call: Call<ModelGetTotalMembers>,
                response: Response<ModelGetTotalMembers>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val totalMembersData = response.body()!!.data.toString()
                        number_of_totalmembers.text = totalMembersData
                    } else {
                        HelpFunctions.ShowLongToast(getString(R.string.Nullbackresponse), context)

//                        Toast.makeText(activity,"Null-back response",Toast.LENGTH_LONG).show()
                    }
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.FailedResponse), context)
//                    Toast.makeText(activity,"Failed Response",Toast.LENGTH_LONG).show()
                }

            }

            override fun onFailure(call: Call<ModelGetTotalMembers>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, context) }

//                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}