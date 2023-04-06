package com.malka.androidappp.newPhase.presentation.addProduct.activity5

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malka.androidappp.R
import com.malka.androidappp.newPhase.core.BaseActivity
import com.malka.androidappp.newPhase.data.helper.HelpFunctions
import com.malka.androidappp.newPhase.data.helper.hide
import com.malka.androidappp.newPhase.data.helper.linearLayoutManager
import com.malka.androidappp.newPhase.data.helper.show
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malka.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malka.androidappp.newPhase.presentation.addProduct.AddProductObjectData
import com.malka.androidappp.newPhase.presentation.addProduct.activity6.ListingDetailsActivity
import com.malka.androidappp.newPhase.presentation.addProduct.viewmodel.AddProductViewModel
import kotlinx.android.synthetic.main.fragment_dynamic_template.*
import kotlinx.android.synthetic.main.toolbar_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DynamicTemplateActivtiy : BaseActivity(), DynamicSpecificationsAdapter.OnChangeValueListener {

    //    var file_name: String = ""
//    var Title: String = ""
//
//    val json_key_data: String = "data";
//    val json_key_id: String = "id";
//    val json_key_title: String = "title";
//    val json_key_placeholder: String = "placeholder";
//    val type: String = "type";
//    val json_key_enum: String = "enum";
//    var fm: FragmentManager? = null
//
//    var selectdate = ""
//    var selectTime = ""
    private lateinit var addProductViewModel: AddProductViewModel
    lateinit var dynamicSpecificationsAdapter: DynamicSpecificationsAdapter
    var dynamicSpecificationsArrayList: ArrayList<DynamicSpecificationItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dynamic_template)
        toolbar_title.text = getString(R.string.item_details)
        setViewClickListeners()
        setDynamicListAdapter()
        setUpViewModel()
        //  println("hhh  cat id : ${AddProductObjectData.selectedCategoryId}")
        addProductViewModel.getDynamicSpecification(AddProductObjectData.selectedCategoryId)
        // addProductViewModel.getDynamicSpecification(50)
    }

    private fun setUpViewModel() {
        addProductViewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)
        addProductViewModel.isLoading.observe(this) {
            if (it)
                progressBar.show()
            else
                progressBar.hide()
        }
        addProductViewModel.isNetworkFail.observe(this) {
            if (it) {
                HelpFunctions.ShowLongToast(
                    getString(R.string.connectionError),
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.errorResponseObserver.observe(this) {
            if (it.message != null && it.message != "") {
                HelpFunctions.ShowLongToast(
                    it.message,
                    this
                )
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.serverError),
                    this
                )
            }

        }
        addProductViewModel.getDynamicSpecificationObserver.observe(this) { dynamicSpecificationResp ->
            if (dynamicSpecificationResp.status_code == 200) {
                if (dynamicSpecificationResp.dynamicList != null && dynamicSpecificationResp.dynamicList.isNotEmpty()) {
                    dynamicSpecificationsArrayList.clear()
                    dynamicSpecificationsArrayList.addAll(dynamicSpecificationResp.dynamicList)
                    dynamicSpecificationsAdapter.notifyDataSetChanged()
                } else {
                    goNextScreen(true)
                    HelpFunctions.ShowLongToast(
                        getString(R.string.noSpecificationFound),
                        this
                    )
                }
            } else {
                HelpFunctions.ShowLongToast(
                    getString(R.string.noSpecificationFound),
                    this
                )
            }
        }
    }

    private fun setDynamicListAdapter() {
        dynamicSpecificationsAdapter =
            DynamicSpecificationsAdapter(dynamicSpecificationsArrayList, this)
        rvDynamicList.apply {
            adapter = dynamicSpecificationsAdapter
            layoutManager = linearLayoutManager(RecyclerView.VERTICAL)
        }
    }

    private fun setViewClickListeners() {
        back_btn.setOnClickListener {
            finish()
        }

        btn_dynamic_next.setOnClickListener() {
            checkAllDataSet()
            // GetDataFromDynamicControl()

        }
        //   btnSubCatgeoryFilter.setOnClickListener {
//            startActivity(Intent(this, SubCategories::class.java).apply {
//                putExtra("categoryid", allCategoryList[position].categoryKey.toString())
//                putExtra("categoryName", allCategoryList[position].categoryName.toString())
//            })
        //  }

    }

    private fun checkAllDataSet() {
        lifecycleScope.launch(Dispatchers.IO) {
            var allValuesSet = true
            var data:ArrayList<DynamicSpecificationSentObject> = ArrayList()
            dynamicSpecificationsArrayList.forEach { dynamicSpecificationItem ->
//                if(dynamicSpecificationItem.type==1){
//                    if(dynamicSpecificationItem.subSpecificationsValue==null){
//                        allValuesSet=false
//
//                    }
//                }else if(dynamicSpecificationItem.type==2){
//                    if(dynamicSpecificationItem.valueText==""||dynamicSpecificationItem.valueText==null){
//                        allValuesSet=false
//                    }
//                }
                if (dynamicSpecificationItem.subSpecifications != null && dynamicSpecificationItem.subSpecifications!!.isNotEmpty()) {
                    if (dynamicSpecificationItem.valueArText == "" || dynamicSpecificationItem.valueArText == null||dynamicSpecificationItem.valueEnText==""||dynamicSpecificationItem.valueEnText==null) {
                        allValuesSet = false

                    }else{
                        data.add(DynamicSpecificationSentObject(
                            HeaderSpeAr = dynamicSpecificationItem.nameAr.toString(),
                            HeaderSpeEn = dynamicSpecificationItem.nameEn.toString(),
                            ValueSpeAr = dynamicSpecificationItem.valueArText,
                            ValueSpeEn = dynamicSpecificationItem.valueEnText,
                        ))
                    }
                } else {
                    if (dynamicSpecificationItem.valueArText == "" || dynamicSpecificationItem.valueArText == null||dynamicSpecificationItem.valueEnText==""||dynamicSpecificationItem.valueEnText==null) {
                        allValuesSet = false
                    }else{
                        data.add(DynamicSpecificationSentObject(
                            HeaderSpeAr = dynamicSpecificationItem.nameAr.toString(),
                            HeaderSpeEn = dynamicSpecificationItem.nameEn.toString(),
                            ValueSpeAr = dynamicSpecificationItem.valueArText,
                            ValueSpeEn = dynamicSpecificationItem.valueEnText,
                        ))
                    }
                }


            }
            withContext(Dispatchers.Main) {
                if (allValuesSet) {
                    AddProductObjectData.productSpecificationList = data
                    goNextScreen(false)
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.enterAllSpecificaiton),
                        this@DynamicTemplateActivtiy
                    )
                }
            }
        }
    }

    private fun goNextScreen(isFinished: Boolean) {
        if (isFinished) {
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {})
            finish()
        } else {
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {})
        }

    }

    override fun setOnTextBoxTextChangeAR(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueArText = value
    }

    override fun setOnTextBoxTextChangeEN(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueEnText = value
    }

    override fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int) {
        dynamicSpecificationsArrayList[mainAttributesPosition].valueArText =
            dynamicSpecificationsArrayList[mainAttributesPosition].subSpecifications?.get(spinnerPosition)?.nameAr?:""
        dynamicSpecificationsArrayList[mainAttributesPosition].valueEnText =
            dynamicSpecificationsArrayList[mainAttributesPosition].subSpecifications?.get(spinnerPosition)?.nameEn?:""
    }


//    fun CreateControlsFromJson() {
//        try {
//            if (file_name.trim().length > 0) {
//                HelpFunctions.GetTemplatesJson(
//                    file_name
//                ) { json_string ->
//                    if (json_string.trim().length > 0) {
//                        val parsed_data = JSONObject(json_string)
//                        val controls_array: JSONArray = parsed_data.getJSONArray(json_key_data);
//                        if (controls_array.length() > 0) {
//                            for (i in 0 until controls_array.length()) {
//                                val IndControl = controls_array.getJSONObject(i)
//                                if (IndControl != null) {
//                                    AddControlToScreen(IndControl);
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }
//        } catch (ex: Exception) {
//            HelpFunctions.ReportError(ex);
//        }
//    }
//
//    fun AddControlToScreen(IndControl: JSONObject) {
//        try {
//            val isdropdown: Boolean = IndControl.has(json_key_enum);
//
//            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//            val _view: View;
//            if (!isdropdown) {
//                _view = inflater.inflate(R.layout.dynamic_text_field, null)
//
//                val txt_dynamic: TextView = _view.findViewById(R.id.dynamic_text_field)
//                txt_dynamic.text =
//                    if (IndControl.has(json_key_placeholder)) IndControl.getString(
//                        json_key_placeholder
//                    ) else ""
//
//                val txt_dynamic_text_field: EditText =
//                    _view.findViewById(R.id.txt_dynamic_text_field)
//
//
//                val iv_end_icon: ImageView =
//                    _view.findViewById(R.id.iv_end_icon)
//
//                val type = IndControl.getString(
//                    type
//                )
//                when (type) {
//                    "string" -> {
//                        txt_dynamic_text_field.inputType = InputType.TYPE_CLASS_TEXT
//                    }
//                    "number" -> {
//                        txt_dynamic_text_field.inputType = InputType.TYPE_CLASS_NUMBER
//                    }
//                    "decimal" -> {
//                        txt_dynamic_text_field.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
//                    }
//                    "datetime" -> {
//                        txt_dynamic_text_field.apply {
//                            isFocusable = false
//                            isFocusableInTouchMode = false
//                            isClickable = true
////                            val img = ContextCompat.getDrawable(
////                                this@DynamicTemplate,
////                                R.drawable.right_arrow
////                            )
////                            //  txt_dynamic_text_field.setCompoundDrawables(null, null, img, null)
////                            txt_dynamic_text_field.setCompoundDrawablesWithIntrinsicBounds(
////                                0,
////                                0,
////                                R.drawable.right_arrow,
////                                0
////                            );
//
//                            iv_end_icon.show()
//                            setOnClickListener {
//                                fm = supportFragmentManager
//                                val dateDialog = DatePickerFragment(false, false) { selectdate ->
//                                    val timeDialog = TimePickerFragment { selectTime ->
//                                        txt_dynamic_text_field.setText("$selectdate - $selectTime")
//                                    }
//                                    timeDialog.show(fm!!, "fragment_time")
//                                }
//                                dateDialog.show(fm!!, "fragment_date")
//                            }
//                        }
//                    }
//                    else -> {
//                        txt_dynamic_text_field.inputType = InputType.TYPE_CLASS_TEXT
//
//                    }
//                }
//                txt_dynamic_text_field.tag =
//                    if (IndControl.has(json_key_id)) IndControl.getString(json_key_id) else ""
//
//            } else {
//                _view = inflater.inflate(R.layout.dynamic_spinner, null)
//                val lbl_dynamic: TextView =
//                    _view.findViewById(R.id.lbl_dynamic_spinner);
//                lbl_dynamic.text =
//                    if (IndControl.has(json_key_title)) IndControl.getString(json_key_title) else ""
//                val spin_dynamic: TextFieldComponent = _view.findViewById(R.id.spinner_dynamic);
//                val _json_array_values = IndControl.getJSONArray(json_key_enum);
//                if (_json_array_values.length() > 0) {
//                    val spinner_values = Array(_json_array_values.length()) {
//                        _json_array_values.getString(it)
//                    }
//
//                    spin_dynamic.tag =
//                        if (IndControl.has(json_key_id)) IndControl.getString(json_key_id) else ""
//                    spin_dynamic._setOnClickListener {
//                        val items: ArrayList<SearchListItem> = ArrayList()
//                        spinner_values.forEachIndexed { index, item ->
//                            items.add(SearchListItem(index, item))
//                        }
//                        spin_dynamic.showSpinner(this, items, "Select ${lbl_dynamic.text}") {
//                            spin_dynamic.text = it.title
//                        }
//
//                    }
//
//                }
//            }
//            parent_layout.addView(_view)
//        } catch (ex: Exception) {
//            HelpFunctions.ReportError(ex);
//        }
//    }
//
//    fun GetDataFromDynamicControl() {
//        try {
//            var num_child_views: Int = parent_layout.childCount;
//            if (num_child_views > 0)
//                num_child_views = num_child_views - 1
//            for (i in 0..num_child_views) {
//                val indcontol: View = parent_layout.getChildAt(i);
//                if (indcontol.id == R.id.card_dynamic_text) {
//                    val txtfield: EditText =
//                        indcontol.findViewById(R.id.txt_dynamic_text_field)
//                    if (txtfield.tag != null && txtfield.tag.toString().length > 0) {
//                        ConstantObjects.dynamic_json_dictionary[txtfield.tag.toString()] =
//                            txtfield.text.toString()
//                    }
//                }
//                //Spinner
//                else if (indcontol.id == R.id.card_dynamic_spinner) {
//                    val _spinner: TextFieldComponent = indcontol.findViewById(R.id.spinner_dynamic)
//                    if (_spinner.tag != null && _spinner.tag.toString().length > 0) {
//                        ConstantObjects.dynamic_json_dictionary[_spinner.tag.toString()] =
//                            _spinner.getText()
//                    }
//                }
//            }
//        } catch (ex: Exception) {
//            HelpFunctions.ReportError(ex);
//        }
//    }


}