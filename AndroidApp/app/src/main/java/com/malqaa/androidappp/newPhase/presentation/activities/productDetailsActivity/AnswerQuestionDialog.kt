package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import com.bumptech.glide.Glide
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.DialogAnswerQuestionBinding
import com.malqaa.androidappp.newPhase.core.BaseDialog
import com.malqaa.androidappp.newPhase.data.network.service.ListenerCallBack
import com.malqaa.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malqaa.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.utils.Extension.requestBody
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.show
import retrofit2.Call

class AnswerQuestionDialog(
    var productDetialsViewModel: ProductDetailsViewModel,
    context: Context,
    var questionItem: QuestionItem,
    var positionMain: Int,
    var setOnSendAnswer: SetOnSendAnswer
) : BaseDialog<DialogAnswerQuestionBinding>(context), ListenerCallBack {

    private var answerQuestinoCallback: Call<AddQuestionResp>? = null

    override fun inflateViewBinding(): DialogAnswerQuestionBinding {
        return DialogAnswerQuestionBinding.inflate(layoutInflater)
    }

    override fun isFullScreen(): Boolean = false

    override fun isCancelable(): Boolean {
        return true
    }

    override fun isLoadingDialog(): Boolean {
        return false
    }

    override fun initialization() {

        setViewClickListenrs()
        binding.tvQuestion.text = questionItem.question.toString()
        binding.questionTime.text =
            HelpFunctions.getViewFormatForDateTrack(questionItem.createdAt, "dd/MM/yyyy HH:mm:ss")
        Glide.with(context).load(questionItem.clientImage).error(R.mipmap.ic_launcher_round)
            .into(binding.ivQuestionUser)
    }

    private fun setViewClickListenrs() {
        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.contianerAskQuestion.setOnClickListener {
            if (binding.etWriteQuestion.text.trim().toString() == "") {
                binding.etWriteQuestion.error = context.getString(R.string.addyourAnswer)
            } else {
                binding.progressBar.show()
                binding.contianerAskQuestion.isEnabled = false
                productDetialsViewModel.addReplay(
                    binding.etWriteQuestion.text.trim().toString().requestBody(),
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
            binding.contianerAskQuestion.isEnabled = true
            binding.progressBar.hide()
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