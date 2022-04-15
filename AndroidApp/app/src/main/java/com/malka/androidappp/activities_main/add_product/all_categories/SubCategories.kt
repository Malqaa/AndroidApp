package com.malka.androidappp.activities_main.add_product.all_categories

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.activities_main.add_product.AddPhoto
import com.malka.androidappp.activities_main.add_product.DynamicTemplate
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.helper.Extension.truncateString
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubCategories :  BaseActivity() , AdapterSubCategories.OnItemClickListener {

    var categoryid: String = ""
    var categoryName: String = ""

    var allCategoryList: List<AllCategoriesModel> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sub_categories)
        toolbar_title.text = getString(R.string.subsection)
        back_btn.setOnClickListener {
            finish()
        }


        categoryid = intent.getStringExtra("categoryid").toString()
        categoryName = intent.getStringExtra("categoryName").toString()

        getSubCategoriesByTemplateID(categoryid)
    }




    private fun getSubCategoriesByTemplateID(categoryKey: String) {
        HelpFunctions.startProgressBar(this)


        try {

            val malqaa: MalqaApiService =
                RetrofitBuilder.GetRetrofitBuilder()

            val call: Call<AllCategoriesResponseBack> =
                malqaa.getAllCategoriesByTemplateID(categoryKey,culture())

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


                                allCategoriesRecyclerView.adapter =
                                    AdapterSubCategories(
                                        allCategoryList,
                                        this@SubCategories
                                    )

                                HelpFunctions.dismissProgressBar()
                            } else {
                                HelpFunctions.dismissProgressBar()
                                Toast.makeText(this@SubCategories, "No Categories found", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } else {
                        HelpFunctions.dismissProgressBar()
                        Toast.makeText(this@SubCategories, "No Categories found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(this@SubCategories, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.dismissProgressBar()
            throw ex
        }
    }

    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)
        if (!allCategoryList[position].isCategory) {

            StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())

            val templateName =
                truncateString(allCategoryList[position].template.toString())
            StaticClassAdCreate.template = templateName
            startActivity(Intent(this, AddPhoto::class.java).apply {
                putExtra("Title", allCategoryList[position].categoryName.toString())
                putExtra("file_name", allCategoryList[position].jsonFilePath)
            })

        } else {

            StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())

            getSubCategoriesByTemplateID(allCategoryList[position].categoryKey!!)

        }
    }




}