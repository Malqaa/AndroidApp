package com.malka.androidappp.botmnav_fragments.question_ans_comnt

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.adapter_ques_ans.AdapterQuesAns
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.ModelQuesAnswr
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.Question
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_answer_api.ModelPostAns
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_ask_ques_api_edittext.ModelAskQues
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_comment_api_model.ModelPostComment
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_ques_ans.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuesAnsFragment : BaseActivity(), AdapterQuesAns.OnQAPostItemClickLisentener {
    var AdvId: String = ""
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_ques_ans)

        AdvId = intent.getStringExtra("AdvId") ?: ""
        SharedPreferencesStaticClass.is_selected_ans_or_comment=""
        toolbar_title.text = getString(R.string.QuestionAnswers)
        back_btn.setOnClickListener {
            finish()
        }

        quesAnss(AdvId)

        imageView39.setOnClickListener() {
            confrmAskQues()
        }

        if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
            askques_bottom.visibility = View.GONE
        } else if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
            askques_bottom.visibility = View.VISIBLE
        }

    }



 

    private fun validateAskQuesInputText(): Boolean {
        val Inputemail = editTextques!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            HelpFunctions.ShowLongToast(getString(R.string.Fieldcantbeempty), this)
            false
        } else {
            true
        }
    }


    fun confrmAskQues() {
        if (!validateAskQuesInputText()) {
            return
        } else {
            if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
                askquesApi()
            } else if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid &&
                SharedPreferencesStaticClass.is_selected_ans_or_comment == "answer"
            ) {
                PostAnsApi()
            } else if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid &&
                SharedPreferencesStaticClass.is_selected_ans_or_comment == "comment"
            ) {
                postCommentAPi()
            }

        }

    }


    fun askquesApi() {

        val quesgetInput: String = editTextques.text.toString().trim()
//        val adsId: String = "thol1637CI"
//        val buyersId: String = "119392dd-8b27-4f35-84c8-1ccf50343df7"


        val adsId: String = AdvId
        val buyersId: String = ConstantObjects.logged_userid

//        Toast.makeText(this, "AdvID: "+AdvId, Toast.LENGTH_LONG).show()
//        Toast.makeText(this, "BuyersId: "+buyersId, Toast.LENGTH_LONG).show()

        val insertAskQuesinModel = ModelAskQues(adsId, buyersId, quesgetInput)
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<ModelAskQues> = malqaa.askQues(insertAskQuesinModel)

        call.enqueue(object : Callback<ModelAskQues> {
            override fun onResponse(call: Call<ModelAskQues>, response: Response<ModelAskQues>) {
                if (response.isSuccessful) {
                    editTextques.text = null
                    HelpFunctions.ShowLongToast(getString(R.string.Questionhasbeenpost), this@QuesAnsFragment)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.FailedtopostQuestion), this@QuesAnsFragment)
                }

            }

            override fun onFailure(call: Call<ModelAskQues>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@QuesAnsFragment) }
            }
        })

    }

    fun quesAnss(adsId: String) {
        val malqaa: MalqaApiService =
            RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<ModelQuesAnswr> = malqaa.quesAns(adsId, ConstantObjects.logged_userid)

        val recyclerQuesAns: RecyclerView = findViewById(R.id.quesAns_recyclerview)

        call.enqueue(object : Callback<ModelQuesAnswr> {
            override fun onResponse(
                call: Call<ModelQuesAnswr>, response: Response<ModelQuesAnswr>
            ) {
                if (response.isSuccessful) {
                    recyclerQuesAns.layoutManager = LinearLayoutManager(
                        this@QuesAnsFragment,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recyclerQuesAns.adapter =
                        AdapterQuesAns(response.body()!!.questions, this@QuesAnsFragment)


                    // Get item count

//                    Toast.makeText(this, "Total questions are : "+totalQuestions, Toast.LENGTH_LONG).show()

                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.FailedtogetQuestions), this@QuesAnsFragment)
                }
            }

            override fun onFailure(call: Call<ModelQuesAnswr>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@QuesAnsFragment) }
            }
        })

    }

    fun PostAnsApi() {

        val ansvalue = editTextques.text.toString().trim()
        askques_bottom.visibility = View.GONE

        val malqaa = RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<ModelPostAns> = malqaa.postAnsByQid(
            SharedPreferencesStaticClass.getReqQuestionId_toReplyAns,
            ansvalue,
            ConstantObjects.logged_userid
        )
        call.enqueue(object : Callback<ModelPostAns> {
            override fun onResponse(
                call: Call<ModelPostAns>, response: Response<ModelPostAns>
            ) {
                if (response.isSuccessful) {
                    HelpFunctions.ShowLongToast(getString(R.string.Answerhasbeenposted), this@QuesAnsFragment)
                }
            }

            override fun onFailure(call: Call<ModelPostAns>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@QuesAnsFragment) }

            }
        })
    }

    fun postCommentAPi() {
        val getCommentValue: String = editTextques.getText().toString().trim()
        askques_bottom.visibility = View.GONE

        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelPostComment> = malqaa.postCommentByQId(
            SharedPreferencesStaticClass.getReqQuestionId_toComment,
            getCommentValue,
            ConstantObjects.logged_userid
        )

        call.enqueue(object : Callback<ModelPostComment> {
            override fun onResponse(
                call: Call<ModelPostComment>,
                response: Response<ModelPostComment>
            ) {
                if (response.isSuccessful) {
                    HelpFunctions.ShowLongToast(getString(R.string.CommenthasbeenPosted), this@QuesAnsFragment)

                }
            }

            override fun onFailure(call: Call<ModelPostComment>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@QuesAnsFragment) }
            }
        })
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////On ITEM-CLICK///////////////////////////////////////////////////////////////////////////////////////
    override fun OnQAItemClick(item: Question, position: Int) {
        if (item.answer != null && item.answer.description != null) {
            askques_bottom.visibility = View.GONE
            HelpFunctions.ShowLongToast(getString(R.string.Answerisalreadygiven), this)

        } else {
            ////////////////////////////////Send Answer Apiii///////////////////////////
            SharedPreferencesStaticClass.getReqQuestionId_toReplyAns = item._id
            HelpFunctions.ShowLongToast(
                getString(R.string.TypeYourAnswerBelowTextBoxAgainstTheSelectedQuestion),
                this
            )

            editTextques.hint = getString(R.string.ReplyAnswer)
            askques_bottom.visibility = View.VISIBLE
            SharedPreferencesStaticClass.is_selected_ans_or_comment = "answer"
        }
    }


    override fun onCommentItemClick(item: Question, position: Int) {
        if (item.answer != null && item.answer.description != null) {

            SharedPreferencesStaticClass.getReqQuestionId_toComment = item._id
            HelpFunctions.ShowLongToast(
                getString(R.string.TypeYourCommentBelowTextBoxAgainstTheSelectedQuestion),
                this
            )

            editTextques.hint = getString(R.string.comment)
            askques_bottom.visibility = View.VISIBLE
            SharedPreferencesStaticClass.is_selected_ans_or_comment = "comment"
        } else {
            askques_bottom.visibility = View.GONE
            HelpFunctions.ShowLongToast(
                getString(R.string.FirstGiveAnswerThenYouCanDoComment),
                this
            )

        }

    }

}