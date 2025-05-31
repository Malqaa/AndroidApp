package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
    val pickUpOneOptionList: ArrayList<ShippingOptionObject> = ArrayList()
    val shippingOptionList: ArrayList<ShippingOptionObject> = ArrayList()
    var shippingAdapter: ShippingAdapter? = null
    var pickUpOption: Int = 0
    private lateinit var shippingViewModel: ShippingViewModel
    var cashed = 0
    var hasAdded = false
    var closed = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityListingDurationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shippingViewModel = ViewModelProvider(this).get(ShippingViewModel::class.java)

        binding.toolbarListduration.toolbarTitle.text = getString(R.string.shipping)
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)
        cashed = intent.getIntExtra("cashed", 0)
        closed = intent.getBooleanExtra("closed", false)

        setVieClickListeners()
        shippingAdapter = ShippingAdapter(arrayListOf(), this)
        binding.containerPickUpOption.hide()

        shippingViewModel.getAllShippingOptions()

        /**shipping data */
        ConstantObjects.shippingOption_integratedShippingCompanyOptions

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



    fun confirmListDuration2() {
        if (binding.rbMustPickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_Must
        } else if (binding.rbNoPickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_No
        } else if (binding.rbAvailablePickup.isChecked) {
            pickUpOption = ConstantObjects.pickUp_Available
        }

        if (!validatePickUpOption()) {
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

    private fun pickOneUpOptionAdapter(list: ArrayList<ShippingOptionObject>, rcv: RecyclerView) {
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

                            AddProductObjectData.pickUpOption = list[0].id

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
                putExtra("closed", closed)
                finish()
            })

        }
    }

    override fun setOnSelectedShipping(shippingItem: ShippingOptionObject) {
        AddProductObjectData.shippingOptionSelections = arrayListOf()
        if (shippingItem.selected) {
            AddProductObjectData.shippingOptionSelections?.add(shippingItem.id)
            AddProductObjectData.shippingOption = shippingItem.id
            if (shippingItem.id == 3||(shippingItem.id == 2&&cashed==0)) {
                pickUpOption = shippingItem.id
                binding.containerPickUpOption.show()
                pickUpOptionAdapter(pickUpOptionList, binding.rvShippingOption)
            }
             else if (shippingItem.id == 2&&cashed==1) {
                pickUpOption = shippingItem.id
                binding.containerPickUpOption.show()
                if (!hasAdded) {
                    pickUpOneOptionList.add(pickUpOptionList[2])
                    hasAdded = true
                }
                pickOneUpOptionAdapter(pickUpOneOptionList, binding.rvShippingOption)
            }else {
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
