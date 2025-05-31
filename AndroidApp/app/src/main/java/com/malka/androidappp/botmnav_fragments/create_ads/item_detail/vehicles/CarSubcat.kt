package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.vehicles

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import kotlinx.android.synthetic.main.fragment_car_subcat.*
import kotlinx.android.synthetic.main.fragment_general.*
import kotlinx.android.synthetic.main.fragment_other_details.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.toolbar_otherdetailsautombile
import java.util.*


class CarSubcat : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_subcat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_itemdetail_car.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_itemdetail_car.setTitle("Item Details")
        toolbar_itemdetail_car.setTitleTextColor(Color.WHITE)
        toolbar_itemdetail_car.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_itemdetail_car.setNavigationOnClickListener({
            requireActivity().onBackPressed()
            //   super.onBackPressed()
            //  finish()
        })

        /////////////////CodingFlow For Country textDropdown/Spiner/////////////////////
        val spinnner: Spinner = requireActivity().findViewById(R.id.make_spinner_caritemdetail)
        val adapter = ArrayAdapter.createFromResource(
            this.requireActivity(), R.array.carslist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnner.adapter = adapter

        /////////////////CodingFlow For Country textDropdown/Spiner/////////////////////
        val spinnner2: Spinner = requireActivity().findViewById(R.id.body_spinner_caritemdetail)
        val adapter2 = ArrayAdapter.createFromResource(
            this.requireActivity(), R.array.bodylist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinnner2.adapter = adapter2



        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


//////////////////////////////////////Featured ExpireyDate//////////////////////////////////////////////
        featuredexpdate_caritemdetail.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    featuredexpdate_caritemdetail.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }
///////////////////////////////Urgent Expiry Date////////////////////////////////////////////////////////

        urgentexp_caritemdetaill.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    urgentexp_caritemdetaill.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }

///////////////////////////////Hightlight Expiry Date////////////////////////////////////////////////////////

        highlight_caritemdetaill.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    highlight_caritemdetaill.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }

        btnother_caritemdetail.setOnClickListener(){

            val highlight:String = highlight_caritemdetaill.text.toString()
            StaticClassAdCreate.highlightexpirydate = highlight

            val urgent:String = urgentexp_caritemdetaill.text.toString()
            StaticClassAdCreate.urgentexpirydate = urgent

            val feature:String = featuredexpdate_caritemdetail.text.toString()
            StaticClassAdCreate.featureexpirydate = feature


            val yearr: String = year1_caritemdetail.getText().toString()
            StaticClassAdCreate.year = yearr
            
            val bodyType :String= body_spinner_caritemdetail.getSelectedItem().toString()
            StaticClassAdCreate.body = bodyType

            val makeText :String= make_spinner_caritemdetail.getSelectedItem().toString()
            StaticClassAdCreate.make = makeText
            
            val cyclinder: String = cyclinder_caritemdetail.getText().toString()
            StaticClassAdCreate.cylinders = cyclinder
            
            val kilometer: String = kilometers_caritemdetail.getText().toString()
            StaticClassAdCreate.kilometers = kilometer
            
            val motorInspection: String = periodic_inspection_caritemdetail.getText().toString()
            StaticClassAdCreate.motorvehiclesperiodicinspection = motorInspection
            
            val model: String = model_caritemdetail.getText().toString()
            StaticClassAdCreate.model = model
            
            val platenum: String = platenum_caritemdetail.getText().toString()
            StaticClassAdCreate.platenumber = platenum

//            Toast.makeText(activity,"Plate number :"+StaticClassAdCreate.platenumber, Toast.LENGTH_LONG).show()

            val seqNo: String = sequenceno_caritemdetail.getText().toString()
            StaticClassAdCreate.squencenumber = seqNo
            
            val fuelType: String = fueltype_caritemdetail.getText().toString()
            StaticClassAdCreate.fuel = fuelType
            
            val transmission: String = transmission_caritemdetail.getText().toString()
            StaticClassAdCreate.transmission = transmission
            
            val sellerType: String = sellertype_caritemdetail.getText().toString()
            StaticClassAdCreate.sellertype = sellerType
            
            val ownerNums: String = noofowners_caritemdetail.getText().toString()
            StaticClassAdCreate.noofpreviousowners = ownerNums
            //
            // isimport checked data saving
            if (check1_caritemdetail.isChecked==true)
            {
                StaticClassAdCreate.isimported = true
            }

            if (check2_caritemdetail.isChecked==true)
            {
                StaticClassAdCreate.isnogotiable = true
            }




            findNavController().navigate(R.id.itemdetail_listingdetails)
////////////////////////////////////////
        }
    }

    fun hidekeyboard(){
        val inputMethodManager = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }




}