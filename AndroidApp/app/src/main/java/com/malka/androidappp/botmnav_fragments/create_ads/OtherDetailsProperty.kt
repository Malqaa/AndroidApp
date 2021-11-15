package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_other_details.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.*
import kotlinx.android.synthetic.main.fragment_other_details_property.*


class OtherDetailsProperty : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_details_property, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ////////////////////Toolbar////////////////////////
        toolbar_otherdetailsproperty.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_otherdetailsproperty.setTitle("Other Details")
        toolbar_otherdetailsproperty.setTitleTextColor(Color.WHITE)
        toolbar_otherdetailsproperty.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_otherdetailsproperty.setNavigationOnClickListener({
            activity!!.onBackPressed()
            //   super.onBackPressed()
            //  finish()
        })
        ///////////////////////////////Toolbar Menu Click//////////////////////////////
        toolbar_otherdetailsproperty.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.action_close) {
                    findNavController().navigate(R.id.close_otherdetailsproperty)
                    //closefrag()
                } else {
                    // do something
                }
                return false
            }
        })
///////////////////////////////////////////Onclick Next Btn Validates//////////////////////////////////////////
        btnotherdetail_property.setOnClickListener(){
            confirmationOtherDetailsProperty()
        }

    }





//////////////////////////////////Validation of all Input Feilds///////////////////////////////////////////
    private fun validateFloorareaProperty(): Boolean {
        val textFloorArea = activity!!.findViewById(R.id.floorarea_property1) as TextInputEditText

        val InputFloorArea = textFloorArea.text.toString().trim { it <= ' ' }
        return if (InputFloorArea.isEmpty()) {
            textFloorArea.error = "Field can't be empty"
            false
        } else {
            textFloorArea.error = null
            true
        }
    }

    private fun validateBedroomProperty(): Boolean {
        val textBedRoom = activity!!.findViewById(R.id.bedroom_property1) as TextInputEditText

        val InputBedroom = textBedRoom.text.toString().trim { it <= ' ' }
        return if (InputBedroom.isEmpty()) {
            textBedRoom.error = "Field can't be empty"
            false
        } else {
            textBedRoom.error = null
            true
        }
    }
    private fun validateBathroomProperty(): Boolean {
        val textBathroom = activity!!.findViewById(R.id.bathroom_property1) as TextInputEditText

        val InputBathroom = textBathroom.text.toString().trim { it <= ' ' }
        return if (InputBathroom.isEmpty()) {
            textBathroom.error = "Field can't be empty"
            false
        } else {
            textBathroom.error = null
            true
        }
    }

    private fun validateFloornumProperty(): Boolean {
        val textFloornumProperty = activity!!.findViewById(R.id.floornum_property1) as TextInputEditText

        val InputFloornumProperty = textFloornumProperty.text.toString().trim { it <= ' ' }
        return if (InputFloornumProperty.isEmpty()) {
            textFloornumProperty.error = "Field can't be empty"
            false
        } else {
            textFloornumProperty.error = null
            true
        }
    }


    private fun validateLandareaProperty(): Boolean {
        val textLandArea = activity!!.findViewById(R.id.landarea_property1) as TextInputEditText

        val InputLandarea = textLandArea.text.toString().trim { it <= ' ' }
        return if (InputLandarea.isEmpty()) {
            textLandArea.error = "Field can't be empty"
            false
        } else {
            textLandArea.error = null
            true
        }
    }

    //////////////////////////////////Function of Confirmation Validation Button Check///////////////////////////////////////////
    fun confirmationOtherDetailsProperty(){
        if (!validateFloorareaProperty() or !validateBedroomProperty() or !validateBathroomProperty()
            or !validateFloornumProperty() or !validateLandareaProperty()) {
            return }
        else
        {

            val floorArea: String = floorarea_property1.getText().toString()
            StaticClassAdCreate.floorarea = floorArea
            val bedRoom: String = bedroom_property1.getText().toString()
            StaticClassAdCreate.bedrooms = bedRoom
            val bathRoom: String = bathroom_property1.getText().toString()
            StaticClassAdCreate.bathrooms = bathRoom
            val floornum: String = floornum_property1.getText().toString()
            StaticClassAdCreate.floorsnumber = floornum
            val landarea: String = landarea_property1.getText().toString()
            StaticClassAdCreate.landarea = landarea

            findNavController().navigate(R.id.otherdetailProperty_listingdetail)
        }
    }




}