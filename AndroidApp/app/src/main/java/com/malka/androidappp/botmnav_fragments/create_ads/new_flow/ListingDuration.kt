package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.helper.widgets.DatePickerFragment
import com.malka.androidappp.helper.widgets.TimePickerFragment
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.servicemodels.TimeSelection
import kotlinx.android.synthetic.main.fragment_listing_duration.*
import kotlinx.android.synthetic.main.selection_item.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ListingDuration : BaseActivity() {
    var fixlenghtselected: TimeSelection? = null
    var selectdate = ""
    var selectTime = ""
    var fm: FragmentManager? = null
    var isSelectShipping = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_listing_duration)


        toolbar_title.text = getString(R.string.shipping_options)
        back_btn.setOnClickListener {
            finish()
        }

        option_1.setOnClickListener {
            option_1.background =
                ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.bg))

            option_2.setSelected(false)
            radiobtn1.isChecked = true
            radiobtn2.isChecked = false
        }
        option_2.setOnClickListener {
            option_2.setSelected(true)
            option_1.background = ContextCompat.getDrawable(this, R.drawable.edittext_bg)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            radiobtn1.isChecked = false
            radiobtn2.isChecked = true
        }

        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)


        // Variables to get weeks from the current date
        val currentDate = SimpleDateFormat("MM/dd/yyyy")
        val todayDate = Date()
        val thisDate = currentDate.format(todayDate)

        val week1 = addDay(day.toString(), 7)
        val week2 = addDay(day.toString(), 14)
        val week3 = addDay(day.toString(), 21)
        val week4 = addDay(day.toString(), 28)


        val allWeeks: ArrayList<TimeSelection> = ArrayList()
        allWeeks.apply {
            add(TimeSelection("1 week", week1))
            add(TimeSelection("2 week", week2))
            add(TimeSelection("3 week", week3))
            add(TimeSelection("4 week", week4))
        }
        fixLenghtAdaptor(allWeeks)



        own_time_tv.setOnClickListener {
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                val timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    own_time_tv.text = "$selectdate_ - $selectTime"
                    radiobtn2.isChecked = true
                }
                timeDialog.show(fm!!, "")
                selectdate = selectdate_

            }
            dateDialog.show(fm!!, "")
        }




        radiobtn1.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                option_1.performClick()
            }
        }
        radiobtn2.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                option_2.performClick()
            }
        }


        btn_listduration.setOnClickListener {
            confirmListDuration()
        }

        pricepay_radioGroupp.setOnCheckedChangeListener { group, checkedId ->
            isSelectShipping = true
        }
    }


    private fun validateListDuration(): Boolean {


        return if (radiobtn1.isChecked) {
            if (fixlenghtselected == null) {
                showError(getString(R.string.Please_select, getString(R.string.close_time)))
                return false
            } else {
                return true
            }
        }
        ////////////////////////////////////////////////////
        else if (radiobtn2.isChecked) {

            if (own_time_tv.text.toString().isEmpty()) {
                showError(getString(R.string.Please_select, getString(R.string.close_time)))
                return false
            } else {
                return true
            }

        } else {
            false
        }

    }

    private fun ValidateRadiobtmchecked(): Boolean {

        return if (radiobtn1.isChecked or radiobtn2.isChecked) {
            true
        } else {
            showError(getString(R.string.Selectanyoneoption))
            false
        }
    }

    //////////////////////////////////////////////////////////
    private fun validaterShippingRadiobutton(): Boolean {
        return isSelectShipping
    }


    fun confirmListDuration() {
        if (!validateListDuration() or !ValidateRadiobtmchecked() or !validaterShippingRadiobutton()) {
            return
        } else {

            if (radiobtn1.isChecked) {
                StaticClassAdCreate.fixLength = "fixed_length"
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                StaticClassAdCreate.timepicker = sdf.format(Date())
                StaticClassAdCreate.duration = fixlenghtselected!!.text
                StaticClassAdCreate.endtime = fixlenghtselected!!.endTime

            } else if (radiobtn2.isChecked) {
                StaticClassAdCreate.fixLength = "end_time"
                StaticClassAdCreate.timepicker = selectTime
                StaticClassAdCreate.duration = ""
                StaticClassAdCreate.endtime = selectdate
            }


            ///////get value of radiobtn pickup opt1 to static class/////////////
            val selectedId: Int = pricepay_radioGroupp.checkedRadioButtonId
            val pickupopt1: RadioButton = findViewById(selectedId)
            val pickupoptRadiobtnnn1: String = pickupopt1.text.toString()
            StaticClassAdCreate.pickup_option = pickupoptRadiobtnnn1

            ///////get value of radiobtn pickup opt2 to static class/////////////
            val selectedId2: Int = pricepay_radioGroupp.checkedRadioButtonId
            val pickupopt2: RadioButton = findViewById(selectedId2)
            val pickupoptRadiobtnnn2: String = pickupopt2.text.toString()
            StaticClassAdCreate.shipping_option = pickupoptRadiobtnnn2

            startActivity(Intent(this, PromotionalActivity::class.java).apply {
            })

        }

    }


    fun addDay(oldDate: String?, numberOfDays: Int): String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val c = Calendar.getInstance()
        try {
            c.time = dateFormat.parse(oldDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.DAY_OF_YEAR, numberOfDays)
        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newDate = Date(c.timeInMillis)
        return dateFormat.format(newDate)
    }


    private fun fixLenghtAdaptor(list: ArrayList<TimeSelection>) {
        fix_lenght_rcv.adapter = object : GenericListAdapter<TimeSelection>(
            R.layout.selection_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

                        selection_tv.text = text
                        if (isSelect) {
                            selection_tv.setSelected(true)
                        } else {
                            selection_tv.setSelected(false)
                        }
                        setOnClickListener {
                            list.forEachIndexed { index, addBankDetail ->
                                addBankDetail.isSelect = index == position
                            }
                            fix_lenght_rcv.adapter!!.notifyDataSetChanged()
                            fixlenghtselected = element
                            radiobtn1.isChecked = true
                        }


                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }
}

