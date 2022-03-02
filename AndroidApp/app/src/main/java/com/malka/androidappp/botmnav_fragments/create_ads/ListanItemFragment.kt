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



        button2.setOnClickListener() {

            confirmListItem(view)
        }

        button_2.setOnClickListener() {

            button2.performClick()
        }




    }

    fun confirmListItem(v: View) {
        if (!validateitem()) {
            return
        } else {

            val producttitle: String = textInputLayout11.getText().toString()
            StaticClassAdCreate.producttitle = producttitle
            findNavController().navigate(R.id.lisitem_choosecate)

        }

    }

    private fun validateitem(): Boolean {

        val Inputname = textInputLayout11.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            textInputLayout11.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textInputLayout11.error = null
            true
        }
    }


    fun clearingfocus() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
    }

}