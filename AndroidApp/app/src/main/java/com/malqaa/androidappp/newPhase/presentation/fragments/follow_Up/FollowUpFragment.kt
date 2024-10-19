package com.malqaa.androidappp.fragments.follow_Up

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.FavoriteCategoriesDesignBinding
import com.malqaa.androidappp.databinding.FavoriteSearchDesignBinding
import com.malqaa.androidappp.databinding.FavoriteSellerDesignBinding
import com.malqaa.androidappp.databinding.FragmentFollowUpFragmentBinding
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowItem
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.FavoriteSeller
import com.malqaa.androidappp.newPhase.domain.models.categoryFollowResp.SavedSearch
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.PicassoSingleton.getPicassoInstance
import com.malqaa.androidappp.newPhase.utils.helper.widgets.rcv.GenericListAdapter
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show

@SuppressLint("ResourceType")
class FollowUpFragment : Fragment(R.layout.fragment_follow_up_fragment) {

    // Declare the binding object
    private var _binding: FragmentFollowUpFragmentBinding? = null
    private val binding get() = _binding!!


    private var favoriteCategory: List<FavoriteSeller> = ArrayList()
    private var categoryFollow: List<CategoryFollowItem> = ArrayList()

    private var savedSearchList: List<SavedSearch> = ArrayList()
    private lateinit var followCategoryViewModel: FollowCategoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the binding object
        _binding = FragmentFollowUpFragmentBinding.bind(view)

        initView()
        setListenser()

        setUpViewModel()
        followCategoryViewModel.getCategoryFollow()
    }

    private fun setUpViewModel() {

        followCategoryViewModel = ViewModelProvider(this).get(FollowCategoryViewModel::class.java)

        followCategoryViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        followCategoryViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message!!)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        followCategoryViewModel.favoriteSellerRespObserver.observe(this) {
            favoriteCategory = it
            favoriteSellerAdapter(it)

        }

        followCategoryViewModel.categoryFollowRespObserver.observe(this) { categoryFolloeResp ->
            if (categoryFolloeResp.status_code == 200) {
                categoryFolloeResp.CategoryFollowList?.let {
                    categoryFollow = it
                    favoriteCategoryAdapter(it)
                }
            }
        }

        followCategoryViewModel.savedSearchObserve.observe(this) {
            savedSearchList = it
            favoriteSearchAdapter(it)
        }

        followCategoryViewModel.removeSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                followCategoryViewModel.getListFavoriteSeller()
            }
        }
        followCategoryViewModel.isLoading.observe(this) {
            if (it) {
                binding.progressBar.show()
            } else {
                binding.progressBar.hide()
            }
        }
    }

    private fun showProductApiError(message: String) {
        binding.tvError.show()
        binding.tvError.text = message
    }

    private fun initView() {
        binding.toolbarMain.toolbarTitle.text = getString(R.string.follow_up)
    }


    private fun setListenser() {

        binding.toolbarMain.toolbarTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.addNewCategory.setOnClickListener() {
            findNavController().navigate(R.id.newCategory)
        }

        binding.favCategory.setOnClickListener {

            followCategoryViewModel.getCategoryFollow()
            binding.favSeller.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.favSearch.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.favCategory.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.favCategory.setTextColor(Color.parseColor("#FFFFFF"));
            binding.favSeller.setTextColor(Color.parseColor("#45495E"));
            binding.favSearch.setTextColor(Color.parseColor("#45495E"));
            binding.favCategoryRcv.show()
            binding.favSellerRcv.hide()
            binding.favSearchRcv.hide()
            binding.addNewCategory.show()
            favoriteCategoryAdapter(categoryFollow)

        }

        binding.favSeller.setOnClickListener {
            followCategoryViewModel.getListFavoriteSeller()
            binding.favCategory.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.favSearch.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.favSeller.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.favSeller.setTextColor(Color.parseColor("#FFFFFF"));
            binding.favCategory.setTextColor(Color.parseColor("#45495E"));
            binding.favSearch.setTextColor(Color.parseColor("#45495E"));
            binding.favCategoryRcv.hide()
            binding.favSellerRcv.show()
            binding.favSearchRcv.hide()
            binding.addNewCategory.hide()
            favoriteSellerAdapter(favoriteCategory)

        }

        binding.favSearch.setOnClickListener {

            followCategoryViewModel.getListSaveSearch()

            binding.favCategory.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.favSeller.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            binding.favSearch.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )

            binding.favSearch.setTextColor(Color.parseColor("#FFFFFF"));
            binding.favCategory.setTextColor(Color.parseColor("#45495E"));
            binding.favSeller.setTextColor(Color.parseColor("#45495E"));
            binding.favCategoryRcv.hide()
            binding.favSellerRcv.hide()
            binding.favSearchRcv.show()
            binding.addNewCategory.hide()
            favoriteSearchAdapter(savedSearchList)

        }

    }


    private fun favoriteCategoryAdapter(list: List<CategoryFollowItem>) {
        binding.favCategoryRcv.adapter =
            object : GenericListAdapter<CategoryFollowItem>(
                R.layout.favorite_categories_design,
                bind = { element, holder, itemCount, position ->

                    // Use view binding to bind each item in the RecyclerView
                    val itemBinding = FavoriteCategoriesDesignBinding.bind(holder.itemView)

                    holder.view.run {
                        binding.favCategoryRcv.show()
                        binding.favSellerRcv.hide()
                        binding.favSearchRcv.hide()
                        element.run {
                            itemBinding.categoryNameTv.text = name
                            getPicassoInstance()
                                .load(image)
                                .into(itemBinding.categoryImg)

                            itemBinding.linFollowCategory.setOnClickListener {
                                followCategoryViewModel.removeCategoryFollow(id)
                            }
                        }

                    }
                }
            ) {
                override fun getFilter(): Filter {
                    TODO("Not yet implemented")
                }

            }.apply {
                submitList(
                    list
                )
            }
    }


    private fun favoriteSellerAdapter(list: List<FavoriteSeller>) {
        binding.favSellerRcv.adapter = object : GenericListAdapter<FavoriteSeller>(
            R.layout.favorite_seller_design,
            bind = { element, holder, itemCount, position ->

                // Use view binding to bind each item in the RecyclerView
                val itemBinding = FavoriteSellerDesignBinding.bind(holder.itemView)

                holder.view.run {
                    binding.favCategoryRcv.hide()
                    binding.favSellerRcv.show()
                    binding.favSearchRcv.hide()

                    element.run {
                        itemBinding.sellerName.text = name
                        itemBinding.txtCity.text = city
                        itemBinding.txtPhone.text = phone
                        itemBinding.txtDateSince.text = HelpFunctions.getViewFormatForDateTrack(
                            createdAt,
                            "dd/MM/yyyy HH:mm:ss"
                        )
                        getPicassoInstance()
                            .load(image)
                            .into(itemBinding.imgSeller)

                        itemBinding.skypeBtn.setOnClickListener {
                            if (skype != null && skype != "") {
                                HelpFunctions.openExternalLInk(skype.toString(), requireContext())
                            }
                        }
                        itemBinding.youtubeBtn.setOnClickListener {
                            if (youTube != null && youTube != "") {
                                HelpFunctions.openExternalLInk(youTube.toString(), requireContext())

                            }
                        }
                        itemBinding.instagramBtn.setOnClickListener {
                            if (instagram != null && instagram != "") {
                                HelpFunctions.openExternalLInk(
                                    instagram.toString(),
                                    requireContext()
                                )
                            }
                        }

                        when (rate) {
                            3 -> {
                                itemBinding.ivRateSeller.setImageResource(R.drawable.happyface_color)
                            }

                            2 -> {
                                itemBinding.ivRateSeller.setImageResource(R.drawable.smileface_color)
                            }

                            1 -> {
                                itemBinding.ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
                            }
                        }

                        if (isMerchant) {
                            itemBinding.txtMerchant.visibility = View.VISIBLE
                        } else {
                            itemBinding.txtMerchant.visibility = View.GONE
                        }
                        if (isFollowed) {
                            itemBinding.imgFollow.setImageResource(R.drawable.notification)
                        } else {
                            itemBinding.imgFollow.setImageResource(R.drawable.notification_log)
                        }
                        itemBinding.linFollow.setOnClickListener {
                            followCategoryViewModel.removeSellerToFav(providerId, businessAccountId)
                        }
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }

    private fun favoriteSearchAdapter(list: List<SavedSearch>) {
        binding.favSearchRcv.adapter = object : GenericListAdapter<SavedSearch>(
            R.layout.favorite_search_design,
            bind = { element, holder, itemCount, position ->

                // Use view binding to bind each item in the RecyclerView
                val itemBinding = FavoriteSearchDesignBinding.bind(holder.itemView)

                holder.view.run {
                    binding.favCategoryRcv.hide()
                    binding.favSellerRcv.hide()
                    binding.favSearchRcv.show()
                    element.run {
                        itemBinding.searchTv.text = searchString
                        itemBinding.linFollowSearch.setOnClickListener {
                            followCategoryViewModel.removeSearchFollow(id)
                        }
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }

    override fun onResume() {
        super.onResume()

        binding.addNewCategory.show()
    }

}