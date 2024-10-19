package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.viewpager_adapter_piechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentPieChartFrag3Binding
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.total_members.ModelGetTotalMembers
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PieChartFrag3 : Fragment() {

    private var _binding: FragmentPieChartFrag3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout using view binding
        _binding = FragmentPieChartFrag3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Call the API to get total members
        getTotalMembersApi()
    }

    private fun getTotalMembersApi() {
        val malqaApiService: MalqaApiService = getRetrofitBuilder()
        val call: Call<ModelGetTotalMembers> = malqaApiService.getTotalMembers()

        call.enqueue(object : Callback<ModelGetTotalMembers> {
            override fun onResponse(
                call: Call<ModelGetTotalMembers>,
                response: Response<ModelGetTotalMembers>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        // Update the TextView with the total members
                        binding.numberOfTotalmembers.text = data.data.toString()
                    } ?: run {
                        // Handle null response body case
                        HelpFunctions.ShowLongToast(
                            getString(R.string.Nullbackresponse),
                            requireContext()
                        )
                    }
                } else {
                    // Handle response failure
                    HelpFunctions.ShowLongToast(
                        getString(R.string.FailedResponse),
                        requireContext()
                    )
                }
            }

            override fun onFailure(call: Call<ModelGetTotalMembers>, t: Throwable) {
                // Handle request failure
                t.message?.let { HelpFunctions.ShowLongToast(it, requireContext()) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clear the binding when view is destroyed
        _binding = null
    }
}
