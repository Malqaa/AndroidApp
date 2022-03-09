package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Intent
import android.os.Bundle
import android.widget.Filter
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.design.Models.itemDetailmodel
import com.malka.androidappp.helper.hide
import com.malka.androidappp.helper.show
import com.malka.androidappp.helper.widgets.rcv.GenericListAdapter
import kotlinx.android.synthetic.main.fragment_promotional.*
import kotlinx.android.synthetic.main.item_details2_desgin.view.*
import kotlinx.android.synthetic.main.toolbar_main.*


class PromotionalFragment : BaseActivity() {
    val list: ArrayList<itemDetailmodel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_promotional)

        toolbar_title.text = getString(R.string.distinguish_your_product)
        back_btn.setOnClickListener {
            finish()
        }

        list.add(
            itemDetailmodel(
                getString(R.string.SuperFeature),
                "49.99",
                getString(R.string.largerimage)
            )
        )
        list.add(
            itemDetailmodel(
                getString(R.string.FeatureCombo),
                "29.99",
                getString(R.string.Bestpositionincategory)
            )
        )
        list.add(
            itemDetailmodel(
                getString(R.string.Feature),
                "33.99",
                getString(R.string.featuretext)
            )
        )
        list.add(
            itemDetailmodel(
                getString(R.string.Gallery),
                "8.99",
                getString(R.string.gallerytext)
            )
        )
        list.add(itemDetailmodel(getString(R.string.Basic), "0", getString(R.string.basictext)))



        setCategoryAdaptor(list)


        button16611.setOnClickListener() {
            confirmpromotion()
        }
        no_thank_you.setOnClickListener() {
            startActivity(Intent(this, Confirmation::class.java).apply {
            })
        }
    }


    private fun setCategoryAdaptor(list: List<itemDetailmodel>) {
        category_rcv.adapter = object : GenericListAdapter<itemDetailmodel>(
            R.layout.item_details2_desgin,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        pkg_name.text = packagename
                        pkg_price.text = "${getString(R.string.Rayal)} $packageprice"
                        pkg_service1.text = packageservice1
                        if (is_select) {
                            bgline.setBackgroundResource(R.drawable.product_attribute_linebg)
                            is_selectimage.show()

                        } else {
                            bgline.setBackgroundResource(R.drawable.product_attribute_bg4)

                            is_selectimage.hide()
                        }
                        setOnClickListener {
                            list.forEach {
                                it.is_select = false
                            }
                            list.get(position).is_select = true
                            category_rcv.adapter!!.notifyDataSetChanged()
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

    //Promotion Validation
    private fun validatepromotion(): Boolean {
        val list = list.filter {
            it.is_select == true
        }
        return list.size > 0
    }

    fun confirmpromotion() {
        if (!validatepromotion()) {
            return
        } else {
            saveSelectedcheckbox()
            startActivity(Intent(this, Confirmation::class.java).apply {
            })
        }

    }

    fun saveSelectedcheckbox() {

        val list = list.filter {
            it.is_select == true
        }
        list.forEach {
            StaticClassAdCreate.pack4=  it.packageprice
        }
    }


}