package com.malka.androidappp.newPhase.presentation.cartActivity.activity2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemAddressAddBinding
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import kotlinx.android.synthetic.main.item_address_add.view.*

class AddressesAdapter(
    var userAddressesList: ArrayList<AddressItem>,
    var setOnSelectedAddress: SetOnSelectedAddress
) : RecyclerView.Adapter<AddressesAdapter.AddressesViewHolder>() {

    lateinit var context: Context

    class AddressesViewHolder(var viewBinding: ItemAddressAddBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressesViewHolder {
        context = parent.context
        return AddressesViewHolder(
            ItemAddressAddBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = userAddressesList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AddressesViewHolder, position: Int) {
        holder.viewBinding.addressName.text =
            context.getString(R.string.delivery_to_my_current_address)
//        holder.viewBinding.countryName.text = "$country - $region -$city"
        holder.viewBinding.countryName.text = "${userAddressesList[position].title} "
        holder.viewBinding.phonenum.text = userAddressesList[position].phone
        holder.viewBinding.addressTv.text =
            "${userAddressesList[position].location} - ${userAddressesList[position].street} - " +
                    "${context.getString(R.string.building)}:${userAddressesList[position].building} - " +
                    "${context.getString(R.string.floor)}:${userAddressesList[position].floor} - " +
                    "${context.getString(R.string.apartment)}:${userAddressesList[position].appartment}"
        if(userAddressesList[position].isSelected){
            holder.viewBinding.ivSelectedAddress.setImageResource(R.drawable.ic_radio_button_checked)
        }else{
            holder.viewBinding.ivSelectedAddress.setImageResource(R.drawable.ic_radio_button_unchecked)
        }
        holder.viewBinding.ivSelectedAddress.setOnClickListener {
            setOnSelectedAddress.setOnSelectedAddress(position)
        }
        holder.viewBinding.editAddressBtn.setOnClickListener {
            setOnSelectedAddress.setOnSelectedEditAddress(position)
        }
    }

    interface SetOnSelectedAddress {
        fun setOnSelectedAddress(position: Int)
        fun setOnSelectedEditAddress(position: Int)
    }
}