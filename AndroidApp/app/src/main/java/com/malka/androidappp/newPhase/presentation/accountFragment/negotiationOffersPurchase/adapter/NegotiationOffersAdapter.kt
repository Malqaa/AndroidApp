package com.malka.androidappp.newPhase.presentation.accountFragment.negotiationOffersPurchase.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.malka.androidappp.R
import com.malka.androidappp.databinding.ItemNegotiationOffersBinding
import com.malka.androidappp.newPhase.data.helper.Extension
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.negotiationOfferResp.NegotiationOfferDetails


class NegotiationOffersAdapter(
    var negotiationOfferDetailsList: ArrayList<NegotiationOfferDetails>,
    var setOnOfferClickListeners: SetOnOfferClickListeners,
    var saleOrNot: Boolean = false
) :
    Adapter<NegotiationOffersAdapter.NegotiationOffersViewHolder>() {
    private var isSend: Boolean = false
    lateinit var context: Context

    class NegotiationOffersViewHolder(var viewBinding: ItemNegotiationOffersBinding) :
        ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NegotiationOffersViewHolder {
        context = parent.context
        return NegotiationOffersViewHolder(
            ItemNegotiationOffersBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun getItemCount(): Int = negotiationOfferDetailsList.size

    //@SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NegotiationOffersViewHolder, position: Int) {
//        val getrow: Any = negotiationOfferDetailsList[position].
//        val t: LinkedTreeMap<*, *> = getrow as LinkedTreeMap<*, *>
//        val productCategory = t["productCategory"].toString()
//        val region = t["region"].toString()
//        val productName = t["productName"].toString()
//        val productImage = t["productImage"].toString()
//        val productPrice = t["productPrice"].toString()
//        val receiverImage = t["receiverImage"].toString()
//        val offerId = t["offerId"].toString()
        if(negotiationOfferDetailsList[position].offerExpireHours!=null){
            holder.viewBinding.tvExpireHours.text="${negotiationOfferDetailsList[position].offerExpireHours} ${context.getString(R.string._3_days_after_the_offer_ends)}"
        }else{
            holder.viewBinding.tvExpireHours.text=""
        }

        holder.viewBinding.productType.text = negotiationOfferDetailsList[position].productCategory
        holder.viewBinding.productName.text = negotiationOfferDetailsList[position].productName
        holder.viewBinding.productCity.text = negotiationOfferDetailsList[position].region
        holder.viewBinding.tvProductPrice.text=negotiationOfferDetailsList[position].productPrice.toString()
        holder.viewBinding.tvOfferedPrice.text="${context.getString(R.string.offerPrice)} ${negotiationOfferDetailsList[position].offerPrice} ${context.getString(R.string.sar)}"

        Extension.loadThumbnail(
            context,
            negotiationOfferDetailsList[position].productImage,
            holder.viewBinding.productImage,
            holder.viewBinding.loader
        )
        holder.viewBinding.tvProductPrice.text =
            "${negotiationOfferDetailsList[position].productPrice} ${
                context.getString(
                    R.string.SAR
                )
            }"

        if (saleOrNot) {
            Extension.loadThumbnail(
                context,
                negotiationOfferDetailsList[position].senderImage,
                holder.viewBinding.sellerPicture,
                holder.viewBinding.loader2
            )
            holder.viewBinding.personName.text =
                negotiationOfferDetailsList[position].sellerName ?: ""
            holder.viewBinding.containerSaleButton.hide()
            holder.viewBinding.btnCancel.hide()
            if (isSend) {
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text =
                            context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.show()
                    }
                    "Canceled" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.accepted)
                    }
                    "Refused" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.rejected)
                    }
                    else -> {
                        holder.viewBinding.tvStatus.text = ""
                    }
                }

            } else {
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text =
                            context.getString(R.string.waitForYourResponse)
                        holder.viewBinding.containerSaleButton.show()
                    }
                    "Canceled" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.accepted)
                    }
                    "Refused" -> {
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.rejected)
                    }
                    else -> {
                        holder.viewBinding.tvStatus.text = ""
                    }
                }
            }
        } else {
            Extension.loadThumbnail(
                context,
                negotiationOfferDetailsList[position].receiverImage,
                holder.viewBinding.sellerPicture,
                holder.viewBinding.loader2
            )
            holder.viewBinding.personName.text =
                negotiationOfferDetailsList[position].buyerName ?: ""
            holder.viewBinding.btnCancel.show()
            holder.viewBinding.containerSaleButton.hide()
            if(isSend){
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text = context.getString(R.string.noResponse)
                    }
                    "Canceled" -> {
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.accepted)
                    }
                    "Refused"->{
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.rejected)
                    }
                    else -> {
                        holder.viewBinding.tvStatus.text = ""
                    }
                }
            }else{
                when (negotiationOfferDetailsList[position].offerStatus) {
                    "New" -> {
                        holder.viewBinding.tvStatus.text = context.getString(R.string.noResponse)
                        holder.viewBinding.containerSaleButton.show()
                        holder.viewBinding.btnCancel.hide()
                    }
                    "Canceled" -> {
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.OfferCanceled)
                    }
                    "Accepted" -> {
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.accepted)
                    }
                    "Refused"->{
                        holder.viewBinding.btnCancel.hide()
                        holder.viewBinding.containerSaleButton.hide()
                        holder.viewBinding.tvStatus.text = context.getString(R.string.rejected)
                    }
                    else -> {
                        holder.viewBinding.tvStatus.text = ""
                    }
                }
            }

        }

        holder.viewBinding.btnCancel.setOnClickListener {
            setOnOfferClickListeners.onCancelOffer(
                offerID = negotiationOfferDetailsList[position].offerId.toInt(),
                position
            )
        }
        holder.viewBinding.btnAccept.setOnClickListener {
            setOnOfferClickListeners.onAcceptOffer(position)
        }
        holder.viewBinding.btnReject.setOnClickListener {
            setOnOfferClickListeners.onRejectOffer(position)
        }
    }

    fun setIsSend(sent: Boolean) {
        isSend = sent
    }

    interface SetOnOfferClickListeners {
        fun onCancelOffer(offerID: Int, position: Int)
        fun onAcceptOffer(position: Int)
        fun onRejectOffer(position: Int)
    }
}