package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myWalletFragment

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentMyWalletFragmentBinding
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AccountObject
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletDetails
import com.malqaa.androidappp.newPhase.domain.models.walletDetailsResp.WalletTransactionsDetails
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show


class MyWalletFragment : Fragment(R.layout.fragment_my_wallet_fragment),
    SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentMyWalletFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var recentOperationsAdapter: RecentOperationsAdapter
    private lateinit var walletTransactionsList: ArrayList<WalletTransactionsDetails>
    private lateinit var accountViewModel: AccountViewModel
    private var transactionType: String = ConstantObjects.transactionType_Out

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyWalletFragmentBinding.bind(view)
        initView()
        setRecentOperationAdapter()
        setViewsClickListeners()
        setUpViewModel()
        accountViewModel.getWalletDetailsInWallet()
    }

    private fun setData(walletDetails: WalletDetails) {
        binding.tvTotalBalance.text = walletDetails.walletBalance.toString()
        walletDetails.walletTransactionslist?.let {
            walletTransactionsList.clear()
            walletTransactionsList.addAll(it)
            recentOperationsAdapter.notifyDataSetChanged()
        }
    }

    private fun setUpViewModel() {
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        accountViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it)
                HelpFunctions.startProgressBar(requireActivity())
            else
                HelpFunctions.dismissProgressBar()
        }
        accountViewModel.isNetworkFail.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(getString(R.string.connectionError), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
            }
        }
        accountViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            it.message?.let { message ->
                HelpFunctions.ShowLongToast(message, requireActivity())
            } ?: HelpFunctions.ShowLongToast(getString(R.string.serverError), requireActivity())
        }
        accountViewModel.walletDetailsObserver.observe(viewLifecycleOwner) { walletDetailsResp ->
            if (walletDetailsResp.status_code == 200) {
                AccountObject.walletDetails = walletDetailsResp.walletDetails
                walletDetailsResp.walletDetails?.let { setData(it) }
            }
        }
        accountViewModel.addWalletTransactionObserver.observe(viewLifecycleOwner) { addWalletTransaction ->
            if (addWalletTransaction.status_code == 200) {
                binding.etAmount.text = null
                onRefresh()
            } else {
                HelpFunctions.ShowLongToast(
                    addWalletTransaction.message ?: getString(R.string.serverError),
                    requireActivity()
                )
            }
        }
    }

    private fun setRecentOperationAdapter() {
        walletTransactionsList = ArrayList()
        recentOperationsAdapter = RecentOperationsAdapter(walletTransactionsList)
        binding.rvWalletRecentOperation.apply {
            adapter = recentOperationsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            isNestedScrollingEnabled = false
        }
    }

    private fun initView() {
        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_wallet)
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
    }

    private fun setViewsClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.balanceWithdraw.setOnClickListener {
            transactionType = ConstantObjects.transactionType_Out
            binding.chooseAccount.show()

            binding.rechargeTheBalance.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.balanceWithdraw.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.btnAddTranasction.text = getString(R.string.balance_withdrawal)
            binding.balanceWithdraw.setTextColor(Color.parseColor("#FFFFFF"))
            binding.rechargeTheBalance.setTextColor(Color.parseColor("#45495E"))
        }

        binding.rechargeTheBalance.setOnClickListener {
            transactionType = ConstantObjects.transactionType_In
            binding.chooseAccount.hide()

            binding.balanceWithdraw.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.rechargeTheBalance.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.btnAddTranasction.text = getString(R.string.recharge_the_balance)
            binding.rechargeTheBalance.setTextColor(Color.parseColor("#FFFFFF"))
            binding.balanceWithdraw.setTextColor(Color.parseColor("#45495E"))
        }

        binding.btnAddTranasction.setOnClickListener {
            if (binding.etAmount.text.toString().trim().isEmpty()) {
                binding.etAmount.error = getString(R.string.enterAmount)
            } else {
                val amount = binding.etAmount.text.toString().toFloat()
                if (transactionType == ConstantObjects.transactionType_Out && amount > binding.tvTotalBalance.text.toString()
                        .toFloat()
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.cannotWithdrawal),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    accountViewModel.addWalletTransaction(
                        if (transactionType == ConstantObjects.transactionType_Out) "3" else "1",
                        transactionType,
                        binding.etAmount.text.toString().trim()
                    )
                }
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        accountViewModel.getWalletDetailsInWallet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
