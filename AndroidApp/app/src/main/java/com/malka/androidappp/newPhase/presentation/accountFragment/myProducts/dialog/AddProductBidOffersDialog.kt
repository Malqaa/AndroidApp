package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.dialog

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.AdapterView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.productResp.RequestBidOffers
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.adapter.SpinnerExpireHoursAdapter
import kotlinx.android.synthetic.main.dialog_acceot_offer.spinnerValues
import kotlinx.android.synthetic.main.dialog_add_product_bid_offers.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AddProductBidOffersDialog(
    val expireHoursList: ArrayList<Float>,
    context: Context,
    var productId: Int,
    var bidIDsList: List<String>,
    var listener: AddProductBidOffersDialog.SetClickListeners
) : BaseDialog(context) {
    var countriesCallback: Call<GeneralResponse>? = null
    var generalRespone: GeneralResponse? = null
    var expireHour: Float = 0f
    private lateinit var spinnerExpireHoursAdapter: SpinnerExpireHoursAdapter
    override fun getViewId(): Int {
        return R.layout.dialog_add_product_bid_offers
    }

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        setSpinnerExpireHoursAdapter()
        setOnClickListenres()
    }

    private fun setOnClickListenres() {
        close_alert_bid.setOnClickListener {
            dismiss()
        }
        btnSend.setOnClickListener {
            var readytosend = true
            if (etNegotiationPrice.text.toString().trim() == "") {
                readytosend = false
                etNegotiationPrice.error = context.getString(R.string.writeNegotiationPrice2)
            }
            if (etQuentity.text.toString().trim() == "") {
                readytosend = false
                etQuentity.error = context.getString(R.string.quantity)
            }

            if (readytosend) {
                addProductOffer(
                    expireHour,
                    productId,
                    etNegotiationPrice.text.toString().trim().toFloat(),
                    etQuentity.text.toString().trim().toInt()
                )
            }
        }
    }
    private fun setSpinnerExpireHoursAdapter() {
        spinnerExpireHoursAdapter = SpinnerExpireHoursAdapter(context, expireHoursList)
        spinnerValues.adapter = spinnerExpireHoursAdapter
        spinnerValues.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View,
                spinnerPosition: Int,
                l: Long
            ) {
                expireHour = expireHoursList[spinnerPosition]
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }


    fun addProductOffer(
        expire: Float,
        productId: Int,
        price: Float,
        quentity: Int
    ) {
        close_alert_bid.isEnabled = false
        btnSend.isEnabled = false

        val req = RequestBidOffers(expire, price, productId, quentity, bidIDsList)
//        var data: HashMap<String, Any> = HashMap()
//        data["productId"] = productId
//        data["quantity"] = quentity
//        data["price"] = price
//        var list: ArrayList<String> = ArrayList()
//        for (item in bidIDsList) {
//            list.add(item.toString())
//        }
        println("hhhh " + productId + " " + price + " " + quentity + " " + bidIDsList.toString())
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().addProductBidOffers(req)
        countriesCallback?.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnSend.isEnabled = true
                close_alert_bid.isEnabled = true
                if (call.isCanceled) {

                } else if (t is HttpException) {
                    HelpFunctions.ShowLongToast(context.getString(R.string.serverError), context)

                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.connectionError),
                        context
                    )
                }
            }

            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                btnSend.isEnabled = true
                close_alert_bid.isEnabled = true
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            generalRespone = it
                            if (generalRespone?.status_code == 200) {
                                listener.setOnSuccessListeners()
                                dismiss()
                            }
                        }
                    } else {
                        HelpFunctions.ShowLongToast(
                            context.getString(R.string.serverError),
                            context
                        )

                    }
                } catch (e: Exception) {
                }
            }
        })
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (countriesCallback != null) {
            countriesCallback?.cancel()
        }
    }

    interface SetClickListeners {
        fun setOnSuccessListeners()
    }
}