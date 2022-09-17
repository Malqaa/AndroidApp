package com.malka.androidappp.fragments.setting.privacy_option

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_privacy_option.*


class PrivacyOption : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_privacy_opt.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_privacy_opt.setTitle("Privacy Option")
        toolbar_privacy_opt.setTitleTextColor(Color.WHITE)
        toolbar_privacy_opt.setNavigationOnClickListener({
            activity!!.onBackPressed()
        })

    }
}