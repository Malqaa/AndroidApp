package com.malka.androidappp.botmnav_fragments.watchlist_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.recycler_watchlist.WatchlistAdap
import com.malka.androidappp.recycler_watchlist.WatchlistModel
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_watchlist.*

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
        ////////////////////////////////////////////////////////////////////////
        //Zack
        //Date: 11/04/2020
        GetUserWatchList()
    }

    fun GetUserWatchList() {
        try {
            HelpFunctions.GetUserWatchlist(this@WatchlistFragment);
            BindUserWatchlist();
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun BindUserWatchlist() {

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

            var browadptxl = WatchlistAdap(watchlistpost, this@WatchlistFragment)
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
    }
}