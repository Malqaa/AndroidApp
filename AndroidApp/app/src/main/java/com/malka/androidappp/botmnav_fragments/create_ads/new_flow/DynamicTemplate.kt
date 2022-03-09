package com.malka.androidappp.botmnav_fragments.create_ads.new_flow

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.malka.androidappp.R
import com.malka.androidappp.activities_main.BaseActivity
import com.malka.androidappp.botmnav_fragments.create_ads.StaticClassAdCreate
import com.malka.androidappp.botmnav_fragments.create_ads.new_flow.all_categories.SubCategories
import com.malka.androidappp.helper.HelpFunctions
import com.malka.androidappp.helper.widgets.edittext.DetailedTextField
import com.malka.androidappp.helper.widgets.searchdialog.SearchListItem
import com.malka.androidappp.servicemodels.ConstantObjects
import kotlinx.android.synthetic.main.fragment_dynamic_template.*
import kotlinx.android.synthetic.main.toolbar_main.*
import org.json.JSONArray
import org.json.JSONObject

class DynamicTemplate : BaseActivity() {

    var file_name: String = ""
    var Title: String = ""

    val json_key_data: String = "data";
    val json_key_id: String = "id";
    val json_key_title: String = "title";
    val json_key_placeholder: String = "placeholder";
    val json_key_enum: String = "enum";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dynamic_template)


        toolbar_title.text = getString(R.string.item_details)
        back_btn.setOnClickListener {
            finish()
        }

        btn_dynamic_next.setOnClickListener() {
            startActivity(Intent(this, ListingDetails::class.java).apply {
            })
            GetDataFromDynamicControl()
        }

        file_name = intent?.getStringExtra("file_name").toString()
        Title = intent?.getStringExtra("Title").toString()
        CreateControlsFromJson()

        var name = ""
        StaticClassAdCreate.subCategoryPath.forEach {
            name = name + it + " - "
        }
        if (name.length > 2) {
            name = name.dropLast(3)
        }

        cetgory_name.text = name
        sub_catgeory.setOnClickListener {
//            startActivity(Intent(this, SubCategories::class.java).apply {
//                putExtra("categoryid", allCategoryList[position].categoryKey.toString())
//                putExtra("categoryName", allCategoryList[position].categoryName.toString())
//            })
        }
    }


    fun CreateControlsFromJson() {
        try {
            if (file_name != null && file_name.trim().length > 0) {
                var json_string = HelpFunctions.GetTemplatesJson(
                    this,
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

                val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val _view: View;
                if (!isdropdown) {
                    _view = inflater.inflate(R.layout.dynamic_text_field, null)

                    val txt_dynamic: TextView = _view.findViewById(R.id.dynamic_text_field)
                    txt_dynamic.text =
                        if (IndControl.has(json_key_placeholder)) IndControl.getString(
                            json_key_placeholder
                        ) else ""

                    val txt_dynamic_text_field: EditText =
                        _view.findViewById(R.id.txt_dynamic_text_field)

                    txt_dynamic_text_field.tag =
                        if (IndControl.has(json_key_id)) IndControl.getString(json_key_id) else ""

                } else {
                    _view = inflater.inflate(R.layout.dynamic_spinner, null)
                    val lbl_dynamic: TextView =
                        _view.findViewById(R.id.lbl_dynamic_spinner);
                    lbl_dynamic.text =
                        if (IndControl.has(json_key_title)) IndControl.getString(json_key_title) else ""
                    val spin_dynamic: DetailedTextField = _view.findViewById(R.id.spinner_dynamic);
                    val _json_array_values = IndControl.getJSONArray(json_key_enum);
                    if (_json_array_values.length() > 0) {
                        val spinner_values = Array(_json_array_values.length()) {
                            _json_array_values.getString(it)
                        }

                        spin_dynamic.tag =
                            if (IndControl.has(json_key_id)) IndControl.getString(json_key_id) else ""
                        spin_dynamic._setOnClickListener {
                            val items: ArrayList<SearchListItem> = ArrayList()
                            spinner_values.forEachIndexed { index, item ->
                                items.add(SearchListItem(index, item))
                            }
                            spin_dynamic.showSpinner(this,items, "Select ${lbl_dynamic.text}") {
                                spin_dynamic.text = it.title
                            }

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
                        val txtfield: EditText =
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


}