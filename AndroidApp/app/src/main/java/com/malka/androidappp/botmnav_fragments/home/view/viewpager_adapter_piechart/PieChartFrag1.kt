package com.malka.androidappp.botmnav_fragments.home.view.viewpager_adapter_piechart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.malka.androidappp.R
import kotlinx.android.synthetic.main.alertpopup.*
import kotlinx.android.synthetic.main.fragment_pie_chart_frag1.*


class PieChartFrag1() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // TODO: Rename and change types of parameters
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pie_chart_frag1, container, false)
        BindValues(view)
        return view
    }

    private fun BindValues(view: View) {
        val _lbl_total_count = view.findViewById<TextView>(R.id.lbl_total_count)
        val _lbl_count_label = view.findViewById<TextView>(R.id.lbl_count_label)

        if (_lbl_total_count != null && _lbl_count_label != null) {
            _lbl_total_count.setText(if (lbl_count != null) lbl_count else "0")
            _lbl_count_label.setText(if (lbl_legend_text != null) lbl_legend_text else "Error API")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BindValues(view)
    }

    companion object {
        var lbl_legend_text: String? = null
        var lbl_count: String? = null

        @JvmStatic
        fun newInstance() =
            PieChartFrag1().apply {
            }
    }
}