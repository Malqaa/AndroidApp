package com.malka.androidappp.botmnav_fragments.feedback_frag.insert_feedback

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_product.CreateProduct2
import com.malka.androidappp.botmnav_fragments.create_product.CreateProductResponseBack
import com.malka.androidappp.botmnav_fragments.create_product.ModelCreateProduct
import com.malka.androidappp.botmnav_fragments.create_product.StaticClassProductCreate
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_create_product_pg2.*
import kotlinx.android.synthetic.main.fragment_create_product_pg2.toolbar_createpg2
import kotlinx.android.synthetic.main.fragment_give_feedback.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class GiveFeedback : Fragment() {

    var AdvId: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        

        AdvId = arguments?.getString("AdvId").toString()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_give_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_give_feedback.title = "Feedback"
        toolbar_give_feedback.setTitleTextColor(Color.WHITE)
        toolbar_give_feedback.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_give_feedback.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        btn_confirm_feedback.setOnClickListener() {
            val feedBackRate: Int = checkFeedback()
            if (!TextUtils.isEmpty(feedback_description.text.toString()) && feedBackRate != -1) {
                giveFeedBack()
            } else {
                Toast.makeText(context, "Please enter some description ", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }

    fun checkFeedback(): Int {
        return when {
            btn_positive.isChecked -> {
                2
            }
            btn_negative.isChecked -> {
                1
            }
            btn_neutral.isChecked -> {
                0
            }
            else -> -1
        }
    }

    private fun giveFeedBack() {
        try {
            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder()
            val resp = ModelGiveFeedBack(
                loginUserId = ConstantObjects.logged_userid,
                sellerId = SharedPreferencesStaticClass.ad_userid,
                advId = AdvId,
                description = feedback_description.text.toString(),
                rating = checkFeedback()
            )

            val call: Call<GiveFeedbackResponseBack> = malqaa.giveFeedback(resp)
            call.enqueue(object : Callback<GiveFeedbackResponseBack> {

                override fun onResponse(
                    call: Call<GiveFeedbackResponseBack>,
                    response: Response<GiveFeedbackResponseBack>
                ) {
                    if (response.isSuccessful) {

                        feedback_description.text = null
                        Toast.makeText(
                            activity,
                            "Posted Feedback Successfully",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(activity, "Failed to post feedback", Toast.LENGTH_LONG)
                            .show()
                    }
                }

                override fun onFailure(call: Call<GiveFeedbackResponseBack>, t: Throwable) {
                    Toast.makeText(
                        activity,
                        t.message + "Failed to post feedback",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }
}