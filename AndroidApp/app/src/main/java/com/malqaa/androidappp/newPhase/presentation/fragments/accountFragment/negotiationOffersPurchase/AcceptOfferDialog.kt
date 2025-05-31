package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase


import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.AdapterView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogAcceotOfferBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.core.BaseViewModel
import com.malqaa.androidappp.newPhase.data.network.callApi
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.configrationResp.ConfigurationResp
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.adapter.SpinnerExpireHoursAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AcceptOfferDialog(
    context: Context,
    var accept: Boolean,
    var position: Int,
    var offerID: Int,
    var productId: Int,
    var listener: SetClickListeners
) : BaseDialog<DialogAcceotOfferBinding>(context) {
    var countriesCallback: Call<ConfigurationResp>? = null
    private var acceptRejectOfferCallBack: Call<GeneralResponse>? = null
    var configurationResp: ConfigurationResp? = null
    var generalResponse: GeneralResponse? = null
    private lateinit var expireHoursList: ArrayList<Float>
    private lateinit var spinnerExpireHoursAdapter: SpinnerExpireHoursAdapter
    var expireHour: Float = 0f

    override fun inflateViewBinding(): DialogAcceotOfferBinding {
        return DialogAcceotOfferBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        if (accept) {
            binding.spTHours.visibility = View.VISIBLE
            binding.txtTHours.visibility = View.VISIBLE
            binding.tvAcceptAndRejectTitle.text =
                context.getString(R.string.acceptNegotiationsOffers)
            binding.btnSend.text = context.getString(R.string.accept)
            binding.contianerRefuseReason.hide()
        } else {
            binding.spTHours.visibility = View.GONE
            binding.txtTHours.visibility = View.GONE
            binding.btnSend.text = context.getString(R.string.reject)
            binding.tvAcceptAndRejectTitle.text =
                context.getString(R.string.rejectNegotiationsOffers)
        }
        setSpinnerExpireHoursAdapter()
        setOnClickListeners()
        getExpireHourse()
    }

    private fun setSpinnerExpireHoursAdapter() {
        expireHoursList = ArrayList()
        spinnerExpireHoursAdapter = SpinnerExpireHoursAdapter(context, expireHoursList)
        binding.spinnerValues.adapter = spinnerExpireHoursAdapter
        binding.spinnerValues.onItemSelectedListener = object :
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

    private fun setOnClickListeners() {
        binding.closeAlertBid.setOnClickListener {
            dismiss()
        }
        binding.btnSend.setOnClickListener {
            var readytosend = true
            if (accept) {
                if (expireHour == 0f) {
                    readytosend = false
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.selectExpirationHours),
                        context
                    )
                }
            }
            if (readytosend) {
                getAcceptRejectOffer()
            }
        }
    }

    fun getExpireHourse() {
        binding.closeAlertBid.isEnabled = false
        binding.btnSend.isEnabled = false

        binding.progressBar.visibility = View.VISIBLE
        countriesCallback = getRetrofitBuilder().getConfigurationData(
            ConstantObjects.configration_OfferExpiredHours
        )
        countriesCallback?.enqueue(object : Callback<ConfigurationResp> {
            override fun onFailure(call: Call<ConfigurationResp>, t: Throwable) {
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
                call: Call<ConfigurationResp>,
                response: Response<ConfigurationResp>
            ) {
                binding.btnSend.isEnabled = true
                binding.closeAlertBid.isEnabled = true
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            configurationResp = it
                            if (configurationResp?.status_code == 200) {
                                try {
                                    //  println("hhhh "+Gson().toJson( configurationResp?.configurationData))
                                    val result: List<Float>? =
                                        configurationResp?.configurationData?.configValue?.split(",")
                                            ?.map { it.trim().toFloat() }
                                    result?.let { it1 ->
                                        expireHoursList.addAll(it1)
                                        spinnerExpireHoursAdapter.notifyDataSetChanged()
                                    }

                                } catch (e: java.lang.Exception) {
                                }
                                //listener.setOnSuccessListeners(etNegotiationPrice.text.toString().trim())
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

    private fun getAcceptRejectOffer() {
        binding.closeAlertBid.isEnabled = false
        binding.btnSend.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        acceptRejectOfferCallBack = getRetrofitBuilder()
            .acceptRejectOffer(
                ConstantObjects.currentLanguage,
                offerID,
                productId,
                accept,
                binding.etRefuseReason.text.toString().trim(),
                expireHour
            )
        callApi(acceptRejectOfferCallBack!!,
            onSuccess = {
                binding.btnSend.isEnabled = true
                binding.closeAlertBid.isEnabled = true
                binding.progressBar.visibility = View.GONE

                generalResponse = it
                if (generalResponse?.status_code == 200) {
                    listener.setOnSuccessListeners(offerID, position, accept)
                    dismiss()
                } else {
                    HelpFunctions.ShowLongToast(
                        generalResponse?.message.toString(),
                        context
                    )
                }

            },
            onFailure = { throwable, statusCode, errorBody ->
                binding.progressBar.visibility = View.GONE
                binding.btnSend.isEnabled = true
                binding.closeAlertBid.isEnabled = true
                if (throwable != null && errorBody == null)
                    HelpFunctions.ShowLongToast(context.getString(R.string.serverError), context)
                else {
                    val error = BaseViewModel().getErrorResponse(statusCode, errorBody)
                    HelpFunctions.ShowLongToast(error?.message.toString(), context)
                }
            },
            goLogin = {
                binding.progressBar.visibility = View.GONE
                binding.btnSend.isEnabled = true
                binding.closeAlertBid.isEnabled = true
            })
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (countriesCallback != null) {
            countriesCallback?.cancel()
        }
        if (acceptRejectOfferCallBack != null) {
            acceptRejectOfferCallBack?.cancel()
        }
    }

    interface SetClickListeners {
        fun setOnSuccessListeners(offerID: Int, position: Int, accept: Boolean)
    }
}
