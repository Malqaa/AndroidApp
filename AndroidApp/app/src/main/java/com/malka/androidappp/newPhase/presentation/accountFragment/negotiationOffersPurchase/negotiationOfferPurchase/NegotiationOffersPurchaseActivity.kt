package com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.negotiationOfferPurchase

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferDetails
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.AcceptOfferDialog
import com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.adapter.NegotiationOffersAdapter
import com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.NegotiationOffersViewModel
import kotlinx.android.synthetic.main.activity_negotiation_offers_purchase.*
import kotlinx.android.synthetic.main.toolbar_main.*

class NegotiationOffersPurchaseActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener,
    NegotiationOffersAdapter.SetOnOfferClickListeners {

    lateinit var negotiationOffersAdapter: NegotiationOffersAdapter
    lateinit var negotiationOfferDetailsList: ArrayList<NegotiationOfferDetails>
    private lateinit var negotiationOffersViewModel: NegotiationOffersViewModel
    private var isSent = true
    private var lastCancelPosition = -1
    private var purchasePosition = -1
    var comeFrom =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_negotiation_offers_purchase)
        toolbar_title.text = getString(R.string.negotiation_offers)
        comeFrom=   intent.getStringExtra("ComeFrom")?:""
        swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipeToRefresh.setOnRefreshListener(this)
        setViewClickListeners()
        setNegotiationOffersAdapter()
        setUpViewModel()
        onRefresh()
    }

    private fun setUpViewModel() {
        negotiationOffersViewModel =
            ViewModelProvider(this).get(NegotiationOffersViewModel::class.java)
        negotiationOffersViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        negotiationOffersViewModel.loadingDialogObserver.observe(this) {
            if (it) {
                HelpFunctions.startProgressBar(this)
            } else {
                HelpFunctions.dismissProgressBar()
            }
        }
        negotiationOffersViewModel.isNetworkFail.observe(this) {
            if (it) {
                showApiError(getString(R.string.connectionError))
            } else {
                showApiError(getString(R.string.serverError))
            }
        }
        negotiationOffersViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showApiError(it.message!!)
            } else {
                showApiError(getString(R.string.serverError))
            }
        }
        negotiationOffersViewModel.noOffersObserver.observe(this) {
            showApiError(getString(R.string.noOffersFound))
        }
        negotiationOffersViewModel.purchaseProductsOffersObserver.observe(this) {
            if (it.status_code == 200 && it.negotiationOfferDetailsList != null) {
                val dataList: List<NegotiationOfferDetails> =
                    it.negotiationOfferDetailsList
                negotiationOfferDetailsList.clear()
                negotiationOfferDetailsList.addAll(dataList)
                negotiationOffersAdapter.notifyDataSetChanged()
            } else if (it.status_code == 400 && it.message == "No offers found") {
                showApiError(getString(R.string.noOffersFound))
            } else {
                showApiError(getString(R.string.serverError))
            }
        }
        negotiationOffersViewModel.cancelOfferObserver.observe(this) {
            if (it.status_code == 200) {
                negotiationOfferDetailsList[lastCancelPosition].offerStatus="Canceled"
                negotiationOffersAdapter.notifyItemChanged(lastCancelPosition)
                lastCancelPosition=-1
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
        negotiationOffersViewModel.purchaseOfferObserver.observe(this) {
            if (it.status_code == 200) {
                negotiationOfferDetailsList[purchasePosition].offerStatus="Purchcased"
                negotiationOffersAdapter.notifyItemChanged(purchasePosition)
                purchasePosition=-1
            } else {
                if (it.message != null) {
                    HelpFunctions.ShowLongToast(it.message, this)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), this)
                }
            }
        }
    }

    private fun showApiError(message: String) {
        if (negotiationOfferDetailsList.isEmpty()) {
            tvError.show()
            tvError.text = message
        } else {
            HelpFunctions.ShowLongToast(message, this)
        }
    }

    private fun setNegotiationOffersAdapter() {
        negotiationOfferDetailsList = ArrayList()
        negotiationOffersAdapter = NegotiationOffersAdapter(negotiationOfferDetailsList, this)
        rvNegotiation.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = negotiationOffersAdapter

        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            onBackPressed()
        }
        sent.setOnClickListener {
            onSentNegotiation()
        }

        received.setOnClickListener {
            onReceivedNegotiation()
        }

    }

    private fun onReceivedNegotiation(){
        received.background = ContextCompat.getDrawable(
            this,
            R.drawable.round_btn
        )
        received.setTextColor(Color.parseColor("#FFFFFF"))
        sent.setTextColor(Color.parseColor("#45495E"))
        sent.background = null
        isSent = false
        onRefresh()
    }
    private fun onSentNegotiation(){
        sent.background = ContextCompat.getDrawable(
            this,
            R.drawable.round_btn
        )
        sent.setTextColor(Color.parseColor("#FFFFFF"))
        received.setTextColor(Color.parseColor("#45495E"))
//            btn.setText("Accept")
        received.background = null
        isSent = true
        onRefresh()
    }

    override fun onRefresh() {
        swipeToRefresh.isRefreshing = false
        tvError.hide()
        negotiationOfferDetailsList.clear()
        negotiationOffersAdapter.notifyDataSetChanged()
        negotiationOffersViewModel.getPurchaseProductsOffers(isSent)
        negotiationOffersAdapter.setIsSend(isSent)
    }

    override fun onCancelOffer(type:Boolean ,offerID: Int, position: Int) {
        if(type){
            println("hhhh " + offerID + " " + negotiationOfferDetailsList[position].offerId)
            lastCancelPosition = position
            negotiationOffersViewModel.cancelOfferProvider(offerID)
        }else{
            println("hhhh " + offerID + " " + negotiationOfferDetailsList[position].offerId)
            lastCancelPosition = position
            negotiationOffersViewModel.cancelOffer(offerID)
        }
    }
    override fun onPurchaseOffer(offerID: Int, position: Int) {
        println("hhhh " + offerID + " " + negotiationOfferDetailsList[position].offerId)
        purchasePosition = position
        negotiationOffersViewModel.purchaseOffer(offerID)
    }
    override fun onAcceptOffer(position: Int) {
        val acceptOfferDialog = AcceptOfferDialog(this,
            true,
            position,
            negotiationOfferDetailsList[position].offerId,
            negotiationOfferDetailsList[position].productId,
            object : AcceptOfferDialog.SetClickListeners {
                override fun setOnSuccessListeners(offerID: Int, position: Int, accept: Boolean) {
                    negotiationOfferDetailsList[position].offerStatus =
                        if (accept) "Accepted" else "Reject"
                    negotiationOffersAdapter.notifyItemChanged(position)
                }

            })
        acceptOfferDialog.show()
    }

    override fun onRejectOffer(position: Int) {
        val acceptOfferDialog = AcceptOfferDialog(this,
            false,
            position,
            negotiationOfferDetailsList[position].offerId,
            negotiationOfferDetailsList[position].productId,
            object : AcceptOfferDialog.SetClickListeners {
                override fun setOnSuccessListeners(offerID: Int, position: Int, accept: Boolean) {
                    negotiationOfferDetailsList[position].offerStatus =
                        if (accept) "Accepted" else "Refused"
                    negotiationOffersAdapter.notifyItemChanged(position)
                }

            })
        acceptOfferDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(comeFrom=="AccountFragment"){
            finish()
        }else{
            startActivity(Intent(this, MainActivity::class.java).apply {})
            finish()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        negotiationOffersViewModel.closeAllCall()
    }
}