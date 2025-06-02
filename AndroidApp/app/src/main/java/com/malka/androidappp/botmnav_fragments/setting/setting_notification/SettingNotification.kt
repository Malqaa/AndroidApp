package com.malka.androidappp.botmnav_fragments.setting.setting_notification

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting_notification.*


class SettingNotification : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_notification, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_setting_notif.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_setting_notif.title = getString(R.string.NotificationSettings)
        toolbar_setting_notif.setTitleTextColor(Color.WHITE)
        toolbar_setting_notif.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}