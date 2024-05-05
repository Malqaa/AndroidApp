package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBack

import com.malqaa.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malqaa.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.dialog_answer_question.*
import retrofit2.Call

class AnswerQuestionDialog(
    var productDetialsViewModel: ProductDetailsViewModel,
    context: Context,
    var questionItem: QuestionItem,
    var positionMain: Int,
    var setOnSendAnswer: SetOnSendAnswer
) : BaseDialog(context), ListenerCallBack {
    // var countriesCallback: Call<CountriesResp>? = nul

    private var answerQuestinoCallback: Call<AddQuestionResp>? = null

    override fun getViewId(): Int = R.layout.dialog_answer_question

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean {
        return true
    }

    override fun isLoadingDialog(): Boolean {
        return false
    }

    override fun initialization() {

        setViewClickListenrs()
        tvQuestion.text = questionItem.question.toString()
        question_time.text = HelpFunctions.getViewFormatForDateTrack(questionItem.createdAt,"dd/MM/yyyy HH:mm:ss")
        Glide.with(context).load(questionItem.clientImage).error(R.mipmap.malqa_iconn_round)
            .into(ivQuestionUser)
    }

    private fun setViewClickListenrs() {
        ivClose.setOnClickListener {
            dismiss()
        }
        contianerAskQuestion.setOnClickListener {
            if (etWriteQuestion.text.trim().toString() == "") {
                etWriteQuestion.error = context.getString(R.string.addyourAnswer)
            } else {
                progressBar.show()
                contianerAskQuestion.isEnabled = false
                productDetialsViewModel.addReplay(
                    etWriteQuestion.text.trim().toString().requestBody(),
                    questionItem.id.toString().requestBody(),
                    context,
                    this
                )
            }
        }
    }

    interface SetOnSendAnswer {
        fun onAnswerSuccess(questionItem: QuestionItem, position: Int)
    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (answerQuestinoCallback != null) {
            answerQuestinoCallback?.cancel()
        }
    }

    override fun callBackListener(isFailed: Boolean, response: Any?) {
        if (isFailed) {
            contianerAskQuestion.isEnabled = true
            progressBar.hide()
        } else {
            val obj = response as AddQuestionResp
            if (obj?.status_code == 200 && obj.question != null) {
                dismiss()
                obj.question?.let { setOnSendAnswer.onAnswerSuccess(it, position = positionMain) }
            } else {
                HelpFunctions.ShowLongToast(
                    obj?.message ?: context.getString(R.string.serverError),
                    context
                )
            }
        }
    }
}