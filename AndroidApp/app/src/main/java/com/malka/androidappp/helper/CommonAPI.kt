package com.malka.androidappp.helper

import android.app.Activity
import android.content.Context
import com.malka.androidappp.R
import com.malka.androidappp.design.Models.GetAddressResponse
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.user.UserObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonAPI {


    fun GetUserInfo(context:Context,userid: String, onSuccess: () -> Unit) {
        HelpFunctions.startProgressBar(context as Activity)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call: Call<UserObject> = malqa.getuser(userid)
        call.enqueue(object : Callback<UserObject> {
            override fun onResponse(call: Call<UserObject>, response: Response<UserObject>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        response.body()!!.run {
                            if(status_code==200){
                                ConstantObjects.userobj = response.body()!!.data
                                ConstantObjects.logged_userid = response.body()!!.data.id?:""
                                onSuccess.invoke()
                            }
                        }
                    }
                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<UserObject>, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }
        })
    }

    fun getAddress(context: Context, onSuccess: ((data: List<GetAddressResponse.AddressModel>) -> Unit)) {

        HelpFunctions.startProgressBar(context as Activity)

        val malqa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
        val call = malqa.getAddress(ConstantObjects.logged_userid)


        call.enqueue(object : Callback<GetAddressResponse?> {
            override fun onFailure(call: Call<GetAddressResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<GetAddressResponse?>,
                response: Response<GetAddressResponse?>
            ) {
                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: GetAddressResponse = response.body()!!
                        if (respone.status_code == 200) {
                            onSuccess.invoke(respone.data)
                        } else {

                            HelpFunctions.ShowLongToast(
                                context.getString(R.string.ErrorOccur),
                                context
                            )
                        }
                    }

                }
                HelpFunctions.dismissProgressBar()
            }
        })


    }

}