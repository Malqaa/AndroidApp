package com.malqaa.androidappp.newPhase.presentation.activities.shipmentRateActivity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.RateObject
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.RateProductObject
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.SellerDateDto
import com.malqaa.androidappp.newPhase.domain.models.orderRateResp.ShippmentRateDto
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.SuccessProductActivity
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import kotlinx.android.synthetic.main.activity_shipment_rate.btnSend
import kotlinx.android.synthetic.main.activity_shipment_rate.etProductComment
import kotlinx.android.synthetic.main.activity_shipment_rate.etSellerCommnet
import kotlinx.android.synthetic.main.activity_shipment_rate.etShipmentCommnet
import kotlinx.android.synthetic.main.activity_shipment_rate.ivProductHappyRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivProductNeutralRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivProductSadeRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivSellerHappyRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivSellerNeutralRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivSellerSadeRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivShipmentHappyRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivShipmentNeutralRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivShipmentSadeRate
import kotlinx.android.synthetic.main.activity_shipment_rate.lLinearProductRateIcon
import kotlinx.android.synthetic.main.activity_shipment_rate.linearProductRateComment
import kotlinx.android.synthetic.main.activity_shipment_rate.rvProduct
import kotlinx.android.synthetic.main.activity_shipment_rate.textProductsRateCommentTitle
import kotlinx.android.synthetic.main.activity_shipment_rate.textProductsRateTitle
import kotlinx.android.synthetic.main.activity_shipment_rate.view.lLinearProductRateIcon
import kotlinx.android.synthetic.main.toolbar_main.back_btn
import kotlinx.android.synthetic.main.toolbar_main.toolbar_title
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShipmentRateActivity : BaseActivity(), ProductRateAdapter.SetOnClickListeners {

    lateinit var productRateAdapter: ProductRateAdapter
    lateinit var orderProductFullInfoDtoList: ArrayList<OrderProductFullInfoDto>
    private var orderFullInfoDto: OrderFullInfoDto? = null
    private var selectedProductPosition: Int = 0
    private var sellerRate = 0
    private var shipmentRate = 0
    private var orderId = 0
    private var sellerRateId = 0
    private var shippmentRateId = 0
    private lateinit var rateObject: RateObject
    private lateinit var shipmentRateViewModel: ShipmentRateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment_rate)
        toolbar_title.text = getString(R.string.add_Review)
        orderFullInfoDto = intent.getParcelableExtra(ConstantObjects.shipmentOrderDataKey)
        orderId = intent.getIntExtra(ConstantObjects.orderMasterIdKey, 0)
        setonClickListeners()
        setProductRateAdapter()
        setData()
        setupViewModel()
        shipmentRateViewModel.getShipmentRate(orderId)
        handelProductsRate(orderProductFullInfoDtoList)
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
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message!!, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        shipmentRateViewModel.addShipmentRateObserver.observe(this) {
            HelpFunctions.ShowLongToast(it.message, this)
            if (it.status_code == 200) {

                val intent = Intent(
                    this@ShipmentRateActivity, SuccessProductActivity::class.java
                ).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("comeFrom", "RatingShipping")

                }

                startActivity(intent)
                finish()
            }
        }
        shipmentRateViewModel.getShipmentRate.observe(this) {
            if (it.status_code == 200) {
                setRateData(it.rateObject)
            }
        }
    }

    private fun setRateData(rateObject: RateObject?) {
        rateObject?.let {
            if (rateObject.sellerDateDto != null) {
                //setSellerInfo
                sellerRateId = rateObject.sellerDateDto.sellerRateId
                when (rateObject.sellerDateDto.rate) {
                    3 -> {
                        sellerRate = 3
                        ivSellerHappyRate.setImageResource(R.drawable.happyface_color)
                        ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
                        ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    2 -> {
                        sellerRate = 2
                        ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
                        ivSellerNeutralRate.setImageResource(R.drawable.smileface_color)
                        ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    1 -> {
                        sellerRate = 1
                        ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
                        ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
                        ivSellerSadeRate.setImageResource(R.drawable.sadcolor_gray)
                    }
                }
                etSellerCommnet.setText(rateObject.sellerDateDto.comment)
            }
            //shipment
            if (rateObject.shippmentRateDto != null) {
                shippmentRateId = rateObject.shippmentRateDto.shippmentRateId
                when (rateObject.shippmentRateDto.rate) {
                    3 -> {
                        shipmentRate = 3
                        ivShipmentHappyRate.setImageResource(R.drawable.happyface_color)
                        ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
                        ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    2 -> {
                        shipmentRate = 2
                        ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
                        ivShipmentNeutralRate.setImageResource(R.drawable.smileface_color)
                        ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    1 -> {
                        shipmentRate = 1
                        ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
                        ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
                        ivShipmentSadeRate.setImageResource(R.drawable.sadcolor_gray)
                    }
                }
                etShipmentCommnet.setText(rateObject.shippmentRateDto.comment)
            }
            rateObject.productsRate.let { productsRateList ->
                for (item in orderProductFullInfoDtoList) {
                    for (product in productsRateList) {
                        if (item.productId == product.productId) {
                            item.productRateId = product.productRateId
                            item.comment = product.comment
                            item.rate = product.rate
                        }
                    }
                }
                productRateAdapter.notifyDataSetChanged()
                if (orderProductFullInfoDtoList.isNotEmpty()) {
                    etProductComment.setText(orderProductFullInfoDtoList[0].comment)
                    when (orderProductFullInfoDtoList[0].rate) {
                        3 -> {
                            ivProductHappyRate.setImageResource(R.drawable.happyface_color)
                            ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                            ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
                        }

                        2 -> {
                            ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                            ivProductNeutralRate.setImageResource(R.drawable.smileface_color)
                            ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
                        }

                        1 -> {
                            ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                            ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                            ivProductSadeRate.setImageResource(R.drawable.sadcolor_gray)
                        }
                    }
                }
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
            sellerRate = 3
            ivSellerHappyRate.setImageResource(R.drawable.happyface_color)
            ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivSellerNeutralRate.setOnClickListener {
            sellerRate = 2
            ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
            ivSellerNeutralRate.setImageResource(R.drawable.smileface_color)
            ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivSellerSadeRate.setOnClickListener {
            sellerRate = 1
            ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
            ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivSellerSadeRate.setImageResource(R.drawable.sadcolor_gray)
        }
        /**producct rate */
        ivProductHappyRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 3
            ivProductHappyRate.setImageResource(R.drawable.happyface_color)
            ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivProductNeutralRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 2
            ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
            ivProductNeutralRate.setImageResource(R.drawable.smileface_color)
            ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivProductSadeRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 1
            ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
            ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivProductSadeRate.setImageResource(R.drawable.sadcolor_gray)
        }


        etProductComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // println("hhhh  2"+etProductComment.text.toString().trim())
                if (orderProductFullInfoDtoList.isNotEmpty()) {
                    orderProductFullInfoDtoList[selectedProductPosition].comment =
                        etProductComment.text.toString().trim()
                    productRateAdapter.notifyItemChanged(selectedProductPosition)
                }
            }
        })
        /**shipment rate */

        ivShipmentHappyRate.setOnClickListener {
            shipmentRate = 3
            ivShipmentHappyRate.setImageResource(R.drawable.happyface_color)
            ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivShipmentNeutralRate.setOnClickListener {
            shipmentRate = 2
            ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
            ivShipmentNeutralRate.setImageResource(R.drawable.smileface_color)
            ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        ivShipmentSadeRate.setOnClickListener {
            shipmentRate = 1
            ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
            ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
            ivShipmentSadeRate.setImageResource(R.drawable.sadcolor_gray)
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
            val productRateList: ArrayList<RateProductObject> = ArrayList()
            var readToAdd = true
            for (item in orderProductFullInfoDtoList) {
                if (item.rate == 0 || item.comment == "" || item.comment == null) {
                    readToAdd = false
                } else {
                    productRateList.add(
                        RateProductObject(
                            item.productRateId,
                            item.productId,
                            item.rate,
                            item.comment ?: ""
                        )
                    )
                }
            }
            withContext(Dispatchers.Main) {
                if (readToAdd) {
                    rateObject = RateObject(
                        orderId = orderId,
                        productsRate = productRateList,
                        sellerDateDto = SellerDateDto(
                            sellerRateId = sellerRateId,
                            sellerId = orderFullInfoDto?.providerId ?: "",
                            businessAccountId = orderFullInfoDto?.businessAcountId,
                            rate = sellerRate,
                            comment = etSellerCommnet.text.toString().trim()
                        ),
                        shippmentRateDto = ShippmentRateDto(
                            shippmentRateId = shippmentRateId,
                            shippmentId = 0,
                            rate = shipmentRate,
                            comment = etShipmentCommnet.text.toString(),
                        )
                    )
                    println("hhhh " + Gson().toJson(rateObject))
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

            3 -> {
                ivProductHappyRate.setImageResource(R.drawable.happyface_color)
                ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
            }

            2 -> {
                ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                ivProductNeutralRate.setImageResource(R.drawable.smileface_color)
                ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
            }

            1 -> {
                ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                ivProductSadeRate.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                ivProductSadeRate.setImageResource(R.drawable.sadface_gray)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shipmentRateViewModel.closeAllCall()
    }

    private fun handelProductsRate(orderProductFullInfoDtoList: ArrayList<OrderProductFullInfoDto>) {
        if (orderProductFullInfoDtoList.size > 0) {
            textProductsRateTitle.visibility = View.VISIBLE
            rvProduct.visibility = View.VISIBLE
            lLinearProductRateIcon.visibility = View.VISIBLE
            textProductsRateCommentTitle.visibility = View.VISIBLE
            linearProductRateComment.visibility = View.VISIBLE
        } else {
            textProductsRateTitle.visibility = View.GONE
            rvProduct.visibility = View.GONE
            lLinearProductRateIcon.visibility = View.GONE
            textProductsRateCommentTitle.visibility = View.GONE
            linearProductRateComment.visibility = View.GONE
        }
    }
}