package com.malka.androidappp.botmnav_fragments.watchlist_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.recycler_browsecat.BrowseMarketXLAdap
import com.malka.androidappp.servicemodels.AdDetailModel
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
        BindUserWatchlist()
    }



    fun BindUserWatchlist() {


        if (ConstantObjects.userwatchlist != null && ConstantObjects.userwatchlist!!.data.size > 0) {
            ConstantObjects.userwatchlist!!.data
            val list: ArrayList<AdDetailModel> = ArrayList()

            ConstantObjects.userwatchlist!!.data.forEach {
                list.add(it.advertisement)
            }
            fav_rcv!!.adapter = BrowseMarketXLAdap(list,requireContext())


        } else {

            HelpFunctions.ShowLongToast(
                getString(R.string.NoRecordFound),
                this@WatchlistFragment.context
            )
        }

    }
}
