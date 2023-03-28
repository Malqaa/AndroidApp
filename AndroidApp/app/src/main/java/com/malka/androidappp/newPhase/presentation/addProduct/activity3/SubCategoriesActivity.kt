package com.malka.androidappp.newPhase.presentation.addProduct.activity3

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.addProduct.activity4.AddPhotoActivity
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.network.retrofit.RetrofitBuilder
import com.malka.androidappp.newPhase.data.network.service.MalqaApiService
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import com.malka.androidappp.newPhase.domain.models.servicemodels.GeneralResponse
import com.malka.androidappp.newPhase.domain.models.servicemodels.model.Category
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubCategoriesActivity : BaseActivity(), AdapterSubCategories.OnItemClickListener {

    var categoryid: String = ""
    var categoryName: String = ""

    var allCategoryList: List<Category> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_sub_categories)
        toolbar_title.text = getString(R.string.subsection)
        back_btn.setOnClickListener {
            finish()
        }

        categoryid = intent.getStringExtra(ConstantObjects.categoryIdKey).toString()
        categoryName = intent.getStringExtra(ConstantObjects.categoryName).toString()
        getSubCategoriesByTemplateID(categoryid)
    }


    private fun getSubCategoriesByTemplateID(categoryId: String) {
        HelpFunctions.startProgressBar(this)
        try {

            val malqaa: MalqaApiService =
                RetrofitBuilder.GetRetrofitBuilder()

            val call: Call<GeneralResponse> =
                malqaa.GetSubCategoryByMainCategory(categoryId)

            call.enqueue(object : Callback<GeneralResponse> {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {

                    if (response.isSuccessful) {

                        response.body()?.run {
                            if (status_code == 200) {
                                allCategoryList = Gson().fromJson(
                                    Gson().toJson(data),
                                    object : TypeToken<ArrayList<Category>>() {}.type
                                )

                                if (allCategoryList.count() > 0) {
                                    allCategoriesRecyclerView.adapter =
                                        AdapterSubCategories(
                                            allCategoryList,
                                            this@SubCategoriesActivity
                                        )

                                    HelpFunctions.dismissProgressBar()
                                } else {
                                    HelpFunctions.dismissProgressBar()
                                    HelpFunctions.ShowLongToast(getString(R.string.noSubCategoryFound),this@SubCategoriesActivity)
                                      goNextScreen()

                                }
                            }

                        }
                    }

                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(this@SubCategoriesActivity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.dismissProgressBar()
            throw ex
        }
    }

    private fun goNextScreen() {
        startActivity(Intent(this, AddPhotoActivity::class.java))
    }

    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)
        AddProductObjectData.selectedCategoryId = allCategoryList[position].id
        AddProductObjectData.selectedCategoryName== allCategoryList[position].name
        goNextScreen()

//        if (!allCategoryList[position].isCategory) {
//            AddProductObjectData.subCategoryPath.add(allCategoryList[position].name.toString())
//
//            val templateName =
//                truncateString(allCategoryList[position].template.toString())
//            AddProductObjectData.template = templateName
//            startActivity(Intent(this, AddPhotoActivity::class.java).apply {
//                putExtra("Title", allCategoryList[position].name.toString())
//                putExtra("file_name", allCategoryList[position].jsonFilePath)
//            })
//
//        } else {
//
//            AddProductObjectData.subCategoryPath.add(allCategoryList[position].name.toString())
//
//            getSubCategoriesByTemplateID(allCategoryList[position].categoryKey!!)
//
//        }
    }


}