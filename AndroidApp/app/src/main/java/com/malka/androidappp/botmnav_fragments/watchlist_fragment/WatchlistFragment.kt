package com.malka.androidappp.botmnav_fragments.watchlist_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.recycler_browsecat.BrowseMarketXLAdap
import com.malka.androidappp.servicemodels.AdDetailModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.WatchList
import kotlinx.android.synthetic.main.fragment_watchlist.*
import kotlinx.android.synthetic.main.toolbar_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import androidx.navigation.fragment.findNavController


class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {


    val list: ArrayList<AdDetailModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_title.text = getString(R.string.favorite)
        back_btn.setOnClickListener {
            findNavController().popBackStack()

        }

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }


        fav_rcv!!.adapter = BrowseMarketXLAdap(list,requireContext())

        if (ConstantObjects.userwatchlist!!.data.size > 0) {
            ConstantObjects.userwatchlist!!.data.forEach {
                list.add(it.advertisement)
            }
            fav_rcv!!.adapter!!.notifyDataSetChanged()
        } else {
            HelpFunctions.ShowLongToast(
                getString(R.string.NoRecordFound),
                this@WatchlistFragment.context
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: WatchList?) {
        list.clear()
        ConstantObjects.userwatchlist!!.data.forEach {
            list.add(it.advertisement)
        }
        fav_rcv!!.adapter!!.notifyDataSetChanged()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)

    }
}
