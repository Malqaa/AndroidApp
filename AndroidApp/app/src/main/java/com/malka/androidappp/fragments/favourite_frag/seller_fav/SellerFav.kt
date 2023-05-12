package com.malka.androidappp.fragments.favourite_frag.seller_fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.fragment_fav_frag_pager3.*


class SellerFav : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_frag_pager3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RefreshScreen()
    }


    fun RefreshScreen() {
        val favsellerposts: ArrayList<ModelFavSeller> = ArrayList()
        if (ConstantObjects.userfavourite != null && ConstantObjects.userfavourite!!.data != null && ConstantObjects.userfavourite!!.data.seller != null && ConstantObjects.userfavourite!!.data.seller.size > 0) {
            for (IndFavourite in ConstantObjects.userfavourite!!.data.seller) {
                favsellerposts.add(
                    ModelFavSeller(
                        IndFavourite.username,
                        R.drawable.car,
                        IndFavourite.totallistings + " " + getString(R.string.CurrentListings),
                        "",
                        "",
                        "",
                        IndFavourite.userid
                    )
                )
            }
        } else {
            HelpFunctions.ShowLongToast(
                getString(R.string.NoRecordFound), this@SellerFav.context
            )
        }
        recycler_favseller.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        var _adapter: AdapterFavSeller = AdapterFavSeller(favsellerposts, this)
        recycler_favseller.adapter = _adapter
        recycler_favseller.invalidate()
    }
}