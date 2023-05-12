package com.malka.androidappp.newPhase.presentation.appSettingFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ApplicationSettingFragment : Fragment(R.layout.activity_application_setting) {



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
            (requireActivity() as BaseActivity).setLocate()
        }


    }
}