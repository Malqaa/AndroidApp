package com.malka.androidappp.botmnav_fragments.favourite_frag

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.favourite_frag.category_fav.CategoriesFav
import com.malka.androidappp.botmnav_fragments.favourite_frag.search_fav.SearchFav
import com.malka.androidappp.botmnav_fragments.favourite_frag.seller_fav.SellerFav
import com.malka.androidappp.botmnav_fragments.feedback_frag.FeedbackFragment
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.fragment_favourites.*


class MainFavouritesFrag : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_fav.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_fav.title = getString(R.string.Favourites)
        toolbar_fav.setTitleTextColor(Color.WHITE)
        toolbar_fav.navigationIcon?.isAutoMirrored = true
        toolbar_fav.setNavigationOnClickListener {
            findNavController().navigate(R.id.fav_acc)
        }

        //Zack
        //Date: 11/13/2020
        HelpFunctions.GetUserFavourites(this@MainFavouritesFrag);
        val adapter = FeedbackFragment.MyFeedbackViewPagerAdapter(childFragmentManager)

        adapter.addFragment(SearchFav(), getString(R.string.Searches))
        adapter.addFragment(CategoriesFav(), getString(R.string.Categories))
        adapter.addFragment(SellerFav(), getString(R.string.Sellers))
        viewpagerfavourites.adapter = adapter
        mTabs.setupWithViewPager(viewpagerfavourites)
//        viewpagerfavourites.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//                if (position == 0) {
//                    searchpne.RefreshScreen()
//                } else if (position == 1) {
//                    categorypne.RefreshScreen()
//                } else if (position == 2) {
//                    sellerpne.RefreshScreen()
//                }
//            }
//            override fun onPageSelected(position: Int) {
//                if (position == 0) {
//                    searchpne.RefreshScreen()
//                } else if (position == 1) {
//                    categorypne.RefreshScreen()
//                } else if (position == 2) {
//                    sellerpne.RefreshScreen()
//                }
//            }
//            override fun onPageScrollStateChanged(state: Int) {
//            }
//        })
//    }
    }

        class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

            private val fragmentList: MutableList<Fragment> = ArrayList()
            private val titleList: MutableList<String> = ArrayList()

            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            fun addFragment(fragment: Fragment, title: String) {
                fragmentList.add(fragment)
                titleList.add(title)
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titleList[position]
            }
        }
}
