package com.malka.androidappp.botmnav_fragments.item_em_selling

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.sellerdetails.Advertisement
import com.malka.androidappp.botmnav_fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_item_im_selling.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ItemImSelling : Fragment(), AdapterImSelling.OnItemClickListener {

    val itemEmSellingPost: ArrayList<ModelImSelling> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        itemEmSellingPost.clear()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_im_selling, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        item_imselling_toolbar.title = getString(R.string.ItemsImSelling)
        item_imselling_toolbar.setTitleTextColor(Color.WHITE)
        item_imselling_toolbar.setNavigationIcon(R.drawable.nav_icon_back)
        item_imselling_toolbar.navigationIcon?.isAutoMirrored = true
        item_imselling_toolbar.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }

        getSellerByID(ConstantObjects.logged_userid, ConstantObjects.logged_userid)

    }

    private fun getSellerByID(id: String, loggedUserID: String) {

        val malqa: MalqaApiService = RetrofitBuilder.getAdSeller(id, loggedUserID)
        val call: Call<SellerResponseBack> = malqa.getAdSeller(id, loggedUserID)

        call.enqueue(object : Callback<SellerResponseBack> {

            override fun onResponse(
                call: Call<SellerResponseBack>,
                response: Response<SellerResponseBack>
            ) {
                if (response.isSuccessful) {

                    val details: SellerResponseBack = response.body()!!
                    if (details != null) {

                        var sellerAdsList: List<Advertisement> =
                            response.body()!!.data.advertisements
                        if (sellerAdsList != null && sellerAdsList.count() > 0) {
                            for (IndProperty in sellerAdsList) {
                                itemEmSellingPost.add(
                                    ModelImSelling(
                                        IndProperty.description,
                                        IndProperty.title,
                                        if (!IndProperty.reservePrice.isNullOrEmpty()) IndProperty.reservePrice else "0",
                                        if (!IndProperty.price.isNullOrEmpty()) IndProperty.price else "0",
                                        if (IndProperty.homepageImage != null) IndProperty.homepageImage else null,
                                        if (!IndProperty.template.isNullOrEmpty()) IndProperty.template else "",
                                        if (!IndProperty.referenceId.isNullOrEmpty()) IndProperty.referenceId else "",
                                    )
                                )
                            }

                            val itemEmSellingRecycler: RecyclerView =
                                requireActivity().findViewById(R.id.item_imselling_recycler)

                            itemEmSellingRecycler.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            itemEmSellingRecycler.adapter =
                                AdapterImSelling(
                                    itemEmSellingPost,
                                    ItemImSelling(),
                                    this@ItemImSelling
                                )

                        } else {
                            HelpFunctions.ShowAlert(
                                this@ItemImSelling.context,
                                getString(R.string.Information),
                                getString(R.string.NoRecordFound)
                            )
                        }


                    } else {
                        HelpFunctions.ShowAlert(
                            this@ItemImSelling.context,
                            getString(R.string.Information),
                            getString(R.string.NoRecordFound)
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@ItemImSelling.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()
            }

            override fun onFailure(call: Call<SellerResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(t.message!!, this@ItemImSelling.context)
            }
        })
    }

    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)

        val args = Bundle()
        args.putString("AdvId", itemEmSellingPost[position].referenceId)
        args.putString("Template", itemEmSellingPost[position].template)

        NavHostFragment.findNavController(this@ItemImSelling)
            .navigate(R.id.my_items_to_detail_page, args)

    }
}