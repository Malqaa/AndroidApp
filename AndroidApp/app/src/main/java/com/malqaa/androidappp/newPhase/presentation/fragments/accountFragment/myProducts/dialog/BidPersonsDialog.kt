package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myProducts.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogBidPersonsBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonData
import com.malqaa.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonsResp
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class BidPersonsDialog(
    var priceAuction: String,
    context: Context,
    var productId: Int,
    var setOnAddBidOffersListeners: SetOnAddBidOffersListeners,
    var fromProductDetails: Boolean = false
) : BaseDialog<DialogBidPersonsBinding>(context), BidPersonsAdapter.SetOnViewClickListeners {

    lateinit var bidPersonsAdapter: BidPersonsAdapter
    lateinit var bidPersonsDataList: ArrayList<BidPersonData>
    var countriesCallback: Call<BidPersonsResp>? = null
    var bidPersonsResp: BidPersonsResp? = null
    var bidIdList: ArrayList<String> = ArrayList()
    var offerList: ArrayList<BidPersonData> = ArrayList()

    override fun inflateViewBinding(): DialogBidPersonsBinding {
        return DialogBidPersonsBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        setViewClickListeners()
        setRecyclerView()
        getBidsPersons()
        if (fromProductDetails) {
            binding.tvSelctAll.hide()
            binding.tvUserCount.hide()
            binding.tvTitle.text = context.getString(R.string.my_bids)
            binding.btnSend.hide()
            binding.containerBidOnPrice.show()
            binding.BidOnPriceTv.text = priceAuction
        } else {
            binding.tvSelctAll.show()
            binding.tvUserCount.show()
            binding.tvTitle.text = context.getString(R.string.sendOffer)
            binding.btnSend.show()
            binding.containerBidOnPrice.hide()
        }
    }

    private fun setRecyclerView() {
        bidPersonsDataList = ArrayList()
        bidPersonsAdapter = BidPersonsAdapter(bidPersonsDataList, this, fromProductDetails)
        binding.rvBid.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = bidPersonsAdapter
        }
    }

    private fun setViewClickListeners() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.tvSelctAll.setOnClickListener {
            bidIdList.clear()
            for (item in bidPersonsDataList) {
                item.isSelected = true
                item.userId?.let { it1 -> bidIdList.add(it1) }
            }
            bidPersonsAdapter.notifyDataSetChanged()
            binding.tvUserCount.text = "${bidIdList.size} ${context.getString(R.string.user)}"
        }
        binding.btnSend.setOnClickListener {
            if (offerList.isNullOrEmpty()) {
                HelpFunctions.ShowLongToast(
                    context.getString(R.string.noFoundUserOffers),
                    context
                )
                dismiss()
            } else {

                if (bidIdList.isNotEmpty()) {
                    setOnAddBidOffersListeners.onAddOpenBidOfferDailog(bidIdList)
                    dismiss()
                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.selectUser),
                        context
                    )
                }
            }
        }
        binding.containerBidOnPrice.setOnClickListener {
            setOnAddBidOffersListeners.onOpenAuctionDialog()
            dismiss()
        }
    }


    private fun getBidsPersons() {
        binding.ivClose.isEnabled = false
        binding.btnSend.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE
        countriesCallback = getRetrofitBuilder().getBidsPersons(
            productId
        )
        countriesCallback?.enqueue(object : Callback<BidPersonsResp> {
            override fun onFailure(call: Call<BidPersonsResp>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.btnSend.isEnabled = true
                binding.ivClose.isEnabled = true
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
                call: Call<BidPersonsResp>,
                response: Response<BidPersonsResp>
            ) {
                binding.btnSend.isEnabled = true
                binding.ivClose.isEnabled = true
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            bidPersonsResp = it
                            if (bidPersonsResp?.status_code == 200) {
                                offerList = bidPersonsResp?.bidPersonsDataList ?: arrayListOf()
                                bidPersonsResp?.bidPersonsDataList?.let { list ->
                                    bidPersonsDataList.clear()
                                    bidPersonsDataList.addAll(list)
                                    bidPersonsAdapter.notifyDataSetChanged()
                                }
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

    @SuppressLint("SetTextI18n")
    override fun setOnBidSelect(position: Int) {
        if (bidPersonsDataList[position].isSelected) {
            bidPersonsDataList[position].isSelected = false
            if (!bidIdList.contains(bidPersonsDataList[position].userId)) {
                bidIdList.remove(bidPersonsDataList[position].userId)
            }
        } else {
            bidPersonsDataList[position].isSelected = true
            if (!bidIdList.contains(bidPersonsDataList[position].userId)) {
                bidPersonsDataList[position].userId?.let { bidIdList.add(it) }
            }
        }
        bidPersonsAdapter.notifyDataSetChanged()
        binding.tvUserCount.text = "${bidIdList.size} ${context.getString(R.string.user)}"

    }

    interface SetOnAddBidOffersListeners {
        fun onAddOpenBidOfferDailog(bidsList: List<String>)
        fun onOpenAuctionDialog()
    }
}