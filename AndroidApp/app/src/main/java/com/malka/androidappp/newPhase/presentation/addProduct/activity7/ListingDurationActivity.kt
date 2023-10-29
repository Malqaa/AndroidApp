package com.malka.androidappp.newPhase.presentation.addProduct.activity7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.DatePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.TimePickerFragment
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malka.androidappp.newPhase.domain.models.servicemodels.TimeAuctionSelection
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.addProduct.ConfirmationAddProductActivity
import com.malka.androidappp.newPhase.presentation.addProduct.activity8.PromotionalActivity
import kotlinx.android.synthetic.main.activity_listing_duration.*
import kotlinx.android.synthetic.main.activity_listing_duration.switchMustPickUp
import kotlinx.android.synthetic.main.activity_listing_duration.tvMustPickUp
import kotlinx.android.synthetic.main.activity_pricing_payment.*
import kotlinx.android.synthetic.main.selection_item.view.*
import kotlinx.android.synthetic.main.shipping_option.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class ListingDurationActivity : BaseActivity() {
    var fixlenghtselected: TimeAuctionSelection? = null
    var selectdate = ""
    var selectTime = ""
    var fm: FragmentManager? = null
    var isEdit: Boolean = false
    val allWeeks: ArrayList<TimeAuctionSelection> = ArrayList()
    val shippingOptionList: ArrayList<Selection> = ArrayList()
    var pickUpOption: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_duration)
        toolbar_title.text = getString(R.string.shipping_options)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setVieClickListeners()
        containerPickUpOption.hide()
        if (AddProductObjectData.auctionOption) {
            contianerClosingOption.show()
        } else {
            contianerClosingOption.hide()
        }

        /**set time for normal clossing*/
        val c = Calendar.getInstance()
        val currentDay = c.get(Calendar.DAY_OF_MONTH)
        // Variables to get weeks from the current date
//        val currentDate = SimpleDateFormat("MM/dd/yyyy")
//        val todayDate = Date()
//        val thisDate = currentDate.format(todayDate)
//        val week1 = addDay(day.toString(), 7)
//        val week2 = addDay(day.toString(), 14)
//        val week3 = addDay(day.toString(), 21)
//        val week4 = addDay(day.toString(), 28)
        try {
            AddProductObjectData.selectedCategory?.let {
                var characterList = it.auctionClosingPeriods?.toCharArray()
                if (characterList != null) {
                    for (c in characterList) {
                        if (c != ',') {
                            var cNumer: Int = 1
                            try {
                                cNumer = c.toString().toInt()
                            } catch (e: java.lang.Exception) {

                            }

                            when (it.auctionClosingPeriodsUnit) {
                                ConstantObjects.auctionClosingPeriodsUnit_day -> {
                                    var date = addDay(currentDay.toString(), cNumer)
                                    allWeeks.add(
                                        TimeAuctionSelection(
                                            c.toString(),
                                            date,
                                            HelpFunctions.getAuctionClosingTime2(date),
                                            it.auctionClosingPeriodsUnit
                                        )
                                    )
                                }
                                ConstantObjects.auctionClosingPeriodsUnit_month -> {
                                    var date = addMonth(currentDay.toString(), cNumer)
                                    allWeeks.add(
                                        TimeAuctionSelection(
                                            c.toString(),
                                            date,
                                            HelpFunctions.getAuctionClosingTime2(date),
                                            it.auctionClosingPeriodsUnit
                                        )
                                    )
                                }
                                else -> {
                                    var date = addDay(currentDay.toString(), cNumer * 7)
                                    allWeeks.add(
                                        TimeAuctionSelection(
                                            c.toString(),
                                            date,
                                            HelpFunctions.getAuctionClosingTime2(date),
                                            it.auctionClosingPeriodsUnit
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
            fixLenghtAdaptor(allWeeks)
        } catch (e: Exception) {
        }


        /**shipping data */
        shippingOptionList.apply {
            add(
                Selection(
                    getString(R.string.integratedShippingCompanies),
                    ConstantObjects.shippingOption_integratedShippingCompanyOptions
                )
            )
            add(
                Selection(
                    getString(R.string.free_shipping_within_Saudi_Arabia),
                    ConstantObjects.shippingOption_freeShippingWithinSaudiArabia
                )
            )
            add(
                Selection(
                    getString(R.string.arrangementWillBeMadeWithTheBuyer),
                    ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer
                )
            )
//            add(Selection("Shipping Not Available" ))
//            add(Selection("To be Arranged" ))
//            add(Selection("Specify Shipping Cost" ))
        }
        /**adding price off custom clossing fee*/
        AddProductObjectData.selectedCategory?.let {
            tvPriceCustomClosingAuctionOption2.text =
                "${it.auctionClosingTimeFee} ${getString(R.string.Rayal)}"
        }
        /****/
        if (isEdit) {
            setData()
        } else {
            shippingOptionAdaptor(shippingOptionList, rvShippingOption)
        }
    }

    private fun setData() {
        pickUpOption = AddProductObjectData.pickUpOption
        when (AddProductObjectData.pickUpOption) {
            ConstantObjects.pickUp_Must -> {
                setPickUpMust()
                containerPickUpOption.hide()
            }
            ConstantObjects.pickUp_No -> {
                setPickUpNo()
                containerPickUpOption.show()
            }
            ConstantObjects.pickUp_Available -> {
                setPickUpAvailable()
                containerPickUpOption.show()
            }

        }

        shippingOptionList.forEach { item ->
            AddProductObjectData.shippingOptionSelection?.let {
                for (item2 in it) {
                    if (item.id == item2.id) {
                        item.isSelected = true
                        break
                    }
                }

            }

        }
        shippingOptionAdaptor(shippingOptionList, rvShippingOption)
        AddProductObjectData.selectTimeAuction?.let {
            if (it.customOption) {
                closingAuctionOption2.performClick()
                tvClosingAuctionCustomDataOption2.text = it.text
            } else {
                closingAuctionOption1.performClick()
                for (item in allWeeks) {
                    if (item.text == it.text) {
                        item.isSelect = true
                        break
                    }
                }
                fixLenghtAdaptor(allWeeks)
            }
        }
    }

    private fun setPickUpMust() {

        /**pickUpNo*/
        switchNoPickUp.setBackgroundResource(R.drawable.edittext_bg)
        tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        rbNoPickup.isChecked = false
        /**pickUpAvailable*/
        switchAvailablePickUp.setBackgroundResource(R.drawable.edittext_bg)
        tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        rbAvailablePickup.isChecked = false
        /**pickUpMust*/
        switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
        tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        rbMustPickup.isChecked = true
    }

    private fun setPickUpNo() {
        /**pickUpMust*/
        switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
        tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        rbMustPickup.isChecked = false

        /**pickUpAvailable*/
        switchAvailablePickUp.setBackgroundResource(R.drawable.edittext_bg)
        tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        rbAvailablePickup.isChecked = false
        /**pickUpNo*/
        switchNoPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
        tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        rbNoPickup.isChecked = true
    }

    private fun setPickUpAvailable() {
        /**pickUpMust*/
        switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
        tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        rbMustPickup.isChecked = false
        /**pickUpNo*/
        switchNoPickUp.setBackgroundResource(R.drawable.edittext_bg)
        tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        rbNoPickup.isChecked = false
        /**pickUpAvailable*/
        switchAvailablePickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
        tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        rbAvailablePickup.isChecked = true
    }

    private fun setVieClickListeners() {
        btnRadioClosingAuctionOption1.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                closingAuctionOption1.performClick()
            }
        }
        closingAuctionOption1.setOnClickListener {
            closingAuctionOption1.background =
                ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.bg))
            closingAuctionOption2.setSelected(false)
            btnRadioClosingAuctionOption1.isChecked = true
            btnRadioClosingAuctionOption2.isChecked = false

            tvClosingAuctionCustomDataOption2.setText("")
            tvClosingAuctionCustomDataOption2.hint = getString(R.string.SelectTime)
        }
        btnRadioClosingAuctionOption2.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                closingAuctionOption2.performClick()
            }
        }
        closingAuctionOption2.setOnClickListener {
            closingAuctionOption2.setSelected(true)
            closingAuctionOption1.background = ContextCompat.getDrawable(this, R.drawable.edittext_bg)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            btnRadioClosingAuctionOption1.isChecked = false
            btnRadioClosingAuctionOption2.isChecked = true
            allWeeks.forEach {
                it.isSelect = false
            }
            fixLenghtAdaptor(allWeeks)
            //tvClosingAuctionCustomDataOption2.text = "$selectdate - $selectTime"


        }
        tvClosingAuctionCustomDataOption2.setOnClickListener {
            fm = supportFragmentManager
            var dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                tvClosingAuctionCustomDataOption2.text = ""
                selectdate = selectdate_
                var timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    btnRadioClosingAuctionOption2.isChecked = true
                    tvClosingAuctionCustomDataOption2.text = selectdate +" "+ selectTime
                    println("hhhh " + HelpFunctions.getAuctionClosingTime("$selectdate_ $selectTime$"))

                }
                timeDialog.show(fm!!, "")
            }
            dateDialog.show(fm!!, "")
        }
        back_btn.setOnClickListener {
            onBackPressed()
        }

        rbMustPickup.setOnCheckedChangeListener { _, b ->
            if (b) {
                pickUpOption = ConstantObjects.pickUp_Must
                setPickUpMust()
            } else {
                pickUpOption = 0
                switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
                tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
            containerPickUpOption.hide()

        }
        rbNoPickup.setOnCheckedChangeListener { _, b ->
            if (b) {
                pickUpOption = ConstantObjects.pickUp_No
                setPickUpNo()
                containerPickUpOption.show()
            } else {
                pickUpOption = 0
                switchNoPickUp.setBackgroundResource(R.drawable.edittext_bg)
                tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                containerPickUpOption.hide()
            }
        }
        rbAvailablePickup.setOnCheckedChangeListener { _, b ->
            if (b) {
                pickUpOption = ConstantObjects.pickUp_Available
                setPickUpAvailable()
                containerPickUpOption.show()
            } else {
                pickUpOption = 0
                switchAvailablePickUp.setBackgroundResource(R.drawable.edittext_bg)
                tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                containerPickUpOption.hide()
            }
        }
        btn_listduration.setOnClickListener {
            confirmListDuration2()
        }
    }

    @SuppressLint("ResourceType")
    private fun fixLenghtAdaptor(list: ArrayList<TimeAuctionSelection>) {
        println("hhh " + Gson().toJson(list))
        rvClosingTimeListOption1.adapter =
            object : GenericListAdapter<TimeAuctionSelection>(
                R.layout.selection_item,
                bind = { element, holder, itemCount, position ->
                    holder.view.run {
                        element.run {
                            val unit: String = when (unitType) {
                                ConstantObjects.auctionClosingPeriodsUnit_day -> {
                                    getString(R.string.day)
                                }
                                ConstantObjects.auctionClosingPeriodsUnit_month -> {
                                    getString(R.string.month)
                                }
                                else -> {
                                    getString(R.string.week)
                                }
                            }
                            selection_tv.text = "$text $unit"
                            if (isSelect) {
                                selection_tv.isSelected = true
                            } else {
                                selection_tv.isSelected = false
                            }
                            setOnClickListener {
                                list.forEachIndexed { index, addBankDetail ->
                                    addBankDetail.isSelect = index == position
                                }
                                rvClosingTimeListOption1.post {
                                    rvClosingTimeListOption1.adapter!!.notifyDataSetChanged()
                                }
                                fixlenghtselected = element
                                btnRadioClosingAuctionOption1.isChecked = true
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

    fun confirmListDuration2() {
//        if (!ValidateRadiobtmchecked()) {
//            return
//        } else
        if (rbMustPickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_Must
        } else if (rbNoPickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_No
        } else if (rbAvailablePickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_Available
        }

        if (AddProductObjectData.auctionOption && !validateListDuration()) {
            return
        } else if (!validatePickUpOption()) {
            return
        } else {
            println("hhh date "+Gson().toJson(AddProductObjectData.selectTimeAuction))
            goNextActivity()

        }

    }

    private fun validatePickUpOption(): Boolean {
        if (pickUpOption == 0) {
            showError(getString(R.string.Please_select, getString(R.string.SelectaPickupOption)))
            return false
        } else if (pickUpOption == ConstantObjects.pickUp_No || pickUpOption == ConstantObjects.pickUp_Available) {
            val list = shippingOptionList.filter { it.isSelected == true }
            if (list.isEmpty()) {
                showError(
                    getString(
                        R.string.Please_select,
                        getString(R.string.Selectshippingoptions)
                    )
                )
                return false
            } else {
                AddProductObjectData.shippingOptionSelection = list
                return true
            }
        }
        return true
    }

//    private fun ValidateRadiobtmchecked(): Boolean {
//        shippingOptionList.filter {
//            it.isSelected == true
//        }.isEmpty().let {
//            if (it) {
//                showError(getString(R.string.Please_select, getString(R.string.shipping_options)))
//                return false
//            } else {
//                return true
//            }
//        }
//    }

    private fun validateListDuration(): Boolean {
        return if (btnRadioClosingAuctionOption1.isChecked) {
            if (fixlenghtselected == null) {
                showError( getString(R.string.close_time))
                return false
            } else {
                println("hhhh "+fixlenghtselected)
                AddProductObjectData.selectTimeAuction = fixlenghtselected
                return true
            }
        } else if (btnRadioClosingAuctionOption2.isChecked) {
            if (tvClosingAuctionCustomDataOption2.text.toString().isEmpty()) {

                showError( getString(R.string.close_time))
                return false
            } else {
                AddProductObjectData.selectTimeAuction = TimeAuctionSelection(
                    tvClosingAuctionCustomDataOption2.text.toString(),
                    tvClosingAuctionCustomDataOption2.text.toString(),
                    HelpFunctions.getAuctionClosingTime(tvClosingAuctionCustomDataOption2.text.toString()),
                    0,
                    customOption = true
                )
                return true
            }

        } else {

            showError(getString(R.string.close_time))
            false
        }

    }

    override fun onBackPressed() {

        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                finish()
            })
        } else {
            finish()
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

    fun addMonth(oldDate: String?, numberOfDays: Int): String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val c = Calendar.getInstance()
        try {
            c.time = dateFormat.parse(oldDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        c.add(Calendar.MONTH, numberOfDays)
        dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val newDate = Date(c.timeInMillis)
        return dateFormat.format(newDate)
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
                        if (isSelected) {
                            rvSelected.setImageResource(R.drawable.ic_radio_button_checked)
                        } else {
                            rvSelected.setImageResource(R.drawable.ic_radio_button_unchecked)
                        }
                        shipping_option_layout.setOnClickListener {
                            list.forEach { item->
                                item.isSelected=false
                            }
                            list[position].isSelected = true
                            rcv.post { rcv.adapter?.notifyDataSetChanged() }
                            // selection = element
                        }
                        rvSelected.setOnClickListener {
                            shipping_option_layout.performClick()
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


    private fun goNextActivity() {
        if (isEdit) {
            startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
                finish()
            })
        } else {
            startActivity(Intent(this, PromotionalActivity::class.java).apply {
            })

        }
    }

    /*****************/


//    fun confirmListDuration() {
//        if (!validateListDuration() or !ValidateRadiobtmchecked()) {
//            return
//        } else {
//            if (btnRadioClosingAuctionOption1.isChecked) {
//                AddProductObjectData.fixLength = "fixed_length"
//                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//                AddProductObjectData.timepicker = sdf.format(Date())
//                AddProductObjectData.duration = fixlenghtselected!!.text
//                AddProductObjectData.endtime =
//                    fixlenghtselected!!.endTime + " " + sdf.format(Date())
//                AddProductObjectData.fixlenghtselected = fixlenghtselected
//
//            } else if (btnRadioClosingAuctionOption2.isChecked) {
//                AddProductObjectData.fixLength = "end_time"
//                AddProductObjectData.timepicker = selectTime
//                AddProductObjectData.duration = ""
//                AddProductObjectData.endtime = selectdate + " " + selectTime
//            }
//            saveSelectedcheckbox()
//            saveShippingOption()
//            if (isEdit) {
//                startActivity(Intent(this, ConfirmationAddProductActivity::class.java).apply {
//                    finish()
//                })
//            } else {
//                startActivity(Intent(this, PromotionalActivity::class.java).apply {
//                })
//
//            }
//        }
//
//    }


//    fun saveSelectedcheckbox() {
//
//        val list = allWeeks.filter {
//            it.isSelect == true
//
//        }
//        list.forEach {
//            AddProductObjectData.weekSelection = it
//        }
//    }


}

