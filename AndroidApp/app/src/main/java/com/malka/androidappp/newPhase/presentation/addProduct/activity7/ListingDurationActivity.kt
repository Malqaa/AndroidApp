package com.malka.androidappp.newPhase.presentation.addProduct.activity7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.TimePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.domain.models.servicemodels.TimeSelection
import com.malka.androidappp.newPhase.presentation.addProduct.ConfirmationAddProductActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity8.PromotionalActivity
import kotlinx.android.synthetic.main.activity_listing_duration.*
import kotlinx.android.synthetic.main.activity_listing_duration.Tender_pickUp_l
import kotlinx.android.synthetic.main.activity_listing_duration.Tender_pickUp_tv
import kotlinx.android.synthetic.main.activity_pricing_payment.*
import kotlinx.android.synthetic.main.selection_item.view.*
import kotlinx.android.synthetic.main.shipping_option.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ListingDurationActivity : BaseActivity() {
    var fixlenghtselected: TimeSelection? = null
    var selectdate = ""
    var selectTime = ""
    var fm: FragmentManager? = null
    var isEdit: Boolean = false
    val allWeeks: ArrayList<TimeSelection> = ArrayList()
    val shippingOptionList: ArrayList<Selection> = ArrayList()


    override fun onBackPressed() {

        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                finish()
            })
        } else {
            finish()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_duration)
        toolbar_title.text = getString(R.string.shipping_options)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setVieClickListeners()
        option_1.isEnabled = false
        option_2.isEnabled = false
        /***/

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

        allWeeks.apply {
            add(TimeSelection("1 week", week1))
            add(TimeSelection("2 week", week2))
            add(TimeSelection("3 week", week3))
            add(TimeSelection("4 week", week4))
        }


        fixLenghtAdaptor(allWeeks)


        /**shipping data */
        shippingOptionList.apply {
            add(Selection(getString(R.string.free_shipping_within_Saudi_Arabia)))
//            add(Selection("Shipping Not Available" ))
//            add(Selection("To be Arranged" ))
//            add(Selection("Specify Shipping Cost" ))
        }

        if (isEdit) {
            setData()
        }else{
            shippingOptionAdaptor(shippingOptionList, shipping_option)
        }
//        if (isEdit) {
//            selectTime = AddProductObjectData.timepicker
//            selectdate = AddProductObjectData.endtime
//            if (AddProductObjectData.fixLength.equals("fixed_length")) {
//                allWeeks.forEach {
//                    it.isSelect = it.text.equals(AddProductObjectData.weekSelection!!.text)
//                }
//                fixlenghtselected = AddProductObjectData.fixlenghtselected
//                option_1.performClick()
//            } else {
//                option_2.performClick()
//            }
//
//
//            btn_listduration.setOnClickListener {
//                confirmListDuration()
//            }
//        }
    }

    private fun setData() {
        if(AddProductObjectData.pickUpOption){
            Tender_pickUp_l.setBackgroundResource(R.drawable.field_selection_border_enable)
            Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))
            pickup_rb.isChecked =true
        }else{
            Tender_pickUp_l.setBackgroundResource(R.drawable.edittext_bg)
            Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            pickup_rb.isChecked =false
        }
        shippingOptionList.forEach {item->
            if(item.name==AddProductObjectData.shippingOptionSelection?.name){
                item.isSelected=true
            }
        }
        shippingOptionAdaptor(shippingOptionList, shipping_option)
    }

    private fun setVieClickListeners() {
        btn_listduration.setOnClickListener {
            confirmListDuration2()
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
        own_time_tv.setOnClickListener {
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                val timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    radiobtn2.isChecked = true
                }
                timeDialog.show(fm!!, "")
                selectdate = selectdate_

            }
            dateDialog.show(fm!!, "")
        }
        option_1.setOnClickListener {
            option_1.background =
                ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.bg))
            option_2.setSelected(false)
            radiobtn1.isChecked = true
            radiobtn2.isChecked = false

            own_time_tv.setText("")
            own_time_tv.hint = getString(R.string.SelectTime)
        }
        option_2.setOnClickListener {
            option_2.setSelected(true)
            option_1.background = ContextCompat.getDrawable(this, R.drawable.edittext_bg)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            radiobtn1.isChecked = false
            radiobtn2.isChecked = true
            allWeeks.forEach {
                it.isSelect = false
            }
            fixLenghtAdaptor(allWeeks)
            own_time_tv.text = "$selectdate - $selectTime"


        }
        back_btn.setOnClickListener {
            onBackPressed()
        }
        pickup_rb.setOnCheckedChangeListener { _, b ->
            if (b) {
                Tender_pickUp_l.setBackgroundResource(R.drawable.field_selection_border_enable)
                Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.bg))

            } else {
                Tender_pickUp_l.setBackgroundResource(R.drawable.edittext_bg)
                Tender_pickUp_tv.setTextColor(ContextCompat.getColor(this, R.color.text_color))

            }
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
        } else if (radiobtn2.isChecked) {

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


        shippingOptionList.filter {
            it.isSelected == true
        }.isEmpty().let {
            if (it) {
                showError(getString(R.string.Please_select, getString(R.string.shipping_options)))
                return false
            } else {
                return true
            }
        }
    }

    fun confirmListDuration2() {
        if (!ValidateRadiobtmchecked()) {
            return
        } else {
            goNextActivity()

        }

    }

    private fun goNextActivity() {
        saveShippingOption()
        AddProductObjectData.pickUpOption = pickup_rb.isChecked
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                finish()
            })
        } else {
            startActivity(Intent(this, PromotionalActivity::class.java).apply {
            })

        }
    }

    fun confirmListDuration() {
        if (!validateListDuration() or !ValidateRadiobtmchecked()) {
            return
        } else {
            if (radiobtn1.isChecked) {
                AddProductObjectData.fixLength = "fixed_length"
                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                AddProductObjectData.timepicker = sdf.format(Date())
                AddProductObjectData.duration = fixlenghtselected!!.text
                AddProductObjectData.endtime =
                    fixlenghtselected!!.endTime + " " + sdf.format(Date())
                AddProductObjectData.fixlenghtselected = fixlenghtselected

            } else if (radiobtn2.isChecked) {
                AddProductObjectData.fixLength = "end_time"
                AddProductObjectData.timepicker = selectTime
                AddProductObjectData.duration = ""
                AddProductObjectData.endtime = selectdate + " " + selectTime
            }
            saveSelectedcheckbox()
            saveShippingOption()
            if (isEdit) {
                startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                    finish()
                })
            } else {
                startActivity(Intent(this, PromotionalActivity::class.java).apply {
                })

            }
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
    @SuppressLint("ResourceType")
    private fun fixLenghtAdaptor(list: ArrayList<TimeSelection>) {
        fix_lenght_rcv.adapter =
        object : GenericListAdapter<TimeSelection>(
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
                            fix_lenght_rcv.post {
                                fix_lenght_rcv.adapter!!.notifyDataSetChanged()
                            }
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

    var selection: Selection? = null

    @SuppressLint("ResourceType")
    private fun shippingOptionAdaptor(list: ArrayList<Selection>, rcv: RecyclerView) {
        rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.shipping_option,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        shipping_opt_tv.text = name
                        rb2_1.isChecked = isSelected
                        shipping_option_layout.setOnClickListener {
                            list.forEach {
                                it.isSelected = false
                            }
                            list.get(position).isSelected = true
                            rcv.post { rcv.adapter?.notifyDataSetChanged() }
                            selection = element
                        }
                        rb2_1.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                shipping_option_layout.performClick()
                            }

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


    fun saveSelectedcheckbox() {

        val list = allWeeks.filter {
            it.isSelect == true

        }
        list.forEach {
            AddProductObjectData.weekSelection = it
        }
    }

    fun saveShippingOption() {

        val list = shippingOptionList.filter {
            it.isSelected == true
        }
        list.forEach {
            AddProductObjectData.shippingOptionSelection = it
        }

    }

}

