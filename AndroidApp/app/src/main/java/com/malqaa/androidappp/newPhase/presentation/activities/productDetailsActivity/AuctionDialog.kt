package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.google.gson.Gson
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogAuctionBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.addBidResp.AddBidResp
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
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
) : BaseDialog<DialogAuctionBinding>(context) {

    var automaticBid = false
    var countriesCallback: Call<AddBidResp>? = null
    var generalRespone: AddBidResp? = null

    override fun inflateViewBinding(): DialogAuctionBinding {
        return DialogAuctionBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean = false
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        binding.containerAutomaticBidding.hide()
        binding.tvBidStartPrice.text = auctionStartPrice.toString()
        binding.etCountNumber.setText(auctionNegotiatePrice.toString())
        if (ConstantObjects.currentLanguage == ConstantObjects.ARABIC) {
            binding.btnAdd.setBackgroundResource(R.drawable.background_corener_from_end_light_orange)
            binding.btnSubtract.setBackgroundResource(R.drawable.background_corener_from_start_light_orange)
            binding.conatinerRayal.setBackgroundResource(R.drawable.background_corener_from_start_light_orange)
        } else {
            binding.btnSubtract.setBackgroundResource(R.drawable.background_corener_from_end_light_orange)
            binding.btnAdd.setBackgroundResource(R.drawable.background_corener_from_start_light_orange)
            binding.conatinerRayal.setBackgroundResource(R.drawable.background_corener_from_end_light_orange)
        }
        setOnClickListenres()

    }

    private fun setOnClickListenres() {
        binding.switchAutoBid.setOnCheckedChangeListener { _, b ->
            if (b) {
                automaticBid = true
                binding.containerAutomaticBidding.show()
            } else {
                automaticBid = false
                binding.containerAutomaticBidding.hide()
            }
        }

        binding.btnAdd.setOnClickListener {
            var count = binding.etCountNumber.text.toString().toFloat()
            count += 1
            println(count)
            binding.etCountNumber.setText(count.toString())
        }

        binding.btnSubtract.setOnClickListener {
            if (binding.etCountNumber.text.toString().trim()
                    .toFloat() > auctionStartPrice && binding.etCountNumber.text.toString().trim()
                    .toFloat() > auctionNegotiatePrice
            ) {
                var count = binding.etCountNumber.text.toString().toFloat()
                count -= 1
                binding.etCountNumber.setText(count.toString())
            }

        }
        binding.closeAlertBid.setOnClickListener {
            dismiss()
        }
        binding.btnBid.setOnClickListener {
            if (binding.switchAutoBid.isChecked) {

                var ready = true
                if (binding.etCountNumber.text.toString().trim() == "") {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.enterBidPrice),
                        context
                    )
                } else if (binding.etIncreaseForEachTIme.text.toString()
                        .trim() == "" || binding.etIncreaseForEachTIme.text.toString().trim()
                        .toFloat() == 0f
                ) {
                    ready = false
                    binding.etIncreaseForEachTIme.error =
                        context.getString(R.string.Fieldcantbeempty)
                }

                if (binding.etIncreaseForEachTIme.text.toString()
                        .trim() == "" || binding.etIncreaseForEachTIme.text.toString().trim()
                        .toFloat() == 0f
                ) {
                    ready = false
                    binding.etIncreaseForEachTIme.error =
                        context.getString(R.string.Fieldcantbeempty)
                }

                if (binding.etHighestBidPrice.text.toString().trim() == "") {
                    ready = false
                    binding.etHighestBidPrice.error = context.getString(R.string.Fieldcantbeempty)
                } else if (binding.etHighestBidPrice.text.toString().trim()
                        .toFloat() < binding.etCountNumber.text.toString().trim().toFloat()
                ) {
                    ready = false
                    binding.etHighestBidPrice.error =
                        context.getString(R.string.enterCorrectBidPrice)
                }

                if (ready) {
                    addBid(
                        productId,
                        binding.etCountNumber.text.toString().toFloat(),
                        true,
                        binding.etIncreaseForEachTIme.text.toString().trim().toFloat(),
                        binding.etHighestBidPrice.text.toString().trim().toFloat()
                    )
                }
            } else {
                if (binding.etCountNumber.text.toString().trim() == "") {
                    HelpFunctions.ShowLongToast(context.getString(R.string.enterBidPrice), context)
                } else {
                    addBid(
                        productId,
                        binding.etCountNumber.text.toString().toFloat(),
                        false,
                        0f,
                        0f
                    )
                }
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
        binding.progressBar.visibility = View.VISIBLE
        binding.closeAlertBid.isEnabled = false
        binding.btnBid.isEnabled = false
        var data: HashMap<String, Any> = HashMap()
        data["productId"] = productId
        data["id"] = 0
        data["bidPrice"] = bidPrice
        data["activateAutomaticBidding"] = autoBidding
        data["increaseEachTimePrice"] = increaseEachTimePrice
        data["highestBidPrice"] = highestBidPrice
        println("hhhh " + Gson().toJson(data))
        countriesCallback = getRetrofitBuilder().addBid(data)
        countriesCallback?.enqueue(object : Callback<AddBidResp> {
            override fun onFailure(call: Call<AddBidResp>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.btnBid.isEnabled = true
                binding.closeAlertBid.isEnabled = true
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
                binding.btnBid.isEnabled = true
                binding.closeAlertBid.isEnabled = true
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            generalRespone = it
                            if (generalRespone?.status_code == 200) {
                                dismiss()
                                setClickListeners.setOnSuccessListeners(
                                    generalRespone?.BidObject?.bidPrice ?: 0f
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
        fun setOnSuccessListeners(highestBidPrice: Float)
    }

}