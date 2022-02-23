package com.malka.androidappp.botmnav_fragments.dynamic_template

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.malka.androidappp.R
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_dynamic_template.*
import org.json.JSONArray
import org.json.JSONObject

class FragmentDynamicTemplate : Fragment() {

    var file_name: String = ""
    var Title: String = ""

    val json_key_data: String = "data";
    val json_key_id: String = "id";
    val json_key_type: String = "type";
    val json_key_title: String = "title";
    val json_key_placeholder: String = "placeholder";
    val json_key_isearchable: String = "isearchable";
    val json_key_enum: String = "enum";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dynamic_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar_dynamic_template.setNavigationIcon(R.drawable.nav_icon_back)
        toolbar_dynamic_template.setTitleTextColor(Color.WHITE)
        toolbar_dynamic_template.inflateMenu(R.menu.adcreation_close_btn)
        toolbar_dynamic_template.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
        toolbar_dynamic_template.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_close) {
                //findNavController().navigate(R.id.close_otherdetailsautomobile)

            } else {
            }
            false
        }
        btn_dynamic_next.setOnClickListener() {
//            Toast.makeText(context, "Clicked me: $file_name", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.fragment_dynamic_to_listing)
            GetDataFromDynamicControl()
        }

        file_name = arguments?.getString("file_name").toString()
        Title = arguments?.getString("Title").toString()
        SetScreenTitle(Title)
        CreateControlsFromJson()
    }

    fun CreateControlsFromJson() {
        try {
            if (file_name != null && file_name.trim().length > 0) {
                val gson = Gson()
                var json_string = HelpFunctions.GetTemplatesJson(
                    this@FragmentDynamicTemplate.requireContext(),
                    file_name
                );
                if (json_string != null && json_string.trim().length > 0) {
                    val parsed_data = JSONObject(json_string)
                    if (parsed_data != null) {
                        var controls_array: JSONArray = parsed_data.getJSONArray(json_key_data);
                        if (controls_array != null && controls_array.length() > 0) {
                            for (i in 0 until controls_array.length()) {
                                var IndControl = controls_array.getJSONObject(i)
                                if (IndControl != null) {
                                    AddControlToScreen(IndControl);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex);
        }
    }

    fun AddControlToScreen(IndControl: JSONObject) {
        try {
            if (IndControl != null) {
                val isdropdown: Boolean = IndControl.has(json_key_enum);

                val inflater = this@FragmentDynamicTemplate.requireContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val _view: View;
                if (!isdropdown) {
                    _view = inflater.inflate(R.layout.dynamic_text_field, null)

                    val txt_dynamic: TextInputEditText =
                        _view.findViewById<TextInputEditText>(R.id.txt_dynamic_text_field);
                    if (txt_dynamic != null) {
                        txt_dynamic.hint =
                            if (IndControl.has(json_key_placeholder)) IndControl.getString(
                                json_key_placeholder
                            ) else ""
                        txt_dynamic.tag =
                            if (IndControl.has(json_key_id)) IndControl.getString(json_key_id) else ""
                    }

                } else {
                    _view = inflater.inflate(R.layout.dynamic_spinner, null)
                    val lbl_dynamic: TextView =
                        _view.findViewById<TextView>(R.id.lbl_dynamic_spinner);
                    if (lbl_dynamic != null) {
                        lbl_dynamic.text =
                            if (IndControl.has(json_key_title)) IndControl.getString(json_key_title) else ""
                    }
                    val spin_dynamic: Spinner = _view.findViewById<Spinner>(R.id.spinner_dynamic);
                    if (spin_dynamic != null) {
                        val _json_array_values = IndControl.getJSONArray(json_key_enum);
                        if (_json_array_values != null && _json_array_values.length() > 0) {
                            var spinner_values = Array(_json_array_values.length()) {
                                _json_array_values.getString(it)
                            }

                            val adapter = ArrayAdapter(
                                this@FragmentDynamicTemplate.requireContext(),
                                android.R.layout.simple_spinner_item,
                                spinner_values
                            )
                            spin_dynamic.tag =
                                if (IndControl.has(json_key_id)) IndControl.getString(json_key_id) else ""
                            spin_dynamic.adapter = adapter
                        }
                    }
                }
                parent_layout.addView(_view)
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex);
        }
    }

    fun GetDataFromDynamicControl() {
        try {
            var num_child_views: Int = parent_layout.childCount;
            if (num_child_views > 0)
                num_child_views = num_child_views - 1
            for (i in 0..num_child_views) {
                val indcontol: View = parent_layout.getChildAt(i);
                if (indcontol != null && indcontol is CardView) {
                    //Text Field
                    if (indcontol.id == R.id.card_dynamic_text) {
                        val txtfield: TextInputEditText =
                            indcontol.findViewById(R.id.txt_dynamic_text_field)
                        if (txtfield != null && txtfield.tag != null && txtfield.tag.toString().length > 0) {
                            ConstantObjects.dynamic_json_dictionary[txtfield.tag.toString()] =
                                txtfield.text.toString()
                        }
                    }
                    //Spinner
                    else if (indcontol.id == R.id.card_dynamic_spinner) {
                        val _spinner: Spinner = indcontol.findViewById(R.id.spinner_dynamic)
                        if (_spinner != null && _spinner.tag != null && _spinner.tag.toString().length > 0) {
                            ConstantObjects.dynamic_json_dictionary[_spinner.tag.toString()] =
                                _spinner.selectedItem.toString()
                        }
                    }
                }
            }
        } catch (ex: Exception) {
            HelpFunctions.ReportError(ex);
        }
    }

    fun SetScreenTitle(title: String) {
        toolbar_dynamic_template.title = title
    }

}