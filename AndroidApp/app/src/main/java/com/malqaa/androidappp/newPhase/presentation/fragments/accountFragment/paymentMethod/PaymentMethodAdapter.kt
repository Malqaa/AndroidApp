package com.malqaa.androidappp.newPhase.presentation.fragments.accountFragment.paymentMethod

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.domain.enums.PaymentAccountType

data class PaymentMethod(
    val id: Int,
    val cardTypeImageRes: Int,
    val cardNumber: String,
    val userName: String,
    val expiryDate: String,
    val bankName: String? = null,
    val holderName: String? = null,
    val iban: String? = null,
    val paymentAccountType: PaymentAccountType?
)

class PaymentMethodAdapter(
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (PaymentMethod) -> Unit
) : RecyclerView.Adapter<PaymentMethodAdapter.PaymentMethodViewHolder>() {

    private val paymentMethods: MutableList<PaymentMethod> = mutableListOf()

    inner class PaymentMethodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val linearLayout: LinearLayout = itemView.findViewById(R.id.linear_layout)
        val cardImage: ImageView = itemView.findViewById(R.id.image_payment_card)
        val editButton: ImageView = itemView.findViewById(R.id.edit_card)
        val deleteButton: ImageView = itemView.findViewById(R.id.image_delete)
        val cardNumber: TextView = itemView.findViewById(R.id.card_number_tv)
        val userName: TextView = itemView.findViewById(R.id.card_user_name)
        val expiryDate: TextView = itemView.findViewById(R.id.expiry_date)
        val linearCard: LinearLayout = itemView.findViewById(R.id.linear_card)
        val linearBank: LinearLayout = itemView.findViewById(R.id.linear_bank)

        val textBankName: TextView = itemView.findViewById(R.id.text_bank_name)
        val textHolderName: TextView = itemView.findViewById(R.id.text_bank_holders_name)
        val textIban: TextView = itemView.findViewById(R.id.text_iban)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_payment_method, parent, false)
        return PaymentMethodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaymentMethodViewHolder, position: Int) {
        val item = paymentMethods[position]

        val (cardTypeImageRes, cardBackground) = when (item.paymentAccountType) {
            PaymentAccountType.VisaMasterCard -> R.drawable.mastercard to R.drawable.card_background_visa_master_card
            PaymentAccountType.BankAccount -> R.drawable.card_bank_account to R.drawable.card_background_bank_account
            PaymentAccountType.Mada -> R.drawable.mada_logo to R.drawable.card_background_mada
            else -> R.drawable.visa_ic to R.drawable.card_background_visa_master_card
        }

        holder.linearLayout.setBackgroundResource(cardBackground)
        holder.cardImage.setImageResource(cardTypeImageRes)
        holder.cardNumber.text = item.cardNumber
        holder.userName.text = item.userName
        holder.expiryDate.text = item.expiryDate

        if (item.paymentAccountType == PaymentAccountType.BankAccount) {
            holder.linearCard.visibility = View.GONE
            holder.linearBank.visibility = View.VISIBLE
            holder.textBankName.text = item.bankName
            holder.textHolderName.text = item.holderName
            holder.textIban.text = item.iban
        } else {
            holder.linearCard.visibility = View.VISIBLE
            holder.linearBank.visibility = View.GONE
        }

        holder.editButton.setOnClickListener { onEditClick(item) }
        holder.deleteButton.setOnClickListener { onDeleteClick(item.id) }
    }

    override fun getItemCount(): Int = paymentMethods.size

    fun updateList(newList: List<PaymentMethod>?) {
        paymentMethods.clear()
        newList?.let { paymentMethods.addAll(it) }
        notifyDataSetChanged()
    }
}
