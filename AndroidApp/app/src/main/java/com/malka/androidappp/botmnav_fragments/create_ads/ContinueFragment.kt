package com.malka.androidappp.botmnav_fragments.create_ads

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_continue.*


class ContinueFragment : Fragment() {

    var AdvId: String = ""
    var template: String = ""
    var sellerID: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val navBar: BottomNavigationView = activity!!.findViewById(R.id.nav_view)
        //navBar.setVisibility(View.GONE)
        //textView39.setText(strtext)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_continue, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_continue.title = getString(R.string.Continue)
        toolbar_continue.setTitleTextColor(Color.WHITE)

        AdvId = arguments?.getString("AdvId").toString()
        template = arguments?.getString("Template").toString()

        textView49.text = AdvId + template
        button6.setOnClickListener(){
            val args = Bundle()
            args.putString("AdvId", AdvId)
            args.putString("Template",template)

            SharedPreferencesStaticClass.ad_userid = ConstantObjects.logged_userid

            NavHostFragment.findNavController(this@ContinueFragment).navigate(R.id.contine_carspec, args)


        }


    }


}