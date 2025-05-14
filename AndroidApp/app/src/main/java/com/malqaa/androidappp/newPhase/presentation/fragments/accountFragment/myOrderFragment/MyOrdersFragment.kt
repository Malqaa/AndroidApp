package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentMyOrdersBinding
import com.malqaa.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malqaa.androidappp.newPhase.presentation.activities.cartActivity.activity2.AddressPaymentActivity
import com.malqaa.androidappp.newPhase.presentation.activities.myOrderDetails.MyOrderDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.myOrderFragment.adapter.MyOrdersAdapter
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show


class MyOrdersFragment : Fragment(R.layout.fragment_my_orders),
    SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentMyOrdersBinding? = null
    private val binding get() = _binding!!

    lateinit var currentOrdersList: ArrayList<OrderItem>
    private lateinit var currentOrderAdapter: MyOrdersAdapter
    lateinit var currentOrderLayOutManager: GridLayoutManager
    private lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    private var tapId: Int = 1
    lateinit var myOrdersViewModel: MyOrdersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyOrdersBinding.bind(view)  // Initialize binding
        initView()
        setListener()
        setupViewModel()

        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)

        setUpCurrentOrderAdapter()
        onRefresh()
    }

    private fun setupViewModel() {
        myOrdersViewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        myOrdersViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) binding.progressBar.show() else binding.progressBar.hide()
        }
        myOrdersViewModel.isloadingMore.observe(viewLifecycleOwner) {
            if (it) binding.progressBarMore.show() else binding.progressBarMore.hide()
        }
        myOrdersViewModel.isNetworkFail.observe(viewLifecycleOwner) {
            showApiError(if (it) getString(R.string.connectionError) else getString(R.string.serverError))
        }
        myOrdersViewModel.errorResponseObserver.observe(viewLifecycleOwner) {
            if (it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                showApiError(it.message ?: getString(R.string.serverError))
            }
        }
        myOrdersViewModel.errorResponseObserverProductToFav.observe(viewLifecycleOwner) {
            if (it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), requireActivity())
            } else {
                HelpFunctions.ShowLongToast(
                    it.message ?: getString(R.string.serverError),
                    requireActivity()
                )
            }
        }
        myOrdersViewModel.currentOrderRespObserver.observe(viewLifecycleOwner) { orderListResp ->
            if (orderListResp.status_code == 200) {
                orderListResp.orderList?.let {
                    currentOrdersList.addAll(it)
                    currentOrderAdapter.notifyDataSetChanged()
                    if (currentOrdersList.isEmpty()) {
                        showApiError(getString(R.string.noOrdersFound))
                    }
                }
            }
        }
    }

    private fun setUpCurrentOrderAdapter() {
        currentOrdersList = ArrayList()
        currentOrderLayOutManager = GridLayoutManager(requireActivity(), 1)
        currentOrderAdapter = MyOrdersAdapter(currentOrdersList,
            object : MyOrdersAdapter.SetOnClickListeners {
                override fun onOrderSelected(position: Int) {
                    goToMyOrderDetails(
                        currentOrdersList[position].orderMasterId,
                        currentOrdersList[position]
                    )
                }
            })
        binding.currentRecycler.apply {
            adapter = currentOrderAdapter
            layoutManager = currentOrderLayOutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(currentOrderLayOutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    if (tapId == 1) {
                        myOrdersViewModel.getCurrentOrderOrders(page, ConstantObjects.logged_userid)
                    } else if (tapId == 2) {
                        myOrdersViewModel.getFinishOOrders(page, ConstantObjects.logged_userid)
                    }
                }
            }
        binding.currentRecycler.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun goToMyOrderDetails(orderId: Int, orderItem: OrderItem) {
        val intent =
            if (orderItem.orderStatus == ConstantObjects.WaitingForPayment && orderItem.requestType != ConstantObjects.Fixed_Price) {
                Intent(requireContext(), AddressPaymentActivity::class.java).apply {
                    putExtra("flagTypeSale", false)
                    putExtra("fromNegotiation", true)
                    putExtra("comeFromMyOrders", true) // or false, based on your condition
                    putExtra(ConstantObjects.orderNumberKey, orderId)
                }
            } else {
                Intent(requireActivity(), MyOrderDetailsActivity::class.java).apply {
                    putExtra(ConstantObjects.orderItemKey, orderItem)
                    putExtra(ConstantObjects.orderNumberKey, orderId)
                    putExtra(ConstantObjects.orderTypeKey, tapId)
                    putExtra("flagTypeSale", true)
                }
            }
        startActivity(intent)
    }

    private fun showApiError(message: String) {
        binding.tvError.show()
        binding.tvError.text = message
    }

    private fun initView() {
        binding.toolbarMain.toolbarTitle.text = getString(R.string.my_orders)
    }

    private fun setListener() {
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnCurrent.setOnClickListener {
            tapId = 1
            binding.btnCurrent.setBackgroundResource(R.drawable.round_btn)
            binding.btnCurrent.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            binding.btnReceived.setBackgroundResource(R.drawable.round_btn_white)
            binding.btnReceived.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.gray
                )
            )
            onRefresh()
        }
        binding.btnReceived.setOnClickListener {
            tapId = 2
            binding.btnCurrent.setBackgroundResource(R.drawable.round_btn_white)
            binding.btnCurrent.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            binding.btnReceived.setBackgroundResource(R.drawable.round_btn)
            binding.btnReceived.setTextColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.white
                )
            )
            onRefresh()
        }
    }

    override fun onRefresh() {
        binding.swipeToRefresh.isRefreshing = false
        currentOrdersList.clear()
        currentOrderAdapter.notifyDataSetChanged()
        binding.tvError.hide()
        endlessRecyclerViewScrollListener.resetState()
        if (tapId == 1) {
            currentOrderAdapter.currentOrder = true
            myOrdersViewModel.getCurrentOrderOrders(1, ConstantObjects.logged_userid)
        } else if (tapId == 2) {
            currentOrderAdapter.currentOrder = false
            myOrdersViewModel.getFinishOOrders(1, ConstantObjects.logged_userid)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        myOrdersViewModel.closeAllCall()
        _binding = null  // Clear the binding reference
    }
}
