package com.malka.androidappp.botmnav_fragments.create_ads

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.adapter_ques_ans.AdapterQuesAns
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.ModelQuesAnswr
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_listan_item.*
import kotlinx.android.synthetic.main.fragment_setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListanItemFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
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