package com.malka.androidappp.botmnav_fragments.shoppingcart8_addnewcard

import android.app.Activity
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.malka.androidappp.R
import com.malka.androidappp.helper.FourDigitCardFormatWatcher
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import com.malka.androidappp.servicemodels.creditcard.CreditCardRequestModel
import kotlinx.android.synthetic.main.fragment_add_new_card.*
import java.util.*

class AddNewCard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        toolbar_add_new_card.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_add_new_card.title = "Add New Card"
        toolbar_add_new_card.setTitleTextColor(Color.WHITE)
        toolbar_add_new_card.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        ///////////Calender EditText///////////////
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        card_expiry_date2.setOnClickListener() {
            hidekeyboard()
            val dpd = DatePickerDialog(
                this.requireContext(),
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    card_expiry_date2.setText((mMonth + 1).toString() + "/" + mYear.toString())
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        card_number2.addTextChangedListener(FourDigitCardFormatWatcher())
        save_card.setOnClickListener() {
            AddNewUserCard()
        }
    }

    fun AddNewUserCard() {
        try {
            if (ValidateScreen()) {
                var cardinfo: CreditCardRequestModel = CreditCardRequestModel(
                    card_number = card_number2.text.toString().trim(),
                    expiryDate = card_expiry_date2.text.toString().trim(),
                    cvcNumber = card_cvv2.text.toString().toInt(),
                    userId = ConstantObjects.logged_userid,
                    id = ""
                )
//                val resp = HelpFunctions.InsertUserCreditCard(cardinfo, requireContext())
//                if (resp) {
//                    requireActivity().supportFragmentManager.popBackStack()
//                }
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex)
        }
    }

    fun ValidateScreen(): Boolean {
        var RetVal: Boolean = false;
        if (card_number2.text == null || card_number2.text.toString() == null || card_number2.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Card Numbers!",
                this@AddNewCard.requireContext()
            );
            RetVal = false;
        } else if (card_expiry_date2.text == null || card_expiry_date2.text.toString() == null || card_expiry_date2.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Card Expiry Date!",
                this@AddNewCard.requireContext()
            );
            RetVal = false;
        } else if (card_cvv2.text == null || card_cvv2.text.toString() == null || card_cvv2.text.toString()
                .trim().length == 0
        ) {
            HelpFunctions.ShowLongToast(
                "Please Enter Card CVV Code!",
                this@AddNewCard.requireContext()
            );
            RetVal = false;
        } else {
            RetVal = true;
        }
        return RetVal;
    }

    fun hidekeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}