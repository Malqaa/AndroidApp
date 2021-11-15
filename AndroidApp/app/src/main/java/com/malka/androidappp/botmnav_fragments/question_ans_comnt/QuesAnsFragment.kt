package com.malka.androidappp.botmnav_fragments.question_ans_comnt

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.adapter_ques_ans.AdapterQuesAns
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_ask_ques_api_edittext.ModelAskQues
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.ModelQuesAnswr
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.get_models_quesans.Question
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_answer_api.ModelPostAns
import com.malka.androidappp.botmnav_fragments.question_ans_comnt.post_comment_api_model.ModelPostComment
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_listan_item.*
import kotlinx.android.synthetic.main.fragment_ques_ans.*
import kotlinx.android.synthetic.main.fragment_ques_ans.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuesAnsFragment : Fragment(), AdapterQuesAns.OnQAPostItemClickLisentener {
    var AdvId: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        AdvId = arguments?.getString("AdvId").toString()

        return inflater.inflate(R.layout.fragment_ques_ans, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_quesans.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_quesans.title = getString(R.string.QuestionAnswers)
        toolbar_quesans.setTitleTextColor(Color.WHITE)
        toolbar_quesans.navigationIcon?.isAutoMirrored = true
        toolbar_quesans.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }

//////////////////////////GETAPi - Shows Recycler Messaging Question_Answer//////////////////////////////
        quesAnss(AdvId)

///////////////////////////////////////Validation check on confrimFunction/////////////////////////////////////////////////
        imageView39.setOnClickListener() {
            confrmAskQues()
        }
////////////////////////////////check buyers id's for ask question Api//////////////////////////////////////

        if (ConstantObjects.logged_userid == SharedPreferencesStaticClass.ad_userid) {
            askques_bottom.visibility = View.GONE
        } else if (ConstantObjects.logged_userid != SharedPreferencesStaticClass.ad_userid) {
            askques_bottom.visibility = View.VISIBLE
        }


        ////////////////////////////check seller id's for ask question layout///////////////////////////////////////////
    }


    private fun validateAskQuesInputText(): Boolean {
        val Inputemail = editTextques!!.text.toString().trim { it <= ' ' }

        return if (Inputemail.isEmpty()) {
            HelpFunctions.ShowLongToast(getString(R.string.Fieldcantbeempty), activity)
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

//        Toast.makeText(activity, "AdvID: "+AdvId, Toast.LENGTH_LONG).show()
//        Toast.makeText(activity, "BuyersId: "+buyersId, Toast.LENGTH_LONG).show()

        val insertAskQuesinModel = ModelAskQues(adsId, buyersId, quesgetInput)
        val malqaa: MalqaApiService = RetrofitBuilder.askQuestion()

        val call: Call<ModelAskQues> = malqaa.askQues(insertAskQuesinModel)

        call.enqueue(object : Callback<ModelAskQues> {
            override fun onResponse(call: Call<ModelAskQues>, response: Response<ModelAskQues>) {
                if (response.isSuccessful) {
                    editTextques.text = null
                    HelpFunctions.ShowLongToast(getString(R.string.Questionhasbeenpost), activity)
                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.FailedtopostQuestion), activity)
                }

            }

            override fun onFailure(call: Call<ModelAskQues>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }
            }
        })

    }

    fun quesAnss(adsId: String) {
        val malqaa: MalqaApiService =
            RetrofitBuilder.getQuesAnsComnt(adsId, ConstantObjects.logged_userid)

        val call: Call<ModelQuesAnswr> = malqaa.quesAns(adsId, ConstantObjects.logged_userid)

        val recyclerQuesAns: RecyclerView =
            requireActivity().findViewById(R.id.quesAns_recyclerview)

        call.enqueue(object : Callback<ModelQuesAnswr> {
            override fun onResponse(
                call: Call<ModelQuesAnswr>, response: Response<ModelQuesAnswr>
            ) {
                if (response.isSuccessful) {
                    recyclerQuesAns.layoutManager = LinearLayoutManager(
                        activity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    recyclerQuesAns.adapter =
                        AdapterQuesAns(response.body()!!.questions, this@QuesAnsFragment)


                    // Get item count

//                    Toast.makeText(activity, "Total questions are : "+totalQuestions, Toast.LENGTH_LONG).show()

                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.FailedtogetQuestions), activity)
                }
            }

            override fun onFailure(call: Call<ModelQuesAnswr>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }
            }
        })

    }

    fun PostAnsApi() {

        val ansvalue = editTextques.text.toString().trim()
        askques_bottom.visibility = View.GONE

        val malqaa: MalqaApiService = RetrofitBuilder.postAns(
            SharedPreferencesStaticClass.getReqQuestionId_toReplyAns,
            ansvalue,
            ConstantObjects.logged_userid
        )

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
                    HelpFunctions.ShowLongToast(getString(R.string.Answerhasbeenposted), activity)
                }
            }

            override fun onFailure(call: Call<ModelPostAns>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }

            }
        })
    }

    fun postCommentAPi() {
        val getCommentValue: String = editTextques.getText().toString().trim()
        askques_bottom.visibility = View.GONE

        val malqaa: MalqaApiService = RetrofitBuilder.postComment(
            SharedPreferencesStaticClass.getReqQuestionId_toComment,
            getCommentValue,
            ConstantObjects.logged_userid
        )
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
                    HelpFunctions.ShowLongToast(getString(R.string.CommenthasbeenPosted), activity)

                }
            }

            override fun onFailure(call: Call<ModelPostComment>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, activity) }
            }
        })
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////On ITEM-CLICK///////////////////////////////////////////////////////////////////////////////////////
    override fun OnQAItemClick(item: Question, position: Int) {
        if (item.answer != null && item.answer.description != null) {
            askques_bottom.visibility = View.GONE
            HelpFunctions.ShowLongToast(getString(R.string.Answerisalreadygiven), activity)

        } else {
            ////////////////////////////////Send Answer Apiii///////////////////////////
            SharedPreferencesStaticClass.getReqQuestionId_toReplyAns = item._id
            HelpFunctions.ShowLongToast(
                getString(R.string.TypeYourAnswerBelowTextBoxAgainstTheSelectedQuestion),
                activity
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
                activity
            )

            editTextques.hint = getString(R.string.comment)
            askques_bottom.visibility = View.VISIBLE
            SharedPreferencesStaticClass.is_selected_ans_or_comment = "comment"
        } else {
            askques_bottom.visibility = View.GONE
            HelpFunctions.ShowLongToast(
                getString(R.string.FirstGiveAnswerThenYouCanDoComment),
                activity
            )

        }

    }

}