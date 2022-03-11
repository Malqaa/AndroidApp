package com.malka.androidappp.helper.widgets

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*
import kotlin.Int
import kotlin.Unit

class DatePickerFragment(val maxdayToday:Boolean=false,val minDateToday:Boolean=false,val onItemClick:
    (selectdate: String) -> Unit) :
    DialogFragment(), OnDateSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        val dialog = DatePickerDialog(requireActivity(), this, year, month, day)
        if(maxdayToday){
            dialog.datePicker.maxDate = Date().time
        }
        if(minDateToday){
            dialog.datePicker.minDate = Date().time
        }
        return dialog
    }


    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        var monthString = month.toString()
        if (monthString.length == 1) {
            monthString = "0$monthString"
        }

        val selectdate = "$day/${monthString}/$year"
        onItemClick.invoke(selectdate)
    }
}