package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myPointFragment

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentMyPointsFragmentBinding
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AccountObject
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.PointsTransactionsItem
import com.malqaa.androidappp.newPhase.domain.models.userPointsDataResp.UserPointData
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.AccountViewModel
import com.malqaa.androidappp.newPhase.utils.Extension.shared
import com.malqaa.androidappp.newPhase.utils.HelpFunctions


class MyPointsFragment : Fragment(R.layout.fragment_my_points_fragment),
    SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentMyPointsFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var recentOperationsPointsAdapter: RecentOperationsPointsAdapter
    lateinit var pointsTransactionsList: ArrayList<PointsTransactionsItem>
    private lateinit var accountViewModel: AccountViewModel
    var userPointData: UserPointData? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyPointsFragmentBinding.bind(view) // Initialize View Binding

        binding.textView.paintFlags = binding.textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

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

        binding.imgShare.setOnClickListener {
            requireActivity().shared("${getString(R.string.msgShowCode)} (${binding.tvRefrerCode.text.toString()})")
        }
    }

    private fun setRecentOperationAdapter() {
        pointsTransactionsList = ArrayList()
        recentOperationsPointsAdapter = RecentOperationsPointsAdapter(pointsTransactionsList)
        binding.rvUserPointRecentOperation.apply {
            adapter = recentOperationsPointsAdapter
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            isNestedScrollingEnabled = false
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
        accountViewModel.userPointsDetailsObserver.observe(viewLifecycleOwner) { userPointsResp ->
            if (userPointsResp.status_code == 200) {
                binding.etAmount.text = null
                userPointsResp.userPointData?.let { setData(it) }
            } else {
                HelpFunctions.ShowLongToast(
                    userPointsResp.message ?: getString(R.string.serverError), requireActivity()
                )
            }
        }
    }

    private fun setData(userPointData: UserPointData) {
        binding.tvTotalBalnce.text = userPointData.pointsBalance.toString()
        binding.tvRefrerCode.text = userPointData.newInvitationCode ?: ""
        userPointData.pointsTransactionslist?.let {
            pointsTransactionsList.clear()
            pointsTransactionsList.addAll(it)
            recentOperationsPointsAdapter.notifyDataSetChanged()
        }
    }

    private fun initView() {
        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_points)
        binding.swipeRefresh.setOnRefreshListener(this)
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
    }

    private fun setViewClickListeners() {
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnConvertion.setOnClickListener {
            val amount = binding.etAmount.text.toString().trim()
            if (amount.isEmpty()) {
                binding.etAmount.error = getString(R.string.enterAmountPoint)
            } else if (amount.toLong() <= binding.tvTotalBalnce.text.toString().toLong()) {
                accountViewModel.convertMountToPoints(amount)
            } else {
                binding.etAmount.error = getString(R.string.enterAmountPointLess)
            }
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        accountViewModel.getUserPointDetailsInWallet()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
        accountViewModel.closeAllCall()
    }
}
