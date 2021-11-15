package com.malka.androidappp.botmnav_fragments.favourite_frag.category_fav

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.favourite_frag.search_fav.AdapterSearchFav
import com.malka.androidappp.botmnav_fragments.favourite_frag.search_fav.ModelSearchFav
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_fav_frag_pager1.*
import kotlinx.android.synthetic.main.fragment_fav_frag_pager1.recylerview_searchfav
import kotlinx.android.synthetic.main.fragment_favr_frag_pager2.*


class CategoriesFav : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favr_frag_pager2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        RefreshScreen()
    }

    fun RefreshScreen() {
        val favcatposts: ArrayList<ModelFavCat> = ArrayList()
        if (ConstantObjects.userfavourite != null && ConstantObjects.userfavourite!!.data != null && ConstantObjects.userfavourite!!.data.category != null && ConstantObjects.userfavourite!!.data.category.size > 0) {
            for (IndFavourite in ConstantObjects.userfavourite!!.data.category) {
                favcatposts.add(
                    ModelFavCat(
                        IndFavourite.categoryName,
                        getString(R.string.TheProducthasbeenrelisted),
                        "","",""
                    )
                )
            }
        } else {
            HelpFunctions.ShowLongToast(
                getString(R.string.NoRecordFound), this@CategoriesFav.context
            )
        }
        recycler_favcat.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        var _adapter: AdapterFavCat = AdapterFavCat(favcatposts, this)
        recycler_favcat.adapter = _adapter
        recycler_favcat.invalidate()
    }
}