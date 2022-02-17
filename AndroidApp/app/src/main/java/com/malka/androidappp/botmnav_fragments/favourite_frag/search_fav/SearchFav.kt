package com.malka.androidappp.botmnav_fragments.favourite_frag.search_fav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_fav_frag_pager1.*

class SearchFav : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_frag_pager1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RefreshScreen()
    }

    fun RefreshScreen() {
        val searchposts: ArrayList<ModelSearchFav> = ArrayList()
        if (ConstantObjects.userfavourite != null && ConstantObjects.userfavourite!!.data != null && ConstantObjects.userfavourite!!.data.searchQue != null && ConstantObjects.userfavourite!!.data.searchQue.size > 0) {
            for (IndFavourite in ConstantObjects.userfavourite!!.data.searchQue) {
                searchposts.add(
                    ModelSearchFav(
                        IndFavourite.searchQuery,
                        getString(R.string.TheProducthasbeenRestocked)
                    )
                )
            }
        } else {
            HelpFunctions.ShowLongToast(
                getString(R.string.NoRecordFound), this@SearchFav.context
            )
        }
        recylerview_searchfav.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        var _adapter: AdapterSearchFav = AdapterSearchFav(searchposts, this)
        recylerview_searchfav.adapter = _adapter
        recylerview_searchfav.invalidate()
    }
}