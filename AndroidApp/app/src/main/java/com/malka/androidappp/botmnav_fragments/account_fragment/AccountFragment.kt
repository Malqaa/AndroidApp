package com.malka.androidappp.botmnav_fragments.account_fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.add_product.ListanItem
import com.malka.androidappp.activities_main.business_signup.Switch_Account
import com.malka.androidappp.activities_main.login.LoginData
import com.malka.androidappp.activities_main.login.SignInActivity
import com.malka.androidappp.botmnav_fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import io.paperdb.Paper
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {


    fun disableenableoptions(enable: Boolean) {
        profilecardv.isEnabled = enable
        watchlist_card.isEnabled = enable
        shop_card.isEnabled = enable
        fav_card.isEnabled = enable
        fixed_price.isEnabled = enable
        wonfragg.isEnabled = enable
        lostfragg.isEnabled = enable
        product.isEnabled = enable
        buttonlistitem.isEnabled = enable
        item_imselling_btn.isEnabled = enable
        soldfragg.isEnabled = enable
        unsold_card.isEnabled = enable
        settingcardv.isEnabled = enable
        selling_opt.isEnabled = enable
        helpbtn.isEnabled = enable
        logout_signin.isEnabled = enable
        logout_signin.isEnabled = enable
        btn_signin.isEnabled = !enable
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(container?.context).inflate(
            R.layout.fragment_account,
            container,
            false
        )

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!HelpFunctions.IsUserLoggedIn()) {
            disableenableoptions(false)
            logout_signin.visibility = View.GONE
            btn_signin.visibility = View.VISIBLE
        } else {
            disableenableoptions(true)
            logout_signin.visibility = View.VISIBLE
            btn_signin.visibility = View.GONE

            ConstantObjects.userobj!!.run {
                userName.text = fullName
                member_since.text = "${getString(R.string.member_since)} $createdatFormated"
            }

        }

        helpbtn.setOnClickListener() {
//            val fragment = ListanItemFragment()
//            val fragmentManager = fragmentManager
//            val fragmentTransaction = fragmentManager!!.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_account, fragment)
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
        }



        view.findViewById<CardView>(R.id.profilecardv).setOnClickListener() {

            findNavController().navigate(R.id.accountsettingtoprofile)
        }
        view.findViewById<CardView>(R.id.settingcardv).setOnClickListener() {
            findNavController().navigate(R.id.accountsettingtosettings)
        }

        buttonlistitem.setOnClickListener() {
            requireActivity().startActivity(Intent(requireActivity(), ListanItem::class.java))
        }

        wonfragg.setOnClickListener() {
            findNavController().navigate(R.id.acc_won)
        }

        lostfragg.setOnClickListener() {
            findNavController().navigate(R.id.acc_lost)
        }

        fixed_price.setOnClickListener() {
            findNavController().navigate(R.id.account_fixedprice)
        }

        MyProducts.setOnClickListener() {
            findNavController().navigate(R.id.myProduct)
        }

        edit_profile_setting.setOnClickListener() {
            findNavController().navigate(R.id.editProfile)
        }
        negotiation_offers.setOnClickListener() {
            findNavController().navigate(R.id.negotiationOffer)
        }

        my_bids.setOnClickListener() {
            findNavController().navigate(R.id.mybids)
        }


        MySavedAddress.setOnClickListener() {
            findNavController().navigate(R.id.MySavedAddress)

        }

        application_setting.setOnClickListener {
            findNavController().navigate(R.id.applicationSetting)
        }

        technincal_support.setOnClickListener {
            findNavController().navigate(R.id.technicalSupport)
        }


        shop_card.setOnClickListener() {
            findNavController().navigate(R.id.acc_shopcart)
        }

        watchlist_card.setOnClickListener() {
            findNavController().navigate(R.id.acc_watchlist)
        }

        fav_card.setOnClickListener() {
            findNavController().navigate(R.id.acc_fav)
        }

        selling_opt.setOnClickListener() {
            findNavController().navigate(R.id.account_sellingopt)
        }

        item_imselling_btn.setOnClickListener() {
            HelpFunctions.startProgressBar(requireActivity())
            findNavController().navigate(R.id.account_itemimselling)
        }

        product.setOnClickListener() {
            findNavController().navigate(R.id.account_products)
        }

        logout_signin.setOnClickListener() {
            Paper.book().write(SharedPreferencesStaticClass.islogin, false)
            ConstantObjects.logged_userid = ""

            HelpFunctions.ShowLongToast(getString(R.string.loggedoutsuccessfully), context)
            findNavController().navigate(R.id.logout_to_home)
        }

        btn_signin.setOnClickListener() {
            val intentt = Intent(this.activity, SignInActivity::class.java)
            startActivity(intentt)
            requireActivity().finish()
        }

        switch_accounts_and_business_account.setOnClickListener {
            val intentt = Intent(this.activity, Switch_Account::class.java)
            startActivity(intentt)
        }


        userType()
    }


    fun userType() {

        if (!ConstantObjects.isBusinessUser) {
            productview.visibility = View.GONE
        } else {
            productview.visibility = View.VISIBLE
        }

    }

}
