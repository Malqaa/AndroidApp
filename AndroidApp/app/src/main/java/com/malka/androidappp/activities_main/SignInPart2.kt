package com.malka.androidappp.activities_main

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.login.LoginClass
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.activity_sign_in.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInPart2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


//        val malqa: MalqaApiService = RetrofitBuilder.createAccountsInstance()
  //      val emaill = editText3.text.toString().trim()
    //    val passwordd = editText4.text.toString().trim()
      //  val call: Call<LoginClass?>? = malqa.loginUser(LoginClass(email = "hashir.techxcape@gmail.com", password = "Aptech1@3"))
       // call?.enqueue(object : Callback<LoginClass?> {

         //   override fun onFailure(call: Call<LoginClass?>?, t: Throwable) {

           //     Toast.makeText(this@SignInPart2, "${t.message}", Toast.LENGTH_LONG).show()
            //}

           // override fun onResponse(
             //   call: Call<LoginClass?>,
               // response: Response<LoginClass?>
           // ) {
             //   button.setOnClickListener {

               //     Toast.makeText(
                 //       this@SignInPart2,
                   //     response.message(),
                     //   Toast.LENGTH_LONG
                    //).show()
                //}
            //}
        //})



        }

    }



data class User(val email: String, val password: String)