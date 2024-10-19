package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.viewpager_adapter_piechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentPieChartFrag2Binding
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.total_online_users.ModelGetTotalOnlineUsers
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PieChartFrag2 : Fragment() {

    private var _binding: FragmentPieChartFrag2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentPieChartFrag2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call the API to get total online users
        getTotalOnlineUsersApi()
    }

    private fun getTotalOnlineUsersApi() {
        val malqaApiService: MalqaApiService = getRetrofitBuilder()
        val call: Call<ModelGetTotalOnlineUsers> = malqaApiService.getTotalOnlineUsers()

        call.enqueue(object : Callback<ModelGetTotalOnlineUsers> {
            override fun onResponse(
                call: Call<ModelGetTotalOnlineUsers>,
                response: Response<ModelGetTotalOnlineUsers>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        // Update the TextView with the total online users
                        binding.numberOfTotalonlineusers.text = data.data.toString()
                    } ?: run {
                        // Handle the case when the response body is null
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Nullbackresponse),
                            requireContext()
                        )
                    }
                } else {
                    // Handle the failed response scenario
                    HelpFunctions.ShowLongToast(
                        getString(R.string.FailedResponse),
                        requireContext()
                    )
                }
            }

            override fun onFailure(call: Call<ModelGetTotalOnlineUsers>, t: Throwable) {
                // Handle the failure case
                t.message?.let {
                    HelpFunctions.ShowLongToast(it, requireContext())
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding when the view is destroyed
        _binding = null
    }
}
