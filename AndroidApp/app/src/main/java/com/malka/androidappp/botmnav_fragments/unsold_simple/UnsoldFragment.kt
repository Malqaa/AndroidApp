package com.malka.androidappp.botmnav_fragments.unsold_simple

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.malka.androidappp.R
import com.malka.androidappp.botmnav_fragments.unsold_simple.recycler_unsold.UnSoldAdapter
import com.malka.androidappp.botmnav_fragments.unsold_simple.recycler_unsold.UnSoldModel
import com.malka.androidappp.botmnav_fragments.unsold_simple.recycler_unsold.UnSoldXLAdapter
import kotlinx.android.synthetic.main.fragment_unsold.*


class UnsoldFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_unsold, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_unsold.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_unsold.setTitle("Unsold")
        toolbar_unsold.setTitleTextColor(Color.WHITE)
        toolbar_unsold.setNavigationOnClickListener({
            findNavController().navigate(R.id.unsold_acc)
        })
        /////////////////////////////Switching toolbar code upside///////////////////////////////////

        val unsoldpost: ArrayList<UnSoldModel> = ArrayList()
        //for (i in 1..100){posts.add(Model("Kashan $i",1))}
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel("Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now"))
        unsoldpost.add(UnSoldModel( "Product Name", "Closed : 26 Nov", "$1,000", "Buy now",R.drawable.car2))




        recyclerView5.layoutManager =
            LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView5.adapter = UnSoldAdapter(unsoldpost, this)


        var count=0
        button238.setOnClickListener(){
            if(count==0)
            {
                recyclerView5.adapter = UnSoldXLAdapter(unsoldpost, this)
                count++
            }

            else
            {
                recyclerView5.adapter = UnSoldAdapter(unsoldpost, this)
                count--
            }
        }


    }

}
