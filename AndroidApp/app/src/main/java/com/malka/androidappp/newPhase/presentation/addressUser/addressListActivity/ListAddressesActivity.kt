package com.malka.androidappp.newPhase.presentation.addressUser.addressListActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.domain.models.userAddressesResp.AddressItem
import com.malka.androidappp.newPhase.presentation.addressUser.AddressViewModel
import com.malka.androidappp.newPhase.presentation.addressUser.addAddressActivity.AddAddressActivity

import kotlinx.android.synthetic.main.address_list_activity.*
import kotlinx.android.synthetic.main.address_list_activity.swipe_to_refresh
import kotlinx.android.synthetic.main.toolbar_main.*

@SuppressLint("NotifyDataSetChanged")
class ListAddressesActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    AddressesAdapter.SetOnSelectedAddress {
    private lateinit var addressViewModel: AddressViewModel
    private lateinit var userAddressesList: ArrayList<AddressItem>
    private lateinit var addressesAdapter: AddressesAdapter
    private var lastSelectedPosition = 0

    private val addAddressLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                userAddressesList.clear()
                addressesAdapter.notifyDataSetChanged()
                addressViewModel.getUserAddress()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.address_list_activity)
        toolbar_title.text = getString(R.string.addresses)
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setAddressesAdapter()
        setClickListeners()
        setUpViewModel()
        onRefresh()
    }

    private fun setClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        containerAddNewAddress.setOnClickListener {
            addAddressLauncher.launch(Intent(this, AddAddressActivity::class.java))
        }

    }

    private fun setAddressesAdapter() {
        userAddressesList = ArrayList()
        addressesAdapter = AddressesAdapter(userAddressesList, this, false)
        rvAdddresses.apply {
            adapter = addressesAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }

    }

    private fun setUpViewModel() {

        addressViewModel = ViewModelProvider(this).get(AddressViewModel::class.java)

        addressViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        addressViewModel.isLoadingDeleteAddress.observe(this) {
            if (it) {
                HelpFunctions.startProgressBar(this)
            } else {
                HelpFunctions.dismissProgressBar()
            }
        }
        addressViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        addressViewModel.errorResponseObserver.observe(this) {
            if(it.status!=null && it.status=="409"){
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            }else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        addressViewModel.userAddressesListObserver.observe(this) { userAddressResp ->
            if (userAddressResp.status_code == 200) {
                if (userAddressResp.addressesList != null && userAddressResp.addressesList?.isNotEmpty() == true) {
                    tvError.hide()
                    userAddressesList.clear()
                    userAddressesList.addAll(userAddressResp.addressesList!!)
                    addressesAdapter.notifyDataSetChanged()
                } else {
                    tvError.show()
                }
            }
        }
        addressViewModel.deleteUserAddressesObserver.observe(this) { resp ->
            if (resp.status_code == 200) {
                if (lastSelectedPosition < userAddressesList.size) {
                    userAddressesList.removeAt(lastSelectedPosition)
                    addressesAdapter.notifyDataSetChanged()
                    if(userAddressesList.isEmpty()){
                        tvError.show()
                    }
                }
            } else {
                if (resp.message != null) {
                    HelpFunctions.ShowLongToast(resp.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }

        }
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        userAddressesList.clear()
        addressesAdapter.notifyDataSetChanged()
        addressViewModel.getUserAddress()
    }

    override fun setOnSelectedAddress(position: Int) {
        //====not used here
    }

    override fun setOnSelectedEditAddress(position: Int) {
        addAddressLauncher.launch(Intent(this, AddAddressActivity::class.java).apply {
            putExtra(ConstantObjects.isEditKey, true)
            putExtra(ConstantObjects.addressKey, userAddressesList[position])
        })
    }

    override fun onDeleteAddress(position: Int) {
        lastSelectedPosition = position
        addressViewModel.deleteUSerAddress(userAddressesList[position].id)
    }
    override fun onDestroy() {
        super.onDestroy()
        addressViewModel.closeAllCall()
    }
}