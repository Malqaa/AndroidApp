package com.malka.androidappp.botmnav_fragments.selling_option.blacklist_member

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.fragment_blacklist_menbers.*
import kotlinx.android.synthetic.main.fragment_browse_market.*
import kotlinx.android.synthetic.main.fragment_selling_option.*


class BlacklistMenbers : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blacklist_menbers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_blacklist.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_blacklist.setTitle("Blacklist")
        toolbar_blacklist.setTitleTextColor(Color.WHITE)
        toolbar_blacklist.setNavigationOnClickListener({
            activity!!.onBackPressed()

        })

        toolbar_blacklist.inflateMenu(R.menu.blacklist_member_menu)
        toolbar_blacklist.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                if (item.itemId == R.id.add_bliacklist) {
                    // do something
                    add_blacklist()

                }  else {
                    // do something
                }
                return false
            }
        })

    }


    fun add_blacklist() {
        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
// ...Irrelevant code for customizing the buttons and title
// ...Irrelevant code for customizing the buttons and title
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.card_blacklist, null)
        dialogBuilder.setView(dialogView)
        val alertDialog: AlertDialog = dialogBuilder.create()
        //this line used for layout backgrounf corner
        alertDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.show()

        val cancelbtn: Button = alertDialog.findViewById(R.id.button88) as Button
        val okbtn: Button = alertDialog.findViewById(R.id.button8) as Button
        cancelbtn.setOnClickListener({
            alertDialog.dismiss()
        })

        okbtn.setOnClickListener({
            alertDialog.dismiss()
        })
    }
}