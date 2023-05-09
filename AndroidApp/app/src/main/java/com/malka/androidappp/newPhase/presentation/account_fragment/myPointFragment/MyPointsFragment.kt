package com.malka.androidappp.newPhase.presentation.account_fragment.myPointFragment

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.domain.models.productResp.Product
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.PointsTransactionsItem
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.UserPointData
import com.malka.androidappp.newPhase.presentation.account_fragment.AccountViewModel
import com.malka.androidappp.newPhase.presentation.addProduct.AccountObject
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.fragment_my_points_fragment.*
import kotlinx.android.synthetic.main.fragment_my_points_fragment.etAmount
import kotlinx.android.synthetic.main.fragment_my_points_fragment.swipeRefresh
import kotlinx.android.synthetic.main.fragment_my_points_fragment.textView
import kotlinx.android.synthetic.main.fragment_my_wallet_fragment.*
import kotlinx.android.synthetic.main.item_wallet_recent_operations.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyPointsFragment : Fragment(R.layout.fragment_my_points_fragment),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var recentOperationsPointsAdapter: RecentOperationsPointsAdapter
    lateinit var pointsTransactionslist: ArrayList<PointsTransactionsItem>
    private lateinit var accountViewModel: AccountViewModel
    var userPointData: UserPointData? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.setPaintFlags(textView.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        initView()
        setViewClickListeners()
        setRecentOperationAdapter()
        setUpViewModel()
        if (AccountObject.userPointData != null) {
            userPointData = AccountObject.userPointData
            setData(userPointData!!)
        } else {
            accountViewModel.getWalletDetailsInWallet()
        }
    }

    private fun setRecentOperationAdapter() {
        pointsTransactionslist = ArrayList()
        recentOperationsPointsAdapter = RecentOperationsPointsAdapter(pointsTransactionslist)
        rvUserPointRecentOperation.apply {
            adapter = recentOperationsPointsAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = false
        }
    }

    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        accountViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }

        }
        accountViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                HelpFunctions.ShowLongToast(it.message!!, requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }


        accountViewModel.userPointsDetailsObserver.observe(this) { userPointsResp ->
            if (userPointsResp.status_code == 200) {
                etAmount.text = null
                userPointsResp.userPointData?.let { setData(it) }
            } else {
                if (userPointsResp.message != null) {
                    HelpFunctions.ShowLongToast(userPointsResp.message, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
                }
            }
        }

        accountViewModel.convertMoneyToPointObserver.observe(this){convertMoneyToPoint->
            if(convertMoneyToPoint.status_code==200){
                onRefresh()
                tvAmount.text=null
            }else{
                if (convertMoneyToPoint.message != null) {
                    HelpFunctions.ShowLongToast(convertMoneyToPoint.message!!, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
                }
            }
        }
    }

    private fun setData(userPointData: UserPointData) {
        tvTotalBalnce.text=userPointData.pointsBalance.toString()
        tvRefrerCode.text = userPointData.newInvitationCode ?: ""
        userPointData.pointsTransactionslist?.let {
            pointsTransactionslist.clear()
            pointsTransactionslist.addAll(it)
            recentOperationsPointsAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        toolbar_title.text = getString(R.string.my_points)
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)

    }


    private fun setViewClickListeners() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btnConvertion.setOnClickListener {
            if (etAmount.text.toString().trim() == "") {
                etAmount.error = getString(R.string.enterAmount)
            } else {
                accountViewModel.convertMountToPoints(
                    etAmount.text.toString().trim()
                )
            }
        }

    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
        accountViewModel.getUserPointDetailsInWallet()
    }

}