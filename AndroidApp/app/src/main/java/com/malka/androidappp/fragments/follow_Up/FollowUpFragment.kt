package com.malka.androidappp.fragments.follow_Up

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.CategoryFollowItem
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.FavoriteSeller
import com.malka.androidappp.newPhase.domain.models.categoryFollowResp.SavedSearch
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_product_details_item_2.instagram_btn
import kotlinx.android.synthetic.main.activity_product_details_item_2.skype_btn
import kotlinx.android.synthetic.main.activity_product_details_item_2.youtube_btn
import kotlinx.android.synthetic.main.activity_shipment_rate.ivSellerHappyRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivSellerNeutralRate
import kotlinx.android.synthetic.main.activity_shipment_rate.ivSellerSadeRate
import kotlinx.android.synthetic.main.favorite_categories_design.view.*
import kotlinx.android.synthetic.main.favorite_search_design.view.*
import kotlinx.android.synthetic.main.favorite_seller_design.view.*
import kotlinx.android.synthetic.main.fragment_follow_up_fragment.*
import kotlinx.android.synthetic.main.toolbar_main.*

@SuppressLint("ResourceType")
class FollowUpFragment : Fragment(R.layout.fragment_follow_up_fragment) {
    private var favoriteCategory: List<FavoriteSeller> = ArrayList()
    private var categoryFollow: List<CategoryFollowItem> = ArrayList()

    private var savedSearchList: List<SavedSearch> = ArrayList()
    private lateinit var followCategoryViewModel:FollowCategoryViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setListenser()

        setUpViewModel()
        followCategoryViewModel.getCategoryFollow()
    }

    private fun setUpViewModel(){

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
                    favoriteCategory=it
                    favoriteSellerAdapter(it)

        }

        followCategoryViewModel.categoryFollowRespObserver.observe(this) { categoryFolloeResp ->
            if (categoryFolloeResp.status_code == 200) {
                categoryFolloeResp.CategoryFollowList?.let {
                    categoryFollow= it
                    favoriteCategoryAdapter(it)
                }
            }
        }

        followCategoryViewModel.savedSearchObserve.observe(this){
            savedSearchList =it
            favoriteSearchAdapter(it)
            }

        followCategoryViewModel.removeSellerToFavObserver.observe(this) {
            if (it.status_code == 200) {
                followCategoryViewModel.getListFavoriteSeller()
            }
        }
        followCategoryViewModel.isLoading.observe(this) {
            if (it) {
                progressBar.show()
            } else {
                progressBar.hide()
            }
        }
    }

    private fun showProductApiError(message: String) {
        tvError.show()
        tvError.text = message
    }
    private fun initView() {
        toolbar_title.text = getString(R.string.follow_up)
    }


    private fun setListenser() {

        back_btn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        add_new_category.setOnClickListener() {
            findNavController().navigate(R.id.newCategory)
        }

        fav_category.setOnClickListener {

            followCategoryViewModel.getCategoryFollow()
            fav_seller.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            fav_search.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            fav_category.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )


            fav_category.setTextColor(Color.parseColor("#FFFFFF"));
            fav_seller.setTextColor(Color.parseColor("#45495E"));
            fav_search.setTextColor(Color.parseColor("#45495E"));
            fav_category_rcv.show()
            fav_seller_rcv.hide()
            fav_search_rcv.hide()
            add_new_category.show()
            favoriteCategoryAdapter(categoryFollow)

        }



        fav_seller.setOnClickListener {
            followCategoryViewModel.getListFavoriteSeller()
            fav_category.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            fav_search.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            fav_seller.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )


            fav_seller.setTextColor(Color.parseColor("#FFFFFF"));
            fav_category.setTextColor(Color.parseColor("#45495E"));
            fav_search.setTextColor(Color.parseColor("#45495E"));
            fav_category_rcv.hide()
            fav_seller_rcv.show()
            fav_search_rcv.hide()
            add_new_category.hide()
            favoriteSellerAdapter(favoriteCategory)

        }

        fav_search.setOnClickListener {

            followCategoryViewModel.getListSaveSearch()

            fav_category.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            fav_seller.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.edittext_bg
            )
            fav_search.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.round_btn
            )


            fav_search.setTextColor(Color.parseColor("#FFFFFF"));
            fav_category.setTextColor(Color.parseColor("#45495E"));
            fav_seller.setTextColor(Color.parseColor("#45495E"));
            fav_category_rcv.hide()
            fav_seller_rcv.hide()
            fav_search_rcv.show()
            add_new_category.hide()
            favoriteSearchAdapter(savedSearchList)

        }

    }


    private fun favoriteCategoryAdapter(list: List<CategoryFollowItem>) {
        fav_category_rcv.adapter =
        object : GenericListAdapter<CategoryFollowItem>(
            R.layout.favorite_categories_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    fav_category_rcv.show()
                    fav_seller_rcv.hide()
                    fav_search_rcv.hide()
                    element.run {
                        category_name_tv.text = name
                        Picasso.get()
                            .load( image)
                            .into(category_img)

                        linFollowCategory.setOnClickListener {
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
        fav_seller_rcv.adapter = object : GenericListAdapter<FavoriteSeller>(
            R.layout.favorite_seller_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    fav_category_rcv.hide()
                    fav_seller_rcv.show()
                    fav_search_rcv.hide()

                    element.run {
                        seller_name.text = name
                        txtCity.text=city
                        txt_phone.text=phone
                        txt_date_since.text= HelpFunctions.getViewFormatForDateTrack(createdAt)
                        Picasso.get()
                            .load( image)
                            .into(imgSeller)

                        skype_btn.setOnClickListener {
                            if (skype != null &&skype != "") {
                                HelpFunctions.openExternalLInk(skype.toString(), requireContext())
                            }
                        }
                        youtube_btn.setOnClickListener {
                            if (youTube != null &&youTube != "") {
                                HelpFunctions.openExternalLInk(youTube.toString(), requireContext())

                            }
                        }
                        instagram_btn.setOnClickListener {
                            if (instagram != null &&instagram != "") {
                                HelpFunctions.openExternalLInk(instagram.toString(), requireContext())
                            }
                        }

                        when (rate) {
                            3 -> {
                                ivRateSeller.setImageResource(R.drawable.happyface_color)
                            }
                            2 -> {
                                ivRateSeller.setImageResource(R.drawable.smileface_color)
                            }
                            1 -> {
                                ivRateSeller.setImageResource(R.drawable.sadcolor_gray)
                            }
                        }

                        if(isMerchant){
                            txtMerchant.visibility=View.VISIBLE
                        }else{
                            txtMerchant.visibility=View.GONE
                        }
                        if (isFollowed) {
                            imgFollow.setImageResource(R.drawable.notification)
                        } else {
                            imgFollow.setImageResource(R.drawable.notification_log)
                        }
                        linFollow.setOnClickListener {
                            followCategoryViewModel.removeSellerToFav(providerId,businessAccountId)
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
        fav_search_rcv.adapter = object : GenericListAdapter<SavedSearch>(
            R.layout.favorite_search_design,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    fav_category_rcv.hide()
                    fav_seller_rcv.hide()
                    fav_search_rcv.show()
                    element.run {
                        search_tv.text = searchString
                        linFollowSearch.setOnClickListener {
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

        add_new_category.show()
    }

}