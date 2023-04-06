package com.malka.androidappp.newPhase.presentation.productQuestionActivity

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.domain.models.servicemodels.questionModel.Question
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.*
import com.malka.androidappp.newPhase.data.helper.swipe.MessageSwipeController
import com.malka.androidappp.newPhase.data.helper.swipe.SwipeControllerActions
import com.malka.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.AnswerQuestionDialog
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailHelper
import com.malka.androidappp.newPhase.presentation.productDetailsActivity.viewModels.ProductDetailsViewModel
import kotlinx.android.synthetic.main.activity_question.containerMainAskQuestion
import kotlinx.android.synthetic.main.activity_question.contianerAskQuestion
import kotlinx.android.synthetic.main.activity_question.etWriteQuestion
import kotlinx.android.synthetic.main.activity_question.rvQuestionForProduct
import kotlinx.android.synthetic.main.activity_question.tvErrorNoQuestion
import kotlinx.android.synthetic.main.activity_question.tvNumberQuestionNotAnswer
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : BaseActivity(), QuestionAnswerAdapter.SetonSelectedQuestion {
    var productId: Int = 0
    lateinit var productDetailHelper: ProductDetailHelper
  //  var questionList: MutableList<Question> = ArrayList()
    private lateinit var productDetialsViewModel: ProductDetailsViewModel
    lateinit var subQuestionsList: ArrayList<QuestionItem>
    lateinit var questionAnswerAdapter: QuestionAnswerAdapter
    var isMyProduct=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        toolbar_title.text = getString(R.string.questions_and_answers)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, 0)
        isMyProduct=intent.getBooleanExtra(ConstantObjects.isMyProduct,false)
        setQuestionAnswerAdapter()
        setClickListeners()
        setProductDetailsViewModel()
        tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")
        if (HelpFunctions.isUserLoggedIn()) {
            containerMainAskQuestion.show()
        } else {
            containerMainAskQuestion.hide()
        }
        if(isMyProduct){
            containerMainAskQuestion.hide()
        }

        productDetialsViewModel.getListOfQuestionsForActivity(productId)
        // productDetailHelper= ProductDetailHelper(this)
        //   GenericAdaptor(). questionAnswerAdaptor(rvQuestionForProduct,questionList)
//        quesAnss()
//
    }

    private fun setProductDetailsViewModel() {
        productDetialsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetialsViewModel.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        productDetialsViewModel.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productDetialsViewModel.errorResponseObserver.observe(this) {
            if (it.message != null) {
                showProductApiError(it.message)
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }

        productDetialsViewModel.addQuestionObservable.observe(this) { questResp ->
            HelpFunctions.ShowLongToast(questResp.message, this)
            if (questResp.status_code == 200) {
                etWriteQuestion.setText("")
                questResp.question?.let {
                    var tempArraylist: ArrayList<QuestionItem> = ArrayList()
                    tempArraylist.addAll(subQuestionsList)
                    subQuestionsList.clear()
                    subQuestionsList.add(it)
                    subQuestionsList.addAll(tempArraylist)
                    questionAnswerAdapter.notifyDataSetChanged()
                    rvQuestionForProduct.scrollToPosition(0)
                }
                //resetQuestionAndAnswerAdapter()
            }
        }
        productDetialsViewModel.getListOfQuestionsObservable.observe(this) { questionListResp ->
            if (questionListResp.questionList != null && questionListResp.questionList.isNotEmpty()) {
                tvErrorNoQuestion.hide()
                setQuestionsView(questionListResp.questionList)
            } else {
                tvErrorNoQuestion.show()
            }
        }

    }

    private fun setQuestionsView(questionList: List<QuestionItem>) {
        subQuestionsList.clear()
        subQuestionsList.addAll(questionList)
        questionAnswerAdapter.notifyDataSetChanged()
        lifecycleScope.launch(Dispatchers.IO) {
            var numberOfNotAnswerYet = 0
            for (question in subQuestionsList) {
                if (question.answer == null || question.answer == "") {
                    numberOfNotAnswerYet += 1
                }
            }
            withContext(Dispatchers.Main) {
                tvNumberQuestionNotAnswer.text = getString(
                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
                    numberOfNotAnswerYet.toString()
                )
            }
        }
    }

    private fun showProductApiError(message: String) {

        HelpFunctions.ShowLongToast(message, this)
    }

    private fun setClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }
        contianerAskQuestion.setOnClickListener {
            confrmAskQues()
        }
    }

    private fun setQuestionAnswerAdapter() {
        subQuestionsList = ArrayList()
        questionAnswerAdapter = QuestionAnswerAdapter(subQuestionsList,this)
        rvQuestionForProduct.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = questionAnswerAdapter
        }
    }
    fun confrmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            askquesApi()
        }

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

    fun askquesApi() {
//        productDetailHelper.askquesApi(etWriteQuestion.text.toString(), productId.toString(), {
//            etWriteQuestion.setText("")
//            quesAnss()
//        })
        productDetialsViewModel.addQuestion(productId, etWriteQuestion.text.trim().toString())
    }

        override fun onSelectQuestion(position: Int) {
        if (isMyProduct) {
            var answerDialog = AnswerQuestionDialog(
                this,
                subQuestionsList[position],
                position,
                object : AnswerQuestionDialog.SetOnSendAnswer {
                    override fun onAnswerSuccess(questionItem: QuestionItem, position: Int) {
                        subQuestionsList[position] = questionItem
                        questionAnswerAdapter.notifyItemChanged(position)
                    }
                })
            answerDialog.show()


        }
    }

    /****************/
    /****************/
    /****************/



//    fun quesAnss() {
//        productDetailHelper.quesAnss(productId.toString()) {
//            it.run {
//                tvNumberQuestionNotAnswer.text = getString(
//                    R.string.there_are_2_questions_that_the_seller_did_not_answer,
//                    unAnswered.toString()
//                )
//                questionList.clear()
//                questionList.addAll(questions)
//                rvQuestionForProduct.adapter!!.notifyDataSetChanged()
//                if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
//                    containerMainAskQuestion.visibility = View.GONE
//                    enableSwipeToDeleteAndUndo()
//
//                } else if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
//                    containerMainAskQuestion.visibility = View.VISIBLE
//
//                }
//            }
//
//        }
//
//    }
//
//
//    fun PostAnsApi(questionId: String, answer: String) {
//
//
//        productDetailHelper.PostAnsApi(questionId, answer) { respone ->
//
//
//            if (respone.status_code >= 200 || respone.status_code <= 299) {
//                answerLayout.isVisible = false
//                ReplyAnswer.setText("")
//                quesAnss()
//                HelpFunctions.ShowLongToast(
//                    getString(R.string.Answerhasbeenposted),
//                    this@QuestionActivity
//                )
//
//            }
//        }
//
//    }
//
//
//    private fun enableSwipeToDeleteAndUndo() {
//        val messageSwipeController =
//            MessageSwipeController(this, object : SwipeControllerActions {
//                override fun showReplyUI(position: Int) {
//                    val question = questionList.get(position)
//                    replyItemClicked(question)
//                    vibration()
//                }
//            })
//        val itemTouchHelper = ItemTouchHelper(messageSwipeController)
//        itemTouchHelper.attachToRecyclerView(rvQuestionForProduct)
//
//    }
//
//    private fun replyItemClicked(question: Question) {
//
//        answerLayout.isVisible = true
//        cross_reply_layout.setOnClickListener {
//            answerLayout.isVisible = false
//        }
//        quetsion_tv.text = question.question
//        ReplyAnswer_btn.setOnClickListener {
//            ReplyAnswer.text.toString().let {
//                if (it.isEmpty()) {
//                    showError(getString(R.string.Please_enter, getString(R.string.Answer)))
//                } else {
//                    PostAnsApi(question._id, it)
//                }
//            }
//        }
//    }
//


}