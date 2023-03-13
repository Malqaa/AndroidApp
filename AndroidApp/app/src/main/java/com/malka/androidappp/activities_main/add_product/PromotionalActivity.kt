package com.malka.androidappp.activities_main.add_product

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Filter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.servicemodels.PromotionModel
import kotlinx.android.synthetic.main.fragment_promotional.*
import kotlinx.android.synthetic.main.item_details2_desgin.view.*
import kotlinx.android.synthetic.main.toolbar_main.*


class PromotionalActivity : BaseActivity() {
    val list: ArrayList<PromotionModel> = ArrayList()
    var isEdit: Boolean = false

    override fun onBackPressed() {
        intent.getBooleanExtra("isEdit",false).let {
            if (it){
                startActivity(Intent(this, Confirmation::class.java).apply {
                    finish()
                })
            }else{
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_promotional)

        toolbar_title.text = getString(R.string.distinguish_your_product)
        back_btn.setOnClickListener {
            onBackPressed()
        }

        if(StaticClassAdCreate.selectPromotiion!=null){
            isEdit=true
        }



        list.add(
            PromotionModel(
                getString(R.string.Golden_Package),
                "160",
                arrayListOf(
                    getString(R.string.your_product_will_be_displayed_on_the_home_page),
                    getString(R.string.your_product_will_be_displayed_on_the_home_page),
                    getString(R.string.your_product_will_be_displayed_on_the_home_page)
                ), is_common = true
            )
        )
        list.add(
            PromotionModel(
                getString(R.string.Silver_Package),
                "160",
                arrayListOf(
                    getString(R.string.your_product_will_be_displayed_on_the_home_page),
                    getString(R.string.your_product_will_be_displayed_on_the_home_page),
                    getString(R.string.your_product_will_be_displayed_on_the_home_page)
                )
            )
        )
        list.add(
            PromotionModel(
                getString(R.string.Bronze_Package),
                "160",
                arrayListOf(
                    getString(R.string.your_product_will_be_displayed_on_the_home_page),
                    getString(R.string.your_product_will_be_displayed_on_the_home_page)
                )
            )
        )
        list.add(
            PromotionModel(
                getString(R.string.Standard_Package),
                "160",
                arrayListOf(getString(R.string.your_product_will_be_displayed_on_the_home_page))
            )
        )

        if (isEdit){

            list.forEach{
                it.is_select = it.packagename.equals(StaticClassAdCreate.selectPromotiion!!.packagename)
            }

        }




        setPromotionalAdaptor(list)


        button16611.setOnClickListener() {
            confirmpromotion()
        }
        no_thank_you.setOnClickListener {
            StaticClassAdCreate.selectPromotiion = null
            startActivity(Intent(this, Confirmation::class.java).apply {
            })
        }


    }


    private fun setPromotionalAdaptor(
        list: List<PromotionModel>
    ) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        category_rcv.adapter = object : GenericListAdapter<PromotionModel>(
            R.layout.item_details2_desgin,
            bind = { element, holder, itemCount, parent_position ->
                holder.view.run {
                    element.run {
                        pkg_name.text = packagename
                        pkg_price.text = "$packageprice ${getString(R.string.Rayal)}"
                        parent_layout.removeAllViews()
                        packageservice.forEach {
                            val _view = inflater.inflate(R.layout.promotion_item, null)
                            val pkg_service1: TextView = _view.findViewById(R.id.pkg_service1)
                            pkg_service1.text = it
                            parent_layout.addView(_view)
                        }
                        if (is_select) {
                            bgline.setBackgroundResource(R.drawable.product_attribute_linebg)
                            is_selectimage.show()

                        } else {
                            bgline.setBackgroundResource(R.drawable.product_attribute_bg4)
                            is_selectimage.hide()
                        }

                        if (is_common) {
                            common.show()
                            is_selectimage.setImageResource(R.drawable.ic_check_black)
                            item_bg.setBackgroundColor(
                                ContextCompat.getColor(
                                    this@PromotionalActivity,
                                    R.color.bg
                                )
                            )
                        } else {
                            common.hide()
                            is_selectimage.setImageResource(R.drawable.ic_check)

                            item_bg.setBackgroundColor(
                                ContextCompat.getColor(
                                    this@PromotionalActivity,
                                    R.color.textColor
                                )
                            )

                        }
                        main_layout.setOnClickListener {
                            packageSelection(list, parent_position)
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

    private fun packageSelection(list: List<PromotionModel>, position: Int) {
        list.forEach {
            it.is_select = false
        }
        list.get(position).is_select = true
        category_rcv.post {
            category_rcv.adapter!!.notifyDataSetChanged()
        }
    }


    //Promotion Validation
    private fun validatepromotion(): Boolean {
        val list = list.filter {
            it.is_select == true
        }
        return list.size > 0
    }

    fun confirmpromotion() {
        if (!validatepromotion()) {
            showError(getString(R.string.choose_one_of_our_special_packages))
        } else {

            saveSelectedcheckbox()

            intent.getBooleanExtra("isEdit",false).let {
                if (it){
                    startActivity(Intent(this, Confirmation::class.java).apply {
                        finish()
                    })
                }else{
                    startActivity(Intent(this, Confirmation::class.java).apply {
                    })

                }
            }
        }

    }

    fun saveSelectedcheckbox() {

        val list = list.filter {
            it.is_select == true
        }
        list.forEach {
            StaticClassAdCreate.selectPromotiion = it
        }
    }


}