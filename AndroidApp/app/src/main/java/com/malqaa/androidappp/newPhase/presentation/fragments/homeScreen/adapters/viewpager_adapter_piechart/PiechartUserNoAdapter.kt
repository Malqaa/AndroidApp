package com.malqaa.androidappp.newPhase.presentation.fragments.homeScreen.adapters.viewpager_adapter_piechart

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PiechartUserNoAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PieChartFrag1.newInstance()
            1 -> PieChartFrag2()
            2 -> PieChartFrag3()
            3 -> PieChartFrag4()
            else -> throw Exception("No such position")
        }
    }
}
