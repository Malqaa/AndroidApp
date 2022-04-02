package com.malka.androidappp.botmnav_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.toolbar_main.*

class ApplicationSetting : Fragment(R.layout.activity_application_setting) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.negotiation_offers)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }
}