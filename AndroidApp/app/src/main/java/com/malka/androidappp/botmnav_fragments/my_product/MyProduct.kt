package com.malka.androidappp.botmnav_fragments.my_product

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.network.Retrofit.RetrofitBuilder
import com.malka.androidappp.network.service.MalqaApiService
import kotlinx.android.synthetic.main.fragment_my_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyProduct : Fragment(), AdapterMyProduct.OnItemClickListener {
    var res: String? = ""
    var count = 1
    val myProductPosts: ArrayList<ModelMyProduct> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        

        HelpFunctions.startProgressBar(requireActivity())
        myProductPosts.clear()
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myproduct_toolbar.title = getString(R.string.MyProducts)
        myproduct_toolbar.setTitleTextColor(Color.WHITE)
        myproduct_toolbar.navigationIcon?.isAutoMirrored = true
        myproduct_toolbar.setNavigationIcon(R.drawable.nav_icon_back)
        myproduct_toolbar.setNavigationOnClickListener() { requireActivity().onBackPressed() }


        getAllProducts()

        main_create_product.setOnClickListener() {
            findNavController().navigate(R.id.create_a_product)
        }


    }


    fun getAllProducts() {

        val malqaa: MalqaApiService = RetrofitBuilder.getAllProducts()
        val call: Call<AllProductsResponseBack> = malqaa.getAllProducts()

        call.enqueue(object : Callback<AllProductsResponseBack> {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onResponse(
                call: Call<AllProductsResponseBack>,
                response: Response<AllProductsResponseBack>
            ) {

                if (response.isSuccessful) {

                    if (response.body() != null) {

                        var resp: AllProductsResponseBack = response.body()!!
                        var lists: List<ModelMyProduct> = resp.data

                        if (lists != null && lists.count() > 0) {
                            for (IndProperty in lists) {
                                myProductPosts.add(
                                    ModelMyProduct(
                                        if (IndProperty.images != null) IndProperty.images else null,
                                        if (IndProperty.title != null) IndProperty.title else "0",
                                        if (IndProperty.code != null) IndProperty.code else "0",
                                        if (IndProperty.sKU != null) IndProperty.sKU else "0",
                                        if (IndProperty.id != null) IndProperty.id else "0",
                                        if (IndProperty.listingDuration != null) IndProperty.listingDuration else "0",
                                        if (IndProperty.stock != null) IndProperty.stock else "0",
                                        if (IndProperty.startPrice != null) IndProperty.startPrice else "0",
                                        if (IndProperty.specifyReserve != null) IndProperty.specifyReserve else "",
                                        if (IndProperty.buyNow != null) IndProperty.buyNow else "",
                                        if (IndProperty.userId != null) IndProperty.userId else ""
                                    )
                                )
                            }
                            val myProductRecycler: RecyclerView =
                                requireActivity().findViewById(R.id.myproduct_recycler)

                            myProductRecycler.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
                            myProductRecycler.adapter =
                                AdapterMyProduct(myProductPosts, MyProduct(), this@MyProduct)
                            HelpFunctions.dismissProgressBar()
                        } else {
                            HelpFunctions.dismissProgressBar()
                            HelpFunctions.ShowAlert(
                                this@MyProduct.context,
                                "Information",
                                "No Products Found"
                            )
                        }
                    }

                } else {
                    HelpFunctions.dismissProgressBar()
                    HelpFunctions.ShowAlert(
                        this@MyProduct.context, "Information", "No Products Found"
                    )
                }
            }

            override fun onFailure(call: Call<AllProductsResponseBack>, t: Throwable) {
                HelpFunctions.dismissProgressBar()
                Toast.makeText(activity, t.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun OnItemClick(position: Int) {
        super.OnItemClick(position)
        val args = Bundle()
        args.putString("AdvId", myProductPosts[position].id)
        args.putString("sellerID", myProductPosts[position].userId)
        NavHostFragment.findNavController(this@MyProduct)
            .navigate(R.id.my_product_to_product_detail, args)
    }

    override fun onEdit(position: Int) {
        super.onEdit(position)
        val args = Bundle()
        args.putString("AdvId", myProductPosts[position].id)
        NavHostFragment.findNavController(this@MyProduct)
            .navigate(R.id.my_product_to_edit_product, args)
    }

}