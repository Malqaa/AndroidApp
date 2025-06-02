package com.malka.androidappp.botmnav_fragments.create_ads

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_listing_duration.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ListingDuration : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listing_duration, container, false)
    }

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ///////////////////////Toolbar Work////////////////////////////////////
        toolbar_listduration.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_listduration.title = getString(R.string.ListingDuration)
        toolbar_listduration.setTitleTextColor(Color.WHITE)
        toolbar_listduration.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_listduration.setNavigationOnClickListener {
            requireActivity().onBackPressed()
            //finish()
        }

        toolbar_listduration.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_close) {
                findNavController().navigate(R.id.close_listduration)
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
        val minute = c.get(Calendar.MINUTE)
        val hour = c.get(Calendar.HOUR_OF_DAY)


        // Variables to get weeks from the current date
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val todayDate = Date()
        val thisDate = currentDate.format(todayDate)

        val week1 = addDay(day.toString(), 7)
        val week2 = addDay(day.toString(), 14)
        val week3 = addDay(day.toString(), 21)
        val week4 = addDay(day.toString(), 28)

        val allWeeks: Array<String> = arrayOf(
            "- - Select Duration - -",
            "1 week ($thisDate - $week1)",
            "2 week ($thisDate - $week2)",
            "3 week ($thisDate - $week3)",
            "4 week ($thisDate - $week4)",
        )

        /////////////////For Duration Dropdown/Spinner/////////////////////
        val spinner: Spinner = requireActivity().findViewById(R.id.select_dur)
        spinner.adapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            allWeeks
        )


        ///////////////////////////////Hightlight Expiry Date////////////////////////////////////////////////////////


        select_date.setOnClickListener() {
            hidekeyboard()
            val dpd = DatePickerDialog(

                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
//                    val monthplus: Int = mMonth + 1
//                    select_date.setText("" + mDay + "/" + month_name + "/" + mYear)

                    // To get month in alphabets
//                    val monthInLetters = SimpleDateFormat("MMM")
//                    val month_name = monthInLetters.format(c.time)

                    select_date.setText("$mDay/${dateInAlpha(mMonth + 1)}/$mYear")

                }, year, month, day
            )
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000;
            dpd.show()

        }

////////////////////////////////////////////TimePicker #1/////////////////////////////////
        select_time_btn1.setOnClickListener() {
            hidekeyboard()
            val tpd = TimePickerDialog(
                this.requireContext(),
                TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMinute ->
                    select_time_btn1.setText(
                        String.format(
                            "%02d:%02d",
                            selectedHour,
                            selectedMinute
                        )
                    )
                },

                hour,
                minute,
                true
            )
            tpd.show()

        }

/////////////////////////////////////TimePicker #2/////////////////////////////////
        select_time2.setOnClickListener() {
            hidekeyboard()
            val tpd = TimePickerDialog(
                this.requireContext(),
                TimePickerDialog.OnTimeSetListener { view, selectedHour, selectedMinute ->
                    select_time2.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                },

                hour,
                minute,
                true
            )
            tpd.show()
        }


        var fixedLengthSpinner: RelativeLayout =
            requireActivity().findViewById(R.id.textInputlist_duration)
        var fixedLengthTime: TextInputLayout =
            requireActivity().findViewById(R.id.textInputlist_duration1)

        var endTimeDate: TextInputLayout =
            requireActivity().findViewById(R.id.textInputlist_duration3)
        var endTimeTime: TextInputLayout =
            requireActivity().findViewById(R.id.textInputlist_duration4)

        //////////////////////////////////////Activity Work///////////////////////////////////////////
        radiobtn1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                radiobtn2.isChecked = false
                select_date.isEnabled = false
                select_date.setText("")
                select_date.inputType = InputType.TYPE_NULL
                select_date.isFocusableInTouchMode = false

                select_time2.isEnabled = false
                select_time2.setText("")
                select_time2.inputType = InputType.TYPE_NULL;
                select_time2.isFocusableInTouchMode = false

                select_dur.isEnabled = true
//                select_dur.setInputType(InputType.TYPE_CLASS_TEXT)
                select_dur.isFocusableInTouchMode = true
                select_dur.requestFocus()

                select_time_btn1.isEnabled = true
                //select_time_btn1.setInputType(InputType.TYPE_CLASS_DATETIME)
                select_time_btn1.isFocusableInTouchMode = true

                select_date.error = null
                select_time2.error = null

                endTimeDate.visibility = View.GONE
                endTimeTime.visibility = View.GONE
                fixedLengthSpinner.visibility = View.VISIBLE
                fixedLengthTime.visibility = View.VISIBLE

                error_radiobtn_check.visibility = View.GONE
            }
        })
        radiobtn2.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton, b ->
            if (b) {
                radiobtn1.isChecked = false
                select_dur.isEnabled = false
//                select_dur.setInputType(InputType.TYPE_NULL)
//                select_dur.setText("")
                select_dur.isFocusableInTouchMode = false

                select_time_btn1.isEnabled = false;
                select_time_btn1.inputType = InputType.TYPE_NULL;
                select_time_btn1.setText("")
                select_time_btn1.isFocusableInTouchMode = false

                select_date.isEnabled = true
                //select_date.setInputType(InputType.TYPE_CLASS_DATETIME)
                select_date.isFocusableInTouchMode = true
                select_date.requestFocus()

                select_time2.isEnabled = true
                //select_time2.setInputType(InputType.TYPE_CLASS_DATETIME)
                select_time2.isFocusableInTouchMode = true

//                select_dur.error = null
                select_time_btn1.error = null

                endTimeDate.visibility = View.VISIBLE
                endTimeTime.visibility = View.VISIBLE
                fixedLengthSpinner.visibility = View.GONE
                fixedLengthTime.visibility = View.GONE

                error_radiobtn_check.visibility = View.GONE

            }
        })


        /////////////////////Next Button Validation//////////////////////////
        btn_listduration.setOnClickListener() {
            confirmListDuration(view)
        }

    }


    ///////////////////////////////////////RadioButton if/else Clicking//////////////////////////////////////////
    private fun validateListDuration(): Boolean {
        var duration: Spinner = requireActivity().findViewById<Spinner>(R.id.select_dur)
        val Inputduration = duration.selectedItem.toString().trim { it <= ' ' }
        //
        val time1 = requireActivity().findViewById(R.id.select_time_btn1) as TextInputEditText
        val Inputtime1 = time1.text.toString().trim { it <= ' ' }
        //
        val datee = requireActivity().findViewById(R.id.select_date) as TextInputEditText
        val Inputdate = datee.text.toString().trim { it <= ' ' }
        //
        val time2 = requireActivity().findViewById(R.id.select_time2) as TextInputEditText
        val Inputtime2 = time2.text.toString().trim { it <= ' ' }


        return if (radiobtn1.isChecked) {
            if (Inputduration == "- - Select Country - -" && Inputtime1.isEmpty()) {
//                duration.error = "Field can't be empty"
                time1.error = getString(R.string.Fieldcantbeempty)
                false
            } else if (Inputtime1.isEmpty()) {
                time1.error = getString(R.string.Fieldcantbeempty)
//                 duration.error = null
                false
            } else if (Inputduration == "- - Select Country - -") {
//                duration.error = "Field can't be empty"
                time1.error = null
                false
            } else {
                time1.error = null
//                duration.error = null
                true
            }
        }
        ////////////////////////////////////////////////////
        else if (radiobtn2.isChecked) {

            if (Inputdate.isEmpty() and Inputtime2.isEmpty()) {
                datee.error = getString(R.string.Fieldcantbeempty)
                time2.error = getString(R.string.Fieldcantbeempty)
                false
            } else if (Inputdate.isEmpty()) {
                datee.error = getString(R.string.Fieldcantbeempty)
                time2.error = null
                false
            } else if (Inputtime2.isEmpty()) {
                time2.error = getString(R.string.Fieldcantbeempty)
                datee.error = null
                false
            } else {
                datee.error = null
                time2.error = null
                true
            }

        } else {
            false
        }

    }

    private fun ValidateRadiobtmchecked(): Boolean {

        return if (radiobtn1.isChecked or radiobtn2.isChecked) {
            true
        } else {
            error_radiobtn_check.visibility = View.VISIBLE
            error_radiobtn_check.text = getString(R.string.Selectanyoneoption)
            false
        }
    }

    fun confirmListDuration(v: View) {
        if (!validateListDuration() or !ValidateRadiobtmchecked()) {
            return
        } else {

            if (radiobtn1.isChecked) {
                //lalalalalalala
                val durationtext: String = select_dur.selectedItem.toString()
                StaticClassAdCreate.fixLength = "fixed_length"

                val timetxt: String = select_time_btn1.text.toString()
                var hour = timetxt.substring(0, 2)
                var minute = timetxt.substring(3, 5)
                var time = "$hour : $minute : 00"

                StaticClassAdCreate.timepicker = time

                StaticClassAdCreate.duration = durationtext.take(6)
                StaticClassAdCreate.endtime = ""

            } else if (radiobtn2.isChecked) {
                val datetext: String = select_date.text.toString()
                StaticClassAdCreate.fixLength = "end_time"

                val timetxt2: String = select_time2.text.toString()

                var hour2 = timetxt2.substring(0, 2)
                var minute2 = timetxt2.substring(3, 5)
                var time2 = "$hour2 : $minute2 : 00"
                StaticClassAdCreate.timepicker = time2

                StaticClassAdCreate.endtime = datetext
                StaticClassAdCreate.duration = ""
            }

            findNavController().navigate(R.id.listdur_pickupship)

        }

    }

    private fun dateInAlpha(monthNumber: Int): String {
        when (monthNumber) {
            1 -> {
                return "Jan"
            }
            2 -> {
                return "Feb"
            }
            3 -> {
                return "Mar"
            }
            4 -> {
                return "Apr"
            }
            5 -> {
                return "May"
            }
            6 -> {
                return "Jun"
            }
            7 -> {
                return "Jul"
            }
            8 -> {
                return "Aug"
            }
            9 -> {
                return "Sep"
            }
            10 -> {
                return "Oct"
            }
            11 -> {
                return "Nov"
            }
            12 -> {
                return "Dec"
            }
            else -> return ""
        }
    }

    fun hidekeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun addDay(oldDate: String?, numberOfDays: Int): String? {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val c = Calendar.getInstance()
        try {
            c.time = dateFormat.parse(oldDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.DAY_OF_YEAR, numberOfDays)
        dateFormat = SimpleDateFormat("dd/MM/YYYY")
        val newDate = Date(c.timeInMillis)
        return dateFormat.format(newDate)
    }
}

