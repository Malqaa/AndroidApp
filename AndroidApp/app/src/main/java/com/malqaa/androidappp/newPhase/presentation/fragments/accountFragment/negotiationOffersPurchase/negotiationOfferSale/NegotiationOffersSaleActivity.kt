package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.negotiationOfferSale

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityNegotiationOffersSaleBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferDetails
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.ProductDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.AcceptOfferDialog
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.NegotiationOffersViewModel
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.negotiationOffersPurchase.adapter.NegotiationOffersAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show

class NegotiationOffersSaleActivity : BaseActivity<ActivityNegotiationOffersSaleBinding>(),
    NegotiationOffersAdapter.SetOnOfferClickListeners, SwipeRefreshLayout.OnRefreshListener {

    lateinit var negotiationOffersAdapter: NegotiationOffersAdapter
    lateinit var negotiationOfferDetailsList: ArrayList<NegotiationOfferDetails>
    lateinit var negotiationOffersViewModel: NegotiationOffersViewModel
    var isSent = false
    var lastCancelPosition = -1
    var purchasePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityNegotiationOffersSaleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setContentView(R.layout.activity_negotiation_offers_sale)
        binding.toolbarMain.toolbarTitle.text = getString(R.string.MyProductsOffers)
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
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
                binding.progressBar.show()
            else
                binding.progressBar.hide()
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
            } else if (it.message2 != null) {
                showApiError(it.message2!!)
            } else {
                showApiError(getString(R.string.serverError))
            }
        }
        negotiationOffersViewModel.noOffersObserver.observe(this) {
            showApiError(getString(R.string.noOffersFound))
        }
        negotiationOffersViewModel.purchaseProductsOffersObserver.observe(this) {
            if (it.status_code == 200 && it.negotiationOfferDetailsList != null) {
                negotiationOfferDetailsList.clear()
                negotiationOfferDetailsList.addAll(it.negotiationOfferDetailsList)
                negotiationOffersAdapter.notifyDataSetChanged()
            } else if (it.status_code == 400 && it.message == "No offers found") {
                showApiError(getString(R.string.noOffersFound))
            } else {
                showApiError(getString(R.string.serverError))
            }
        }
        negotiationOffersViewModel.cancelOfferObserver.observe(this) {
            if (it.status_code == 200) {
                negotiationOfferDetailsList[lastCancelPosition].offerStatus = "Canceled"
                negotiationOffersAdapter.notifyItemChanged(lastCancelPosition)
                lastCancelPosition = -1
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
            binding.tvError.show()
            binding.tvError.text = message
        } else {
            HelpFunctions.ShowLongToast(message, this)
        }
    }

    private fun setNegotiationOffersAdapter() {
        negotiationOfferDetailsList = ArrayList()
        negotiationOffersAdapter = NegotiationOffersAdapter(negotiationOfferDetailsList, this, true)
        binding.rvNegotiation.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = negotiationOffersAdapter

        }
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.sent.setOnClickListener {
            binding.sent.background = ContextCompat.getDrawable(
                this,
                R.drawable.round_btn
            )
            binding.sent.setTextColor(Color.parseColor("#FFFFFF"))
            binding.received.setTextColor(Color.parseColor("#45495E"))
            binding.received.background = null
            isSent = true
            onRefresh()
        }

        binding.received.setOnClickListener {
            binding.received.background = ContextCompat.getDrawable(
                this,
                R.drawable.round_btn
            )
            binding.received.setTextColor(Color.parseColor("#FFFFFF"))
            binding.sent.setTextColor(Color.parseColor("#45495E"))
            binding.sent.background = null
            isSent = false
            onRefresh()
        }

    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        binding.tvError.hide()
        negotiationOfferDetailsList.clear()
        negotiationOffersAdapter.notifyDataSetChanged()
        negotiationOffersViewModel.getSaleProductsOffers(isSent)
        negotiationOffersAdapter.setIsSend(isSent)
    }

    override fun onCancelOffer(type: Boolean, offerID: Int, position: Int) {
        if (type) {
            println("hhhh " + offerID + " " + negotiationOfferDetailsList[position].offerId)
            lastCancelPosition = position
            negotiationOffersViewModel.cancelOfferProvider(offerID)
        } else {
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

    override fun onItemDetails(position: Int) {
        startActivity(Intent(this, ProductDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.productIdKey, negotiationOfferDetailsList[position].productId)
            putExtra("Template", "")
        })
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
        val acceptOfferDialog: AcceptOfferDialog = AcceptOfferDialog(this,
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

    override fun onDestroy() {
        super.onDestroy()
        negotiationOffersViewModel.closeAllCall()
    }
}