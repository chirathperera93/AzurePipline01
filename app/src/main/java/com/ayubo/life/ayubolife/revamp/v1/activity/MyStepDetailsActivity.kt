package com.ayubo.life.ayubolife.revamp.v1.activity

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.base.BaseActivity
import com.ayubo.life.ayubolife.revamp.v1.model.MyStepData
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_my_step_details.*

class MyStepDetailsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_step_details)

        mainImageViewForPageClose.setOnClickListener {
            finish()
        }

        tableStepDetailsTableLayout.isStretchAllColumns = true;
        startLoadData();
    }

    private fun startLoadData() {
        val leftRowMargin = 0
        val topRowMargin = 0
        val rightRowMargin = 0
        val bottomRowMargin = 0
        var textSize = 0
        var smallTextSize = 0
        var mediumTextSize = 0

        textSize = baseContext.resources.getDimension(R.dimen.font_size_small).toInt()

//        smallTextSize = baseContext.resources.getDimension(R.dimen.font_size_very_small).toInt()

        mediumTextSize = baseContext.resources.getDimension(R.dimen.font_size_medium).toInt()


        val myStepDataArrayList: ArrayList<Any> = ArrayList<Any>();

        for (i in 0 until 5) {

            myStepDataArrayList.add(
                MyStepData(
                    i + 1,
                    "03/11/21",
                    "12:16PM",
                    "126",
                    "14623",
                    "12kmh",
                    "12km",
                    "Accepted"
                )
            )
        }


        var textSpacer: TextView? = null
        tableStepDetailsTableLayout.removeAllViews()

        // -1 means heading row
        for (i in -1 until myStepDataArrayList.size) {

            var row: Any = MyStepData(-1, "", "", "", "", "", "", "")
            if (i > -1)
                row = myStepDataArrayList[i]
            else {
                textSpacer = TextView(this)
                textSpacer.text = ""
            }

            val rowJsonObject: JsonObject = Gson().toJsonTree(row).asJsonObject;


            // add table row
            val tr: TableRow = TableRow(this)
            tr.id = i + 1
            val trParams: TableLayout.LayoutParams =
                TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
            trParams.setMargins(
                leftRowMargin, topRowMargin, rightRowMargin,
                bottomRowMargin
            )
            tr.setPadding(0, if (i == -1) 16 else 0, 0, if (i == -1) 16 else 0)
            tr.layoutParams = trParams


            if (tr.id % 2 == 1) {
                tr.setBackgroundColor(resources.getColor(R.color.color_F2F2F2))
            }


            val typefaceForHeader: Typeface? =
                ResourcesCompat.getFont(baseContext, R.font.montserrat_bold);

            val typefaceForData: Typeface? =
                ResourcesCompat.getFont(baseContext, R.font.montserrat_regular);

            for (key in rowJsonObject.keySet()) {

                val value = rowJsonObject.get(key)
                val tv: TextView = TextView(this)

                tv.layoutParams = TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
                )
                tv.gravity = Gravity.CENTER
                tv.setPadding(5, 15, 0, 15)
                if (i == -1) {
                    tv.text = key
//                    tv.setBackgroundColor(resources.getColor(R.color.white))
                    tv.setTextColor(resources.getColor(R.color.black))
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
                    tv.typeface = typefaceForHeader
                } else {
                    tv.layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.MATCH_PARENT
                    )
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//                    tv.setBackgroundColor(Color.parseColor("#ffffff"))
                    tv.setTextColor(resources.getColor(R.color.black))
                    tv.text = value.asString
                    tv.typeface = typefaceForData
                }

                tr.addView(tv)
            }

            tableStepDetailsTableLayout.addView(tr, trParams)
            if (i > -1) {
                // add separator row
                val trSep: TableRow = TableRow(this)
                val trParamsSep: TableLayout.LayoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT
                )
                trParamsSep.setMargins(
                    leftRowMargin, topRowMargin,
                    rightRowMargin, bottomRowMargin
                );
                trSep.layoutParams = trParamsSep;
                val tvSep: TextView = TextView(this);
                val tvSepLay: TableRow.LayoutParams =
                    TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    );
                tvSepLay.span = 4;
                tvSep.layoutParams = tvSepLay;
                tvSep.setBackgroundColor(resources.getColor(R.color.trancperent));
                tvSep.height = 1;
                trSep.addView(tvSep);
                tableStepDetailsTableLayout.addView(trSep, trParamsSep);
            }


            // data columns

//            // TextView 1
//            val tv: TextView = TextView(this)
//            tv.layoutParams = TableRow.LayoutParams(
//                TableRow.LayoutParams.WRAP_CONTENT,
//                TableRow.LayoutParams.WRAP_CONTENT
//            )
//            tv.gravity = Gravity.CENTER
//            tv.setPadding(5, 15, 0, 15)
//            if (i == -1) {
//                tv.text = "Date"
//                tv.setBackgroundColor(resources.getColor(R.color.white))
//                tv.setTextColor(resources.getColor(R.color.black))
//                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
//            } else {
//                tv.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.MATCH_PARENT
//                )
//                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//                tv.setBackgroundColor(Color.parseColor("#ffffff"))
//                tv.setTextColor(Color.parseColor("#000000"))
//                tv.text = row.stepDate
//            }


//            // TextView 2
//            val tv2: TextView = TextView(this)
//            if (i == -1) {
//                tv2.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.WRAP_CONTENT
//                )
//                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
//                tv2.text = "Sync Time"
//                tv2.setBackgroundColor(resources.getColor(R.color.white))
//                tv2.setTextColor(resources.getColor(R.color.black))
//            } else {
//                tv2.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.MATCH_PARENT
//                )
//                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//                tv2.setBackgroundColor(Color.parseColor("#ffffff"))
//                tv2.setTextColor(Color.parseColor("#000000"))
//                tv2.text = row.syncTime
//            }
//            tv2.gravity = Gravity.CENTER
//            tv2.setPadding(5, 15, 0, 15)


//            // TextView 3
//            val tv3: TextView = TextView(this)
//            if (i == -1) {
//                tv3.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.WRAP_CONTENT
//                )
//                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
//                tv3.text = "METs"
//                tv3.setBackgroundColor(resources.getColor(R.color.white))
//                tv3.setTextColor(resources.getColor(R.color.black))
//            } else {
//                tv3.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.MATCH_PARENT
//                )
//                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//                tv3.setBackgroundColor(Color.parseColor("#ffffff"))
//                tv3.setTextColor(Color.parseColor("#000000"))
//                tv3.text = row!!.mets
//            }
//            tv3.gravity = Gravity.CENTER
//            tv3.setPadding(5, 15, 0, 15)


//            // TextView 4
//            val tv4: TextView = TextView(this)
//            if (i == -1) {
//                tv4.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.MATCH_PARENT,
//                    TableRow.LayoutParams.WRAP_CONTENT
//                )
//                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize.toFloat())
//                tv4.text = "Steps"
//                tv4.setBackgroundColor(resources.getColor(R.color.white))
//                tv4.setTextColor(resources.getColor(R.color.black))
//            } else {
//                tv4.layoutParams = TableRow.LayoutParams(
//                    TableRow.LayoutParams.WRAP_CONTENT,
//                    TableRow.LayoutParams.MATCH_PARENT
//                )
//                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
//                tv4.setBackgroundColor(Color.parseColor("#ffffff"))
//                tv4.setTextColor(Color.parseColor("#000000"))
//                tv4.text = row!!.steps
//            }
//            tv4.gravity = Gravity.CENTER
//            tv4.setPadding(5, 15, 0, 15)


        }
    }
}