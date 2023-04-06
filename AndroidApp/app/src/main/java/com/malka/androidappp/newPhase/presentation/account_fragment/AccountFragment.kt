package com.malka.androidappp.newPhase.presentation.account_fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.presentation.MainActivity
import com.malka.androidappp.activities_main.business_signup.Switch_Account
import com.malka.androidappp.newPhase.presentation.loginScreen.SignInActivity
import com.malka.androidappp.activities_main.order.CartActivity
import com.malka.androidappp.fragments.shared_preferences.SharedPreferencesStaticClass
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.data.helper.widgets.rcv.GenericListAdapter
import com.malka.androidappp.newPhase.domain.models.loginResp.LoginUser
import com.malka.androidappp.newPhase.domain.models.servicemodels.AccountItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.AccountSubItem
import com.malka.androidappp.newPhase.domain.models.servicemodels.ConstantObjects
import io.paperdb.Paper
import kotlinx.android.synthetic.main.account_main_item.view.*
import kotlinx.android.synthetic.main.account_sub_item.view.*
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment(R.layout.fragment_account) {
    private var userData: LoginUser? = null
    val list: ArrayList<AccountItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        list.apply {
            add(
                AccountItem(
                    getString(R.string.Sale),
                    arrayListOf(AccountSubItem(getString(R.string.MyProducts), R.drawable.newslogo))
                )
            )

            add(
                AccountItem(
                    getString(R.string.purchase), arrayListOf(
                        AccountSubItem(getString(R.string.my_orders), R.drawable.shopingbag),
                        AccountSubItem(getString(R.string.my_bids), R.drawable.bidlogo),
                        AccountSubItem(getString(R.string.Loser), R.drawable.lost),
                        AccountSubItem(
                            getString(R.string.shopping_basket),
                            R.drawable.cart
                        ),
                        AccountSubItem(
                            getString(R.string.negotiation_offers),
                            R.drawable.path
                        ),

                        )
                )
            )

            add(
                AccountItem(
                    getString(R.string.Settings), arrayListOf(
                        AccountSubItem(getString(R.string.edit_profile), R.drawable.setting),
                        AccountSubItem(getString(R.string.payment_cards), R.drawable.paymentcard),
                        AccountSubItem(getString(R.string.save_addresses), R.drawable.maps),
                        AccountSubItem(
                            getString(R.string.application_settings),
                            R.drawable.settinglogo
                        ),
                        AccountSubItem(getString(R.string.help), R.drawable.discord),
                        AccountSubItem(getString(R.string.technical_support), R.drawable.call),
                        AccountSubItem(
                            getString(R.string.switch_accounts_and_business_account),
                            R.drawable.user
                        ),
                        AccountSubItem(getString(R.string.logout), R.drawable.logout),

                        )
                )
            )
        }
    }


    companion object {
        var isProfileLoad = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userData = Paper.book().read<LoginUser>(SharedPreferencesStaticClass.user_object)
        setUserData(userData)
      //  setListenser()
//        if (!isProfileLoad) {
//            CommonAPI().GetUserInfo(requireContext(), ConstantObjects.logged_userid) {
//                isProfileLoad = true
//                loadProfile()
//            }
//        } else {
//            loadProfile()
//        }
//        userType()


    }

    private fun setUserData(userData: LoginUser?) {
        try {
            userData?.let {
                Extension.loadThumbnail(
                    requireContext(),
                    userData.img,
                    ivUserImage,
                    loader
                )
                tvUserName.text = userData.userName.toString()
                userData.createdAt?.let {
                    tvMemberSince.text = "${getString(R.string.member_since)} ${
                        HelpFunctions.getViewFormatForDateTrack(it)
                    }".toString()
                }
                tv_membership_number.text = "${getString(R.string.membership_number)} not found"
                setAdaptor()
            }
        }catch (e:Exception){}
    }

    private fun loadProfile() {
        try {
            fragment_account.isVisible = true
            ConstantObjects.userobj!!.run {
                tvUserName.text = fullName
                tvMemberSince.text = "${getString(R.string.member_since)}: $member_since"
                tv_membership_number.text = "${getString(R.string.membership_number)}: "
            }
            setAdaptor()
        } catch (er: Exception) {
        }

    }

    private fun setListenser() {
        follow_up.setOnClickListener() {
            findNavController().navigate(R.id.followUp)
        }
        my_wallet.setOnClickListener() {
            findNavController().navigate(R.id.myWallet)
        }

        my_points.setOnClickListener() {
            findNavController().navigate(R.id.myPoints)
        }
        rating_btn.setOnClickListener() {
            findNavController().navigate(R.id.sellerRating)
        }




        profilecardv.setOnClickListener() {

            findNavController().navigate(R.id.accountsettingtoprofile)
        }
        settingcardv.setOnClickListener() {
            findNavController().navigate(R.id.accountsettingtosettings)
        }
        tvFixedPrice.setOnClickListener() {
            findNavController().navigate(R.id.account_fixedprice)
        }
        watchlist_card.setOnClickListener() {
            findNavController().navigate(R.id.acc_watchlist)
        }
        fav_card.setOnClickListener() {
            //  findNavController().navigate(R.id.acc_fav)
        }


        selling_opt.setOnClickListener() {
            findNavController().navigate(R.id.account_sellingopt)
        }

        product.setOnClickListener() {
            findNavController().navigate(R.id.account_products)
        }

        btn_signin.setOnClickListener() {
            val intentt = Intent(this.activity, SignInActivity::class.java)
            startActivity(intentt)
            requireActivity().finish()
        }


    }


    fun userType() {
        if (!ConstantObjects.isBusinessUser) {
            productview.visibility = View.GONE
        } else {
            productview.visibility = View.VISIBLE
        }

    }

    @SuppressLint("ResourceType")
    private fun setAdaptor() {
        main_item_rcv.adapter =
        object : GenericListAdapter<AccountItem>(
            R.layout.account_main_item,
            bind = { element, holder, itemCount, position ->
                holder.view.run {
                    element.run {
                        main_item_tv.text = name

                        sub_item_rcv.adapter = object : GenericListAdapter<AccountSubItem>(
                            R.layout.account_sub_item,
                            bind = { element, holder, itemCount, position ->
                                holder.view.run {
                                    element.run {
                                        sub_item_tv.text = name
                                        item_icon.setImageResource(image)
                                        if (MainActivity.myOrderTrigger) {
                                            MainActivity.myOrderTrigger = false
                                            findNavController().navigate(R.id.myRequest)
                                        }
                                        else if (MainActivity.myBidTrigger) {
                                            MainActivity.myBidTrigger = false
                                            findNavController().navigate(R.id.mybids)
                                        }

                                        if (name.equals(getString(R.string.logout))) {
                                            sub_item_tv.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.bg
                                                )
                                            )
                                            item_right_icon.setColorFilter(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.bg
                                                ), android.graphics.PorterDuff.Mode.SRC_IN
                                            );
                                            line.hide()
                                        } else {
                                            sub_item_tv.setTextColor(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.black
                                                )
                                            )
                                            item_right_icon.setColorFilter(
                                                ContextCompat.getColor(
                                                    context,
                                                    R.color.black
                                                ), android.graphics.PorterDuff.Mode.SRC_IN
                                            );
                                            line.show()

                                        }
                                        setOnClickListener {
                                            when (name) {
                                                getString(R.string.MyProducts) -> {
                                                    findNavController().navigate(R.id.myProduct)
                                                }
//                                                getString(R.string.my_orders) -> {
//                                                    findNavController().navigate(R.id.myRequest)
//
//                                                }
//                                                getString(R.string.my_bids) -> {
//                                                    findNavController().navigate(R.id.mybids)
//
//                                                }
//                                                getString(R.string.Loser) -> {
//                                                    findNavController().navigate(R.id.lost_frag)
//
//                                                }
//                                                getString(R.string.shopping_basket) -> {
//                                                    if (ConstantObjects.logged_userid.isEmpty()) {
//                                                        startActivity(
//                                                            Intent(
//                                                                context,
//                                                                SignInActivity::class.java
//                                                            )
//                                                        )
//                                                    } else {
//                                                        startActivity(
//                                                            Intent(
//                                                                requireActivity(),
//                                                                CartActivity::class.java
//                                                            )
//                                                        )
//                                                    }
//                                                }
//                                                getString(R.string.negotiation_offers) -> {
//                                                    findNavController().navigate(R.id.negotiationOffer)
//
//                                                }
//                                                getString(R.string.edit_profile) -> {
//                                                    findNavController().navigate(R.id.editProfile)
//
//                                                }
//                                                getString(R.string.payment_cards) -> {
//                                                    findNavController().navigate(R.id.paymentCard)
//
//                                                }
//                                                getString(R.string.save_addresses) -> {
//                                                    findNavController().navigate(R.id.MySavedAddress)
//                                                }
//                                                getString(R.string.application_settings) -> {
//                                                    findNavController().navigate(R.id.applicationSetting)
//
//                                                }
//                                                getString(R.string.help) -> {}
//                                                getString(R.string.technical_support) -> {
//
//
//                                                    findNavController().navigate(R.id.technicalSupport)
//
//                                                }
//                                                getString(R.string.switch_accounts_and_business_account) -> {
//                                                    startActivity(
//                                                        Intent(
//                                                            requireActivity(),
//                                                            Switch_Account::class.java
//                                                        )
//                                                    )
//                                                }
//                                                getString(R.string.logout) -> {
//                                                    Paper.book().write(
//                                                        SharedPreferencesStaticClass.islogin,
//                                                        false
//                                                    )
//                                                    ConstantObjects.logged_userid = ""
//
//                                                    HelpFunctions.ShowLongToast(
//                                                        getString(R.string.loggedoutsuccessfully),
//                                                        context
//                                                    )
//                                                    findNavController().navigate(R.id.logout_to_home)
//                                                }
                                            }
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

}
