package com.malka.androidappp.fragments.myProductFragment.edit_product

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_continue_product.*


class ContinueEditProduct : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_continue_edit_product, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_continue_product.title = "Continue"
        toolbar_continue_product.setTitleTextColor(Color.WHITE)



        buttonContinue.setOnClickListener() {

            NavHostFragment.findNavController(this@ContinueEditProduct)
                .navigate(R.id.edit_product_continue_to_home)

            val fm = fragmentManager //
            val count = fm!!.backStackEntryCount
            for (i in -1 until count) {
                fm.popBackStack()
            }

        }

    }
}