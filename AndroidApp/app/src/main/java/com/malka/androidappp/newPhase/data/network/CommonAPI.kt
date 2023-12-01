package com.malka.androidappp.newPhase.data.network

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.data.helper.ConstantObjects
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder.getRetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.countryResp.Country
import com.malka.androidappp.newPhase.domain.models.servicemodels.*
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardModel
import com.malka.androidappp.newPhase.domain.models.servicemodels.creditcard.CreditCardResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.user.UserObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonAPI {


    fun GetUserInfo(context: Context, userid: String, onSuccess: () -> Unit) {
        HelpFunctions.startProgressBar(context as Activity)

        val malqa: MalqaApiService = getRetrofitBuilder()
        val call: Call<UserObject> = malqa.getuser(userid)
        call.enqueue(object : Callback<UserObject> {
            override fun onResponse(call: Call<UserObject>, response: Response<UserObject>) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        response.body()!!.run {
                            if (status_code == 200) {
                               // ConstantObjects.userobj = response.body()!!.data
                                ConstantObjects.logged_userid = response.body()!!.data.id ?: ""
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

    fun getAddress(
        context: Context,
        onSuccess: ((data: List<GetAddressResponse.AddressModel>) -> Unit)
    ) {

        HelpFunctions.startProgressBar(context as Activity)

        val malqa: MalqaApiService = getRetrofitBuilder()
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

    fun GetUserCreditCards(context: Context, onSuccess: ((data: List<CreditCardModel>) -> Unit)) {
        HelpFunctions.startProgressBar(context as Activity)

        val malqa: MalqaApiService = getRetrofitBuilder()
        val call: Call<CreditCardResponse> =
            malqa.getUserCreditCards(ConstantObjects.logged_userid)
        call.enqueue(object : Callback<CreditCardResponse?> {
            override fun onFailure(call: Call<CreditCardResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<CreditCardResponse?>,
                response: Response<CreditCardResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val resp: CreditCardResponse = response.body()!!
                        ConstantObjects.usercreditcard = resp.data
                        onSuccess.invoke(resp.data)
                    } else {
                        HelpFunctions.ShowLongToast(
                            "No Record Found", context
                        )
                    }
                }
                HelpFunctions.dismissProgressBar()
            }
        })

    }


    fun getSoldItemsApi(
        userId: String,
        context: Context,
        onSuccess: ((data: ModelSoldUnsold.Data) -> Unit)
    ) {
        HelpFunctions.startProgressBar(context as Activity)

        val malqaa: MalqaApiService = getRetrofitBuilder()
        val call: Call<ModelSoldUnsold> = malqaa.getsolditemsbyId(userId)


        call.enqueue(object : Callback<ModelSoldUnsold> {
            @SuppressLint("ResourceType")
            override fun onResponse(
                call: Call<ModelSoldUnsold>,
                response: Response<ModelSoldUnsold>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        onSuccess.invoke(response.body()!!.data)

                    }

                } else {
                    HelpFunctions.ShowLongToast(
                        context.getString(R.string.ErrorOccur),
                        context
                    )

                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<ModelSoldUnsold>, t: Throwable) {
                t.message?.let {
                    HelpFunctions.ShowLongToast(
                        it,
                        context
                    )
                }
                HelpFunctions.dismissProgressBar()

            }
        })

    }

    fun getCountry() {


        val malqa: MalqaApiService = getRetrofitBuilder()
        val call = malqa.getCountry()
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {

                if (response.isSuccessful) {

                    response.body()?.run {
                        if (status_code == 200) {
                            val list: ArrayList<Country> = Gson().fromJson(
                                Gson().toJson(data),
                                object : TypeToken<ArrayList<Country>>() {}.type
                            )
                            ConstantObjects.countryList = list
                        }
                    }
                }

            }
        })
    }


    fun getRegion(key: Int,activity: Activity, onResponse: (list: ArrayList<Country>) -> Unit) {
        HelpFunctions.startProgressBar(activity)
        val malqa: MalqaApiService = getRetrofitBuilder()
        val call = malqa.getRegion(key)
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                HelpFunctions.dismissProgressBar()

                if (response.isSuccessful) {

                    response.body()?.run {
                        if (status_code == 200) {
                            val list: ArrayList<Country> = Gson().fromJson(
                                Gson().toJson(data),
                                object : TypeToken<ArrayList<Country>>() {}.type
                            )
                            onResponse.invoke(list)
                           
                        }
                    }
                }
                

            }
        })


    }

    fun getRegion(key: Int,onResponse: (list: ArrayList<Country>) -> Unit) {
        val malqa: MalqaApiService = getRetrofitBuilder()
        val call = malqa.getRegion(key)
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()

            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                HelpFunctions.dismissProgressBar()

                if (response.isSuccessful) {

                    response.body()?.run {
                        if (status_code == 200) {
                            val list: ArrayList<Country> = Gson().fromJson(
                                Gson().toJson(data),
                                object : TypeToken<ArrayList<Country>>() {}.type
                            )
                            onResponse.invoke(list)

                        }
                    }
                }


            }
        })


    }
    fun getCity(key: Int,activity: Activity, onResponse: (list: ArrayList<Country>) -> Unit) {
        HelpFunctions.startProgressBar(activity)


        val malqa: MalqaApiService = getRetrofitBuilder()
        val call = malqa.getCity(key)
        call.enqueue(object : Callback<GeneralResponse?> {
            override fun onFailure(call: Call<GeneralResponse?>?, t: Throwable) {
                HelpFunctions.dismissProgressBar()
            }

            override fun onResponse(
                call: Call<GeneralResponse?>,
                response: Response<GeneralResponse?>
            ) {
                HelpFunctions.dismissProgressBar()
                if (response.isSuccessful) {

                    response.body()?.run {
                        if (status_code == 200) {
                            val list: ArrayList<Country> = Gson().fromJson(
                                Gson().toJson(data),
                                object : TypeToken<ArrayList<Country>>() {}.type
                            )
                            onResponse.invoke(list)

                        }
                    }
                }


            }
        })


    }

}