package com.malka.androidappp.activities_main.new_flow

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Filter
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import com.malka.androidappp.R
import com.malka.androidappp.base.BaseActivity
import com.malka.androidappp.servicemodels.CategoryTagsModel
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.servicemodels.Tags
import com.malka.androidappp.helper.Extension
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_listan_item.*
import kotlinx.android.synthetic.main.suggested_categories.view.*
import kotlinx.android.synthetic.main.toolbar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListanItem : BaseActivity() {
    var selectTag: Tags? = null
    var lists: List<Tags> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_listan_item)
        toolbar_title.text = getString(R.string.add_product)
        back_btn.setOnClickListener {
            finish()
        }

        button2.setOnClickListener {

            confirmListItem()
        }


        button_2.setOnClickListener {

            if (validateitem()) {

                if (lists.size>0&&selectTag != null) {
                    selectTag!!.run {
                        ConstantObjects.categoryList.filter {
                            it.categoryid == categoryid
                        }.let {
                            if (it.size > 0) {
                                it.get(0).run {
                                    StaticClassAdCreate.subCategoryPath.add(categoryName.toString())
                                    val templateName =
                                        Extension.truncateString(template.toString())
                                    StaticClassAdCreate.template = templateName
                                    startActivity(
                                        Intent(
                                            this@ListanItem,
                                            AddPhoto::class.java
                                        ).apply {
                                            putExtra("Title", categoryName.toString())
                                        })
                                }

                            }
                        }


                    }


                } else {
                    val producttitle: String = textInputLayout11.getText()
                    StaticClassAdCreate.producttitle = producttitle
                    startActivity(Intent(this, ChooseCategory::class.java))
                }


            }

        }

        textInputLayout11._view2()
            .setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (validateitem()) {
                        getCategoryTags(textInputLayout11.getText())
                    }
                    return@OnEditorActionListener true
                }
                true
            })
        textInputLayout11._attachInfoClickListener {
            if (validateitem()) {
                getCategoryTags(textInputLayout11.getText())
            }
        }
    }


    fun confirmListItem() {
        if (!validateitem()) {
            return
        } else {
            val producttitle: String = textInputLayout11.getText()
            StaticClassAdCreate.producttitle = producttitle
            startActivity(Intent(this, ChooseCategory::class.java))
        }

    }

    private fun validateitem(): Boolean {

        val Inputname = textInputLayout11.getText().trim { it <= ' ' }
        return if (Inputname.isEmpty()) {
            textInputLayout11.error = getString(R.string.Fieldcantbeempty)
            false
        } else {
            textInputLayout11.error = null
            true
        }
    }

    fun getCategoryTags(category: String) {
        HelpFunctions.startProgressBar(this)

        try {
            val malqaa: MalqaApiService = RetrofitBuilder.GetRetrofitBuilder2()

            val call: Call<CategoryTagsModel> = malqaa.getCategoryTags(category)

            call.enqueue(object : Callback<CategoryTagsModel> {
                override fun onResponse(
                    call: Call<CategoryTagsModel>, response: Response<CategoryTagsModel>
                ) {
                    if (response.isSuccessful) {

                        if (response.body() != null) {
                            val resp: CategoryTagsModel = response.body()!!
                             lists = resp.data
                            if (lists.count() > 0) {
                                recycler_suggested_category.visibility = View.VISIBLE
                                setCategoryAdaptor(lists)
                            } else {
                                recycler_suggested_category.visibility = View.GONE
                                Toast.makeText(
                                    this@ListanItem,
                                    getString(R.string.no_tag_found),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }


                    } else {
                        Toast.makeText(this@ListanItem, "Failed to get tags", Toast.LENGTH_LONG)
                            .show()
                    }
                    HelpFunctions.dismissProgressBar()

                }

                override fun onFailure(call: Call<CategoryTagsModel>, t: Throwable) {
                    Toast.makeText(this@ListanItem, t.message, Toast.LENGTH_LONG).show()
                    HelpFunctions.dismissProgressBar()

                }
            })
        } catch (ex: Exception) {
            throw ex
        }
    }

    private fun setCategoryAdaptor(list: List<Tags>) {
        recycler_suggested_category.adapter = object : GenericListAdapter<Tags>(
            R.layout.suggested_categories,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        category_name_tv.text = name
                        is_select.isChecked = isSelect
                        is_select.setOnCheckedChangeListener { buttonView, isChecked ->
                            if (isChecked) {
                                list.forEach {
                                    it.isSelect = false
                                }
                                list.get(position).isSelect = true
                                recycler_suggested_category.adapter!!.notifyDataSetChanged()
                            }
                            selectTag = element
                        }
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