package com.ayubo.life.ayubolife.health

import com.google.gson.JsonObject
import java.io.Serializable

/**
 * Created by Chirath Perera on 2021-07-27.
 */


class OMCreatedOrderObj(
    var id: String?,
    var app_id: String?,
    var user_id: String?,
    var order_id: String?,
    var status: String?,
    var status_color: String?,
    var status_history: ArrayList<OMStatusHistory>?,
    var create_datetime: Double?,
    var lastupdated_datetime: Double?,
    var files: ArrayList<OMMediaFiles>?,
    var address: OMAddress?,
    var partner: OMPartner?,
    var payment: OMPayment?
) : Serializable


class OrderMedicineCreateObject(
    var files: ArrayList<OMMediaFiles>?,
    var address: OMAddress?,
    var partner: OMPartner?,
    var payment: OMPayment?
) : Serializable

class OMMediaFiles(
    var URL: String?,
    var MediaName: String?,
    var MediaFolder: String?,
    var MediaType: String?,
    var note: String?,
    var isClicked: Boolean?
) : Serializable

class OMAddress(
    var id: String?,
    var app_id: String?,
    var user_id: String?,
    var display_name: String?,
    var house_number: String?,
    var street: String?,
    var address_line1: String?,
    var address_line2: String?,
    var landmark: String?,
    var city: String?,
    var postalcode: String?,
    var latitude: Double?,
    var longitude: Double?,
    var fav_address: Boolean?,
    var isSelected: Boolean = false
) : Serializable

class OMPartner(
    var id: String?,
    var app_id: String?,
    var user_id: String?,
    var name: String?,
    var code: String?,
    var logo_url: String?,
    var status: String?,
    var availability: JsonObject?,
    var address_line: String?,
    var payment_methods: ArrayList<PaymentMethod>?,
    var delivery_statement: String?,
    var discount: String?,
    var isSelected: Boolean? = false
)

class OMPayment(
    var selected_payment_type: String?,
    var total_amount: String?,
    var currency: String?,
    var items: ArrayList<PaymentItems>,
    var termsconditions: String?,
    var image_url: String?,
    var title: String?

) : Serializable

class PaymentItems(
    val name: String?,
    val amount: String?,
    val currency: String?
) : Serializable

class OMStatusHistory(
    var status: String?,
    var status_icon: String?,
    var reason: String?,
    var datetime: Double?
) : Serializable

class OMUpdateOrder(
    var id: String?,
    var files: ArrayList<OMMediaFiles>?,
    var address: OMAddress?,
    var partner: OMPartner?,
    var payment: OMPayment?
) : Serializable

class OMTrackOrderItem(
    var title: String?,
    var status_icon: String?,
    var description: String?,
    var status: String?,
    var datetime: Double?
) : Serializable

class OMTrackOrder(
    var id: String?,
    var app_id: String?,
    var user_id: String?,
    var order_id: String?,
    var status: String?,
    var status_color: String?,
    var status_history: ArrayList<OMStatusHistory>?,
    var create_datetime: Double?,
    var lastupdated_datetime: Double?,
    var files: ArrayList<OMMediaFiles>?,
    var address: OMAddress?,
    var partner: OMPartner?,
    var payment: OMPayment?,
    var session_id: String?
) : Serializable

class PaymentMethod(
    val id: String?,
    val title: String?,
    val description: String?,
    val icon_url: String?,
    val enabled: Boolean?
) : Serializable