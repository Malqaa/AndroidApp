package com.malka.androidappp.fragments.memberlisting

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.fragments.AboutTheSeller
import com.malka.androidappp.fragments.sellerdetails.Advertisement
import com.malka.androidappp.fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.fragment_member_listing.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MemberListing : Fragment() {

    val sellerAds: ArrayList<Advertisement> = ArrayList()
    lateinit var sellerUserName: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member_listing, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_memberlisting.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_memberlisting.setTitleTextColor(Color.WHITE)
        toolbar_memberlisting.title = getString(R.string.MemberListing)
        toolbar_memberlisting.setNavigationOnClickListener() {


            requireActivity().onBackPressed()
        }

        sellerUserName = requireActivity().findViewById(R.id.sellerNameText)
        sellerUserName.text = AboutTheSeller.userName

        getSellerByID(SharedPreferencesStaticClass.ad_userid, ConstantObjects.logged_userid)
    }

    private fun getSellerByID(id: String, loggedUserID: String) {

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<SellerResponseBack> = malqa.getAdSeller(id, loggedUserID)

        call.enqueue(object : Callback<SellerResponseBack> {

            override fun onResponse(
                call: Call<SellerResponseBack>,
                response: Response<SellerResponseBack>
            ) {
                if (response.isSuccessful) {

                    val details: SellerResponseBack = response.body()!!
                    if (details != null) {

                        var sellerAdsList= response.body()!!.data.advertisements
                        if (sellerAdsList != null && sellerAdsList.count() > 0) {
                            for (IndProperty in sellerAdsList) {
                                sellerAds.add(
                                    Advertisement(
                                        IndProperty.description,
                                        if (IndProperty.homepageImage != null) IndProperty.homepageImage else null,
                                        IndProperty.price,
                                        if (IndProperty.reservePrice != null) IndProperty.reservePrice else "0",
                                        IndProperty.title,
                                    )
                                )
                            }
                            val sellerAdRecycler: RecyclerView =
                                requireActivity().findViewById(R.id.recyclerViewML)

                            sellerAdRecycler.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            sellerAdRecycler.adapter = AdapterMemberListing(
                                sellerAds as ArrayList<Advertisement>,
                                MemberListing()
                            )

                        } else {
                            HelpFunctions.ShowAlert(
                                this@MemberListing.context,
                                getString(R.string.Information),
                                getString(R.string.NoRecordFound)
                            )
                        }


                    } else {
                        HelpFunctions.ShowAlert(
                            this@MemberListing.context,
                            getString(R.string.Information),
                            getString(R.string.NoRecordFound)
                        )
                    }
                } else {
                    HelpFunctions.ShowAlert(
                        this@MemberListing.context,
                        getString(R.string.Information),
                        getString(R.string.NoRecordFound)
                    )
                }
                HelpFunctions.dismissProgressBar()
            }

            override fun onFailure(call: Call<SellerResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(t.message!!, this@MemberListing.context)
            }
        })
    }
}