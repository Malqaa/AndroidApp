package com.malka.androidappp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.fragments.sellerdetails.SellerResponseBack
import com.malka.androidappp.newPhase.data.helper.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.constants.Constants
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.newPhase.domain.models.servicemodels.feedbacks.FeedbackProperties
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_about_the_seller.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AboutTheSeller : Fragment() {

    companion object {
        var userName = ""
    }

//    val progressLoading = HelpFunctions.startProgressBar(this.requireActivity())


    lateinit var sellerName: TextView
    lateinit var sellerFirstName: TextView
    lateinit var sellerLastName: TextView
    lateinit var sellerGender: TextView
    lateinit var sellerMobile: TextView
    lateinit var sellerLandLine: TextView
    lateinit var sellerLocation: TextView
    lateinit var memberSince: TextView
    lateinit var sellerImage: ImageView
    lateinit var sellerListing: ConstraintLayout
    lateinit var viewAllFeedback: Button
    lateinit var sellersTotalListing: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        HelpFunctions.startProgressBar(this.requireActivity())

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about_the_seller, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_aboutseller.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_aboutseller.setTitle(R.string.title_about_seller)
        toolbar_aboutseller.setTitleTextColor(Color.WHITE)
        toolbar_aboutseller.navigationIcon?.isAutoMirrored = true
        toolbar_aboutseller.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }



        sellerName = requireActivity().findViewById(R.id.textView18)
        sellerFirstName = requireActivity().findViewById(R.id.textView31)
        sellerLastName = requireActivity().findViewById(R.id.textView37)
        sellerGender = requireActivity().findViewById(R.id.textView33)
        sellerMobile = requireActivity().findViewById(R.id.textView39)
        sellerLandLine = requireActivity().findViewById(R.id.textView35)
        sellerLocation = requireActivity().findViewById(R.id.textView41)
        memberSince = requireActivity().findViewById(R.id.textView22)
        sellerImage = requireActivity().findViewById(R.id.imageView15)
        sellerListing = requireActivity().findViewById(R.id.listingg)
        viewAllFeedback = requireActivity().findViewById(R.id.imageButton2)
        sellersTotalListing = requireActivity().findViewById(R.id.textView50)

        getSellerByID(SharedPreferencesStaticClass.ad_userid, ConstantObjects.logged_userid)
        GetUserFeedBack(SharedPreferencesStaticClass.ad_userid)

        sellerListing.setOnClickListener {
            HelpFunctions.startProgressBar(this.requireActivity())
            findNavController().navigate(R.id.aboutseller_memberlisting)
        }
        viewAllFeedback.setOnClickListener() {
            SharedPreferencesStaticClass.myFeedback = false
            findNavController().navigate(R.id.seller_feedback)
        }
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

                        var sellerData = response.body()!!.data.detailOfUser
                        var sellerAdCount =
                            response.body()!!.data.advertisements

                        if (sellerAdCount != null) {
                            sellersTotalListing.text =
                                getString(R.string.listing, sellerAdCount.size)
                        } else {
                            sellersTotalListing.text = getString(R.string.listing, 0)
                        }
                        sellerName.text = sellerData.userName
                        userName = sellerData.userName!!
                        sellerFirstName.text = sellerData.fullName
                        sellerLastName.text = sellerData.lastname
                        sellerGender.text = sellerData.gender
                        sellerMobile.text = sellerData.phone
                        sellerLandLine.text = sellerData.phone
                        sellerLocation.text = sellerData.city
                        memberSince.text = sellerData.createdAt

                        if (sellerData.image != null) {
                            Picasso.get()
                                .load(Constants.IMAGE_URL + sellerData.image)
                                .into(sellerImage)
                        } else {
                            sellerImage.setImageResource(R.drawable.profilepic)
                        }
                        HelpFunctions.dismissProgressBar()
                    } else {
                        HelpFunctions.dismissProgressBar()
                        HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                    }
                    HelpFunctions.dismissProgressBar()
                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                }
            }

            override fun onFailure(call: Call<SellerResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                HelpFunctions.ShowLongToast(getString(R.string.something_went_wrong), context)
            }
        })
    }

    private fun GetUserFeedBack(userid: String) {
        try {
            val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val call: Call<FeedbackObject> = malqa.getuserfeedback(userid)
            call.enqueue(object : Callback<FeedbackObject> {
                override fun onFailure(call: Call<FeedbackObject>, t: Throwable) {
//                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                }

                @SuppressLint("SetTextI18n")
                override fun onResponse(
                    call: Call<FeedbackObject>,
                    response: Response<FeedbackObject>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val resp: FeedbackObject = response.body()!!
                            if (resp != null) {
                                var lists: List<FeedbackProperties> = resp.data
                                try {
                                    if (lists != null && lists.count() > 0) {
                                        resp.Buying = mutableListOf()
                                        resp.Selling = mutableListOf()
                                        resp.negative_count = 0
                                        resp.neutral_count = 0
                                        resp.positive_count = 0

                                        for (IndFeedback in lists) {
                                            if (IndFeedback.rating != null) {
                                                if (IndFeedback.rating == 0) {
                                                    resp.neutral_count += 1;
                                                } else if (IndFeedback.rating == 2) {
                                                    resp.positive_count += 1;
                                                } else if (IndFeedback.rating == 1) {
                                                    resp.negative_count += 1;
                                                }
                                            }
                                            if (IndFeedback.sellerId.trim().toUpperCase()
                                                    .equals(userid.toUpperCase().trim())
                                            ) {
                                                resp.Selling.add(IndFeedback)
                                            } else {
                                                resp.Buying.add(IndFeedback)
                                            }
                                        }

                                        ConstantObjects.userfeedback = resp
                                        if (ConstantObjects.userfeedback != null) {
                                            textView46.text =
                                                ConstantObjects.userfeedback!!.neutral_count.toString() + " " + getString(
                                                    R.string.neutrals
                                                )
                                            textView47.text =
                                                ConstantObjects.userfeedback!!.negative_count.toString() + " " + getString(
                                                    R.string.negatives
                                                )
                                            textView44.text =
                                                ConstantObjects.userfeedback!!.positive_count.toString() + " " + getString(
                                                    R.string.positive
                                                )
                                        }
                                    } else {
//                                        HelpFunctions.dismissProgressBar()
                                        textView46.text = "0 " + getString(R.string.neutrals)
                                        textView47.text = "0 " + getString(R.string.negatives)
                                        textView44.text = "0 " + getString(R.string.positive)
                                        if (context != null) {
                                            HelpFunctions.ShowLongToast(
                                                getString(R.string.NoRecordFound),
                                                context
                                            )
                                        }

                                    }
                                } catch (ex: NullPointerException) {
                                    throw ex
                                }
                            } else {
//                                HelpFunctions.dismissProgressBar()
                                HelpFunctions.ShowLongToast(
                                    getString(R.string.NoRecordFound),
                                    context
                                )
                            }
                        }
                    } else {
//                        HelpFunctions.dismissProgressBar()
                        HelpFunctions.ShowLongToast(getString(R.string.NoRecordFound), context)
                    }

                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }
}