package com.ayubo.life.ayubolife.janashakthionboarding.reportupload

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.ayubo.life.ayubolife.R
import java.util.ArrayList

class SelectReportTypeActivity : AppCompatActivity() {


    var editText: EditText? = null
    lateinit var songsList: ArrayList<Any>
    lateinit var activityName: String
    lateinit var serviceName: String
    lateinit var activityHeading:String
     var view: View? = null

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_report_type)

        recyclerView = view!!.findViewById(R.id.list_expert_view)

        editText = findViewById(R.id.edt_search_value)
//        editText.setHint("Search Expert/Speciality")
//        editText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//            }
//
//            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//                // filterData(charSequence.toString());
//
//           //     adapter.getFilter().filter(charSequence.toString())
//
//            }
//
//            override fun afterTextChanged(editable: Editable) {
//
//            }
//        })

    }
}
