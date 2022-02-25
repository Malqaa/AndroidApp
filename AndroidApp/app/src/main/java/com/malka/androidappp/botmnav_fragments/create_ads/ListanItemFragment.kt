package com.malka.androidappp.botmnav_fragments.create_ads

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_listan_item.*


class ListanItemFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //navBar.setVisibility(View.INVISIBLE)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listan_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //setSupportActionBar(toolbar_listitem)
        toolbar_listitem.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_listitem.title = getString(R.string.Listanitem)
        toolbar_listitem.navigationIcon?.isAutoMirrored = true
        toolbar_listitem.setTitleTextColor(Color.WHITE)
        toolbar_listitem.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            //finish()
        }

        button2.setOnClickListener() {

            confirmListItem(view)
        }


        constraintLayout4.setOnClickListener() {
            textInputLayout11.clearFocus()
            clearingfocus()
        }


    }

    fun confirmListItem(v: View) {
        if (!validateitem()) {
            return
        } else {

            val producttitle: String = itemtext.getText().toString()
            StaticClassAdCreate.producttitle = producttitle
            findNavController().navigate(R.id.lisitem_choosecate)

        }

    }

    private fun validateitem(): Boolean {
        val textName = requireActivity().findViewById(R.id.itemtext) as TextInputEditText
        val Inputname = textName.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            textName.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textName.error = null
            true
        }
    }


    fun clearingfocus() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
    }

}