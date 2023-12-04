package com.malka.androidappp.newPhase.presentation.accountFragment.myWalletFragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
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

    private lateinit var recentOperationsAdapter: RecentOperationsAdapter
    private lateinit var walletTransactionsList: ArrayList<WalletTransactionsDetails>
    private lateinit var accountViewModel: AccountViewModel
    private var transactionType: String = ConstantObjects.transactionType_Out
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setRecentOperationAdapter()
        setViewsClickListeners()
        setUpViewModel()
        accountViewModel.getWalletDetailsInWallet()
    }

    private fun setData(walletDetails: WalletDetails) {
        tvTotalBalance.text = walletDetails.walletBalance.toString()
        walletDetails.walletTransactionslist?.let {
            walletTransactionsList.clear()
            walletTransactionsList.addAll(it)
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
        walletTransactionsList = ArrayList()
        recentOperationsAdapter = RecentOperationsAdapter(walletTransactionsList)
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


            recharge_the_balance.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            balance_withdraw.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )


            btnAddTranasction.text= getString(R.string.balance_withdrawal)
            balance_withdraw.setTextColor(Color.parseColor("#FFFFFF"))
            recharge_the_balance.setTextColor(Color.parseColor("#45495E"))


        }
        recharge_the_balance.setOnClickListener {
            transactionType = ConstantObjects.transactionType_In
            choose_account.hide()

            balance_withdraw.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            recharge_the_balance.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )
            btnAddTranasction.text= getString(R.string.recharge_the_balance)
            recharge_the_balance.setTextColor(Color.parseColor("#FFFFFF"))
            balance_withdraw.setTextColor(Color.parseColor("#45495E"))
        }
        btnAddTranasction.setOnClickListener {
            if (etAmount.text.toString().trim() == "") {
                etAmount.error = getString(R.string.enterAmount)
            } else {
                if (transactionType ==ConstantObjects.transactionType_Out){
                    if(etAmount.text.toString().toFloat() > tvTotalBalance.text.toString().toFloat()){

                        Toast.makeText(
                            requireContext(),
                            getString(R.string.cannotWithdrawal),
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        accountViewModel.addWalletTransaction(
                            "3",
                            transactionType,
                            etAmount.text.toString().trim()
                        )
                    }
                }else{
                    accountViewModel.addWalletTransaction(
                        "1",
                        transactionType,
                        etAmount.text.toString().trim()
                    )
                }


            }
        }


    }

    override fun onRefresh() {
        swipeRefresh.isRefreshing = false
        accountViewModel.getWalletDetailsInWallet()
    }

    override fun onDestroy() {
        super.onDestroy()
        accountViewModel.closeAllCall()
    }
}