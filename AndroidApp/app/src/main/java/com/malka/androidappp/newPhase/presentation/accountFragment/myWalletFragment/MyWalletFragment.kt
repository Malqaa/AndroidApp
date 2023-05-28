package com.malka.androidappp.newPhase.presentation.accountFragment.myWalletFragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetails
import com.malka.androidappp.newPhase.domain.models.walletDetailsResp.WalletTransactionsDetails
import com.malka.androidappp.newPhase.presentation.accountFragment.AccountViewModel
import com.malka.androidappp.newPhase.presentation.addProduct.AccountObject
import kotlinx.android.synthetic.main.fragment_my_wallet_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*


class MyWalletFragment : Fragment(R.layout.fragment_my_wallet_fragment),
    SwipeRefreshLayout.OnRefreshListener {

    lateinit var recentOperationsAdapter: RecentOperationsAdapter
    lateinit var walletTransactionslist: ArrayList<WalletTransactionsDetails>
    private lateinit var accountViewModel: AccountViewModel
    var walletDetails: WalletDetails? = null;

    var transactionType: String = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setRecentOperationAdapter()
        setViewsClickListeners()
        setUpViewModel()
        if (AccountObject.walletDetails != null) {
            walletDetails = AccountObject.walletDetails
            setData(walletDetails!!)
        } else {
            accountViewModel.getWalletDetailsInWallet()
        }

    }

    private fun setData(walletDetails: WalletDetails) {
        tvTotalBalance.text = walletDetails.walletBalance.toString()
        walletDetails.walletTransactionslist?.let {
            walletTransactionslist.clear()
            walletTransactionslist.addAll(it)
            recentOperationsAdapter.notifyDataSetChanged()
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
        accountViewModel.walletDetailsObserver.observe(this) { walletDetailsResp ->
            if (walletDetailsResp.status_code == 200) {
                AccountObject.walletDetails = walletDetailsResp.walletDetails
                walletDetailsResp.walletDetails?.let {
                    setData(it)
                }
            }
        }
        accountViewModel.addWalletTransactionObserver.observe(this) { addWalletTransaction ->
            if (addWalletTransaction.status_code == 200) {
                etAmount.text=null
                onRefresh()
            }else{
                if (addWalletTransaction.message != null) {
                    HelpFunctions.ShowLongToast(addWalletTransaction.message, requireActivity())
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
                }
            }
        }
    }

    private fun setRecentOperationAdapter() {
        walletTransactionslist = ArrayList()
        recentOperationsAdapter = RecentOperationsAdapter(walletTransactionslist)
        rvWalletRecentOperation.apply {
            adapter = recentOperationsAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            isNestedScrollingEnabled = false
        }
    }

    private fun initView() {
        toolbar_title.text = getString(R.string.my_wallet)
        swipeRefresh.setOnRefreshListener(this)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)

    }


    private fun setViewsClickListeners() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        balance_withdraw.setOnClickListener {
            transactionType = ConstantObjects.transactionType_Out
            choose_account.show()


            recharge_the_balance.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            balance_withdraw.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )


            balance_withdraw.setTextColor(Color.parseColor("#FFFFFF"));
            recharge_the_balance.setTextColor(Color.parseColor("#45495E"));


        }
        recharge_the_balance.setOnClickListener {
            transactionType = ConstantObjects.transactionType_In
            choose_account.hide()

            balance_withdraw.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.edittext_bg
                )
            )
            recharge_the_balance.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.round_btn
                )
            )

            recharge_the_balance.setTextColor(Color.parseColor("#FFFFFF"));
            balance_withdraw.setTextColor(Color.parseColor("#45495E"));
        }
        btnAddTranasction.setOnClickListener {
            if (etAmount.text.toString().trim() == "") {
                etAmount.error = getString(R.string.enterAmount)
            } else {
                accountViewModel.addWalletTransaction(
                    transactionType,
                    etAmount.text.toString().trim()
                )
            }
        }
    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
        accountViewModel.getWalletDetailsInWallet()
    }


}