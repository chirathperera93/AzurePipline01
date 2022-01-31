package com.ayubo.life.ayubolife.revamp.v1.activity

import android.app.Activity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.activity.PrefManager
import com.ayubo.life.ayubolife.config.AppConfig
import com.ayubo.life.ayubolife.lifeplus.DeviceScreenDimension
import com.ayubo.life.ayubolife.login.AutoCompleteCityAdapter
import com.ayubo.life.ayubolife.login.model.CityDataInfo
import com.ayubo.life.ayubolife.login.model.GetAllCitiesDataResponse
import com.ayubo.life.ayubolife.login.model.NewUpdateCity
import com.ayubo.life.ayubolife.rest.ApiClient
import com.ayubo.life.ayubolife.rest.ApiInterface
import com.ayubo.life.ayubolife.revamp.v1.model.UpdateCityResponse
import kotlinx.android.synthetic.main.activity_add_city_dialog.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCityDialog : Activity(), AutoCompleteCityAdapter.OnItemClickListener {
    lateinit var appToken: String
    lateinit var pref: PrefManager
    var selectedCityId: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_city_dialog)
        pref = PrefManager(baseContext)
        appToken = pref.userToken
        val displayMetrics: DisplayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val deviceScreenDimension: DeviceScreenDimension = DeviceScreenDimension(baseContext)

        val width = deviceScreenDimension.displayWidth
        val height = deviceScreenDimension.displayHeight

        window.setLayout(((width * .8).toInt()), ((height * .7).toInt()))


        val params: WindowManager.LayoutParams = window.attributes
        params.gravity = Gravity.CENTER
        params.x = 0
        params.y = -20
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = 0.5f
        window.attributes = params
        window.setBackgroundDrawableResource(R.drawable.bg_round)


        getAllCities()

        add_city_close_btn.setOnClickListener {
            finish()
        }
    }

    private fun getAllCities() {
        new_city_data_loading.visibility = View.VISIBLE
        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV3ForUsers().create(ApiInterface::class.java)

        val call: Call<GetAllCitiesDataResponse> =
            apiService.getAllCities(appToken, AppConfig.APP_BRANDING_ID)

        call.enqueue(object : Callback<GetAllCitiesDataResponse> {
            override fun onResponse(
                call: Call<GetAllCitiesDataResponse>,
                response: Response<GetAllCitiesDataResponse>
            ) {
                new_city_data_loading.visibility = View.GONE

                if (response.isSuccessful) {

                    setCityDropDownData(response.body()!!.data)

                } else {
                    Toast.makeText(
                        baseContext,
                        "Failed loading data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<GetAllCitiesDataResponse>, t: Throwable) {
                new_city_data_loading.visibility = View.GONE
                Toast.makeText(
                    baseContext,
                    "Failed loading data",
                    Toast.LENGTH_LONG
                ).show()
            }

        })
    }

    private fun setCityDropDownData(cityDataInfoArrayList: ArrayList<CityDataInfo>) {
        val auto_complete_text_view_city: AutoCompleteTextView =
            findViewById(R.id.auto_complete_text_view_city)
        val autoCompleteCityAdapter =
            AutoCompleteCityAdapter(this@AddCityDialog, cityDataInfoArrayList)
        auto_complete_text_view_city.setAdapter(autoCompleteCityAdapter)

        autoCompleteCityAdapter.setOnItemClickListener(this@AddCityDialog)


        main_layout_for_city.setOnClickListener {
            text_view_for_city.visibility = View.GONE
            auto_complete_text_view_city.visibility = View.VISIBLE
            auto_complete_text_view_city.showDropDown()
        }
    }

    override fun onCityItemClick(cityDataInfo: CityDataInfo?) {
        text_view_for_city.visibility = View.VISIBLE
        auto_complete_text_view_city.visibility = View.GONE
        text_view_for_city.text = cityDataInfo!!.city
        auto_complete_text_view_city.dismissDropDown()
        selectedCityId = cityDataInfo.id
        setUpdateButtonEnableUI()
    }

    private fun setUpdateButtonEnableUI() {
        update_city_btn.isEnabled = true
        update_city_btn.setBackgroundResource(R.drawable.radius_background_orange)
    }

    fun updateCity(view: View) {
        new_city_data_loading.visibility = View.VISIBLE
        val newUpdateCity = NewUpdateCity(selectedCityId)

        val apiService: ApiInterface =
            ApiClient.getAzureApiClientV1ForUsers().create(ApiInterface::class.java)
        val call: Call<UpdateCityResponse> =
            apiService.updateCity(appToken, AppConfig.APP_BRANDING_ID, newUpdateCity)


        call.enqueue(object : Callback<UpdateCityResponse> {
            override fun onResponse(
                call: Call<UpdateCityResponse>,
                response: Response<UpdateCityResponse>
            ) {
                new_city_data_loading.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(
                        baseContext,
                        "Successfully updated city",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        baseContext,
                        "Failed loading data",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<UpdateCityResponse>, t: Throwable) {
                new_city_data_loading.visibility = View.VISIBLE
                Toast.makeText(
                    baseContext,
                    "Failed loading data",
                    Toast.LENGTH_LONG
                ).show()
            }


        })

    }


}