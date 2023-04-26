package com.malka.androidappp.newPhase.presentation.shipmentRateActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderFullInfoDto
import com.malka.androidappp.newPhase.domain.models.orderDetailsByMasterID.OrderProductFullInfoDto
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.activity_shipment_rate.*
import kotlinx.android.synthetic.main.toolbar_main.*

class ShipmentRateActivity : BaseActivity(), ProductRateAdapter.SetOnClickListeners {

    lateinit var productRateAdapter: ProductRateAdapter
    lateinit var orderProductFullInfoDtoList: ArrayList<OrderProductFullInfoDto>
    var orderFullInfoDto: OrderFullInfoDto? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shipment_rate)
        toolbar_title.text = getString(R.string.add_Review)
        orderFullInfoDto = intent.getParcelableExtra(ConstantObjects.shipmentOrderDataKey)
        setonClickListeners()
        setProductRateAdapter()
        setData()

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
    }

    override fun onProductRateSelected(position: Int) {

    }
}