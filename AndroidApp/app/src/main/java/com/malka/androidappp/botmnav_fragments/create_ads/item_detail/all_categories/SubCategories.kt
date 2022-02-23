package com.malka.androidappp.botmnav_fragments.create_ads.item_detail.all_categories

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.create_ads.ChooseCateFragment
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesModel
import com.malka.androidappp.botmnav_fragments.home.model.AllCategoriesResponseBack
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_sub_categories.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SubCategories : Fragment(), AdapterSubCategories.OnItemClickListener {

    var categoryid: String = ""
    var categoryName: String = ""

    val allCategoryList: ArrayList<AllCategoriesModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        HelpFunctions.startProgressBar(requireActivity())
        allCategoryList.clear()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        categoryid = arguments?.getString("categoryid").toString()
        categoryName = arguments?.getString("categoryName").toString()


        toolbar_sub_cats.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_sub_cats.title = categoryName
        toolbar_sub_cats.setTitleTextColor(Color.WHITE)
        toolbar_sub_cats.setNavigationOnClickListener {

            StaticClassAdCreate.subCategoryPath.clear()
            findNavController().navigate(R.id.back_to_cat)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                StaticClassAdCreate.subCategoryPath.clear()
                findNavController().navigate(R.id.back_to_cat)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        getSubCategoriesByTemplateID(categoryid.toInt())

    }

    private fun getSubCategoriesByTemplateID(categoryParentId: Int) {
        try {
            val malqaa: MalqaApiService =
                RetrofitBuilder.getAllCategoriesByTemplateID(categoryParentId)

            val call: Call<AllCategoriesResponseBack> =
                malqaa.getAllCategoriesByTemplateID(categoryParentId)

            call.enqueue(object : Callback<AllCategoriesResponseBack> {
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onResponse(
                    call: Call<AllCategoriesResponseBack>,
                    response: Response<AllCategoriesResponseBack>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            var resp: AllCategoriesResponseBack = response.body()!!
                            var lists: List<AllCategoriesModel> = resp.data

                            if (lists != null && lists.count() > 0) {
                                for (IndCategories in lists) {
                                    allCategoryList.add(
                                        AllCategoriesModel(
                                            IndCategories.id ?: "0",
                                            IndCategories.categoryid ?: 0,
                                            IndCategories.categoryName ?: "",
                                            IndCategories.categoryKey ?: "0",
                                            IndCategories.categoryParentId ?: 0,
                                            IndCategories.isCategory,
                                            IndCategories.isActive,
                                            IndCategories.createdBy ?: "0",
                                            IndCategories.createdOn ?: "0",
                                            IndCategories.template ?: ""
                                        )
                                    )
                                }
                                val allCategoriesRecyclerView: RecyclerView =
                                    requireActivity().findViewById(R.id.recycler_sub_category)

                                allCategoriesRecyclerView.layoutManager =
                                    LinearLayoutManager(
                                        activity,
                                        LinearLayoutManager.VERTICAL,
                                        false
                                    )
                                allCategoriesRecyclerView.adapter =
                                    AdapterSubCategories(
                                        allCategoryList,
                                        SubCategories(),
                                        this@SubCategories
                                    )
                                HelpFunctions.dismissProgressBar()
                            } else {
                                HelpFunctions.dismissProgressBar()
                                Toast.makeText(context, "No Categories found", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    } else {
                        HelpFunctions.dismissProgressBar()
                        Toast.makeText(context, "No Categories found", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<AllCategoriesResponseBack>, t: Throwable) {
                    HelpFunctions.dismissProgressBar()
                    Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
                }
            })
        } catch (ex: Exception) {
            HelpFunctions.dismissProgressBar()
            throw ex
        }
    }

    override fun OnItemClickHandler(position: Int) {
        super.OnItemClickHandler(position)
        if (allCategoryList != null) {
            if (!allCategoryList[position].isCategory) {

                StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())

                var templateName =
                    ChooseCateFragment.truncateString(allCategoryList[position].template.toString())
                StaticClassAdCreate.template = templateName
                val args = Bundle()
                args.putString("Title", allCategoryList[position].categoryName.toString())
                args.putString("file_name", allCategoryList[position].categoryKey.toString())
                findNavController().navigate(R.id.sub_cat_to_add_photo, args)

            } else {

                StaticClassAdCreate.subCategoryPath.add(allCategoryList[position].categoryName.toString())

                HelpFunctions.startProgressBar(requireActivity())
                getSubCategoriesByTemplateID(allCategoryList[position].categoryid!!.toInt())
                toolbar_sub_cats.title = allCategoryList[position].categoryName
                allCategoryList.clear()

            }
        }
    }
}