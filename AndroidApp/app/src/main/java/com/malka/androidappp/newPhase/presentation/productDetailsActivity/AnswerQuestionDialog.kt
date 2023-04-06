package com.malka.androidappp.newPhase.presentation.productDetailsActivity

import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.bumptech.glide.Glide
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseDialog
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.Extension.requestBody
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder

import com.malka.androidappp.newPhase.domain.models.questionResp.AddQuestionResp
import com.malka.androidappp.newPhase.domain.models.questionResp.QuestionItem
import kotlinx.android.synthetic.main.dialog_answer_question.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class AnswerQuestionDialog(context: Context, var questionItem: QuestionItem,var positionMain:Int ,var setOnSendAnswer:SetOnSendAnswer) : BaseDialog(context) {
    // var countriesCallback: Call<CountriesResp>? = nul

    private  var answerQuestinoCallback: Call<AddQuestionResp>?=null

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
        question_time.text = HelpFunctions.getViewFormatForDateTrack(questionItem.createdAt)
        Glide.with(context).load(questionItem.clientImage).error(R.mipmap.malqa_iconn_round).into(  ivQuestionUser)
    }

    private fun setViewClickListenrs() {
        ivClose.setOnClickListener {
            dismiss()
        }
        contianerAskQuestion.setOnClickListener {
            if(etWriteQuestion.text.trim().toString()==""){
                etWriteQuestion.error=context.getString(R.string.addyourAnswer)
            }else{
                addReplay()
            }
        }
    }

    interface SetOnSendAnswer {
        fun onAnswerSuccess(questionItem: QuestionItem,position:Int)
    }
    fun addReplay() {
        progressBar.show()
        contianerAskQuestion.isEnabled=false
        answerQuestinoCallback = RetrofitBuilder.GetRetrofitBuilder().replayQuestion(
            etWriteQuestion.text.trim().toString().requestBody(),
            questionItem.id.toString().requestBody())
        answerQuestinoCallback?.enqueue(object : Callback<AddQuestionResp> {
            override fun onFailure(call: Call<AddQuestionResp>, t: Throwable) {
                // println("hhhh "+t.message)
                contianerAskQuestion.isEnabled=true
                progressBar.hide()
                if (call.isCanceled) {

                } else if (t is HttpException) {
                    HelpFunctions.ShowLongToast(context.getString(R.string.serverError), context)

                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.connectionError),
                        context
                    )
                }
            }

            override fun onResponse(
                call: Call<AddQuestionResp>,
                response: Response<AddQuestionResp>
            ) {
                progressBar.visibility = View.GONE
                contianerAskQuestion.isEnabled=true
                try {
                    if (response.isSuccessful) {
                       var addQuestionResp=response.body()
                        if(addQuestionResp?.status_code==200&& addQuestionResp.question!=null){
                            dismiss()
                            addQuestionResp.question?.let { setOnSendAnswer.onAnswerSuccess(it, position = positionMain) }
                        }else{
                            HelpFunctions.ShowLongToast(
                                addQuestionResp?.message?: context.getString(R.string.serverError),
                                context
                            )
                        }

                    } else {
                        HelpFunctions.ShowLongToast(
                            context.getString(R.string.serverError),
                            context
                        )

                    }
                } catch (e: Exception) {
                }
            }

        })
    }
    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        if (answerQuestinoCallback != null) {
            answerQuestinoCallback?.cancel()
        }
    }
}