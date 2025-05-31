package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.general

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_car_subcat.*
import kotlinx.android.synthetic.main.fragment_diggers.*
import kotlinx.android.synthetic.main.fragment_diggers.toolbar_digger
import kotlinx.android.synthetic.main.fragment_general.*
import java.util.*

class General : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_general.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_general.title = "Item Details"
        toolbar_general.setTitleTextColor(Color.WHITE)
        toolbar_general.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_general.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        toolbar_general.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_close) {
                findNavController().navigate(R.id.close_otherdetailsautomobile)
                //closefrag()
            } else {
                // do something
            }
            false
        }

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


//////////////////////////////////////Featured ExpireyDate//////////////////////////////////////////////
        featuredexpiry_general2.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    featuredexpiry_general2.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }
///////////////////////////////Urgent Expiry Date////////////////////////////////////////////////////////

        urgentexp_general2.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    urgentexp_general2.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }

///////////////////////////////Hightlight Expiry Date////////////////////////////////////////////////////////

        highlight_general2.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    highlight_general2.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }

        btnother_general.setOnClickListener(){

            val highlight:String = highlight_general2.text.toString()
            StaticClassAdCreate.highlightexpirydate = highlight

            val urgent:String = urgentexp_general2.text.toString()
            StaticClassAdCreate.urgentexpirydate = urgent

            val feature:String = featuredexpiry_general2.text.toString()
            StaticClassAdCreate.featureexpirydate = feature

            val name: String = name_general2.text.toString()
            StaticClassAdCreate.name = name

            val Slug: String = slug_general2.text.toString()
            StaticClassAdCreate.slug = Slug

            val tag: String = tag_general2.text.toString()
            StaticClassAdCreate.tag = tag


            // isimport checked data saving
            if (check1_general.isChecked)
            {
                StaticClassAdCreate.iscontactphone = true
            }

            if (check2_general.isChecked)
            {
                StaticClassAdCreate.iscontactemail = true
            }
            if (check3_general.isChecked)
            {
                StaticClassAdCreate.iscontactchat = true
            }

            if (check4_general.isChecked)
            {
                StaticClassAdCreate.isnogotiable = true
            }
            if (check5_general.isChecked)
            {
                StaticClassAdCreate.isphonehidden = true
            }
            if (check6_general.isChecked)
            {
                StaticClassAdCreate.isfeatured = true
            }


            findNavController().navigate(R.id.itemdetail_listingdetails)


        }
    }

    fun hidekeyboard(){
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }




}