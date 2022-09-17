package com.malka.androidappp.fragments.feedback_frag.selling_feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_feedback_frag_pager3.*


class SellingFeedback : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback_frag_pager3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sellingfeedbackposts: ArrayList<ModelSellingFeedback> = ArrayList()
        if (ConstantObjects.userfeedback != null && ConstantObjects.userfeedback!!.Selling != null && ConstantObjects.userfeedback!!.Selling.size > 0) {
            for (IndFeedback in ConstantObjects.userfeedback!!.Selling) {
                sellingfeedbackposts.add(
                    ModelSellingFeedback(
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
            HelpFunctions.ShowAlert(
                this@SellingFeedback.context,
                getString(R.string.Information),
                getString(R.string.NoRecordFound)
            )
        }
        sellingfeedback_recycler.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        sellingfeedback_recycler.adapter = AdapterSellingFeedback(sellingfeedbackposts, this)
    }
}