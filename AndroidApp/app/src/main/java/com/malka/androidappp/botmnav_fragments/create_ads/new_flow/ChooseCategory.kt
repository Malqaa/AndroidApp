package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.ChooseCateFragment
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.new_flow.all_categories.AdapterAllCategories
import com.malka.androidappp.botmnav_fragments.create_ads.new_flow.all_categories.SubCategories
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.helper.Extension.truncateString
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_choose_cate.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseCategory : BaseActivity() {
    var allCategoryList: List<AllCategoriesModel> = ArrayList()
    var position=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_choose_cate)
        toolbar_title.text=getString(R.string.choose_department)
        back_btn.setOnClickListener {
            finish()
        }

        add_product_button4.setOnClickListener {
            if (!allCategoryList[position].isCategory) {

                StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())



                val templateName =
                    truncateString(allCategoryList[position].template.toString())
                StaticClassAdCreate.template = templateName
                startActivity(Intent(this, AddPhoto::class.java).apply {
                    putExtra("Title", allCategoryList[position].categoryName.toString())
                })
            }else {

                StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())
                startActivity(Intent(this, SubCategories::class.java).apply {
                    putExtra("categoryid", allCategoryList[position].categoryKey.toString())
                    putExtra("categoryName", allCategoryList[position].categoryName.toString())
                })
            }

        }
        getAllCategories()

    }



    // To get all categories
    fun getAllCategories() {

        try {
            val malqaa: MalqaApiService = RetrofitBuilder.getAllCategories()
            val call: Call<AllCategoriesResponseBack> = malqaa.getAllCategories()

            call.enqueue(object : Callback<AllCategoriesResponseBack> {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onResponse(
                    call: Call<AllCategoriesResponseBack>,
                    response: Response<AllCategoriesResponseBack>
                ) {

                    if (response.isSuccessful) {

                        if (response.body() != null) {

                            val resp: AllCategoriesResponseBack = response.body()!!
                            allCategoryList = resp.data

                            if (allCategoryList.count() > 0) {



                                if(allCategoryList.size>0){
                                    allCategoryList.get(0).is_select=true
                                }
                                recycler_all_category.adapter =
                                    AdapterAllCategories(
                                        allCategoryList,
                                        ChooseCateFragment(),{
                                            position=it
                                        }
                                    )
                                HelpFunctions.dismissProgressBar()
                            } else {
                                HelpFunctions.dismissProgressBar()
                                Toast.makeText(this@ChooseCategory, "No Categories found", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } else {
                        HelpFunctions.dismissProgressBar()
                        Toast.makeText(this@ChooseCategory, "No Categories found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(this@ChooseCategory, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }







}