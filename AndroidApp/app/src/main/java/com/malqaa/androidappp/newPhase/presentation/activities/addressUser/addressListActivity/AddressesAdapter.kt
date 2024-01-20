package com.malqaa.androidappp.newPhase.presentation.activities.addressUser.addressListActivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemAddressAddBinding
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.helper.shared_preferences.SharedPreferencesStaticClass
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import kotlinx.android.synthetic.main.item_address_add.view.*

class AddressesAdapter(
    var userAddressesList: ArrayList<AddressItem>,
    var setOnSelectedAddress: SetOnSelectedAddress,
    var isSelectable: Boolean
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

        if (isSelectable) {
            holder.viewBinding.ivSelectedAddress.show()
            holder.viewBinding.btnDeleteAddress.hide()
        } else {
            holder.viewBinding.ivSelectedAddress.hide()
            holder.viewBinding.btnDeleteAddress.show()
        }
        if (SharedPreferencesStaticClass.getAddressTitle() == userAddressesList[position].title) {
            userAddressesList[position].isSelected = true
        }
        if (userAddressesList[position].isSelected) {
            holder.viewBinding.ivSelectedAddress.setImageResource(R.drawable.ic_radio_button_checked)
        } else {
            holder.viewBinding.ivSelectedAddress.setImageResource(R.drawable.ic_radio_button_unchecked)
        }



        holder.viewBinding.ivSelectedAddress.setOnClickListener {
            setOnSelectedAddress.setOnSelectedAddress(position)
        }
        holder.viewBinding.addressLay.setOnClickListener {
            setOnSelectedAddress.setOnSelectedAddress(position)
        }
        holder.viewBinding.editAddressBtn.setOnClickListener {
            setOnSelectedAddress.setOnSelectedEditAddress(position)
        }
        holder.viewBinding.btnDeleteAddress.setOnClickListener {
            setOnSelectedAddress.onDeleteAddress(position)
        }
    }

    interface SetOnSelectedAddress {
        fun setOnSelectedAddress(position: Int)
        fun setOnSelectedEditAddress(position: Int)
        fun onDeleteAddress(position: Int)
    }
}