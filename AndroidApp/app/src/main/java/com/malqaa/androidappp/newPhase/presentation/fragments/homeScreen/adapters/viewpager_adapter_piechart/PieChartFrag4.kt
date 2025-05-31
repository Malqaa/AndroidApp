package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.viewpager_adapter_piechart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malqaa.androidappp.databinding.FragmentPieChartFrag4Binding

class PieChartFrag4 : Fragment() {

    // Declare the binding variable
    private var _binding: FragmentPieChartFrag4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        _binding = FragmentPieChartFrag4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllAdsData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the binding when the view is destroyed to prevent memory leaks
        _binding = null
    }

    // Count of listing
    fun getAllAdsData() {
        // You can now use binding to access views directly, e.g.,
        // binding.someTextView.text = "Example"
    }
}
