package com.malka.androidappp.botmnav_fragments.account_fragment.address

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.CommonAPI
import com.malka.androidappp.helper.GenericAdaptor
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.address_list_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*

class AddressList : Fragment(R.layout.address_list_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()
    }

    private fun initView() {

        toolbar_title.text = getString(R.string.save_addresses)

        loadAddress()

    }

    private fun loadAddress() {
        CommonAPI().getAddress(requireContext(), {
            GenericAdaptor().AdressAdaptor(addAddressLaucher, requireContext(), category_rcv, it, ConstantObjects.View, {


            })
        })
    }

    val addAddressLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                loadAddress()
            }
        }

    private fun setListenser() {
        add_new_add.setOnClickListener {
            addAddressLaucher.launch(Intent(requireContext(), AddAddress::class.java))
        }





        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }
}