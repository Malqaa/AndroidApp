package com.malqaa.androidappp.newPhase.utils.helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.BankAccountsBinding
import com.malqaa.androidappp.databinding.BankBottomDesignBinding
import com.malqaa.androidappp.databinding.DeliveryOptionBinding
import com.malqaa.androidappp.databinding.DeliveryOptionLayoutBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Negotiationmodel
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter

class CommonBottomSheet {

    fun bankaAccountBottomSheet(context: Context, list: List<Negotiationmodel>) {
        // Inflate the layout using View Binding
        val binding = BankAccountsBinding.inflate((context as Activity).layoutInflater)

        BottomSheetDialog(context).apply {
            setContentView(binding.root) // Use the root of the binding

            // Set up the user bank adapter
            userBankAdapter(
                binding.bankRcv,
                list,
                false
            ) // Assuming userBankAdapter is a function that sets the adapter

            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            show()
        }
    }

    private fun userBankAdapter(
        rcv: RecyclerView,
        banklist: List<Negotiationmodel>,
        isSelect: Boolean
    ) {
        rcv.adapter = object : GenericListAdapter<Negotiationmodel>(
            R.layout.bank_bottom_design,
            bind = { element, holder, itemCount, position ->
                // Inflate the binding for each item
                val binding =
                    BankBottomDesignBinding.bind(holder.view) // Assuming holder.view is the inflated view

                // Set the bank name using the binding instance
                binding.bankName.text =
                    element.proname // Make sure `bankName` matches the ID in your layout
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }
        }.apply {
            submitList(banklist)
        }
    }

    var selection: Selection? = null

    fun commonSelctinDialog(
        context: Context,
        list: ArrayList<Selection>,
        title: String,
        onConfirm: (type: Selection) -> Unit
    ) {
        selection = null
        val filteredList = list.filter { it.isSelected }
        if (filteredList.isNotEmpty()) {
            selection = filteredList[0]
        }

        // Use View Binding to inflate the layout
        val binding = DeliveryOptionBinding.inflate((context as Activity).layoutInflater)

        // Create the dialog
        val builder = AlertDialog.Builder(context)
            .setView(binding.root)
            .create().apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setCanceledOnTouchOutside(false)

                binding.apply {
                    tvTitleAr.text = title

                    closeDeliveryAlert.setOnClickListener {
                        dismiss()
                    }

                    btnConfirmDelivery.setOnClickListener {
                        if (selection == null) {
                            (context as BaseActivity<*>).showError(context.getString(R.string.Selectanyoneoption))
                        } else {
                            onConfirm.invoke(selection!!)
                            dismiss()
                        }
                    }

                    setDeliveryOptionAdapter(list, deliveryOptionRcv)
                }
            }

        builder.show()
    }

    fun setDeliveryOptionAdapter(list: ArrayList<Selection>, rcv: RecyclerView) {
        rcv.adapter = object : GenericListAdapter<Selection>(
            R.layout.delivery_option_layout,
            bind = { element, holder, itemCount, position ->
                // Inflate the binding
                val binding = DeliveryOptionLayoutBinding.bind(holder.view)

                binding.run {
                    element.run {
                        // Use View Binding to access views
                        deliveryLayout.isSelected = isSelected
                        deliveryOptionTv.isSelected = isSelected
                        deliveryOptionRb.isChecked = isSelected
                        deliveryOptionTv.text = name

                        deliveryLayout.setOnClickListener {
                            list.forEach { it.isSelected = false } // Deselect all
                            isSelected = true // Select current element
                            selection = element // Update selection
                            rcv.post { rcv.adapter?.notifyDataSetChanged() } // Notify changes
                        }

                        deliveryOptionRb.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                deliveryLayout.performClick()
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
            submitList(list)
        }
    }
}