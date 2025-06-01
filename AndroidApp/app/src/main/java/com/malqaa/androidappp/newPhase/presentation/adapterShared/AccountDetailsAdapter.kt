package com.malqaa.androidappp.newPhase.presentation.adapterShared

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.databinding.ItemCardBinding
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType
import com.malqaa.androidappp.newPhase.domain.models.accountBackListResp.AccountDetails

class AccountDetailsAdapter(
    private val onSelected: (AccountDetails) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<AccountDetails>()

    companion object {
        private const val TYPE_CARD = 0
        private const val TYPE_BANK = 1
    }

    fun updateAdapter(newItems: List<AccountDetails>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].paymentAccountType) {
            PaymentAccountType.VisaMasterCard, PaymentAccountType.Mada -> TYPE_CARD
            PaymentAccountType.BankAccount -> TYPE_BANK
            else -> TYPE_CARD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CARD -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
                CardViewHolder(view)
            }

            TYPE_BANK -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.add_bank_layout, parent, false)
                BankViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is CardViewHolder -> holder.bind(item, position)
            is BankViewHolder -> holder.bind(item, position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemCardBinding.bind(itemView)

        fun bind(item: AccountDetails, position: Int) {
            binding.textCardHoldersName.text = item.bankHolderName
            binding.textCardNumber.text = item.accountNumber
            binding.textExpiryDate.text = item.expiaryDate
            binding.radioButtonCard.isChecked = item.isSelected

            binding.editTextCvv.setText(if (item.cvv != 0) item.cvv.toString() else "")
            binding.editTextCvv.isEnabled = item.isSelected
            binding.editTextCvv.isFocusable = item.isSelected
            binding.editTextCvv.isFocusableInTouchMode = item.isSelected

            binding.editTextCvv.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val newCvv = s?.toString()?.toIntOrNull()
                    if (newCvv != null && newCvv.toString().length <= 3) {
                        item.cvv = newCvv
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
                ) {}

                override fun onTextChanged(
                    s: CharSequence?, start: Int, before: Int, count: Int
                ) {}
            })

            binding.radioButtonCard.setOnClickListener {
                updateSelection(position)
            }
        }
    }

    inner class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bankName: TextView = itemView.findViewById(R.id.bankName)
        private val holderName: TextView = itemView.findViewById(R.id.textCardHoldersName)
        private val accountNumber: TextView = itemView.findViewById(R.id.account_number)
        private val userName: TextView = itemView.findViewById(R.id.user_name)
        private val ibanNumber: TextView = itemView.findViewById(R.id.iban_number)
        private val radioButton: RadioButton = itemView.findViewById(R.id.bank)

        fun bind(item: AccountDetails, position: Int) {
            bankName.text = item.bankHolderName
            holderName.text = item.bankHolderName
            accountNumber.text = item.accountNumber
            userName.text = item.bankHolderName
            ibanNumber.text = item.ibanNumber
            radioButton.isChecked = item.isSelected

            radioButton.setOnClickListener {
                updateSelection(position)
            }
        }
    }

    private fun updateSelection(selectedPosition: Int) {
        val previouslySelectedIndex = items.indexOfFirst { it.isSelected }
        items.forEach { it.isSelected = false }
        items[selectedPosition].isSelected = true

        notifyItemChanged(previouslySelectedIndex)
        notifyItemChanged(selectedPosition)

        onSelected(items[selectedPosition])
    }
}

