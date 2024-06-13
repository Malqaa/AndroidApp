package com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity5

import android.R.attr.duration
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.malqaa.androidappp.R
import com.malqaa.androidappp.newPhase.core.BaseActivity
import com.malqaa.androidappp.newPhase.domain.models.addProductToCartResp.AddProductObjectData
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationItem
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationSentObject
import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.SubSpecificationItem
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.activity6.ListingDetailsActivity
import com.malqaa.androidappp.newPhase.presentation.activities.addProduct.viewmodel.AddProductViewModel
import com.malqaa.androidappp.newPhase.utils.ConstantObjects
import com.malqaa.androidappp.newPhase.utils.HelpFunctions
import com.malqaa.androidappp.newPhase.utils.hide
import com.malqaa.androidappp.newPhase.utils.linearLayoutManager
import com.malqaa.androidappp.newPhase.utils.show
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
    private var dataList: ArrayList<DynamicSpecificationSentObject>? = null
    private var isEdit: Boolean = false
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var dynamicSpecificationsAdapter: DynamicSpecificationsAdapter
    private var dynamicSpecificationsArrayList: ArrayList<DynamicSpecificationItem> = ArrayList()
    private var selectSpecificationsArrayList: ArrayList<DynamicSpecificationItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dynamic_template)
        toolbar_title.text = getString(R.string.item_details)
        setViewClickListeners()
        setDynamicListAdapter()
        setUpViewModel()
        dataList = arrayListOf()
        isEdit = intent.getBooleanExtra(ConstantObjects.isEditKey, false)

        //  println("hhh  cat id : ${AddProductObjectData.selectedCategoryId}")
        addProductViewModel.getDynamicSpecification(AddProductObjectData.selectedCategoryId)
        // addProductViewModel.getDynamicSpecification(50)
    }

    private fun setDataForUpdate() {
        if (isEdit) {
            selectSpecificationsArrayList = dynamicSpecificationsArrayList
            for (item in selectSpecificationsArrayList) {
                if (item.type == 2 || item.type == 3 || item.type == 4) {
                    val obj = AddProductObjectData.productSpecificationList!!.find { it.SpecificationId == item.id }
                    if (obj != null) {
                        item.setData(obj)
                        item.subSpecifications?.add(
                            SubSpecificationItem(
                                id = obj.SpecificationId,
                                nameAr = obj.ValueSpeAr,
                                nameEn = obj.ValueSpeEn,
                                isDataSelected=true
                            )
                        )
                    }
                }
                else if(item.type == 5 || item.type == 6){

                }

                else {
                    item.subSpecifications?.forEach {
                        for(j in AddProductObjectData.productSpecificationList!!){
                            if(j.ValueSpeAr == it.id.toString()){
                                item.setData(j)
                                it.isDataSelected=true
                            }
                    }
                    }
                }

            }
            dynamicSpecificationsAdapter.updateAdapter(selectSpecificationsArrayList)
        }

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
            if (it.status != null && it.status == "409") {
                HelpFunctions.ShowLongToast(getString(R.string.dataAlreadyExit), this)
            } else {
                if (it.message != null && it.message != "") {
                    HelpFunctions.ShowLongToast(
                        it.message!!,
                        this
                    )
                } else {
                    HelpFunctions.ShowLongToast(
                        getString(R.string.serverError),
                        this
                    )
                }

            }
        }
        addProductViewModel.getDynamicSpecificationObserver.observe(this) { dynamicSpecificationResp ->
            if (dynamicSpecificationResp.status_code == 200) {
                if (dynamicSpecificationResp.dynamicList != null && dynamicSpecificationResp.dynamicList.isNotEmpty()) {
                    dynamicSpecificationsArrayList.clear()
                    dynamicSpecificationsArrayList.addAll(dynamicSpecificationResp.dynamicList)

                    setDataForUpdate()
                    dynamicSpecificationsAdapter.updateAdapter(dynamicSpecificationsArrayList)
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
            checkAllDataSet(dataList!!)
            // GetDataFromDynamicControl()

        }
        //   btnSubCatgeoryFilter.setOnClickListener {
//            startActivity(Intent(this, SubCategories::class.java).apply {
//                putExtra("categoryid", allCategoryList[position].categoryKey.toString())
//                putExtra("categoryName", allCategoryList[position].categoryName.toString())
//            })
        //  }

    }

    private fun checkAllDataSet(data: ArrayList<DynamicSpecificationSentObject>) {
        lifecycleScope.launch(Dispatchers.IO) {
            var allValuesSet = true
            dynamicSpecificationsArrayList.forEach { dynamicSpecificationItem ->

                val supIdAr =
                    dynamicSpecificationItem.subSpecifications?.find { it.nameAr == dynamicSpecificationItem.valueArText }
                val supIdEn =
                    dynamicSpecificationItem.subSpecifications?.find { it.nameEn == dynamicSpecificationItem.valueEnText }

                if (dynamicSpecificationItem.type == 1) {
                    if (dynamicSpecificationItem.subSpecifications != null && dynamicSpecificationItem.subSpecifications!!.isNotEmpty()) {
                        if (dynamicSpecificationItem.valueArText == "" || dynamicSpecificationItem.valueArText == null || dynamicSpecificationItem.valueEnText == "" || dynamicSpecificationItem.valueEnText == null) {
                            allValuesSet = false

                        } else {
                            data.add(
                                DynamicSpecificationSentObject(
                                    HeaderSpeAr = dynamicSpecificationItem.nameAr.toString(),
                                    HeaderSpeEn = dynamicSpecificationItem.nameEn.toString(),
                                    ValueSpeAr = supIdAr?.id.toString(),
                                    ValueSpeEn = supIdEn?.id.toString(),
                                    Type = dynamicSpecificationItem.type,
                                    SpecificationId = dynamicSpecificationItem.id
                                )
                            )
                        }
                    }
                }

                else if (dynamicSpecificationItem.type == 5 || (dynamicSpecificationItem.type == 6)) {
                    val supIdAr =
                        dynamicSpecificationItem.subSpecifications?.find { dynamicSpecificationItem.valueBoolean }

                    if(supIdAr==null){
                        allValuesSet = false
                        if(dynamicSpecificationItem.isRequired){
                            runOnUiThread {
                                HelpFunctions.ShowLongToast(
                                    getString(R.string.enterAllSpecificaiton),
                                    this@DynamicTemplateActivtiy
                                )
                            }
                        }
                    }else{
                        data.add(
                            DynamicSpecificationSentObject(
                                HeaderSpeAr = dynamicSpecificationItem.nameAr.toString(),
                                HeaderSpeEn = dynamicSpecificationItem.nameEn.toString(),
                                ValueSpeAr = supIdAr?.id.toString(),
                                ValueSpeEn = supIdAr?.id.toString(),
                                Type = dynamicSpecificationItem.type,
                                SpecificationId = dynamicSpecificationItem.id
                            )
                        )
                    }

                }
                else if(dynamicSpecificationItem.type == 4){
                    if(dynamicSpecificationItem.isRequired&&(dynamicSpecificationItem.valueArText == "" || dynamicSpecificationItem.valueArText == null)){
                        allValuesSet = false
                    }else{
                        data.add(
                            DynamicSpecificationSentObject(
                                HeaderSpeAr = dynamicSpecificationItem.nameAr.toString(),
                                HeaderSpeEn = dynamicSpecificationItem.nameAr.toString(),
                                ValueSpeAr = dynamicSpecificationItem.valueArText?:"",
                                Type = dynamicSpecificationItem.type,
                                ValueSpeEn = dynamicSpecificationItem.valueArText?:"",
                                SpecificationId = dynamicSpecificationItem.id
                            )
                        )
                    }
                }
                else {
                    if (dynamicSpecificationItem.isRequired&&(dynamicSpecificationItem.valueArText == "" || dynamicSpecificationItem.valueArText == null || dynamicSpecificationItem.valueEnText == "" || dynamicSpecificationItem.valueEnText == null)) {
                        allValuesSet = false
                    } else {
                        data.add(
                            DynamicSpecificationSentObject(
                                HeaderSpeAr = dynamicSpecificationItem.nameAr.toString(),
                                HeaderSpeEn = dynamicSpecificationItem.nameEn.toString(),
                                ValueSpeAr = dynamicSpecificationItem.valueArText?:"",
                                Type = dynamicSpecificationItem.type,
                                ValueSpeEn = dynamicSpecificationItem.valueEnText?:"",
                                SpecificationId = dynamicSpecificationItem.id
                            )
                        )
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
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {
                putExtra(
                    ConstantObjects.isEditKey,
                    intent.getBooleanExtra(ConstantObjects.isEditKey, false)
                )

            })
            finish()
        } else {
            startActivity(Intent(this, ListingDetailsActivity::class.java).apply {
                putExtra(
                    ConstantObjects.isEditKey,
                    intent.getBooleanExtra(ConstantObjects.isEditKey, false)
                )
            })
            finish()
        }

    }

    override fun setOnTextBoxTextChangeAR(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueArText = value
    }

    override fun setOnTextBoxTextChangeEN(value: String, position: Int) {
        dynamicSpecificationsArrayList[position].valueEnText = value
    }

    override fun setCheckClicked(position: Int) {
        dynamicSpecificationsArrayList[position].valueBoolean =
            dynamicSpecificationsArrayList[position].valueBoolean
    }

    override fun setOnSpinnerListSelected(mainAttributesPosition: Int, spinnerPosition: Int) {
        dynamicSpecificationsArrayList[mainAttributesPosition].valueArText =
            dynamicSpecificationsArrayList[mainAttributesPosition].subSpecifications?.get(
                spinnerPosition
            )?.nameAr ?: ""
        dynamicSpecificationsArrayList[mainAttributesPosition].valueEnText =
            dynamicSpecificationsArrayList[mainAttributesPosition].subSpecifications?.get(
                spinnerPosition
            )?.nameEn ?: ""
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
//                val spin_dynamic: SpinnerComponent = _view.findViewById(R.id.spinner_dynamic);
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
//                    val _spinner: SpinnerComponent = indcontol.findViewById(R.id.spinner_dynamic)
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