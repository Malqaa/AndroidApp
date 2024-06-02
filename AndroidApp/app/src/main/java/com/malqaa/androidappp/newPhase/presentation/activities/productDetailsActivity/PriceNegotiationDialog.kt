package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import kotlinx.android.synthetic.main.dialog_price_negotiation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PriceNegotiationDialog(
    context: Context,
    var quantity:Int,
    var productId: Int,
    var listener: SetClickListeners
) : BaseDialog(context) {
    var countriesCallback: Call<GeneralResponse>? = null
    var generalRespone: GeneralResponse? = null
    override fun getViewId(): Int {
        return R.layout.dialog_price_negotiation
    }

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        if(quantity==1){
            layQuantity.visibility=View.GONE
        }else{
            layQuantity.visibility=View.VISIBLE
        }
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
            if(quantity!=1){
                if (etQuentity.text.toString().trim() == "") {
                    readytosend = false
                    etQuentity.error = context.getString(R.string.quantity)
                }
                if(etQuentity.text.toString().toInt() > quantity){
                    readytosend = false
                    etQuentity.error = context.getString(R.string.prevent_quantity)
                }
            }

            if (readytosend) {
                if(quantity!=1){
                    addNegotiationPrice(
                        productId,
                        etNegotiationPrice.text.toString().trim().toFloat(),

                        etQuentity.text.toString().trim().toInt()
                    )
                }else{
                    addNegotiationPrice(
                        productId,
                        etNegotiationPrice.text.toString().trim().toFloat(),
                        1
                    )
                }

            }
        }
    }

    private fun addNegotiationPrice(
        productId: Int,
        price: Float,
        quantity: Int
    ) {
        close_alert_bid.isEnabled = false
        btnSend.isEnabled = false
//        var data: HashMap<String, Any> = HashMap()
//        data["productId"] = productId
//        data["quantity"] = quentity
//        data["price"] = price

        countriesCallback = getRetrofitBuilder().addProductClientOffer(productId, quantity, price)
        countriesCallback?.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnSend.isEnabled = true
                close_alert_bid.isEnabled = true
                if (t is HttpException) {
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
                                listener.setOnSuccessListeners(
                                    etNegotiationPrice.text.toString().trim()
                                )
                            }
                        }
                    } else {
                        HelpFunctions.ShowLongToast(
                            context.getString(R.string.serverError),
                            context
                        )

                    }
                } catch (e: Exception) {
                    //
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
        fun setOnSuccessListeners(highestBidPrice: String)
    }
}