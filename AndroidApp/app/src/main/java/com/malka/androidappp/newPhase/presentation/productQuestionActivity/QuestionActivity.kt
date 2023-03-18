package com.malka.androidappp.newPhase.presentation.productQuestionActivity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.Question
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.GenericAdaptor
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.swipe.MessageSwipeController
import com.malka.androidappp.newPhase.data.helper.swipe.SwipeControllerActions
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailHelper
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.toolbar_main.*

class QuestionActivity : BaseActivity() {
    var AdvId = ""
    lateinit var productDetailHelper: ProductDetailHelper
    var questionList: MutableList <Question> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        productDetailHelper= ProductDetailHelper(this)

        AdvId = intent.getStringExtra("AdvId") ?: ""

        toolbar_title.text = getString(R.string.questions_and_answers)
        back_btn.setOnClickListener {
            finish()
        }
        GenericAdaptor(). questionAnswerAdaptor(rvQuestionForProduct,questionList)

        quesAnss()
        contianerAskQuestion.setOnClickListener {
            confrmAskQues()
        }
    }


    fun confrmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            askquesApi()
        }

    }
    fun askquesApi() {


        productDetailHelper.askquesApi(etWriteQuestion.text.toString(), AdvId, {
            etWriteQuestion.setText("")
            quesAnss()
        })

    }

    private fun validateAskQuesInputText(): Boolean {
        val Inputemail = etWriteQuestion!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Question)))
            false
        } else {
            true
        }
    }
    fun quesAnss() {
        productDetailHelper. quesAnss(AdvId) {
            it.run {
                quest_ans.text = getString(
                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
                    unAnswered.toString()
                )
                questionList.clear()
                questionList .addAll(questions)
                rvQuestionForProduct.adapter!!.notifyDataSetChanged()
                if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
                    containerMainAskQuestion.visibility = View.GONE
                    enableSwipeToDeleteAndUndo()

                } else if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
                    containerMainAskQuestion.visibility = View.VISIBLE

                }
            }

        }

    }


    fun PostAnsApi(questionId: String, answer: String) {



        productDetailHelper. PostAnsApi(questionId,answer) {respone->


            if (respone.status_code >= 200||respone.status_code<=299) {
                answerLayout.isVisible = false
                ReplyAnswer.setText("")
                quesAnss()
                HelpFunctions.ShowLongToast(
                    getString(R.string.Answerhasbeenposted),
                    this@QuestionActivity
                )

            }
        }

    }


    private fun enableSwipeToDeleteAndUndo() {
        val messageSwipeController =
            MessageSwipeController(this, object : SwipeControllerActions {
                override fun showReplyUI(position: Int) {
                    val question = questionList.get(position)
                    replyItemClicked(question)
                    vibration()
                }
            })
        val itemTouchHelper = ItemTouchHelper(messageSwipeController)
        itemTouchHelper.attachToRecyclerView(rvQuestionForProduct)

    }

    private fun replyItemClicked(question: Question) {

        answerLayout.isVisible = true
        cross_reply_layout.setOnClickListener {
            answerLayout.isVisible = false
        }
        quetsion_tv.text = question.question
        ReplyAnswer_btn.setOnClickListener {
            ReplyAnswer.text.toString().let {
                if (it.isEmpty()) {
                    showError(getString(R.string.Please_enter, getString(R.string.Answer)))
                } else {
                    PostAnsApi(question._id, it)
                }
            }
        }
    }

}