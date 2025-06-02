package com.malka.androidappp.botmnav_fragments.create_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_other_details.*
import java.util.*


class OtherDetailsGeneral : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_details, container, false)
    }



    private fun getCustomText(value: EditText): String {
        return value.text.toString().trim()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ////////////////////////////////////////////
        toolbar_otherdetails.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_otherdetails.setTitle("Other Details")
        toolbar_otherdetails.setTitleTextColor(Color.WHITE)
        toolbar_otherdetails.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_otherdetails.setNavigationOnClickListener({
            activity!!.onBackPressed()
            //   super.onBackPressed()
            //  finish()
        })


        toolbar_otherdetails.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.action_close) {
                    findNavController().navigate(R.id.close_otherdetails)
                    //closefrag()
                } else {
                    // do something
                }
                return false
            }
        })

        ////////////////////////////////////////////

        btnother.setOnClickListener() {
            confirmInputttt(view)
        }

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)




//////////////////////////////////////Featured ExpireyDate//////////////////////////////////////////////
        //feauredexpirydate.setOnTouchListener( { v, event ->
          // feauredexpirydate.showSoftInputOnFocus = false
            //hidekeyboard()
            //false })


        feauredexpirydate.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    feauredexpirydate.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }
///////////////////////////////Urgent Expiry Date////////////////////////////////////////////////////////

        urgentexpirydate.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    urgentexpirydate.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }

///////////////////////////////Hightlight Expiry Date////////////////////////////////////////////////////////

        hightlightexdate.setOnClickListener(){
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    hightlightexdate.setText("" + mDay + "/" + mMonth + "/" + mYear)
                },
                year,
                month,
                day
            )
            dpd.show()        }

////////////////////////////////////////
    }


    private fun validateName(): Boolean {
        val textName = activity!!.findViewById(R.id.name) as EditText
        val Inputname = textName.text.toString().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            textName.error = "Field can't be empty"
            false
        } else {
            textName.error = null
            true
        }
    }

    private fun validateSlug(): Boolean {
        val textSlug = activity!!.findViewById(R.id.slug) as EditText

        val Inputslug = textSlug.text.toString().trim { it <= ' ' }
        return if (Inputslug.isEmpty()) {
            textSlug.error = "Field can't be empty"
            false
        } else {
            textSlug.error = null
            true
        }
    }

    private fun validateTag(): Boolean {
        val textTag = activity!!.findViewById(R.id.tagg) as TextInputEditText

        val Inputtag = textTag.text.toString().trim { it <= ' ' }
        return if (Inputtag.isEmpty()) {
            textTag.error = "Field can't be empty"
            false
        } else {
            textTag.error = null
            true
        }
    }

    private fun validateFExpiry(): Boolean {
        val textFExpiry = activity!!.findViewById(R.id.feauredexpirydate) as TextInputEditText
        val Inputfexpiry = textFExpiry.text.toString().trim { it <= ' ' }
        return if (Inputfexpiry.isEmpty()) {
            textFExpiry.error = "Field can't be empty"
            false
        } else {
            textFExpiry.error = null
            true
        }
    }

    private fun validateUExpiry(): Boolean {
        val textUExpiry = activity!!.findViewById(R.id.urgentexpirydate) as TextInputEditText
        val Inputuexpiry = textUExpiry.text.toString().trim { it <= ' ' }
        return if (Inputuexpiry.isEmpty()) {
            textUExpiry.error = "Field can't be empty"
            false
        } else {
            textUExpiry.error = null
            true
        }
    }

    private fun validateHExpiry(): Boolean {

        val textHighLightExpiry = activity!!.findViewById(R.id.hightlightexdate) as TextInputEditText
        val Inputhexpiry = textHighLightExpiry.text.toString().trim { it <= ' ' }
        return if (Inputhexpiry.isEmpty()) {
            textHighLightExpiry.error = "Field can't be empty"
            false
        } else {
            textHighLightExpiry.error = null
            true
        }
    }

    fun confirmInputttt(v: View) {
        if (!validateName() or !validateSlug() or !validateTag() or !validateFExpiry() or !validateUExpiry() or !validateHExpiry()) {
            return
        } else {
            ////////////////Gettext from Editext and store values on Static Class///////////////
            val namee: String = name.getText().toString()
            StaticClassAdCreate.name = namee
            val slugg: String = slug.getText().toString()
            StaticClassAdCreate.slug = slugg
            val tagg:String = tagg.getText().toString()
            StaticClassAdCreate.tag = tagg
            val feauredexpirydatee: String = feauredexpirydate.getText().toString()
            StaticClassAdCreate.featureexpirydate = feauredexpirydatee
            val urgentexpirydatee: String = urgentexpirydate.getText().toString()
            StaticClassAdCreate.urgentexpirydate = urgentexpirydatee
            val hightlightexdatee: String = hightlightexdate.getText().toString()
            StaticClassAdCreate.highlightexpirydate = hightlightexdatee
            //id.setText(StaticClassAdCreate.name)

            //////////////////To get Checkbox value and save it on static class////////////////
            val conactphonecheckbox: Boolean = checkBox6.isChecked
            StaticClassAdCreate.iscontactphone = conactphonecheckbox
            val contactEmailcheckbox: Boolean = checkBox.isChecked
            StaticClassAdCreate.iscontactemail = contactEmailcheckbox
            val contactchatcheckbox: Boolean = checkBox3.isChecked
            StaticClassAdCreate.iscontactchat = contactchatcheckbox

            //checkBox.setChecked(StaticClassAdCreate.iscontactchat!!)


            findNavController().navigate(R.id.otherde_list)

        }

    }


    fun hidekeyboard(){
        val inputMethodManager = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    fun closefrag()
    {
        val transaction = fragmentManager!!.beginTransaction()
        transaction.addToBackStack(null)
        transaction.commit()
    }
}