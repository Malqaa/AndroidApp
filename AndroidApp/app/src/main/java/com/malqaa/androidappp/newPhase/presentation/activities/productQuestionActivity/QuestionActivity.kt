package com.malqaa.androidappp.newPhase.presentation.activities.productQuestionActivity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ActivityQuestionBinding
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.questionResp.QuestionItem
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.AnswerQuestionDialog
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.adapter.QuestionAnswerAdapter
import com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels.ProductDetailsViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class QuestionActivity : BaseActivity<ActivityQuestionBinding>(),
    QuestionAnswerAdapter.SetonSelectedQuestion {
    var productId: Int = 0
    private var productDetailsViewModel: ProductDetailsViewModel? = null
    lateinit var subQuestionsList: ArrayList<QuestionItem>
    lateinit var questionAnswerAdapter: QuestionAnswerAdapter
    private var isMyProduct = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbarMain.toolbarTitle.text = getString(R.string.questions_and_answers)
        productId = intent.getIntExtra(ConstantObjects.productIdKey, 0)
        isMyProduct = intent.getBooleanExtra(ConstantObjects.isMyProduct, false)
        setQuestionAnswerAdapter()
        setClickListeners()
        setProductDetailsViewModel()
        binding.tvNumberQuestionNotAnswer.text =
            getString(R.string.there_are_2_questions_that_the_seller_did_not_answer, "0")
        if (HelpFunctions.isUserLoggedIn()) {
            binding.containerMainAskQuestion.show()
        } else {
            binding.containerMainAskQuestion.hide()
        }
        if (isMyProduct) {
            binding.containerMainAskQuestion.hide()
        }

        productDetailsViewModel!!.getListOfQuestionsForActivity(productId)
    }

    private fun setProductDetailsViewModel() {
        productDetailsViewModel = ViewModelProvider(this).get(ProductDetailsViewModel::class.java)
        productDetailsViewModel!!.isLoading.observe(this) {
            if (it)
                HelpFunctions.startProgressBar(this)
            else
                HelpFunctions.dismissProgressBar()
        }
        productDetailsViewModel!!.isNetworkFail.observe(this) {
            if (it) {
                showProductApiError(getString(R.string.connectionError))
            } else {
                showProductApiError(getString(R.string.serverError))
            }

        }
        productDetailsViewModel!!.errorResponseObserver.observe(this) {
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null) {
                    showProductApiError(it.message!!)
                } else {
                    showProductApiError(getString(R.string.serverError))
                }
            }
        }

        productDetailsViewModel!!.addQuestionObservable.observe(this) { questResp ->
            HelpFunctions.ShowLongToast(questResp.message, this)
            if (questResp.status_code == 200) {
                binding.etWriteQuestion.setText("")
                questResp.question?.let {
                    val tempArraylist: ArrayList<QuestionItem> = ArrayList()
                    tempArraylist.addAll(subQuestionsList)
                    subQuestionsList.clear()
                    subQuestionsList.add(it)
                    subQuestionsList.addAll(tempArraylist)
                    questionAnswerAdapter.notifyDataSetChanged()
                    binding.rvQuestionForProduct.scrollToPosition(0)
                }
            }
        }
        productDetailsViewModel!!.getListOfQuestionsObservable.observe(this) { questionListResp ->
            if (!questionListResp.questionList.isNullOrEmpty()) {
                binding.tvErrorNoQuestion.hide()
                setQuestionsView(questionListResp.questionList)
            } else {
                binding.tvErrorNoQuestion.show()
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
                binding.tvNumberQuestionNotAnswer.text = getString(
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
        binding.toolbarMain.backBtn.setOnClickListener {
            finish()
        }
        binding.contianerAskQuestion.setOnClickListener {
            confrmAskQues()
        }
    }

    private fun setQuestionAnswerAdapter() {
        subQuestionsList = ArrayList()
        questionAnswerAdapter = QuestionAnswerAdapter(subQuestionsList, this)
        binding.rvQuestionForProduct.apply {
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
            adapter = questionAnswerAdapter
        }
    }

    private fun confrmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            askquesApi()
        }

    }

    private fun validateAskQuesInputText(): Boolean {
        val inputEmail = binding.etWriteQuestion!!.text.toString().trim { it <= ' ' }

        return if (inputEmail.isEmpty()) {
            showError(getString(R.string.Please_enter, getString(R.string.Question)))
            false
        } else {
            true
        }
    }

    private fun askquesApi() {
        productDetailsViewModel!!.addQuestion(
            productId,
            binding.etWriteQuestion.text.trim().toString()
        )
    }

    override fun onSelectQuestion(position: Int) {
        if (isMyProduct) {
            val answerDialog = AnswerQuestionDialog(productDetailsViewModel!!,
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

    override fun onDestroy() {
        super.onDestroy()
        productDetailsViewModel!!.closeAllCall()
    }

}