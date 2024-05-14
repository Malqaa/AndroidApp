package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.malqaa.androidappp.newPhase.utils.helper.widgets.TimePickerFragment
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.TimeAuctionSelection
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionObject
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8.PromotionalActivity
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
import kotlin.collections.ArrayList


class ListingDurationActivity : BaseActivity(), ShippingAdapter.SetOnSelectedShipping {
    var fixlenghtselected: TimeAuctionSelection? = null
    var selectdate = ""
    var selectTime = ""
    var fm: FragmentManager? = null
    var isEdit: Boolean = false
    val allWeeks: ArrayList<TimeAuctionSelection> = ArrayList()
    val pickUpOptionList: ArrayList<ShippingOptionObject> = ArrayList()
    val shippingOptionList: ArrayList<ShippingOptionObject> = ArrayList()
    var shippingAdapter: ShippingAdapter? = null
    var pickUpOption: Int = 0
    private lateinit var shippingViewModel: ShippingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing_duration)
        shippingViewModel = ViewModelProvider(this).get(ShippingViewModel::class.java)

        toolbar_title.text = getString(R.string.shipping_options)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setVieClickListeners()
        shippingAdapter = ShippingAdapter(arrayListOf(), this)
        containerPickUpOption.hide()
        if (AddProductObjectData.auctionOption) {
            contianerClosingOption.show()
        } else {
            contianerClosingOption.hide()
        }
        shippingViewModel.getAllShippingOptions()
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
                val characterList = it.auctionClosingPeriods?.toCharArray()
                if (characterList != null) {
                    for (c in characterList) {
                        if (c != ',') {
                            var cNumer = 1
                            try {
                                cNumer = c.toString().toInt()
                            } catch (e: java.lang.Exception) {
//
                            }

                            when (it.auctionClosingPeriodsUnit) {
                                ConstantObjects.auctionClosingPeriodsUnit_day -> {
                                    val date = addDay(currentDay.toString(), cNumer)
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
                                    val date = addMonth(currentDay.toString(), cNumer)
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
                                    val date = addDay(currentDay.toString(), cNumer * 7)
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
            //
        }


        /**shipping data */
//        pickUpOptionList.apply {
//            add(
//                Selection(
//                    getString(R.string.integratedShippingCompanies),
                    ConstantObjects.shippingOption_integratedShippingCompanyOptions
//                )
//            )
//            add(
//                Selection(
//                    getString(R.string.free_shipping_within_Saudi_Arabia),
//                    ConstantObjects.shippingOption_freeShippingWithinSaudiArabia
//                )
//            )
//            add(
//                Selection(
//                    getString(R.string.arrangementWillBeMadeWithTheBuyer),
//                    ConstantObjects.shippingOption_arrangementWillBeMadeWithTheBuyer
//                )
//            )
////            add(Selection("Shipping Not Available" ))
////            add(Selection("To be Arranged" ))
////            add(Selection("Specify Shipping Cost" ))
//        }
        /**adding price off custom clossing fee*/
        AddProductObjectData.selectedCategory?.let {
            tvPriceCustomClosingAuctionOption2.text =
                "${it.auctionClosingTimeFee} ${getString(R.string.Rayal)}"
        }
        /****/

//        else {
//           a(pickUpOptionList, rvShippingOption)
//        }

        getAllShippingObserver()
    }

    private fun setData() {
        pickUpOption = AddProductObjectData.shippingOption
        for (i in shippingOptionList.indices) {
            if (pickUpOption == shippingOptionList[i].id) {
                shippingOptionList[i].selected = true
            }
        }

        when (AddProductObjectData.shippingOption) {
            ConstantObjects.pickUp_Must -> {
                containerPickUpOption.hide()
            }

            ConstantObjects.pickUp_No -> {
                containerPickUpOption.show()
            }

            ConstantObjects.pickUp_Available -> {
                containerPickUpOption.show()
            }
        }

        shippingOptionList.forEach { item ->
            AddProductObjectData.shippingOptionSelections?.let { shList ->
                for (i in shList) {
                    if (item.id == i) {
                        if (i == ConstantObjects.pickUp_Must)
                            pickUpOption = ConstantObjects.pickUp_Must

                        item.selected = true
                        break
                    }
                }

            }
        }

        shippingAdapter?.updateAdapter(shippingOptionList)

        pickUpOptionList.forEach { item ->
            AddProductObjectData.shippingOptionSelections?.let { shList ->
                for (i in shList) {
                    if (item.id == i) {
                        item.selected = true
                        break
                    }
                }

            }
        }

        pickUpOptionAdapter(pickUpOptionList, rvShippingOption)
        if(AddProductObjectData.isAuctionClosingTimeFixed){
            closingAuctionOption2.performClick()
            tvClosingAuctionCustomDataOption2.text =
                HelpFunctions.getViewFormatForDateTrack(AddProductObjectData.selectTimeAuction?.endTime,"dd/MM/yyyy HH:mm:ss")

        }else{
            AddProductObjectData.selectTimeAuction?.let {
                if (it.customOption) {
                    closingAuctionOption2.performClick()
                    if (it.text == "")
                        tvClosingAuctionCustomDataOption2.text =
                            HelpFunctions.getViewFormatForDateTrack(it.endTime,"dd/MM/yyyy HH:mm:ss")
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

    }

    private fun getAllShippingObserver() {
        shippingViewModel.shippingListObserver.observe(this) {
            pickUpOptionList.addAll(it.shippingOptionObject?.subList(3, 6) ?: arrayListOf())
            shippingOptionList.addAll(it.shippingOptionObject?.subList(0, 3) ?: arrayListOf())
            shippingAdapter?.updateAdapter(shippingOptionList)
            recycleShipping.adapter = shippingAdapter
            pickUpOptionAdapter(pickUpOptionList, rvShippingOption)
            if (isEdit) {
                setData()
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
            AddProductObjectData.isAuctionClosingTimeFixed=false
            closingAuctionOption1.background =
                ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
            FixedLength.setTextColor(ContextCompat.getColor(this, R.color.bg))
            closingAuctionOption2.isSelected = false
            btnRadioClosingAuctionOption1.isChecked = true
            btnRadioClosingAuctionOption2.isChecked = false

            tvClosingAuctionCustomDataOption2.text = ""
            tvClosingAuctionCustomDataOption2.hint = getString(R.string.SelectTime)
        }
        btnRadioClosingAuctionOption2.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                closingAuctionOption2.performClick()
            }
        }
        closingAuctionOption2.setOnClickListener {
            AddProductObjectData.isAuctionClosingTimeFixed=true
            closingAuctionOption2.isSelected = true
            closingAuctionOption1.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_bg)
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
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                tvClosingAuctionCustomDataOption2.text = ""
                selectdate = selectdate_
                val timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    btnRadioClosingAuctionOption2.isChecked = true
                    tvClosingAuctionCustomDataOption2.text = selectdate + " " + selectTime
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
                            selection_tv.isSelected = isSelect
                            fixlenghtselected = list.find { it.isSelect }

                            setOnClickListener {
                                list.forEachIndexed { index, addBankDetail ->
                                    addBankDetail.isSelect = index == position
                                }
                                rvClosingTimeListOption1.post {
                                    rvClosingTimeListOption1.adapter!!.notifyDataSetChanged()
                                }
                                fixlenghtselected = element
                                val dateFormat = SimpleDateFormat("HH:mm:ss")
                                val currentTime = dateFormat.format(Date())
                                fixlenghtselected?.endTime=fixlenghtselected?.endTime+" "+currentTime
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
            println("hhh date " + Gson().toJson(AddProductObjectData.shippingOptionSelections))
            goNextActivity()

        }

    }

    private fun validatePickUpOption(): Boolean {
        if (pickUpOption == 0) {
            showError(getString(R.string.Please_select, getString(R.string.SelectaPickupOption)))
            return false
        } else if (pickUpOption == ConstantObjects.pickUp_No || pickUpOption == ConstantObjects.pickUp_Available) {
            val list = pickUpOptionList.filter { it.selected }
            if (list.isEmpty()) {
                showError(
                    getString(
                        R.string.Please_select,
                        getString(R.string.Selectshippingoptions)
                    )
                )
                return false
            } else {
                AddProductObjectData.shippingOptionSelections?.clear()
                AddProductObjectData.shippingOptionSelections?.add(pickUpOption)
                AddProductObjectData.shippingOptionSelections?.add(list[0].id)
                return true
            }
        }
        return true
    }

//    private fun ValidateRadiobtmchecked(): Boolean {
//        pickUpOptionList.filter {
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
                showError(getString(R.string.close_time))
                return false
            } else {
                println("hhhh " + fixlenghtselected)
//                fixlenghtselected.apply {
//                    this?.endTime=HelpFunctions.getAuctionClosingTime(fixlenghtselected?.endTime.toString())
//                }

                AddProductObjectData.selectTimeAuction = fixlenghtselected
                return true
            }
        } else if (btnRadioClosingAuctionOption2.isChecked) {
            if (tvClosingAuctionCustomDataOption2.text.toString().isEmpty()) {

                showError(getString(R.string.close_time))
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
                putExtra("whereCome", "Add")
                finish()
            })
        } else {
            finish()
        }

    }

    private fun addDay(oldDate: String?, numberOfDays: Int): String {
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

    private fun addMonth(oldDate: String?, numberOfDays: Int): String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val c = Calendar.getInstance()
        try {
            c.time = dateFormat.parse(oldDate ?: "")
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
    private fun pickUpOptionAdapter(list: ArrayList<ShippingOptionObject>, rcv: RecyclerView) {
        rcv.adapter = object : GenericListAdapter<ShippingOptionObject>(
            R.layout.shipping_option,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        shipping_opt_tv.text = list[position].shippingOptionName
                        if (selected) {
                            rvSelected.setImageResource(R.drawable.ic_radio_button_checked)
                        } else {
                            rvSelected.setImageResource(R.drawable.ic_radio_button_unchecked)
                        }
                        shipping_option_layout.setOnClickListener {
                            list.forEach { item ->
                                item.selected = false
                            }
                            list[position].selected = true

                            AddProductObjectData.pickUpOption = list[position].id

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
                putExtra("whereCome", "Add")
                finish()
            })
        } else {
            startActivity(Intent(this, PromotionalActivity::class.java).apply {
            })

        }
    }

    override fun setOnSelectedShipping(shippingItem: ShippingOptionObject) {
        AddProductObjectData.shippingOptionSelections = arrayListOf()
        if (shippingItem.selected) {
            AddProductObjectData.shippingOptionSelections?.add(shippingItem.id)
            AddProductObjectData.shippingOption = shippingItem.id
            if (shippingItem.id != 1) {
                pickUpOption = shippingItem.id
                containerPickUpOption.show()
                pickUpOptionAdapter(pickUpOptionList, rvShippingOption)
            } else {
                pickUpOption = shippingItem.id
                containerPickUpOption.hide()
            }

        } else {
            containerPickUpOption.hide()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        shippingViewModel.closeAllCall()
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

