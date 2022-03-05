package com.malka.androidappp.botmnav_fragments.create_ads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions


class ChooseCateFragment : Fragment(){


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        HelpFunctions.startProgressBar(requireActivity())

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_cate, container, false)



    }







}
