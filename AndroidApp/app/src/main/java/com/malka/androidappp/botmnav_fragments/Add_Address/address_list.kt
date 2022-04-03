package com.malka.androidappp.botmnav_fragments.Add_Address

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.add_address_design.view.*
import kotlinx.android.synthetic.main.address_list_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*

class address_list : Fragment(R.layout.address_list_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()

    }

    private fun initView() {

        toolbar_title.text = getString(R.string.save_addresses)
        getAddress()
    }

    private fun setListenser() {
        add_new_add.setOnClickListener {
            findNavController().navigate(R.id.addAddress)
        }


        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }


    fun getAddress() {

        HelpFunctions.startProgressBar(requireActivity())

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getAddress(ConstantObjects.logged_userid)


        call?.enqueue(object : retrofit2.Callback<GetAddressResponse?> {
            override fun onFailure(call: retrofit2.Call<GetAddressResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: retrofit2.Call<GetAddressResponse?>,
                response: retrofit2.Response<GetAddressResponse?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: GetAddressResponse = response.body()!!
                        if (respone.status_code == 200) {

                            AdressAdaptor(respone.data)

                        } else {

                            HelpFunctions.ShowLongToast(
                                getString(R.string.ErrorOccur),
                                requireContext()
                            )
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })


    }


    private fun AdressAdaptor(list: List<GetAddressResponse.AddressModel>) {
        category_rcv.adapter = object : GenericListAdapter<GetAddressResponse.AddressModel>(
            R.layout.add_address_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        city_name.text = city
                        country_name.text = country
                        phonenum.text = mobileNo
                        adress.text = address

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