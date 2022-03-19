package com.malka.androidappp.botmnav_fragments.nofication_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.toolbar_main.*


class NotificationsFragment : Fragment(R.layout.fragment_notifications) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_title.text = getString(R.string.Notifications)
        back_btn.setOnClickListener {
            findNavController().popBackStack()
        }

    }

}
