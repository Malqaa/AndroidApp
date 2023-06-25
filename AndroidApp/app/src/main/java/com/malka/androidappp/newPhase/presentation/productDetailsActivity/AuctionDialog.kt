package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder

import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import kotlinx.android.synthetic.main.dialog_auction.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AuctionDialog(
    context: Context,
    var productId: Int,
    var auctionStartPrice: Float,
    var auctionMinimumPrice: Float,
    var auctionNegotiatePrice: Float,
    var setClickListeners: SetClickListeners
) : BaseDialog(context) {

    var countriesCallback: Call<GeneralResponse>? = null
    var generalRespone: GeneralResponse? = null
    override fun getViewId(): Int {
        return R.layout.dialog_auction
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = false
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        containerAutomaticBidding.hide()
        tvBidStartPrice.text = auctionStartPrice.toString()
        etCountNumber.setText(auctionNegotiatePrice.toString())
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            btnAdd.setBackgroundResource(R.drawable.background_corener_from_end_light_orange)
            btnSubtract.setBackgroundResource(R.drawable.background_corener_from_start_light_orange)
            conatinerRayal.setBackgroundResource(R.drawable.background_corener_from_start_light_orange)
        } else {
            btnSubtract.setBackgroundResource(R.drawable.background_corener_from_end_light_orange)
            btnAdd.setBackgroundResource(R.drawable.background_corener_from_start_light_orange)
            conatinerRayal.setBackgroundResource(R.drawable.background_corener_from_end_light_orange)
        }
        setOnClickListenres()

    }

    private fun setOnClickListenres() {
        switchAutoBid.setOnCheckedChangeListener { _, b ->
            if (b) {
                containerAutomaticBidding.show()
            } else {
                containerAutomaticBidding.hide()
            }
        }
        btnSubtract.setOnClickListener {
            var count = etCountNumber.text.toString().toInt()
            count += 1
            println(count)
            etCountNumber.setText(count.toString())
        }
        btn_bid.setOnClickListener {
            if (etCountNumber.text.toString().trim()
                    .toFloat() > auctionStartPrice && etCountNumber.text.toString().trim()
                    .toFloat() > auctionNegotiatePrice
            ) {
                var count = etCountNumber.text.toString().toInt()
                count -= 1
                etCountNumber.setText(count.toString())
            }

        }
        close_alert_bid.setOnClickListener {
            dismiss()
        }
        btn_bid.setOnClickListener {
            if (etCountNumber.text.toString().trim() == "") {
                HelpFunctions.ShowLongToast(context.getString(R.string.enterBidPrice), context)
            } else if (etCountNumber.text.toString().trim()
                    .toFloat() <= auctionStartPrice || etCountNumber.text.toString().trim()
                    .toFloat() <= auctionNegotiatePrice
            ) {
                HelpFunctions.ShowLongToast(
                    context.getString(R.string.enterCorrectBidPrice),
                    context
                )
            } else if (switchAutoBid.isChecked) {
                var ready = true
                if (etIncreaseForEachTIme.text.toString().trim() == "") {
                    ready = false
                    etIncreaseForEachTIme.error = context.getString(R.string.Fieldcantbeempty)
                }
                if (etHighestBidPrice.text.toString().trim() == "") {
                    ready = false
                    etHighestBidPrice.error = context.getString(R.string.Fieldcantbeempty)
                }else if(etHighestBidPrice.text.toString().trim().toFloat()>etCountNumber.text.toString().trim().toFloat()){
                    ready = false
                    etHighestBidPrice.error = context.getString(R.string.enterCorrectBidPrice)
                }
                if (ready) {
                    addBid(
                        productId,
                        etCountNumber.text.toString().toFloat(),
                        true,
                        etIncreaseForEachTIme.text.toString().trim().toFloat(),
                        etHighestBidPrice.text.toString().trim().toFloat()
                    )
                }
            } else {
                addBid(productId, etCountNumber.text.toString().toFloat(), false, 0f, 0f)
            }
        }
    }

    fun addBid(
        productId: Int,
        bidPrice: Float,
        autoBidding: Boolean,
        increaseEachTimePrice: Float,
        highestBidPrice: Float
    ) {
        close_alert_bid.isEnabled = false
        btn_bid.isEnabled = false
        var data: HashMap<String, Any> = HashMap()
        data["productId"] = productId
        data["bidPrice"] = bidPrice
        data["activateAutomaticBidding"] = autoBidding
        data["increaseEachTimePrice"] = increaseEachTimePrice
        data["highestBidPrice"] = highestBidPrice
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().addBid(data)
        countriesCallback?.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                btn_bid.isEnabled = true
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
                btn_bid.isEnabled = true
                close_alert_bid.isEnabled = true
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            generalRespone = it
                            if (generalRespone?.status_code == 200) {
                                dismiss()
                                setClickListeners.setOnSuccessListeners()

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