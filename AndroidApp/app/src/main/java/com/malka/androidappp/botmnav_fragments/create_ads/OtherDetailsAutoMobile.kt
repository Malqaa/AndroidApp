package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_list_details.*
import kotlinx.android.synthetic.main.fragment_other_details.*
import kotlinx.android.synthetic.main.fragment_other_details_auto_mobile.*
import kotlinx.android.synthetic.main.fragment_other_details_property.*
import org.w3c.dom.Text


class OtherDetailsAutoMobile : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_details_auto_mobile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_otherdetailsautombile.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_otherdetailsautombile.setTitle("Other Details")
        toolbar_otherdetailsautombile.setTitleTextColor(Color.WHITE)
        toolbar_otherdetailsautombile.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_otherdetailsautombile.setNavigationOnClickListener({
            activity!!.onBackPressed()
            //   super.onBackPressed()
            //  finish()
        })



        toolbar_otherdetailsautombile.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.action_close) {
                    findNavController().navigate(R.id.close_otherdetailsautomobile)
                    //closefrag()
                } else {
                    // do something
                }
                return false
            }
        })
        /////////////////CodingFlow For Country textDropdown/Spiner/////////////////////
        val spinner: Spinner = activity!!.findViewById(R.id.make_spinner_automobile)
        val adapter = ArrayAdapter.createFromResource(
            this.requireActivity(), R.array.carslist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner.adapter = adapter

        /////////////////CodingFlow For Country textDropdown/Spiner/////////////////////
        val spinner2: Spinner = activity!!.findViewById(R.id.body_spinner_automobile)
        val adapter2 = ArrayAdapter.createFromResource(
            this.requireActivity(), R.array.bodylist, R.layout.support_simple_spinner_dropdown_item
        )
        adapter2.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        spinner2.adapter = adapter2


////////////////////////////////////////////////////////////////////////////////
        btnotherautomobile.setOnClickListener(){
            ConfirrmOtherdetailAutomobile()
        }



    }

    /////////////////////////////////////Text Validation/////////////////////////////////////////////////
    private fun validateYearautomobile(): Boolean {
        val textyear = activity!!.findViewById(R.id.year1_automobile) as TextInputEditText

        val Inputyear = textyear.text.toString().trim { it <= ' ' }
        return if (Inputyear.isEmpty()) {
            textyear.error = "Field can't be empty"
            false
        } else {
            textyear.error = null
            true
        }
    }

    private fun validateCylindersautomobile(): Boolean {
        val textCyclinders = activity!!.findViewById(R.id.cyclinder_automobile1) as TextInputEditText

        val Inputcyclinder = textCyclinders.text.toString().trim { it <= ' ' }
        return if (Inputcyclinder.isEmpty()) {
            textCyclinders.error = "Field can't be empty"
            false
        } else {
            textCyclinders.error = null
            true
        }
    }

    private fun validateKilometerautomobile(): Boolean {
        val textkilometer = activity!!.findViewById(R.id.kilometers_automobile1) as TextInputEditText

        val Inputkilometer = textkilometer.text.toString().trim { it <= ' ' }
        return if (Inputkilometer.isEmpty()) {
            textkilometer.error = "Field can't be empty"
            false
        } else {
            textkilometer.error = null
            true
        }
    }

    private fun validatevehicleInspectionautomobile(): Boolean {
        val textvehicleInspection = activity!!.findViewById(R.id.Periodic_Inspection_automobile1) as TextInputEditText

        val InputvehicleInspection = textvehicleInspection.text.toString().trim { it <= ' ' }
        return if (InputvehicleInspection.isEmpty()) {
            textvehicleInspection.error = "Field can't be empty"
            false
        } else {
            textvehicleInspection.error = null
            true
        }
    }

    private fun validatemodelautomobile(): Boolean {
        val textmodel = activity!!.findViewById(R.id.model_automobile1) as TextInputEditText

        val Inputmodel = textmodel.text.toString().trim { it <= ' ' }
        return if (Inputmodel.isEmpty()) {
            textmodel.error = "Field can't be empty"
            false
        } else {
            textmodel.error = null
            true
        }
    }

    private fun validatePlatenumautomobile(): Boolean {
        val textplatenum = activity!!.findViewById(R.id.platenum_automobile1) as TextInputEditText

        val InputPlatenum = textplatenum.text.toString().trim { it <= ' ' }
        return if (InputPlatenum.isEmpty()) {
            textplatenum.error = "Field can't be empty"
            false
        } else {
            textplatenum.error = null
            true
        }
    }

    private fun validateSequenceNoautomobile(): Boolean {
        val textseqno = activity!!.findViewById(R.id.sequenceno_automobile1) as TextInputEditText

        val InputSeqno = textseqno.text.toString().trim { it <= ' ' }
        return if (InputSeqno.isEmpty()) {
            textseqno.error = "Field can't be empty"
            false
        } else {
            textseqno.error = null
            true
        }
    }

    private fun validateFuelTypeautomobile(): Boolean {
        val textFuelType = activity!!.findViewById(R.id.fueltype_automobile1) as TextInputEditText

        val InputFuelType = textFuelType.text.toString().trim { it <= ' ' }
        return if (InputFuelType.isEmpty()) {
            textFuelType.error = "Field can't be empty"
            false
        } else {
            textFuelType.error = null
            true
        }
    }

    private fun validateTransmissionautomobile(): Boolean {
        val texttransmission = activity!!.findViewById(R.id.transmission_automobile1) as TextInputEditText

        val InputTransmission = texttransmission.text.toString().trim { it <= ' ' }
        return if (InputTransmission.isEmpty()) {
            texttransmission.error = "Field can't be empty"
            false
        } else {
            texttransmission.error = null
            true
        }
    }

    private fun validateSellerTypeautomobile(): Boolean {
        val textSellerType = activity!!.findViewById(R.id.sellertype_automobile1) as TextInputEditText

        val InputSellertype = textSellerType.text.toString().trim { it <= ' ' }
        return if (InputSellertype.isEmpty()) {
            textSellerType.error = "Field can't be empty"
            false
        } else {
            textSellerType.error = null
            true
        }
    }

    private fun validateNoOfOwnerautomobile(): Boolean {
        val textNoOfOwner = activity!!.findViewById(R.id.noofowners_automobile1) as TextInputEditText

        val InputNoofOwner = textNoOfOwner.text.toString().trim { it <= ' ' }
        return if (InputNoofOwner.isEmpty()) {
            textNoOfOwner.error = "Field can't be empty"
            false
        } else {
            textNoOfOwner.error = null
            true
        }
    }

////////////////////////Validation on Spinner//////////////////////////////////

    private fun validateCarCompany(): Boolean {
        val errorid = activity!!.findViewById<TextView>(R.id.errorcarcompany)
        val getIdCarCompany = activity!!.findViewById(R.id.make_spinner_automobile) as Spinner
        val getTextCarCompany:String = getIdCarCompany.getSelectedItem().toString().trim()
        return if (getTextCarCompany=="- - Select Company - -" ) {
        errorid.setVisibility(View.VISIBLE)
        false
        } else {
        errorid.visibility = View.GONE
        true
        }
    }
    ////////////////////////Validation on Spinner//////////////////////////////////
    private fun validateBodyType(): Boolean {
        val errorid = activity!!.findViewById<TextView>(R.id.errorbodytype)
        val getIdCarBodyType = activity!!.findViewById(R.id.body_spinner_automobile) as Spinner
        val getTextCarBodyType:String = getIdCarBodyType.getSelectedItem().toString().trim()
        return if (getTextCarBodyType=="- - Select BodyType - -" ) {
            errorid.setVisibility(View.VISIBLE)
            false
        } else {
            errorid.visibility = View.GONE
            true
        }
    }
//////////////////Next Button Validation////////////////////////////////
fun ConfirrmOtherdetailAutomobile(){

        if (!validateYearautomobile() or !validateCylindersautomobile() or !validateKilometerautomobile()
            or !validatevehicleInspectionautomobile() or !validatemodelautomobile() or
            !validatePlatenumautomobile() or !validateSequenceNoautomobile() or !validateFuelTypeautomobile()
            or !validateTransmissionautomobile() or !validateSellerTypeautomobile() or
                !validateNoOfOwnerautomobile() or !validateCarCompany() or !validateBodyType()) {
            return
        } else {
            ////////////////Gettext from Editext and store values on Static Class///////////////
            val year: String = year1_automobile.getText().toString()
            StaticClassAdCreate.year = year
            val cyclinder: String = cyclinder_automobile1.getText().toString()
            StaticClassAdCreate.cylinders = cyclinder
            val kilometer: String = kilometers_automobile1.getText().toString()
            StaticClassAdCreate.kilometers = kilometer
            val motorInspection: String = Periodic_Inspection_automobile1.getText().toString()
            StaticClassAdCreate.motorvehiclesperiodicinspection = motorInspection
            val model: String = model_automobile1.getText().toString()
            StaticClassAdCreate.model = model
            val platenum: String = platenum_automobile1.getText().toString()
            StaticClassAdCreate.platenumber = platenum
            val seqNo: String = sequenceno_automobile1.getText().toString()
            StaticClassAdCreate.squencenumber = seqNo
            val fuelType: String = fueltype_automobile1.getText().toString()
            StaticClassAdCreate.fuel = fuelType
            val transmission: String = transmission_automobile1.getText().toString()
            StaticClassAdCreate.transmission = transmission
            val sellerType: String = sellertype_automobile1.getText().toString()
            StaticClassAdCreate.sellertype = sellerType
            val ownerNums: String = noofowners_automobile1.getText().toString()
            StaticClassAdCreate.noofpreviousowners = ownerNums

            ///////////////////////////////////////////////////////////////////
            val makeCompany:String = make_spinner_automobile.getSelectedItem().toString()
            StaticClassAdCreate.make = makeCompany

            val carBody:String = body_spinner_automobile.getSelectedItem().toString()
            StaticClassAdCreate.body = carBody

            findNavController().navigate(R.id.otherdetailAutomobile_listingdetail)

        }

}


}