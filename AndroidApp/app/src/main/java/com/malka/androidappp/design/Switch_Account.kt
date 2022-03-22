package com.malka.androidappp.design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.activity_add_address.*
import kotlinx.android.synthetic.main.add_address_design.view.*
import kotlinx.android.synthetic.main.search_dialog_layout.*

class Switch_Account : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_account)

    }




    private fun switchAccountAdapter(list: List<GetAddressResponse.AddressModel>) {
        category_rcv.adapter = object : GenericListAdapter<GetAddressResponse.AddressModel>(
            R.layout.switch_account_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {


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