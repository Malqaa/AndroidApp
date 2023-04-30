package com.malka.androidappp.newPhase.presentation.shipmentRateActivity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.myProducts.viewModel.MyProductViewModel
import kotlinx.android.synthetic.main.activity_shipment_rate.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShipmentRateActivity : BaseActivity(), ProductRateAdapter.SetOnClickListeners {

    lateinit var productRateAdapter: ProductRateAdapter
    lateinit var orderProductFullInfoDtoList: ArrayList<OrderProductFullInfoDto>
    var orderFullInfoDto: OrderFullInfoDto? = null
    var selectedProductPosition: Int = 0
    var sellerRate = 0
    var shipmentRate = 0
    var orderMasterId = 0

    private lateinit var shipmentRateViewModel: ShipmentRateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment_rate)
        toolbar_title.text = getString(R.string.add_Review)
        orderFullInfoDto = intent.getParcelableExtra(ConstantObjects.shipmentOrderDataKey)
        orderMasterId = intent.getIntExtra(ConstantObjects.orderMasterIdKey, 0)
        setonClickListeners()
        setProductRateAdapter()
        setData()
        setupViewModel()

    }

    private fun setupViewModel() {
        shipmentRateViewModel = ViewModelProvider(this).get(ShipmentRateViewModel::class.java)
        shipmentRateViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        shipmentRateViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }

        }
        shipmentRateViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, this)
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
            }
        }
        shipmentRateViewModel.addShipmentRateObserver.observe(this) {
            HelpFunctions.ShowLongToast(it.message.toString(), this)
            if (it.status_code == 200) {
                onBackPressed()
            }
        }
    }

    private fun setData() {
        orderProductFullInfoDtoList.clear()
        orderFullInfoDto?.orderProductFullInfoDto?.let { orderProductFullInfoDtoList.addAll(it) }
        productRateAdapter.notifyDataSetChanged()
    }

    private fun setProductRateAdapter() {
        orderProductFullInfoDtoList = ArrayList()
        productRateAdapter = ProductRateAdapter(orderProductFullInfoDtoList, this)
        rvProduct.apply {
            adapter = productRateAdapter
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
        }

    }

    private fun setonClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }

        /**seller rate */
        ivSellerHappyRate.setOnClickListener {
            sellerRate = 1
            ivSellerHappyRate.setBackgroundResource(R.color.gray)
            ivSellerNeutralRate.setBackgroundResource(R.color.white)
            ivSellerSadeRate.setBackgroundResource(R.color.white)

        }
        ivSellerNeutralRate.setOnClickListener {
            sellerRate = 2
            ivSellerHappyRate.setBackgroundResource(R.color.white)
            ivSellerNeutralRate.setBackgroundResource(R.color.gray)
            ivSellerSadeRate.setBackgroundResource(R.color.white)
        }
        ivSellerSadeRate.setOnClickListener {
            sellerRate = 3
            ivSellerHappyRate.setBackgroundResource(R.color.white)
            ivSellerNeutralRate.setBackgroundResource(R.color.white)
            ivSellerSadeRate.setBackgroundResource(R.color.gray)
        }
        /**producct rate */
        ivProductHappyRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 1
            ivProductHappyRate.setBackgroundResource(R.color.gray)
            ivProductNeutralRate.setBackgroundResource(R.color.white)
            ivProductSadeRate.setBackgroundResource(R.color.white)
            productRateAdapter.notifyItemChanged(selectedProductPosition)
        }
        ivProductNeutralRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 2
            ivProductHappyRate.setBackgroundResource(R.color.white)
            ivProductNeutralRate.setBackgroundResource(R.color.gray)
            ivProductSadeRate.setBackgroundResource(R.color.white)
            productRateAdapter.notifyItemChanged(selectedProductPosition)
        }
        ivProductSadeRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 3
            ivProductHappyRate.setBackgroundResource(R.color.white)
            ivProductNeutralRate.setBackgroundResource(R.color.white)
            ivProductSadeRate.setBackgroundResource(R.color.gray)
            productRateAdapter.notifyItemChanged(selectedProductPosition)
        }
        etProductComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                orderProductFullInfoDtoList[selectedProductPosition].comment =
                    etProductComment.text.toString().trim()
                productRateAdapter.notifyItemChanged(selectedProductPosition)
            }
        })
        /**shipment rate */
        ivShipmentHappyRate.setOnClickListener {
            shipmentRate = 1
            ivShipmentHappyRate.setBackgroundResource(R.color.gray)
            ivShipmentNeutralRate.setBackgroundResource(R.color.white)
            ivShipmentSadeRate.setBackgroundResource(R.color.white)

        }
        ivShipmentNeutralRate.setOnClickListener {
            shipmentRate = 2
            ivShipmentHappyRate.setBackgroundResource(R.color.white)
            ivShipmentNeutralRate.setBackgroundResource(R.color.gray)
            ivShipmentSadeRate.setBackgroundResource(R.color.white)
        }
        ivShipmentSadeRate.setOnClickListener {
            shipmentRate = 3
            ivShipmentHappyRate.setBackgroundResource(R.color.white)
            ivShipmentNeutralRate.setBackgroundResource(R.color.white)
            ivShipmentSadeRate.setBackgroundResource(R.color.gray)
        }
        /*****/
        btnSend.setOnClickListener {
            checkRateData()
        }
    }

    private fun checkRateData() {
        if (sellerRate == 0) {
            HelpFunctions.ShowLongToast(getString(R.string.selectSellerRate), this)
        } else if (etSellerCommnet.text.toString().trim() == "") {
            etSellerCommnet.error = getString(R.string.addComment)
        } else if (shipmentRate == 0) {
            HelpFunctions.ShowLongToast(getString(R.string.selectShipmentRate), this)
        } else if (etShipmentCommnet.text.toString().trim() == "") {
            etShipmentCommnet.error = getString(R.string.addComment)
        } else {
            checkProductRates()
        }
    }

    private fun checkProductRates() {
        lifecycleScope.launch(Dispatchers.IO) {
            var productRateList: ArrayList<RateProductObject> = ArrayList()
            var readToAdd = true
            for (item in orderProductFullInfoDtoList) {
                if (item.rate == 0 || item.comment == "" || item.comment == null) {
                    readToAdd = false
                } else {
                    productRateList.add(
                        RateProductObject(
                            0,
                            item.productId,
                            item.rate,
                            item.comment ?: ""
                        )
                    )
                }
            }
            withContext(Dispatchers.Main) {
                if (readToAdd) {
                    var rateObject = RateObject(
                        orderMasterId = orderMasterId,
                        productsRate = productRateList,
                        sellerDateDto = SellerDateDto(
                            sellerRateId = 0,
                            sellerId = orderFullInfoDto?.providerId ?: "",
                            businessAccountId = orderFullInfoDto?.businessAcountId ?: 0,
                            rate = sellerRate,
                            comment = etSellerCommnet.text.toString().trim()
                        ),
                        shippmentRateDto = ShippmentRateDto(
                            shippmentRateId = 0,
                            shippmentId = 0,
                            rate = shipmentRate,
                            comment = etShipmentCommnet.text.toString().toString(),
                        )
                    )
//                    println("hhhh " + Gson().toJson(rateObject))
                    shipmentRateViewModel.addShipmentRate(rateObject)
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.addAllProductsRates),
                        this@ShipmentRateActivity
                    )
                }
            }
        }
    }

    override fun onProductRateSelected(position: Int) {
        selectedProductPosition = position
        etProductComment.setText(orderProductFullInfoDtoList[selectedProductPosition].comment)
        when (orderProductFullInfoDtoList[selectedProductPosition].rate) {
            1 -> {
                ivProductHappyRate.setBackgroundResource(R.color.gray)
                ivProductNeutralRate.setBackgroundResource(R.color.white)
                ivProductSadeRate.setBackgroundResource(R.color.white)
            }
            2 -> {
                ivProductHappyRate.setBackgroundResource(R.color.white)
                ivProductNeutralRate.setBackgroundResource(R.color.gray)
                ivProductSadeRate.setBackgroundResource(R.color.white)
            }
            3 -> {
                ivProductHappyRate.setBackgroundResource(R.color.white)
                ivProductNeutralRate.setBackgroundResource(R.color.white)
                ivProductSadeRate.setBackgroundResource(R.color.gray)
            }
            else -> {
                ivProductHappyRate.setBackgroundResource(R.color.white)
                ivProductNeutralRate.setBackgroundResource(R.color.white)
                ivProductSadeRate.setBackgroundResource(R.color.white)
            }
        }
    }
}