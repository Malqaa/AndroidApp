package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.addBidResp.AddBidResp

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

    var countriesCallback: Call<AddBidResp>? = null
    var generalRespone: AddBidResp? = null
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
        btnAdd.setOnClickListener {
            var count = etCountNumber.text.toString().toFloat()
            count += 1
            println(count)
            etCountNumber.setText(count.toString())
        }
        btnSubtract.setOnClickListener {
            if (etCountNumber.text.toString().trim()
                    .toFloat() > auctionStartPrice && etCountNumber.text.toString().trim()
                    .toFloat() > auctionNegotiatePrice
            ) {
                var count = etCountNumber.text.toString().toFloat()
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
                if (etIncreaseForEachTIme.text.toString().trim() == ""|| etIncreaseForEachTIme.text.toString().trim().toFloat()== 0f ){
                    ready = false
                    etIncreaseForEachTIme.error = context.getString(R.string.Fieldcantbeempty)
                }

                if (etHighestBidPrice.text.toString().trim() == "") {
                    ready = false
                    etHighestBidPrice.error = context.getString(R.string.Fieldcantbeempty)
                }
                if(etHighestBidPrice.text.toString().trim().toFloat()>etCountNumber.text.toString().trim().toFloat()){
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
        progressBar.visibility = View.VISIBLE
        close_alert_bid.isEnabled = false
        btn_bid.isEnabled = false
        var data: HashMap<String, Any> = HashMap()
        data["productId"] = productId
        data["id"] = 0
        data["bidPrice"] = bidPrice
        data["activateAutomaticBidding"] = autoBidding
        data["increaseEachTimePrice"] = increaseEachTimePrice
        data["highestBidPrice"] = highestBidPrice
        println("hhhh "+Gson().toJson(data))
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().addBid(data)
        countriesCallback?.enqueue(object : Callback<AddBidResp> {
            override fun onFailure(call: Call<AddBidResp>, t: Throwable) {
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
                call: Call<AddBidResp>,
                response: Response<AddBidResp>
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
                                setClickListeners.setOnSuccessListeners(generalRespone?.BidObject?.bidPrice?:0f)

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
        fun setOnSuccessListeners(highestBidPrice:Float)
    }

}