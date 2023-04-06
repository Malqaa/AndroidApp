package com.malka.androidappp.newPhase.presentation.myProducts

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions

import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.productResp.Product

import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.adapterShared.ProductHorizontalAdapter
import com.malka.androidappp.newPhase.presentation.adapterShared.SetOnProductItemListeners
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.ProductDetailsActivity

import kotlinx.android.synthetic.main.fragment_sold_business.*
import kotlinx.android.synthetic.main.fragment_sold_business.progressBar
import kotlinx.android.synthetic.main.fragment_sold_business.swipe_to_refresh
import kotlinx.android.synthetic.main.fragment_sold_business.tvError

import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyProductsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener,
    SetOnProductItemListeners {

    private lateinit var  myProductsViewModel: MyProductViewModel
    private lateinit var myPorductForSaleListAdapter: ProductHorizontalAdapter
    private lateinit var productList:ArrayList<Product>
    private var lastUpdateIndex=-1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sold_business, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_title.text = getString(R.string.MyProducts)
        setupViewModel()
        swipe_to_refresh.setColorSchemeResources(R.color.colorPrimaryDark)
        swipe_to_refresh.setOnRefreshListener(this)
        setViewClickListeners()
        setAdapterForSaleAdapter()

        sold_out_rcv.hide()
        did_not_sale_rcv.hide()
        for_sale_recycler.show()
        onRefresh()
    }

    private fun setAdapterForSaleAdapter() {
        productList= ArrayList()
        myPorductForSaleListAdapter=ProductHorizontalAdapter(productList,this,0,false)
        for_sale_recycler.apply {
            adapter=myPorductForSaleListAdapter
            layoutManager= GridLayoutManager(requireActivity(),2)
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
    private fun setupViewModel() {
        myProductsViewModel = ViewModelProvider(this).get(MyProductViewModel::class.java)
        myProductsViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        myProductsViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        myProductsViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        myProductsViewModel.forSaleProductRespObserver.observe(this){productListResp->
            if(productListResp.status_code==200){
                if(productListResp.productList!=null&&productListResp.productList.isNotEmpty()){
                    productList.clear()
                    productList.addAll(productListResp.productList)
                    myPorductForSaleListAdapter.notifyDataSetChanged()

                }else{
                    showProductApiError(getString(R.string.noProductsAdded))
                }
            }
        }
        myProductsViewModel.isNetworkFailProductToFav.observe(viewLifecycleOwner) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        }
        myProductsViewModel.errorResponseObserverProductToFav.observe(viewLifecycleOwner) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    requireActivity()
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    requireActivity()
                )
            }

        }
        myProductsViewModel.addProductToFavObserver.observe(viewLifecycleOwner) {
            if (it.status_code == 200) {
                if(lastUpdateIndex<productList.size){
                    productList[lastUpdateIndex].isFavourite=!productList[lastUpdateIndex].isFavourite
                    myPorductForSaleListAdapter.notifyItemChanged(lastUpdateIndex)
                    myPorductForSaleListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onRefresh() {
        swipe_to_refresh.isRefreshing = false
        tvError.hide()
        productList.clear()
        myPorductForSaleListAdapter.notifyDataSetChanged()
        myProductsViewModel.getForSaleProduct()
    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }

    override fun onProductSelect(position: Int, productID: Int, categoryID: Int) {
        startActivity(
            Intent(requireActivity(), ProductDetailsActivity::class.java).apply {
                putExtra(ConstantObjects.productIdKey, productID)
                putExtra(ConstantObjects.isMyProduct, true)
            })
    }

    override fun onAddProductToFav(position: Int, productID: Int, categoryID: Int) {
        if (HelpFunctions.isUserLoggedIn()) {
            lastUpdateIndex = position
            myProductsViewModel.addProductToFav(productID)

        } else {
            requireActivity().startActivity(
                Intent(
                    context,
                    SignInActivity::class.java
                ).apply {})
        }
    }


//    fun notUsed(){
//        CommonAPI().getSoldItemsApi(ConstantObjects.logged_userid, requireContext()) {
//            sold_business_recycler.adapter = GenericProductAdapter(it.sellingitems, requireContext())
//            did_not_sale_rcv.adapter = GenericProductAdapter(it.unsolditems, requireContext())
////            sold_out_rcv.adapter = GenericOrderAdapter(it.solditems, requireContext())
//
//            for_sale.setOnClickListener {
//
//
//                did_not_Sell.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                sold_out.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                for_sale.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.round_btn
//                    )
//                )
//
//
//                for_sale.setTextColor(Color.parseColor("#FFFFFF"));
//                did_not_Sell.setTextColor(Color.parseColor("#45495E"));
//                sold_out.setTextColor(Color.parseColor("#45495E"));
//                did_not_sale_rcv.hide()
//                sold_out_rcv.hide()
//                sold_business_recycler.show()
//
//            }
//            for_sale.performClick()
//            did_not_Sell.setOnClickListener {
//
//
//                did_not_Sell.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.round_btn
//                    )
//                )
//                sold_out.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                for_sale.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//
//                did_not_Sell.setTextColor(Color.parseColor("#FFFFFF"));
//                sold_out.setTextColor(Color.parseColor("#45495E"));
//                for_sale.setTextColor(Color.parseColor("#45495E"));
//
//                sold_business_recycler.hide()
//                sold_out_rcv.hide()
//                did_not_sale_rcv.show()
//
//            }
//            sold_out.setOnClickListener {
//
//
//                did_not_Sell.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//                sold_out.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.round_btn
//                    )
//                )
//                for_sale.setBackground(
//                    ContextCompat.getDrawable(
//                        requireContext(),
//                        R.drawable.edittext_bg
//                    )
//                )
//
//
//
//                sold_out.setTextColor(Color.parseColor("#FFFFFF"));
//                did_not_Sell.setTextColor(Color.parseColor("#45495E"));
//                for_sale.setTextColor(Color.parseColor("#45495E"));
//                did_not_sale_rcv.hide()
//                sold_business_recycler.hide()
//                sold_out_rcv.show()
//
//
//            }
//
//        }
//    }


}