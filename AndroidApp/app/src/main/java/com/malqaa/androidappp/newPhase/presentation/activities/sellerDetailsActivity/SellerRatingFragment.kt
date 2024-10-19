package com.malqaa.androidappp.newPhase.presentation.activities.sellerDetailsActivity

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Filter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FragmentSellerRatingBinding
import com.malqaa.androidappp.databinding.ReviewDialogLayoutBinding
import com.malqaa.androidappp.databinding.ReviewFilterDesignBinding
import com.malqaa.androidappp.newPhase.domain.models.sellerRateListResp.SellerRateItem
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.Selection
import com.malqaa.androidappp.newPhase.presentation.activities.productsSellerInfoActivity.adapter.SellerRateAdapter
import com.malqaa.androidappp.newPhase.utils.EndlessRecyclerViewScrollListener
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

class SellerRatingFragment : Fragment(R.layout.fragment_seller_rating),
    SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: FragmentSellerRatingBinding
    private val sampleOption: ArrayList<Selection> = ArrayList()
    private val typeOption: ArrayList<Selection> = ArrayList()
    private var selection: Selection? = null
    private var sellerRateAdapter: SellerRateAdapter? = null
    private var sellerRateList: ArrayList<SellerRateItem>? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var sellerRatingViewModel: SellerRatingViewModel? = null
    private var endlessRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize view binding
        binding = FragmentSellerRatingBinding.bind(view)

        sellerRatingViewModel = ViewModelProvider(this).get(SellerRatingViewModel::class.java)
        initView()
        setListener()

        setSellerRateAdapter()
        onRefresh()
        sampleOption.apply {
            add(Selection(getString(R.string.all), id = 4))
            add(Selection(getString(R.string.positive), id = 3))
            add(Selection(getString(R.string.negatives), id = 1))
            add(Selection(getString(R.string.neutrals), id = 2))

        }

        typeOption.apply {
            add(Selection(getString(R.string.all)))
            add(Selection(getString(R.string.reviews_as_a_seller)))
            add(Selection(getString(R.string.reviews_as_a_buyer)))
        }
        sellerRatingViewModel?.sellerRateListObservable?.observe(this) { sellerRateListResp ->
            if (sellerRateListResp.status_code == 200) {

                when {
                    // rate all
                    typeOption[0].isSelected -> {
                        sellerRateListResp.SellerRateObject?.let {
                            it.rateBuyerListDto?.let { buyerList -> sellerRateList?.addAll(buyerList) }
                            it.rateSellerListDto?.let { sellerList ->
                                sellerRateList?.addAll(
                                    sellerList
                                )
                            }
                        }
                    }
                    // reviews as a seller
                    typeOption[1].isSelected -> {
                        sellerRateListResp.SellerRateObject?.let {
                            it.rateBuyerListDto?.let { it1 -> sellerRateList?.addAll(it1) }
                        }
                    }
                    // reviews as a buyer
                    else -> {
                        sellerRateListResp.SellerRateObject?.let {
                            it.rateSellerListDto?.let { it1 -> sellerRateList?.addAll(it1) }
                        }
                    }
                }

                Log.i("test #1", "sellerRateList size: ${sellerRateList?.size}")

                sellerRateAdapter?.notifyDataSetChanged()
                if (sellerRateList?.isEmpty() == true) {
                    binding.tvError.show()
                } else {
                    binding.tvError.hide()
                }
            } else {
                if (sellerRateList?.isEmpty() == true) {
                    if (sellerRateListResp.message != null) {
                        HelpFunctions.ShowLongToast((sellerRateListResp.message), requireContext())
                    } else {
                        HelpFunctions.ShowLongToast(
                            getString(R.string.serverError),
                            requireContext()
                        )
                    }
                } else {
                    binding.tvError.hide()
                }
            }
        }

    }

    private fun setSellerRateAdapter() {

        sellerRateList = ArrayList()
        sellerRateAdapter = SellerRateAdapter(requireContext(), sellerRateList ?: arrayListOf())
        linearLayoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvPakat.apply {
            adapter = sellerRateAdapter
            layoutManager = linearLayoutManager
        }
        endlessRecyclerViewScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager!!) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    sellerRatingViewModel?.getSellerRates(
                        page, null
                    )


                }
            }
        binding.rvPakat.addOnScrollListener(endlessRecyclerViewScrollListener!!)
    }

    override fun onRefresh() {
        endlessRecyclerViewScrollListener?.resetState()
        binding.swipeToRefresh.isRefreshing = false
        sellerRateList?.clear()
        sellerRateAdapter?.notifyDataSetChanged()
        binding.tvError.hide()
        if (typeOption.size != 0) {
            when {
                typeOption[0].isSelected -> {
                    sellerRatingViewModel?.getBuyerRates(1, null)
                    sellerRatingViewModel?.getSellerRates(1, null)
                }

                typeOption[1].isSelected -> {
                    sellerRatingViewModel?.getBuyerRates(1, null)
                }

                else -> {
                    sellerRatingViewModel?.getSellerRates(1, null)
                }
            }
        } else {
            sellerRatingViewModel?.getSellerRates(1, null)
        }


    }

    private fun initView() {
        binding.swipeToRefresh.setColorSchemeResources(R.color.colorPrimaryDark)
        binding.swipeToRefresh.setOnRefreshListener(this)
        binding.toolbarMain.toolbarTitle.text = getString(R.string.rates)
    }


    private fun setListener() {
        // Access views using binding
        binding.toolbarMain.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.reviewType1.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext()).create()
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val viewBinding = ReviewDialogLayoutBinding.inflate(layoutInflater)
            builder.setView(viewBinding.root)
            binding.bottomBtns.hide()

            fun reviewAdaptor(list: List<Selection>, rcv: RecyclerView) {
                rcv.adapter = object : GenericListAdapter<Selection>(
                    R.layout.review_filter_design,
                    bind = { element, holder, itemCount, position ->
                        // Use ViewBinding for the item layout
                        val itemBinding = ReviewFilterDesignBinding.bind(holder.view)

                        // Now use the binding object to access views
                        element.run {
                            itemBinding.checkbox.isChecked = isSelected
                            itemBinding.reviewFilterLayout.isSelected = isSelected
                            itemBinding.filterTv.text = name

                            itemBinding.reviewFilterLayout.setOnClickListener {
                                list.forEach { it.isSelected = false }
                                list[position].isSelected = true
                                rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                selection = element
                            }

                            itemBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    itemBinding.reviewFilterLayout.performClick()
                                }
                            }
                        }
                    }
                ) {
                    override fun getFilter(): Filter {
                        TODO("Not yet implemented")
                    }
                }.apply {
                    submitList(list)
                }
            }

            builder.setCanceledOnTouchOutside(true)
            builder.show()
            builder.setOnCancelListener {
                binding.bottomBtns.show()
            }

            viewBinding.filterApplication.setOnClickListener {
                sellerRateList?.clear()
                when {
                    typeOption[0].isSelected -> {
                        val obj = sampleOption.find { it.isSelected }!!
                        if (obj.id == 4) {
                            sellerRatingViewModel?.getBuyerRates(1, rate = null)
                            sellerRatingViewModel?.getSellerRates(1, rate = null)
                        } else {
                            sellerRatingViewModel?.getBuyerRates(1, obj.id)
                            sellerRatingViewModel?.getSellerRates(1, obj.id)
                        }
                    }

                    typeOption[1].isSelected -> {
                        val obj = sampleOption.find { it.isSelected }!!
                        if (obj.id == 4) {
                            sellerRatingViewModel?.getBuyerRates(1, rate = null)
                        } else {
                            sellerRatingViewModel?.getBuyerRates(1, obj.id)
                        }
                    }

                    else -> {
                        val obj = sampleOption.find { it.isSelected }!!
                        if (obj.id == 4) {
                            sellerRatingViewModel?.getSellerRates(1, rate = null)
                        } else {
                            sellerRatingViewModel?.getSellerRates(1, obj.id)
                        }
                    }
                }

                builder.dismiss()
                binding.bottomBtns.show()
            }

            viewBinding.resetTv.setOnClickListener {
                builder.dismiss()
                binding.bottomBtns.show()
            }

            viewBinding.reviewTypeBtn.setOnClickListener {
                builder.dismiss()
                binding.reviewType2.performClick()
            }

            reviewAdaptor(sampleOption, viewBinding.reviewType1Rcv)
        }

        binding.reviewType2.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext()).create()
            builder.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val viewBinding = ReviewDialogLayoutBinding.inflate(layoutInflater)
            builder.setView(viewBinding.root)
            binding.bottomBtns.hide()

            fun reviewAdaptor(list: List<Selection>, rcv: RecyclerView) {
                rcv.adapter = object : GenericListAdapter<Selection>(
                    R.layout.review_filter_design,
                    bind = { element, holder, itemCount, position ->
                        val itemBinding = ReviewFilterDesignBinding.bind(holder.view)

                        element.run {
                            itemBinding.checkbox.isChecked = isSelected
                            itemBinding.reviewFilterLayout.isSelected = isSelected
                            itemBinding.filterTv.text = name

                            itemBinding.reviewFilterLayout.setOnClickListener {
                                list.forEach { it.isSelected = false }
                                element.isSelected = true
                                rcv.post { rcv.adapter?.notifyDataSetChanged() }
                                selection = element
                            }

                            itemBinding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    itemBinding.reviewFilterLayout.performClick()
                                }
                            }
                        }
                    }
                ) {
                    override fun getFilter(): Filter {
                        TODO("Not yet implemented")
                    }
                }.apply {
                    submitList(list)
                }
            }

            builder.setCanceledOnTouchOutside(true)
            builder.show()
            builder.setOnCancelListener {
                binding.bottomBtns.show()
            }

            viewBinding.filterApplication.setOnClickListener {
                sellerRateList?.clear()
                when {
                    typeOption[0].isSelected -> {
                        sellerRatingViewModel?.getSellerRates(1, null)
                        sellerRatingViewModel?.getBuyerRates(1, null)
                    }

                    typeOption[1].isSelected -> {
                        sellerRatingViewModel?.getSellerRates(1, null)
                    }

                    else -> {
                        sellerRatingViewModel?.getBuyerRates(1, null)
                    }
                }

                onRefresh()
                builder.dismiss()
                binding.bottomBtns.show()
            }

            viewBinding.resetTv.setOnClickListener {
                builder.dismiss()
                binding.bottomBtns.show()
            }

            viewBinding.reviewBtn.setOnClickListener {
                builder.dismiss()
                binding.reviewType1.performClick()
            }

            reviewAdaptor(typeOption, viewBinding.reviewsType2Rcv)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        selection = null
        sellerRateAdapter = null
        sellerRateList = null
        sellerRatingViewModel = null
        linearLayoutManager = null
        endlessRecyclerViewScrollListener = null
        sellerRatingViewModel?.closeAllCall()

    }
}