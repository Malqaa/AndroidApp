package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_adv_models.*
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_promotional.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PromotionalFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotional, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_promotional.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_promotional.title = getString(R.string.Promotional)
        toolbar_promotional.setTitleTextColor(Color.WHITE)
        toolbar_promotional.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_promotional.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            //super.onBackPressed()
            //finish()
        }



        toolbar_promotional.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_close) {
                findNavController().navigate(R.id.close_promotional)
                //closefrag()
            } else {
                // do something
            }
            false
        }

        ///////////////////////////////////////////////////////

        checkBox1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox2.isChecked = false
                checkBox3.isChecked = false
                checkBox4.isChecked = false
                checkBoxBasic.isChecked = false
//                error.visibility = View.GONE
            }
        })
        checkBox2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox1.isChecked = false
                checkBox3.isChecked = false
                checkBox4.isChecked = false
                checkBoxBasic.isChecked = false
//                error.visibility = View.GONE
            }
        })
        checkBox3.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox1.isChecked = false
                checkBox2.isChecked = false
                checkBox4.isChecked = false
                checkBoxBasic.isChecked = false
//                error.visibility = View.GONE
            }
        })
        checkBox4.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox1.isChecked = false
                checkBox2.isChecked = false
                checkBox3.isChecked = false
                checkBoxBasic.isChecked = false
//                error.visibility = View.GONE
            }
        })
        checkBoxBasic.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                checkBox1.isChecked = false
                checkBox2.isChecked = false
                checkBox3.isChecked = false
                checkBox4.isChecked = false
//                error.visibility = View.GONE
            }
        })

        button16611.setOnClickListener() {
            confirmpromotion(view)
        }

    }

    //Promotion Validation
    private fun validatepromotion(): Boolean {

        return if (checkBox1.isChecked or checkBox2.isChecked or checkBox3.isChecked or checkBox4.isChecked or checkBoxBasic.isChecked) {
            true
        } else {
//            error.visibility = View.VISIBLE
//            error.text = "Select any one item"
            HelpFunctions.ShowLongToast(getString(R.string.Selectanyoneitem),context)
//            Toast.makeText(context, "Select any one item", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun confirmpromotion(v: View) {
        if (!validatepromotion()) {
            return
        } else {
            saveSelectedcheckbox()
            findNavController().navigate(R.id.promotional_to_confirmation)
        }

    }

    fun saveSelectedcheckbox() {
        if (checkBox1.isChecked) {
//            val superfeature: String = textView26.getText().toString()
            StaticClassAdCreate.pack4 = "49.99"
        } else if (checkBox2.isChecked) {
//            val featuredcombo: String = textView27.getText().toString()
            StaticClassAdCreate.pack4 = "29.99"
        } else if (checkBox3.isChecked) {
//            val featured: String = textView29.getText().toString()
            StaticClassAdCreate.pack4 = "33.99"
        } else if (checkBox4.isChecked) {
//            val galleryy: String = textView30.getText().toString()
            StaticClassAdCreate.pack4 = "8.99"
        } else if (checkBoxBasic.isChecked) {
//            val galleryy: String = textView30.getText().toString()
            StaticClassAdCreate.pack4 = "0"
        }
    }


}