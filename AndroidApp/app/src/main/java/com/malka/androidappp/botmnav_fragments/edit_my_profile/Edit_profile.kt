package com.malka.androidappp.botmnav_fragments.edit_my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.toolbar_main.*

class EditProfile : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_edit_profile, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.edit_profile)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}