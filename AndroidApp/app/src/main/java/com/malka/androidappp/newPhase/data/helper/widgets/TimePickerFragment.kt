package com.malka.androidappp.newPhase.data.helper.widgets

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.malka.androidappp.R
import java.util.*

class TimePickerFragment(val onItemClick: (selectTime: String) -> Unit) : DialogFragment(),
    OnTimeSetListener {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        val minute = c[Calendar.MINUTE]
        return TimePickerDialog(activity, this, hour, minute, false)
    }


    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        var hourString = hourOfDay.toString()
        if (hourString.length == 1) {
            hourString = "0$hourString"
        }


        var minuteString = minute.toString()
        if (minuteString.length == 1) {
            minuteString = "0$minuteString"
        }
        var other = ""
//        if (hourString.toInt() >= 12) {
//            other = getString(R.string.PM)
//            hourString = (hourString.toInt() - 12).toString()
//        } else {
//            other = getString(R.string.AM)
//        }
        if (hourString.equals("00")) {
            hourString = "12"
        }
        val selectTime = "$hourString:$minuteString"
        onItemClick.invoke(selectTime)
    }
}