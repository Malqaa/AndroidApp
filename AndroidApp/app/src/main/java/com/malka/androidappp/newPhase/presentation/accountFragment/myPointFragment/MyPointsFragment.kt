package com.malka.androidappp.newPhase.presentation.accountFragment.myPointFragment

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.Extension.shared
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.PointsTransactionsItem
import com.malka.androidappp.newPhase.domain.models.userPointsDataResp.UserPointData
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.malka.androidappp.newPhase.presentation.addProduct.AccountObject
import kotlinx.android.synthetic.main.fragment_my_points_fragment.*
import kotlinx.android.synthetic.main.fragment_my_points_fragment.etAmount
import kotlinx.android.synthetic.main.fragment_my_points_fragment.swipeRefresh
import kotlinx.android.synthetic.main.fragment_my_points_fragment.textView
import kotlinx.android.synthetic.main.item_wallet_recent_operations.*
import kotlinx.android.synthetic.main.toolbar_main.*


class MyPointsFragment : Fragment(R.layout.fragment_my_points_fragment),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var recentOperationsPointsAdapter: RecentOperationsPointsAdapter
    lateinit var pointsTransactionsList: ArrayList<PointsTransactionsItem>
    private lateinit var accountViewModel: AccountViewModel
    var userPointData: UserPointData? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textView.paintFlags = textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        initView()
        setViewClickListeners()
        setRecentOperationAdapter()
        setUpViewModel()
        if (AccountObject.userPointData != null) {
            userPointData = AccountObject.userPointData
            setData(userPointData!!)
        } else {
            accountViewModel.getUserPointDetailsInWallet()
        }

        imgShare.setOnClickListener {
            requireActivity().shared("${getString(R.string.msgShowCode)} (${tvRefrerCode.text.toString()})")

        }
    }

    private fun setRecentOperationAdapter() {
        pointsTransactionsList = ArrayList()
        recentOperationsPointsAdapter = RecentOperationsPointsAdapter(pointsTransactionsList)
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

        accountViewModel.convertMoneyToPointObserver.observe(this) { convertMoneyToPoint ->
            if (convertMoneyToPoint.status_code == 200) {
                onRefresh()
                tvAmount.text = null
            } else {
                if (convertMoneyToPoint.message != null) {
                    HelpFunctions.ShowLongToast(convertMoneyToPoint.message, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
                }
            }
        }
    }

    private fun setData(userPointData: UserPointData) {
        tvTotalBalnce.text = userPointData.pointsBalance.toString()
        tvRefrerCode.text = userPointData.newInvitationCode ?: ""
        userPointData.pointsTransactionslist?.let {
            pointsTransactionsList.clear()
            pointsTransactionsList.addAll(it)
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
                etAmount.error = getString(R.string.enterAmountPoint)
            } else {
                Log.i("tvTotalBalnce",tvTotalBalnce.text.toString())
                Log.i("etAmount",etAmount.text.toString())
                Log.i("result",(etAmount.text.toString().toLong() <= tvTotalBalnce.text.toString().toLong()).toString())
                if (etAmount.text.toString().toLong() <= tvTotalBalnce.text.toString().toLong())
                    accountViewModel.convertMountToPoints(
                        etAmount.text.toString().trim()
                    )
                else{

                    etAmount.error = getString(R.string.enterAmountPointLess)
                }
            }
        }

    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
        accountViewModel.getUserPointDetailsInWallet()
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }

}