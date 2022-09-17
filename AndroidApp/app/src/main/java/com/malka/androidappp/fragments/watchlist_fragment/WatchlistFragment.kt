package com.malka.androidappp.fragments.watchlist_fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.recycler_browsecat.GenericProductAdapter
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.WatchList
import kotlinx.android.synthetic.main.fragment_watchlist.*
import kotlinx.android.synthetic.main.toolbar_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WatchlistFragment : Fragment(R.layout.fragment_watchlist) {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_title.text = getString(R.string.favorite)
        back_btn.setOnClickListener {
            findNavController().popBackStack()

        }

        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }


        fav_rcv!!.adapter = GenericProductAdapter(ConstantObjects.userwatchlist,requireContext())

        if (!ConstantObjects.userwatchlist.isEmpty()) {
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
        fav_rcv!!.adapter!!.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)

    }
}
