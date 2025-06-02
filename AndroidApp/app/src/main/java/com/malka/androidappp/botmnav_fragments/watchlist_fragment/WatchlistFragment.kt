package com.malka.androidappp.botmnav_fragments.watchlist_fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.BrowseMarketXLAdap
import com.malka.androidappp.recycler_watchlist.WatchlistAdap
import com.malka.androidappp.recycler_watchlist.WatchlistModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.user.UserObject
import com.malka.androidappp.servicemodels.watchlist.watchlistobject
import kotlinx.android.synthetic.main.frag_profile.*
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.fragment_watchlist.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WatchlistFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return LayoutInflater.from(container?.context)
            .inflate(R.layout.fragment_watchlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ////////////////////////////////////////////////////////////////////////
        toolbar_watchlist.title = getString(R.string.Watchlist)
        toolbar_watchlist.setTitleTextColor(Color.WHITE)
        ////////////////////////////////////////////////////////////////////////
        //Zack
        //Date: 11/04/2020
        GetUserWatchList()
    }

    fun GetUserWatchList()
    {
        try {
            HelpFunctions.GetUserWatchlist(this@WatchlistFragment);
            BindUserWatchlist();
        }
        catch (ex: Exception)
        {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserWatchlist()
    {
        try {
            val watchlistpost: ArrayList<WatchlistModel> = ArrayList()
            if (ConstantObjects.userwatchlist != null && ConstantObjects.userwatchlist!!.data != null && ConstantObjects.userwatchlist!!.data.size > 0) {
                for (IndWatch in ConstantObjects.userwatchlist!!.data) {
                    if (IndWatch.advertisement != null) {
                        watchlistpost.add(
                            WatchlistModel(
                                IndWatch.advertisement.title,
                                IndWatch.advertisement.enddate,
                                IndWatch.advertisement.price,
                                getString(R.string.BuyNow),
                                IndWatch.advertisement.homepageImage,
                                IndWatch.advertisement.referenceId,
                                IndWatch.advertisement.template
                            )
                        )
                    }
                }

            }
            if (watchlistpost == null || watchlistpost.size == 0) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.NoRecordFound),
                    this@WatchlistFragment.context
                )
            }
            recyclerView55.layoutManager =
                LinearLayoutManager(
                    activity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            var browadptxl: WatchlistAdap = WatchlistAdap(watchlistpost, this@WatchlistFragment)
            recyclerView55.adapter = browadptxl
            browadptxl.onItemClick = { indobj ->
                HelpFunctions.ViewAdvertismentDetail(
                    indobj.watchlistadvid!!,
                    indobj.watchlistadvtemplate!!,
                    this@WatchlistFragment
                )
            }
            browadptxl.notifyDataSetChanged()

        }
        catch (ex: Exception)
        {
            HelpFunctions.ReportError(ex)
        }
    }
}