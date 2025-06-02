package com.malka.androidappp.botmnav_fragments.feedback_frag.all_feedback

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.my_product.AdapterMyProduct
import com.malka.androidappp.botmnav_fragments.my_product.AllProductsResponseBack
import com.malka.androidappp.botmnav_fragments.my_product.ModelMyProduct
import com.malka.androidappp.botmnav_fragments.my_product.MyProduct
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.recycler_browsecat.BrowseMarketModel
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.feedbacks.FeedbackObject
import com.malka.androidappp.servicemodels.feedbacks.FeedbackProperties
import com.malka.androidappp.servicemodels.user.UserObject
import kotlinx.android.synthetic.main.frag_profile.*
import kotlinx.android.synthetic.main.fragment_feedback_frag_pager1.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AllFeedback : Fragment() {

    val allFeedback: ArrayList<ModelAllFeedback> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback_frag_pager1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val allfeedbackposts: ArrayList<ModelAllFeedback> = ArrayList()
        if (ConstantObjects.userfeedback != null && ConstantObjects.userfeedback!!.data != null && ConstantObjects.userfeedback!!.data.size > 0) {
            for (IndFeedback in ConstantObjects.userfeedback!!.data) {
                allfeedbackposts.add(
                    ModelAllFeedback(
                        R.drawable.smile_feedbackcard,
                        if (IndFeedback.fullname != null && IndFeedback.fullname.trim().length > 0) IndFeedback.fullname else "",
                        if (IndFeedback.username != null && IndFeedback.username.trim().length > 0) IndFeedback.username else "",
                        HelpFunctions.FormatDateTime(
                            IndFeedback.createdOn,
                            HelpFunctions.datetimeformat_24hrs_milliseconds,
                            HelpFunctions.datetimeformat_mmddyyyy
                        ),
                        IndFeedback.description.trim(),
                        getString(R.string.Respondtothisfeedback)
                    )
                )
            }
        } else {
//            HelpFunctions.ShowAlert(
//                this@AllFeedback.context,
//                "Information",
//                "No Record Found"
//            )
            HelpFunctions.ShowAlert(
                this@AllFeedback.context,
                getString(R.string.Information),
                getString(R.string.NoRecordFound)
            )
        }
        allfeedback_recycler.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        allfeedback_recycler.adapter = AdapterAllFeedback(allfeedbackposts, this)

    }

}