package com.malka.androidappp.newPhase.presentation.accountFragment.myProducts.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonData
import com.malka.androidappp.newPhase.domain.models.bidPersonsResp.BidPersonsResp
import kotlinx.android.synthetic.main.dialog_bid_persons.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class BidPersonsDialog(
    context: Context,
    var productId: Int,
    var setOnAddBidOffersListeners: SetOnAddBidOffersListeners,
    var fromProductDetails:Boolean=false
) :
    BaseDialog(context), BidPersonsAdapter.SetOnViewClickListeners {
    lateinit var bidPersonsAdapter: BidPersonsAdapter
    lateinit var bidPersonsDataList: ArrayList<BidPersonData>
    var countriesCallback: Call<BidPersonsResp>? = null
    var bidPersonsResp: BidPersonsResp? = null
    var bidIdList: ArrayList<String> = ArrayList()
    override fun getViewId(): Int = R.layout.dialog_bid_persons

    override fun isFullScreen(): Boolean = false
    override fun isCancelable(): Boolean = true
    override fun isLoadingDialog(): Boolean = false
    override fun initialization() {
        setViewClickListners()
        setRecyclerView()
        getBidsPersons()
        if(fromProductDetails){
            tvSelctAll.hide()
            tvUserCount.hide()
        }else{
            tvSelctAll.show()
            tvUserCount.show()
        }
    }

    private fun setRecyclerView() {
        bidPersonsDataList = ArrayList()
        bidPersonsAdapter = BidPersonsAdapter(bidPersonsDataList, this,fromProductDetails)
        rvBid.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = bidPersonsAdapter
        }
    }

    private fun setViewClickListners() {
        ivClose.setOnClickListener {
            dismiss()
        }
        tvSelctAll.setOnClickListener {
            bidIdList.clear()
            for (item in bidPersonsDataList) {
                item.isSelected = true
                item.userId?.let { it1 -> bidIdList.add(it1) }
            }
            bidPersonsAdapter.notifyDataSetChanged()
            tvUserCount.text = "${bidIdList.size} ${context.getString(R.string.user)}"
        }
        btnSend.setOnClickListener {
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


    fun getBidsPersons() {
        ivClose.isEnabled = false
        btnSend.isEnabled = false
//        var data: HashMap<String, Any> = HashMap()
//        data["productId"] = productId
//        data["quantity"] = quentity
//        data["price"] = price
        progressBar.visibility = View.VISIBLE
        countriesCallback = RetrofitBuilder.GetRetrofitBuilder().getBidsPersons(
            productId
        )
        countriesCallback?.enqueue(object : Callback<BidPersonsResp> {
            override fun onFailure(call: Call<BidPersonsResp>, t: Throwable) {
                progressBar.visibility = View.GONE
                btnSend.isEnabled = true
                ivClose.isEnabled = true
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
                call: Call<BidPersonsResp>,
                response: Response<BidPersonsResp>
            ) {
                btnSend.isEnabled = true
                ivClose.isEnabled = true
                progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            bidPersonsResp = it
                            if (bidPersonsResp?.status_code == 200) {
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
                }
            }
        })
    }

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
        tvUserCount.text = "${bidIdList.size} ${context.getString(R.string.user)}"

    }

    interface SetOnAddBidOffersListeners {
        fun onAddOpenBidOfferDailog(bidsList: List<String>)
    }
}