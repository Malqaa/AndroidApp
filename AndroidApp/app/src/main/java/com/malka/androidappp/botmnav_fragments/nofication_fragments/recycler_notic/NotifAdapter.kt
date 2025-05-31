package com.malka.androidappp.botmnav_fragments.nofication_fragments.recycler_notic
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.nofication_fragments.NotificationsFragment
import kotlinx.android.synthetic.main.notific_carditems.view.*

class NotifAdapter (
    val notiposts: ArrayList<NotificModel>,
    var context: NotificationsFragment
) : RecyclerView.Adapter<NotifAdapter.NotifAdapterViewHolder>()
{


    class NotifAdapterViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
    {

        val notiicon: ImageView = itemview.imageView13
        val nottitle : TextView = itemview.textView10
        val notidescrip: TextView = itemview.textView13
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifAdapterViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.notific_carditems,parent,false)
        return NotifAdapterViewHolder(view)

    }

    override fun getItemCount() = notiposts.size

    override fun onBindViewHolder(holder: NotifAdapterViewHolder, position: Int) {

        holder.notiicon.setImageResource(notiposts[position].notif_img)
        holder.nottitle.text  = notiposts[position].notif_title
        holder.notidescrip.text = notiposts[position].notfi_des

    }




}