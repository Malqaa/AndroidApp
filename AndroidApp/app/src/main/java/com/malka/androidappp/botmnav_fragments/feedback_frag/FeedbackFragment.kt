package com.malka.androidappp.botmnav_fragments.feedback_frag

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback.AllFeedback
import com.malka.androidappp.botmnav_fragments.feedback_frag.buying_feedback.BuyingFeedback
import com.malka.androidappp.botmnav_fragments.feedback_frag.selling_feedback.SellingFeedback
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.BrowseMarketAdapter
import com.malka.androidappp.recycler_browsecat.BrowseMarketModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.categorylistings.PropertyModel
import com.malka.androidappp.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.servicemodels.feedbacks.FeedbackProperties
import com.malka.androidappp.servicemodels.user.UserObject
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.fragment_feedback.*
import kotlinx.android.synthetic.main.fragment_item_im_selling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class FeedbackFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_feedback.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_feedback.navigationIcon?.isAutoMirrored = true
        toolbar_feedback.title = getString(R.string.Feedback)
        toolbar_feedback.setTitleTextColor(Color.WHITE)
        toolbar_feedback.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        val adapter = MyFeedbackViewPagerAdapter(childFragmentManager)
        adapter.addFragment(AllFeedback(), getString(R.string.all))
        adapter.addFragment(BuyingFeedback(), getString(R.string.Buying))
        adapter.addFragment(SellingFeedback(), getString(R.string.Selling))
        viewpagerFeedback.adapter = adapter
        mFeedbackTabs.setupWithViewPager(viewpagerFeedback)
    }

    class MyFeedbackViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

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
