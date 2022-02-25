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
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import kotlinx.android.synthetic.main.activity_add_product2.*
import kotlinx.android.synthetic.main.add_product.*
import kotlinx.android.synthetic.main.all_categories_card.view.*
import kotlinx.android.synthetic.main.all_categories_cardview.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class add_product2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product2)


        add_product_button3.setOnClickListener(){

            val  intent = Intent(this@add_product2, add_product3::class.java)
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
                    HelpFunctions.ShowLongToast(getString(R.string.NoCategoriesfound), this@add_product2)

                }
                HelpFunctions.dismissProgressBar()

            }

            override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                t.message?.let { HelpFunctions.ShowLongToast(it, this@add_product2) }
                HelpFunctions.dismissProgressBar()
            }
        })
    }

    private fun setCategoryAdaptor(list: List<AllCategoriesModel>) {
        category_rcv.adapter = object : GenericListAdapter<AllCategoriesModel>(
            R.layout.all_categories_cardview,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        category_name_tv.text=categoryName
//                        category_icon.setImageResource(categoryImage)
                        view_button.hide()
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

    private fun dummyAdaptor(list: ArrayList<String>) {
        category_rcv.adapter = object : GenericListAdapter<String>(
            R.layout.all_categories_cardview,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {

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