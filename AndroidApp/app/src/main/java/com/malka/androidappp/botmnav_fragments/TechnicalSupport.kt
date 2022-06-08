package com.malka.androidappp.botmnav_fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import kotlinx.android.synthetic.main.activity_technical_support.*
import kotlinx.android.synthetic.main.toolbar_main.*

class TechnicalSupport : Fragment(R.layout.activity_technical_support) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.technical_support)
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }



        comunication_type.setOnClickListener {

            val list: ArrayList<SearchListItem> = ArrayList()

            list.add(SearchListItem(1, "Type 1"))
            list.add(SearchListItem(2, "Type 2"))
            list.add(SearchListItem(3, "Type 3"))


            comunication_type.showSpinner(requireActivity(),
                list,
                getString(R.string.Select, getString(R.string.type_of_communication))
            ) {
                comunication_type.text = it.title

            }
        }

    }

}