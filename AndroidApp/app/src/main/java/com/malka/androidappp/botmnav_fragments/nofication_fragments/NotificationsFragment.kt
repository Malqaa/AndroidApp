package com.malka.androidappp.botmnav_fragments.nofication_fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.nofication_fragments.recycler_notic.NotifAdapter
import com.malka.androidappp.botmnav_fragments.nofication_fragments.recycler_notic.NotificModel
import kotlinx.android.synthetic.main.fragment_notifications.*


class NotificationsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val toolbaar: Toolbar = activity!!.findViewById<View>(R.id.notitoolbr) as Toolbar
        //(activity as AppCompatActivity?)!!.setSupportActionBar(toolbaar)




        return LayoutInflater.from(container?.context) .inflate(R.layout.fragment_notifications, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //(activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.Notifications)
        toolbar.setTitleTextColor(Color.WHITE)


        //////////////////////////////////////////////////////////
        val notiposts: ArrayList<NotificModel> = ArrayList()
        //for (i in 1..100){posts.add(Model("Kashan $i",1))}
        notiposts.add(
            NotificModel(
                R.drawable.camera_niti,
                "Relisted",
                "The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has ."
            )
        )
        notiposts.add(
            NotificModel(
                R.drawable.camera_niti,
                "Relisted cars",
                "The Italian Models car to see more specification see below"

            )
        )
        notiposts.add(
            NotificModel(
                R.drawable.camera_niti,
                "Relisted",
                "The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has ."
            )
        )
        notiposts.add(
            NotificModel(
                R.drawable.camera_niti,
                "Relisted",
                "The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has ."
            )
        )
        notiposts.add(
            NotificModel(
                R.drawable.camera_niti,
                "Relisted",
                "The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has ."
            )
        )
        notiposts.add(
            NotificModel(
                R.drawable.camera_niti,
                "Relisted",
                "The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has been relisted. The Product has ."
            )
        )



        recyclerViewnoti.layoutManager = LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerViewnoti.adapter = NotifAdapter(notiposts, this)

    }


}
