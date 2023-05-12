package com.malka.androidappp.newPhase.presentation.account_fragment.myOrderFragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.domain.models.orderListResp.OrderItem
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.presentation.myOrderDetails.MyOrderDetailsActivity
import com.malka.androidappp.newPhase.presentation.account_fragment.myOrderFragment.adapter.MyOrdersAdapter
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.fragment_my_orders.progressBar
import kotlinx.android.synthetic.main.fragment_my_orders.progressBarMore
import kotlinx.android.synthetic.main.fragment_my_orders.swipe_to_refresh
import kotlinx.android.synthetic.main.fragment_my_orders.tvError
import kotlinx.android.synthetic.main.toolbar_main.*


class MyOrdersFragment : Fragment(R.layout.fragment_my_orders),
    SwipeRefreshLayout.OnRefreshListener {
    //====
    lateinit var currentOrdersList: ArrayList<OrderItem>
    lateinit var currentOrderAdapter: MyOrdersAdapter
    lateinit var currentOrderLayOutManager: GridLayoutManager
    lateinit var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener

    //  tap id 1 ,2,3
    private var tapId: Int = 1
    lateinit var myOrdersViewModel: MyOrdersViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()
        setupViewModel()
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setUpCurrentOrderAdapter()
        //  myRequestsItemApi()
        onRefresh()
    }

    private fun setupViewModel() {
        myOrdersViewModel = ViewModelProvider(this).get(MyOrdersViewModel::class.java)
        myOrdersViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        myOrdersViewModel.isloadingMore.observe(this) {
            if (it)
                progressBarMore.show()
            else
                progressBarMore.hide()
        }
        myOrdersViewModel.isNetworkFail.observe(this) {
            if (it) {
                showApiError(getString(R.string.connectionError))
            } else {
                showApiError(getString(R.string.serverError))
            }

        }
        myOrdersViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showApiError(it.message!!)
            } else {
                showApiError(getString(R.string.serverError))
            }

        }
        myOrdersViewModel.errorResponseObserverProductToFav.observe(viewLifecycleOwner) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message!!,
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
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
        current_recycler.apply {
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
        current_recycler.addOnScrollListener(endlessRecyclerViewScrollListener)
    }

    private fun goToMyOrderDetails(orderId: Int, orderItem: OrderItem) {
        startActivity(Intent(requireActivity(), MyOrderDetailsActivity::class.java).apply {
            putExtra(ConstantObjects.orderItemKey, orderItem)
            putExtra(ConstantObjects.orderNumberKey, orderId)
            putExtra(ConstantObjects.orderTypeKey, tapId)
        })
    }
//    private fun goToOrderDetailsRequestedFromMe(orderId: Int, orderItem: OrderItem) {
//        startActivity(Intent(requireActivity(), MyOrderDetailsRequestedFromMeActivity::class.java).apply {
//            putExtra(ConstantObjects.orderItemKey,orderItem)
//            putExtra(ConstantObjects.orderNumberKey,orderId)
//            putExtra(ConstantObjects.orderTypeKey,tapId)
//        })
//    }

    private fun showApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    private fun initView() {
        toolbar_title.text = getString(R.string.my_orders)
    }


    private fun setListenser() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btnCurrent.setOnClickListener {
            tapId = 1
            btnCurrent.setBackgroundResource(R.drawable.round_btn)
            btnCurrent.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            btnReceived.setBackgroundResource(R.drawable.edittext_bg)
            btnReceived.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            onRefresh()
        }
        btnReceived.setOnClickListener {
            tapId = 2
            btnCurrent.setBackgroundResource(R.drawable.edittext_bg)
            btnCurrent.setTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
            btnReceived.setBackgroundResource(R.drawable.round_btn)
            btnReceived.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            onRefresh()
        }

    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        currentOrdersList.clear()
        currentOrderAdapter.notifyDataSetChanged()
        tvError.hide()
        endlessRecyclerViewScrollListener.resetState()
        if (tapId == 1) {
            currentOrderAdapter.currentOrder = true
            myOrdersViewModel.getCurrentOrderOrders(1, ConstantObjects.logged_userid)
        } else if (tapId == 2) {
            currentOrderAdapter.currentOrder = false
            myOrdersViewModel.getFinishOOrders(1, ConstantObjects.logged_userid)
        }
    }


}