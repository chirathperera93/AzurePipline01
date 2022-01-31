package com.ayubo.life.ayubolife.prochat.data.model

import android.os.Parcel
import android.os.Parcelable

data class Appointment(
    var appointment_id: String?,
    var timestamp: Long,
    var type: String?,
    var status: String?,
    var patient: Patient?,
    @Transient var last_coversation: Conversation?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Patient::class.java.classLoader),
        null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(appointment_id)
        parcel.writeLong(timestamp)
        parcel.writeString(type)
        parcel.writeString(status)
        parcel.writeParcelable(patient, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Appointment> {
        override fun createFromParcel(parcel: Parcel): Appointment {
            return Appointment(parcel)
        }

        override fun newArray(size: Int): Array<Appointment?> {
            return arrayOfNulls(size)
        }
    }

}