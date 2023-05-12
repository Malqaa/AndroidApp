package com.malka.androidappp.fragments.feedback_frag.buying_feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import kotlinx.android.synthetic.main.fragment_feedback_frag_pager2.*


class BuyingFeedback : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback_frag_pager2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buyfeedbackposts: ArrayList<ModelBuyfeedback> = ArrayList()
        if (ConstantObjects.userfeedback != null && ConstantObjects.userfeedback!!.Buying != null && ConstantObjects.userfeedback!!.Buying.size > 0) {
            for (IndFeedback in ConstantObjects.userfeedback!!.Buying) {
                buyfeedbackposts.add(
                    ModelBuyfeedback(
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
                this@BuyingFeedback.context,
                getString(R.string.Information),
                getString(R.string.NoRecordFound)
            )
        }
        recycler_buyfeedback.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL, false
        )
        recycler_buyfeedback.adapter = AdapterBuyfeedback(buyfeedbackposts, this)
    }

}