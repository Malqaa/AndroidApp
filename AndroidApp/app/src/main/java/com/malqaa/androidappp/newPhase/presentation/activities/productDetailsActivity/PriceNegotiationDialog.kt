package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogPriceNegotiationBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PriceNegotiationDialog(
    context: Context,
    var quantity: Int,
    var productId: Int,
    var listener: SetClickListeners
) : BaseDialog<DialogPriceNegotiationBinding>(context) {

    var countriesCallback: Call<GeneralResponse>? = null
    var generalRespone: GeneralResponse? = null

    override fun inflateViewBinding(): DialogPriceNegotiationBinding {
        return DialogPriceNegotiationBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        if (quantity == 1) {
            binding.layQuantity.visibility = View.GONE
        } else {
            binding.layQuantity.visibility = View.VISIBLE
        }
        setOnClickListenres()
    }

    private fun setOnClickListenres() {
        binding.closeAlertBid.setOnClickListener {
            dismiss()
        }
        binding.btnSend.setOnClickListener {
            var readytosend = true
            if (binding.etNegotiationPrice.text.toString().trim() == "") {
                readytosend = false
                binding.etNegotiationPrice.error =
                    context.getString(R.string.writeNegotiationPrice2)
            } else {
                if (quantity != 1) {
                    if (binding.etQuentity.text.toString().trim() == "") {
                        readytosend = false
                        binding.etQuentity.error = context.getString(R.string.quantity)
                    } else if (binding.etQuentity.text.toString().toInt() > quantity) {
                        readytosend = false
                        binding.etQuentity.error = context.getString(R.string.prevent_quantity)
                    }
                }
            }


            if (readytosend) {
                if (quantity != 1) {
                    addNegotiationPrice(
                        productId,
                        binding.etNegotiationPrice.text.toString().trim().toFloat(),
                        binding.etQuentity.text.toString().trim().toInt()
                    )
                } else {
                    addNegotiationPrice(
                        productId,
                        binding.etNegotiationPrice.text.toString().trim().toFloat(),
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
        binding.closeAlertBid.isEnabled = false
        binding.btnSend.isEnabled = false

        countriesCallback = getRetrofitBuilder().addProductClientOffer(productId, quantity, price)
        countriesCallback?.enqueue(object : Callback<GeneralResponse> {
            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.btnSend.isEnabled = true
                binding.closeAlertBid.isEnabled = true
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
                binding.btnSend.isEnabled = true
                binding.closeAlertBid.isEnabled = true
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            generalRespone = it
                            if (generalRespone?.status_code == 200) {
                                listener.setOnSuccessListeners(
                                    binding.etNegotiationPrice.text.toString().trim()
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
        fun setOnSuccessListeners(highestBidPrice: String)
    }
}