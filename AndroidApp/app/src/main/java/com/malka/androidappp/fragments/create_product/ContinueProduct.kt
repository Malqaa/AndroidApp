package com.malka.androidappp.fragments.create_product

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import kotlinx.android.synthetic.main.fragment_continue_product.*

class ContinueProduct : Fragment() {

    var AdvId: String = "";
//    var template: String = "";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_continue_product, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_continue_product.setTitle("Continue")
        toolbar_continue_product.setTitleTextColor(Color.WHITE)

        AdvId = arguments?.getString("AdvId").toString()

        textViewDescriptionOfAd.setText("Product ID: "+AdvId)
        buttonContinue.setOnClickListener(){
            val args = Bundle()
            args.putString("AdvId", AdvId)
//            args.putString("Template",template)
            HelpFunctions.startProgressBar(requireActivity())
            NavHostFragment.findNavController(this@ContinueProduct).navigate(R.id.continue_to_product_detail, args)

        }


    }
}

