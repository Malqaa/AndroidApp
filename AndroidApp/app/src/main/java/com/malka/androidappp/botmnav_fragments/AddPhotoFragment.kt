package com.malka.androidappp.botmnav_fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_add_photo.*


open class AddPhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_addphoto.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_addphoto.title = getString(R.string.AddPhotos)
        toolbar_addphoto.setTitleTextColor(Color.WHITE)
        toolbar_addphoto.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_addphoto.setNavigationOnClickListener {
            requireActivity().onBackPressed()

        }
        toolbar_addphoto.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_close) {
                findNavController().navigate(R.id.close_addphoto)
                //closefragment()

            } else {
                // do something
            }
            false
        }


        ///////////////////////////////////////////
        butt555.setOnClickListener() {
            findNavController().navigate(R.id.addphoto_otherlistdeta)
        }

    }

    fun closefragment() {
        //this.activity!!.fragmentManager.beginTransaction().remove(this).commit()
        //val transaction = fragmentManager!!.beginTransaction()
        //transaction.popBackStackImmediate()
// Commit the transaction
        //transaction.addToBackStack(null)
        //transaction.commit()
        //transaction.remove(this).commit()
        //
        //val fm:FragmentManager = activity!!.getSupportFragmentManager()
        //for (i in 0 until fm.getBackStackEntryCount() ) {
        //  fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//
        //


    }


}