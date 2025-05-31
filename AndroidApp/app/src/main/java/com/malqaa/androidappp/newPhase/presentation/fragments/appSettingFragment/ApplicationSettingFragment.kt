package com.malqaa.androidappp.newPhase.presentation.fragments.appSettingFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityApplicationSettingBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.yariksoffice.lingver.Lingver
import io.paperdb.Paper

class ApplicationSettingFragment : Fragment(R.layout.activity_application_setting) {

    // Declare the binding object
    private var _binding: ActivityApplicationSettingBinding? = null
    private val binding get() = _binding!!

    private var settingViewModel: SettingViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the binding object
        _binding = ActivityApplicationSettingBinding.bind(view)

        setUpModel()

        // Use binding to access views
        binding.toolbarMain.toolbarTitle.text = getString(R.string.application_settings)
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        if (ConstantObjects.currentLanguage == "en") {
            binding.languageToggle.checkedTogglePosition = 0
        } else {
            binding.languageToggle.checkedTogglePosition = 1
        }
    }

    private fun setUpModel() {
        settingViewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        settingViewModel?.isLoading?.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }

        settingViewModel?.languageObserver?.observe(viewLifecycleOwner) {
            HelpFunctions.ShowLongToast(it.message, requireActivity())
            (requireActivity() as BaseActivity<*>).setLocale()
        }

        // Use binding for the switch and save button
        binding.swNotify.isChecked = SharedPreferencesStaticClass.getSwitchNotify()
        binding.swNotify.setOnClickListener {
            // Add logic if needed
        }

        binding.btnSaveEdit.setOnClickListener {
            SharedPreferencesStaticClass.saveSwitchNotify(binding.swNotify.isChecked)
            if (ConstantObjects.currentLanguage == "en") {
                if (binding.languageToggle.checkedTogglePosition != 0) {
                    changeLanguage()
                } else {
                    requireActivity().onBackPressed()
                }
            } else if (ConstantObjects.currentLanguage == "ar") {
                if (binding.languageToggle.checkedTogglePosition != 1) {
                    changeLanguage()
                } else {
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    private fun changeLanguage() {
        if (Paper.book().read(SharedPreferencesStaticClass.islogin, false) == true)
            settingViewModel?.setLanguageChange(
                if (Lingver.getInstance().getLanguage() == ConstantObjects.ARABIC)
                    ConstantObjects.ENGLISH else ConstantObjects.ARABIC
            )
        else
            (requireActivity() as BaseActivity<*>).setLocale()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        settingViewModel?.closeAllCall()
        settingViewModel = null

        // Clear the binding when the view is destroyed
        _binding = null
    }
}
