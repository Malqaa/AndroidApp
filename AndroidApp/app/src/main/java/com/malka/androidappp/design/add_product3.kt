package com.malka.androidappp.design

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import androidx.appcompat.app.AppCompatActivity
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.activity_add_product2.category_rcv
import kotlinx.android.synthetic.main.activity_add_product3.*
import kotlinx.android.synthetic.main.activity_selection_product.view.*
import kotlinx.android.synthetic.main.add_product.*
import kotlinx.android.synthetic.main.add_product.choose_Department
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class add_product3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product3)


        add_product_button4.setOnClickListener(){

            val  intent = Intent(this@add_product3, add_product4::class.java)
            startActivity(intent)
        }


        getAllCategories()


    }


    fun getAllCategories() {
        HelpFunctions.startProgressBar(this)
        val call = RetrofitBuilder.getAllCategories().getAllCategories()

        call.enqueue(object : Callback<AllCategoriesResponseBack> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<AllCategoriesResponseBack>,
                response: Response<AllCategoriesResponseBack>
            ) {

                if (response.isSuccessful) {

                    if (response.body() != null) {
                        val respone: AllCategoriesResponseBack = response.body()!!
                        val categoryList=respone.data
                        setCategoryAdaptor(categoryList)

                    }

                } else {
                    HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), this@add_product3)

                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@add_product3) }
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun setCategoryAdaptor(list: List<AllCategoriesModel>) {
        category_rcv.adapter = object : GenericListAdapter<AllCategoriesModel>(
            R.layout.activity_selection_product,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        add_product_tv.text=categoryName
                    }
                }
            }
        ) {
            override fun getFilter(): Filter {
                TODO("Not yet implemented")
            }

        }.apply {
            submitList(
                list
            )
        }
    }
}