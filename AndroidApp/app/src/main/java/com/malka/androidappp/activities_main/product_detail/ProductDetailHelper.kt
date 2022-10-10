package com.malka.androidappp.activities_main.product_detail

import android.app.Activity
import android.content.Context
import android.util.Log
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.questionModel.ModelAskQues
import com.malka.androidappp.servicemodels.questionModel.ModelPostAns
import com.malka.androidappp.servicemodels.questionModel.ModelQuesAnswr
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailHelper(val context: Context) {

    fun getRates( productId:String,onSuccess: (( List<RateResponse.RateReview>) -> Unit)) {
        val apiBuilder = RetrofitBuilder.GetRetrofitBuilder()
        val Rates = apiBuilder.getRates(productId)
        Rates.enqueue(object : Callback<RateResponse> {
            override fun onResponse(
                call: Call<RateResponse>,
                response: Response<RateResponse>
            ) {
                response.body()?.let {
                    it.run{
                        if(status_code==200){
                            onSuccess.invoke(it.data)
                        }
                    }


                }
            }

            override fun onFailure(call: Call<RateResponse>, t: Throwable) {
                Log.d("Api", "Error in fetching rates", t)
            }

        })

    }


    fun quesAnss(adsId: String , onSuccess: ((ModelQuesAnswr) -> Unit)) {
        val malqaa: MalqaApiService =
            RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<ModelQuesAnswr> = malqaa.quesAns(adsId, ConstantObjects.logged_userid)

        call.enqueue(object : Callback<ModelQuesAnswr> {
            override fun onResponse(
                call: Call<ModelQuesAnswr>, response: Response<ModelQuesAnswr>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        onSuccess.invoke( response.body()!!)
                    }
                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.FailedtogetQuestions),
                        context
                    )
                }
            }

            override fun onFailure(call: Call<ModelQuesAnswr>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, context) }
            }
        })

    }


    fun PostAnsApi(questionId: String, answer: String ,onSuccess: ((ModelPostAns) -> Unit)) {

        HelpFunctions.startProgressBar(context as Activity)

        val malqaa = RetrofitBuilder.GetRetrofitBuilder()

        val call: Call<ModelPostAns> = malqaa.postAnsByQid(

            questionId,
            answer,
            ConstantObjects.logged_userid,
        )
        call.enqueue(object : Callback<ModelPostAns> {
            override fun onResponse(
                call: Call<ModelPostAns>, response: Response<ModelPostAns>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        onSuccess.invoke(response.body()!!)
                    }
                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<ModelPostAns>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, context) }
                HelpFunctions.dismissProgressBar()

            }
        })
    }

    fun askquesApi(quesgetInput:String,AdvId:String,onSuccess: (() -> Unit)) {
        HelpFunctions.startProgressBar(context as Activity)

        val insertAskQuesinModel = ModelAskQues(AdvId, ConstantObjects.logged_userid, quesgetInput)
        val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<ModelAskQues> = malqaa.askQues(insertAskQuesinModel)
        call.enqueue(object : Callback<ModelAskQues> {
            override fun onResponse(call: Call<ModelAskQues>, response: Response<ModelAskQues>) {
                if (response.isSuccessful) {
                    onSuccess.invoke()
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.Questionhasbeenpost),
                        context
                    )

                } else {
                    HelpFunctions.ShowLongToast(
                        context. getString(R.string.FailedtopostQuestion),
                        context
                    )
                }

                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<ModelAskQues>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, context) }
                HelpFunctions.dismissProgressBar()

            }
        })

    }
}