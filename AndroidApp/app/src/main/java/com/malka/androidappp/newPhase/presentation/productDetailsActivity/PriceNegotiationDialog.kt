package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import kotlinx.android.synthetic.main.dialog_price_negotiation.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PriceNegotiationDialog(
    context: Context,
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
                addNegotiationPrice(
                    productId,
                    etNegotiationPrice.text.toString().trim().toFloat(),
                    etQuentity.text.toString().trim().toInt()
                )
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