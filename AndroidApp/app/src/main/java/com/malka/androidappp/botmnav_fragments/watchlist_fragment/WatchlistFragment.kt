package com.malka.androidappp.botmnav_fragments.watchlist_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.adapter.GeneralAdvertisementAdapter
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.home.GeneralProduct
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


        if (ConstantObjects.userwatchlist != null && ConstantObjects.userwatchlist!!.data != null && ConstantObjects.userwatchlist!!.data.size > 0) {
            val watchlistpost = ConstantObjects.userwatchlist!!.data!!
            val list: ArrayList<GeneralProduct> = ArrayList()

            ConstantObjects.userwatchlist!!.data!!.forEach {
                list.add(it.advertisement)
            }
            fav_rcv!!.adapter = GeneralAdvertisementAdapter(list)


        } else {

            HelpFunctions.ShowLongToast(
                getString(R.string.NoRecordFound),
                this@WatchlistFragment.context
            )
        }

    }
}
