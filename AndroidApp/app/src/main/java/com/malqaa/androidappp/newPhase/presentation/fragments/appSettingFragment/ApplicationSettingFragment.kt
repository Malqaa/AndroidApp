package com.malqaa.androidappp.newPhase.presentation.fragments.appSettingFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_application_setting.btnSaveEdit
import kotlinx.android.synthetic.main.activity_application_setting.language_toggle
import kotlinx.android.synthetic.main.activity_application_setting.swNotify
import kotlinx.android.synthetic.main.toolbar_main.*

class ApplicationSettingFragment : Fragment(R.layout.activity_application_setting) {


    private var settingViewModel: SettingViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpModel()
        toolbar_title.text = getString(R.string.application_settings)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        if (ConstantObjects.currentLanguage == "en") {
            language_toggle.checkedTogglePosition = 0
        } else {
            language_toggle.checkedTogglePosition = 1
        }


    }

    private fun setUpModel(){
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        settingViewModel?.isLoading?.observe(viewLifecycleOwner, Observer {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        })


        settingViewModel?.languageObserver?.observe(viewLifecycleOwner, Observer {
            HelpFunctions.ShowLongToast(it.message, requireActivity())
            (requireActivity() as BaseActivity).setLocate()

        })
        swNotify.isChecked=SharedPreferencesStaticClass.getSwitchNotify()
        swNotify.setOnClickListener {
        }

        btnSaveEdit.setOnClickListener {
            SharedPreferencesStaticClass.saveSwitchNotify(swNotify.isChecked)
            if(ConstantObjects.currentLanguage == "en"){
                if(language_toggle.checkedTogglePosition != 0){
                    changeLanguage()
                }else{
                    requireActivity().onBackPressed()
                }
            }else if(ConstantObjects.currentLanguage == "ar"){
                if(language_toggle.checkedTogglePosition != 1){
                    changeLanguage()
                }else{
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun changeLanguage(){
        if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
            settingViewModel?.setLanguageChange(
                if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC)
                    ConstantObjects.ENGLISH else ConstantObjects.ARABIC
            )
        else
            (requireActivity() as BaseActivity).setLocate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingViewModel?.closeAllCall()
        settingViewModel=null
    }
}