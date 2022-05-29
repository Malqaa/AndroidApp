package com.malka.androidappp.botmnav_fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.SplashActivity
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ApplicationSetting : Fragment(R.layout.activity_application_setting) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.application_settings)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        if (ConstantObjects.currentLanguage == "en") {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }

        language_toggle.setOnToggleSwitchChangeListener { position, isChecked ->
            when (position) {
                0 -> {
                    (requireActivity() as BaseActivity).setLocate("en")
                }
                1 -> {
                    (requireActivity() as BaseActivity).setLocate("ar")

                }

            }
            val intentt = Intent(requireActivity(), SplashActivity::class.java)
            startActivity(intentt)
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }


    }
}