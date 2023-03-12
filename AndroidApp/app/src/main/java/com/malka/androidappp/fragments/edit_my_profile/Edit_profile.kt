package com.malka.androidappp.fragments.edit_my_profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_main.*

class EditProfile : Fragment() {
    var gender_ = ""
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
        date!!._setOnClickListener {
           DatePickerFragment(true, false) { selectdate_ ->
                date.text = "$selectdate_ "
            }.show(requireActivity().supportFragmentManager, "")
        }



        radiomale._setOnClickListener {
            radiomale._setCheck(!radiomale.getCheck())
            radiofemale._setCheck(false)
            gender_ = radiomale.getText()
        }
        radiofemale._setOnClickListener {
            radiofemale._setCheck(!radiofemale.getCheck())
            radiomale._setCheck(false)
            gender_ = radiomale.getText()
        }
    }
}