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
import com.malqaa.androidappp.databinding.ActivityShipmentRateBinding
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ShipmentRateActivity : BaseActivity<ActivityShipmentRateBinding>(),
    ProductRateAdapter.SetOnClickListeners {

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

        // Initialize view binding
        binding = ActivityShipmentRateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.add_Review)
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
                        binding.ivSellerHappyRate.setImageResource(R.drawable.happyface_color)
                        binding.ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
                        binding.ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    2 -> {
                        sellerRate = 2
                        binding.ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
                        binding.ivSellerNeutralRate.setImageResource(R.drawable.smileface_color)
                        binding.ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    1 -> {
                        sellerRate = 1
                        binding.ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
                        binding.ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
                        binding.ivSellerSadeRate.setImageResource(R.drawable.sadcolor_gray)
                    }
                }
                binding.etSellerCommnet.setText(rateObject.sellerDateDto.comment)
            }
            //shipment
            if (rateObject.shippmentRateDto != null) {
                shippmentRateId = rateObject.shippmentRateDto.shippmentRateId
                when (rateObject.shippmentRateDto.rate) {
                    3 -> {
                        shipmentRate = 3
                        binding.ivShipmentHappyRate.setImageResource(R.drawable.happyface_color)
                        binding.ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
                        binding.ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    2 -> {
                        shipmentRate = 2
                        binding.ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
                        binding.ivShipmentNeutralRate.setImageResource(R.drawable.smileface_color)
                        binding.ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
                    }

                    1 -> {
                        shipmentRate = 1
                        binding.ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
                        binding.ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
                        binding.ivShipmentSadeRate.setImageResource(R.drawable.sadcolor_gray)
                    }
                }
                binding.etShipmentCommnet.setText(rateObject.shippmentRateDto.comment)
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
                    binding.etProductComment.setText(orderProductFullInfoDtoList[0].comment)
                    when (orderProductFullInfoDtoList[0].rate) {
                        3 -> {
                            binding.ivProductHappyRate.setImageResource(R.drawable.happyface_color)
                            binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                            binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
                        }

                        2 -> {
                            binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                            binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_color)
                            binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
                        }

                        1 -> {
                            binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                            binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                            binding.ivProductSadeRate.setImageResource(R.drawable.sadcolor_gray)
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
        binding.rvProduct.apply {
            adapter = productRateAdapter
            layoutManager = linearLayoutManager(RecyclerView.HORIZONTAL)
        }

    }

    private fun setonClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            onBackPressed()
        }

        /**seller rate */
        binding.ivSellerHappyRate.setOnClickListener {
            sellerRate = 3
            binding.ivSellerHappyRate.setImageResource(R.drawable.happyface_color)
            binding.ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
            binding.ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        binding.ivSellerNeutralRate.setOnClickListener {
            sellerRate = 2
            binding.ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
            binding.ivSellerNeutralRate.setImageResource(R.drawable.smileface_color)
            binding.ivSellerSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        binding.ivSellerSadeRate.setOnClickListener {
            sellerRate = 1
            binding.ivSellerHappyRate.setImageResource(R.drawable.happyface_gray)
            binding.ivSellerNeutralRate.setImageResource(R.drawable.smileface_gray)
            binding.ivSellerSadeRate.setImageResource(R.drawable.sadcolor_gray)
        }
        /**producct rate */
        binding.ivProductHappyRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 3
            binding.ivProductHappyRate.setImageResource(R.drawable.happyface_color)
            binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
            binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        binding.ivProductNeutralRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 2
            binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
            binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_color)
            binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        binding.ivProductSadeRate.setOnClickListener {
            orderProductFullInfoDtoList[selectedProductPosition].rate = 1
            binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
            binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
            binding.ivProductSadeRate.setImageResource(R.drawable.sadcolor_gray)
        }


        binding.etProductComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                // println("hhhh  2"+etProductComment.text.toString().trim())
                if (orderProductFullInfoDtoList.isNotEmpty()) {
                    orderProductFullInfoDtoList[selectedProductPosition].comment =
                        binding.etProductComment.text.toString().trim()
                    productRateAdapter.notifyItemChanged(selectedProductPosition)
                }
            }
        })
        /**shipment rate */

        binding.ivShipmentHappyRate.setOnClickListener {
            shipmentRate = 3
            binding.ivShipmentHappyRate.setImageResource(R.drawable.happyface_color)
            binding.ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
            binding.ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        binding.ivShipmentNeutralRate.setOnClickListener {
            shipmentRate = 2
            binding.ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
            binding.ivShipmentNeutralRate.setImageResource(R.drawable.smileface_color)
            binding.ivShipmentSadeRate.setImageResource(R.drawable.sadface_gray)
        }
        binding.ivShipmentSadeRate.setOnClickListener {
            shipmentRate = 1
            binding.ivShipmentHappyRate.setImageResource(R.drawable.happyface_gray)
            binding.ivShipmentNeutralRate.setImageResource(R.drawable.smileface_gray)
            binding.ivShipmentSadeRate.setImageResource(R.drawable.sadcolor_gray)
        }


        /*****/
        binding.btnSend.setOnClickListener {
            checkRateData()
        }
    }

    private fun checkRateData() {
        if (sellerRate == 0) {
            HelpFunctions.ShowLongToast(getString(R.string.selectSellerRate), this)
        } else if (binding.etSellerCommnet.text.toString().trim() == "") {
            binding.etSellerCommnet.error = getString(R.string.addComment)
        } else if (shipmentRate == 0) {
            HelpFunctions.ShowLongToast(getString(R.string.selectShipmentRate), this)
        } else if (binding.etShipmentCommnet.text.toString().trim() == "") {
            binding.etShipmentCommnet.error = getString(R.string.addComment)
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
                            comment = binding.etSellerCommnet.text.toString().trim()
                        ),
                        shippmentRateDto = ShippmentRateDto(
                            shippmentRateId = shippmentRateId,
                            shippmentId = 0,
                            rate = shipmentRate,
                            comment = binding.etShipmentCommnet.text.toString(),
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
        binding.etProductComment.setText(orderProductFullInfoDtoList[selectedProductPosition].comment)
        when (orderProductFullInfoDtoList[selectedProductPosition].rate) {

            3 -> {
                binding.ivProductHappyRate.setImageResource(R.drawable.happyface_color)
                binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
            }

            2 -> {
                binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_color)
                binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)
            }

            1 -> {
                binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                binding.ivProductSadeRate.setImageResource(R.drawable.sadcolor_gray)
            }

            else -> {
                binding.ivProductHappyRate.setImageResource(R.drawable.happyface_gray)
                binding.ivProductNeutralRate.setImageResource(R.drawable.smileface_gray)
                binding.ivProductSadeRate.setImageResource(R.drawable.sadface_gray)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        shipmentRateViewModel.closeAllCall()
    }

    private fun handelProductsRate(orderProductFullInfoDtoList: ArrayList<OrderProductFullInfoDto>) {
        if (orderProductFullInfoDtoList.size > 0) {
            binding.textProductsRateTitle.visibility = View.VISIBLE
            binding.rvProduct.visibility = View.VISIBLE
            binding.lLinearProductRateIcon.visibility = View.VISIBLE
            binding.textProductsRateCommentTitle.visibility = View.VISIBLE
            binding.linearProductRateComment.visibility = View.VISIBLE
        } else {
            binding.textProductsRateTitle.visibility = View.GONE
            binding.rvProduct.visibility = View.GONE
            binding.lLinearProductRateIcon.visibility = View.GONE
            binding.textProductsRateCommentTitle.visibility = View.GONE
            binding.linearProductRateComment.visibility = View.GONE
        }
    }
}