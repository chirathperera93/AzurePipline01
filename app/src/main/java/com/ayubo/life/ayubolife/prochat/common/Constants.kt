package com.ayubo.life.ayubolife.prochat.common


const val MSG_FAILED_REQUEST = "Request sending failed"
const val MSG_SUCCESS = "Success"

const val REQUEST_CODE_PATIENT_ADD = 1000
const val REQUEST_CODE_MEDICINE_ADD = 1001
const val REQUEST_CODE_INVESTIGATION_ADD = 1002
const val REQUEST_CODE_DOCTOR_ADD = 1003
const val REQUEST_CODE_CHAT = 1004
const val REQUEST_CODE_REPORT_PREVIEW = 1005
const val REQUEST_CODE_REPORT_UPLOAD_HBALC = 1212
const val REQUEST_CODE_REPORT_UPLOAD_HDL = 1706
const val REQUEST_CODE_REPORT_UPLOAD_OTHER = 1008

const val ADD_TO_DIALOG_NUMBER_COMING_FROM_SETTINGS: Boolean = false

enum class ValueTypes {
    ROUTES, PREPARATIONS, MEAL_TIMES, FREQUENCY, FREQUENCY_TYPE, DURATION_TYPES
}