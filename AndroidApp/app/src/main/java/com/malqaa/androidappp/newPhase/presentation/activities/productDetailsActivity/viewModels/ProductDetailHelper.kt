package com.malqaa.androidappp.newPhase.presentation.activities.productDetailsActivity.viewModels

import android.app.Activity
import android.content.Context
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelQuesAnswr
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelAskQues
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malqaa.androidappp.newPhase.data.network.service.MalqaApiService
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.domain.models.servicemodels.questionModel.ModelPostAns
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductDetailHelper(val context: Context) {




    fun PostAnsApi(questionId: String, answer: String ,onSuccess: ((ModelPostAns) -> Unit)) {

        HelpFunctions.startProgressBar(context as Activity)

        val malqaa = getRetrofitBuilder()

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


}