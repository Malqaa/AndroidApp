package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityListingDurationBinding
import com.malqaa.androidappp.databinding.SelectionItemBinding
import com.malqaa.androidappp.databinding.ShippingOptionBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData.Companion.selectedCategory
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.TimeAuctionSelection
import com.malqaa.androidappp.newPhase.domain.models.shippingOptionsResp.ShippingOptionObject
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity8.PromotionalActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.confirmationAddProduct.ConfirmationAddProductActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.DatePickerFragment
import com.malqaa.androidappp.newPhase.utils.helper.widgets.TimePickerFragment
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ListingDurationActivity : BaseActivity<ActivityListingDurationBinding>(),
    ShippingAdapter.SetOnSelectedShipping {
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

        // Initialize view binding
        binding = ActivityListingDurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shippingViewModel = ViewModelProvider(this).get(ShippingViewModel::class.java)

        binding.toolbarListduration.toolbarTitle.text = getString(R.string.shipping)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        setVieClickListeners()
        shippingAdapter = ShippingAdapter(arrayListOf(), this)
        binding.containerPickUpOption.hide()
        if (AddProductObjectData.auctionOption) {
            binding.contianerClosingOption.show()
        } else {
            binding.contianerClosingOption.hide()
        }
        shippingViewModel.getAllShippingOptions()

        val c = Calendar.getInstance()
        val currentDay = c.get(Calendar.DAY_OF_MONTH)

        try {
            selectedCategory?.let {
                val characterList = convertStringToIntArray(it.auctionClosingPeriods!!)
                if (characterList != null) {
                    for (c in characterList) {
                        var cNumer = 1
                        try {
                            cNumer = c.toString().toInt()
                        } catch (e: java.lang.Exception) {
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
//                }
            }
            fixLengthAdapter(allWeeks)
        } catch (e: Exception) {
            //
        }


        /**shipping data */
        ConstantObjects.shippingOption_integratedShippingCompanyOptions

        /**adding price off custom clossing fee*/
        val auctionClosingTimeFee = selectedCategory?.auctionClosingTimeFee ?: 0
        val productPriceSar = getString(R.string.product_price_sar, auctionClosingTimeFee)
        val priceCustomClosingAuctionOption = getString(
            R.string.please_note_that_choosing_a_custom_date_for_the_auction_closing_time_will_cost_you,
            productPriceSar
        )

        if (auctionClosingTimeFee.toInt() > 0) {
            binding.tvPriceCustomClosingAuctionOption2.text = priceCustomClosingAuctionOption
        } else {
            binding.tvPriceCustomClosingAuctionOption2.visibility = View.GONE
        }

        getAllShippingObserver()
    }


    private fun convertStringToIntArray(str: String): IntArray {
        // Split the string by comma
        val stringArray = str.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        // Initialize an integer array with the same length
        val intArray = IntArray(stringArray.size)

        // Convert each string element to an integer
        for (i in stringArray.indices) {
            intArray[i] = stringArray[i].toInt()
        }
        return intArray
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
                binding.containerPickUpOption.hide()
            }

            ConstantObjects.pickUp_No -> {
                binding.containerPickUpOption.show()
            }

            ConstantObjects.pickUp_Available -> {
                binding.containerPickUpOption.show()
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

        pickUpOptionAdapter(pickUpOptionList, binding.rvShippingOption)
        if (AddProductObjectData.isAuctionClosingTimeFixed) {
            binding.closingAuctionOption2.performClick()
            binding.tvClosingAuctionCustomDataOption2.text =
                HelpFunctions.getViewFormatForDateTrack(
                    AddProductObjectData.selectTimeAuction?.endTime,
                    "dd/MM/yyyy HH:mm:ss"
                )

        } else {
            AddProductObjectData.selectTimeAuction?.let {
                if (it.customOption) {
                    binding.closingAuctionOption2.performClick()
                    if (it.text == "")
                        binding.tvClosingAuctionCustomDataOption2.text =
                            HelpFunctions.getViewFormatForDateTrack(
                                it.endTime,
                                "dd/MM/yyyy HH:mm:ss"
                            )
                } else {
                    binding.closingAuctionOption1.performClick()
                    for (item in allWeeks) {
                        if (item.text == it.text) {
                            item.isSelect = true
                            break
                        }
                    }
                    fixLengthAdapter(allWeeks)
                }
            }
        }

    }

    private fun getAllShippingObserver() {
        shippingViewModel.shippingListObserver.observe(this) {
            val options = it.shippingOptionObject ?: emptyList()

            // Clear the lists before adding (to prevent duplicates if observer is called multiple times)
            pickUpOptionList.clear()
            shippingOptionList.clear()

            // Add items safely based on size
            shippingOptionList.addAll(options.subList(0, minOf(3, options.size)))

            if (options.size > 3) {
                pickUpOptionList.addAll(options.subList(3, minOf(6, options.size)))
            }

            shippingAdapter?.updateAdapter(shippingOptionList)
            binding.recycleShipping.adapter = shippingAdapter

            pickUpOptionAdapter(pickUpOptionList, binding.rvShippingOption)

            if (isEdit) {
                setData()
            }
        }
    }

    private fun setPickUpMust() {
        /** pickUpNo */
        binding.switchNoPickUp.setBackgroundResource(R.drawable.edittext_bg)
        binding.tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        binding.rbNoPickup.isChecked = false
        /** pickUpAvailable */
        binding.switchAvailablePickUp.setBackgroundResource(R.drawable.edittext_bg)
        binding.tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        binding.rbAvailablePickup.isChecked = false
        /** pickUpMust */
        binding.switchMustPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
        binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        binding.rbMustPickup.isChecked = true
    }

    private fun setPickUpNo() {
        /** pickUpMust */
        binding.switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
        binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        binding.rbMustPickup.isChecked = false
        /** pickUpAvailable */
        binding.switchAvailablePickUp.setBackgroundResource(R.drawable.edittext_bg)
        binding.tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        binding.rbAvailablePickup.isChecked = false
        /** pickUpNo */
        binding.switchNoPickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
        binding.tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        binding.rbNoPickup.isChecked = true
    }

    private fun setPickUpAvailable() {
        /** pickUpMust */
        binding.switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
        binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        binding.rbMustPickup.isChecked = false
        /** pickUpNo */
        binding.switchNoPickUp.setBackgroundResource(R.drawable.edittext_bg)
        binding.tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
        binding.rbNoPickup.isChecked = false
        /** pickUpAvailable */
        binding.switchAvailablePickUp.setBackgroundResource(R.drawable.field_selection_border_enable)
        binding.tvAvailablePickUp.setTextColor(ContextCompat.getColor(this, R.color.bg))
        binding.rbAvailablePickup.isChecked = true
    }

    private fun setVieClickListeners() {
        binding.btnRadioClosingAuctionOption1.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.closingAuctionOption1.performClick()
            }
        }
        binding.closingAuctionOption1.setOnClickListener {
            AddProductObjectData.isAuctionClosingTimeFixed = false
            binding.closingAuctionOption1.background =
                ContextCompat.getDrawable(this, R.drawable.field_selection_border_enable)
            binding.FixedLength.setTextColor(ContextCompat.getColor(this, R.color.bg))
            binding.closingAuctionOption2.isSelected = false
            binding.btnRadioClosingAuctionOption1.isChecked = true
            binding.btnRadioClosingAuctionOption2.isChecked = false
            binding.tvClosingAuctionCustomDataOption2.text = ""
            binding.tvClosingAuctionCustomDataOption2.hint = getString(R.string.SelectTime)
        }
        binding.btnRadioClosingAuctionOption2.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                binding.closingAuctionOption2.performClick()
            }
        }
        binding.closingAuctionOption2.setOnClickListener {
            AddProductObjectData.isAuctionClosingTimeFixed = true
            binding.closingAuctionOption2.isSelected = true
            binding.closingAuctionOption1.background =
                ContextCompat.getDrawable(this, R.drawable.edittext_bg)
            binding.FixedLength.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            binding.btnRadioClosingAuctionOption1.isChecked = false
            binding.btnRadioClosingAuctionOption2.isChecked = true
            allWeeks.forEach {
                it.isSelect = false
            }
            fixLengthAdapter(allWeeks)
        }
        binding.tvClosingAuctionCustomDataOption2.setOnClickListener {
            fm = supportFragmentManager
            val dateDialog = DatePickerFragment(false, true) { selectdate_ ->
                binding.tvClosingAuctionCustomDataOption2.text = ""
                selectdate = selectdate_
                val timeDialog = TimePickerFragment { selectTime_ ->
                    selectTime = selectTime_
                    binding.btnRadioClosingAuctionOption2.isChecked = true
                    binding.tvClosingAuctionCustomDataOption2.text = selectdate + " " + selectTime
                    println("hhhh " + HelpFunctions.getAuctionClosingTime("$selectdate_ $selectTime$"))

                }
                timeDialog.show(fm!!, "")
            }
            dateDialog.show(fm!!, "")
        }
        binding.toolbarListduration.backBtn.setOnClickListener {
            onBackPressed()
        }

        binding.rbMustPickup.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pickUpOption = ConstantObjects.pickUp_Must
                setPickUpMust()
            } else {
                pickUpOption = 0
                binding.switchMustPickUp.setBackgroundResource(R.drawable.edittext_bg)
                binding.tvMustPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
            }
            binding.containerPickUpOption.hide()
        }
        binding.rbNoPickup.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pickUpOption = ConstantObjects.pickUp_No
                setPickUpNo()
                binding.containerPickUpOption.show()
            } else {
                pickUpOption = 0
                binding.switchNoPickUp.setBackgroundResource(R.drawable.edittext_bg)
                binding.tvNoPickUp.setTextColor(ContextCompat.getColor(this, R.color.text_color))
                binding.containerPickUpOption.hide()
            }
        }
        binding.rbAvailablePickup.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pickUpOption = ConstantObjects.pickUp_Available
                setPickUpAvailable()
                binding.containerPickUpOption.show()
            } else {
                pickUpOption = 0
                binding.switchAvailablePickUp.setBackgroundResource(R.drawable.edittext_bg)
                binding.tvAvailablePickUp.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.text_color
                    )
                )
                binding.containerPickUpOption.hide()
            }
        }
        binding.btnListduration.setOnClickListener {
            confirmListDuration2()
        }
    }

    @SuppressLint("ResourceType")
    private fun fixLengthAdapter(list: ArrayList<TimeAuctionSelection>) {
        println("hhh " + Gson().toJson(list))

        // Set up adapter with ViewBinding for selection_item layout
        binding.rvClosingTimeListOption1.adapter =
            object : GenericListAdapter<TimeAuctionSelection>(
                R.layout.selection_item,
                bind = { element, holder, itemCount, position ->

                    // Use ViewBinding to bind the view for the current item
                    val bindingItem = SelectionItemBinding.bind(holder.view)

                    // Bind the data using ViewBinding
                    bindingItem.run {
                        element.run {
                            // Determine unit type
                            val unit: String = when (unitType) {
                                ConstantObjects.auctionClosingPeriodsUnit_day -> {
                                    holder.view.context.getString(R.string.day)
                                }

                                ConstantObjects.auctionClosingPeriodsUnit_month -> {
                                    holder.view.context.getString(R.string.month)
                                }

                                else -> {
                                    holder.view.context.getString(R.string.week)
                                }
                            }

                            // Set the selection text and its state
                            selectionTv.text = "$text $unit"
                            selectionTv.isSelected = isSelect
                            fixlenghtselected = list.find { it.isSelect }

                            // Handle item click to select the option
                            root.setOnClickListener {
                                // Update the selected state in the list
                                list.forEachIndexed { index, item ->
                                    item.isSelect = index == position
                                }

                                // Notify adapter to refresh the list
                                binding.rvClosingTimeListOption1.post {
                                    binding.rvClosingTimeListOption1.adapter?.notifyDataSetChanged()
                                }

                                // Set the selected item and adjust the end time
                                fixlenghtselected = element
                                val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                                val currentTime = dateFormat.format(Date())
                                fixlenghtselected?.endTime =
                                    "${fixlenghtselected?.endTime} $currentTime"

                                // Check the button related to this selection
                                binding.btnRadioClosingAuctionOption1.isChecked = true
                            }
                        }
                    }
                }
            ) {
                override fun getFilter(): Filter {
                    // Implement the filter logic here if needed
                    TODO("Not yet implemented")
                }
            }.apply {
                submitList(list)
            }
    }

    fun confirmListDuration2() {
        if (binding.rbMustPickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_Must
        } else if (binding.rbNoPickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_No
        } else if (binding.rbAvailablePickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_Available
        }

        if (AddProductObjectData.auctionOption && !validateListDuration()) {
            return
        } else if (!validatePickUpOption()) {
            return
        } else {
            if (shippingAdapter?.isNoItemSelected() == true) {
                showError(getString(R.string.please_select_a_shipping_option))
            } else {
                val isRequiredShippingOptionSelected =
                    shippingAdapter?.isRequiredShippingOptionSelected() ?: false

                if (isRequiredShippingOptionSelected && AddProductObjectData.pickUpOption == 0) {
                    showError(getString(R.string.please_select_pickup_option))
                    return
                } else {
                    goNextActivity()
                }

            }
        }
    }

    private fun validatePickUpOption(): Boolean {
        // If no pickup option is selected, allow the user to proceed without showing an error.
        if (pickUpOption == 0) {
            return true
        }

        // If `pickUpOption` requires selection of shipping options, check if any are selected.
        if (pickUpOption == ConstantObjects.pickUp_No || pickUpOption == ConstantObjects.pickUp_Available) {
            val selectedOptions = pickUpOptionList.filter { it.selected }

            // If there are no selected options, but it's optional, allow the user to proceed.
            if (selectedOptions.isEmpty()) {
                return true
            } else {
                // If options are selected, store them in `shippingOptionSelections`.
                AddProductObjectData.shippingOptionSelections?.clear()
                AddProductObjectData.shippingOptionSelections?.add(pickUpOption)
                AddProductObjectData.shippingOptionSelections?.add(selectedOptions[0].id)
                return true
            }
        }

        // Default to true if no errors are encountered and all conditions are met.
        return true
    }

    private fun validateListDuration(): Boolean {
        return if (binding.btnRadioClosingAuctionOption1.isChecked) {
            if (fixlenghtselected == null) {
                showError(getString(R.string.close_time))
                return false
            } else {
                AddProductObjectData.selectTimeAuction = fixlenghtselected
                return true
            }
        } else if (binding.btnRadioClosingAuctionOption2.isChecked) {
            if (binding.tvClosingAuctionCustomDataOption2.text.toString().isEmpty()) {
                showError(getString(R.string.close_time))
                return false
            } else {
                AddProductObjectData.selectTimeAuction = TimeAuctionSelection(
                    binding.tvClosingAuctionCustomDataOption2.text.toString(),
                    binding.tvClosingAuctionCustomDataOption2.text.toString(),
                    HelpFunctions.getAuctionClosingTime(binding.tvClosingAuctionCustomDataOption2.text.toString()),
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

                // Use ViewBinding to bind the view for the current item
                val binding = ShippingOptionBinding.bind(holder.view)

                // Bind the data using ViewBinding
                binding.run {
                    element.run {
                        shippingOptTv.text = list[position].shippingOptionName

                        // Set the selected state
                        if (selected) {
                            rvSelected.setImageResource(R.drawable.ic_radio_button_checked)
                        } else {
                            rvSelected.setImageResource(R.drawable.ic_radio_button_unchecked)
                        }

                        // Handle the selection click
                        shippingOptionLayout.setOnClickListener {
                            list.forEach { item -> item.selected = false }
                            list[position].selected = true

                            AddProductObjectData.pickUpOption = list[position].id

                            rcv.post { rcv.adapter?.notifyDataSetChanged() }
                        }

                        // Mirror the click for the selected image
                        rvSelected.setOnClickListener {
                            shippingOptionLayout.performClick()
                        }
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }
        }.apply {
            submitList(list)
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
                binding.containerPickUpOption.show()
                pickUpOptionAdapter(pickUpOptionList, binding.rvShippingOption)
            } else {
                pickUpOption = shippingItem.id
                binding.containerPickUpOption.hide()
            }

        } else {
            binding.containerPickUpOption.hide()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        shippingViewModel.closeAllCall()
    }
}
