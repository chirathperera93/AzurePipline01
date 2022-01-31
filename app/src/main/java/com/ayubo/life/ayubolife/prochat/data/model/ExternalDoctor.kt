package com.ayubo.life.ayubolife.prochat.data.model


import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import javax.annotation.Nonnull

@Entity
data class ExternalDoctor(
    @PrimaryKey @Nonnull
    var id: Int, @SerializedName("full_name") var fullName: String?,
    var specialty: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(fullName)
        parcel.writeString(specialty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExternalDoctor> {
        override fun createFromParcel(parcel: Parcel): ExternalDoctor {
            return ExternalDoctor(parcel)
        }

        override fun newArray(size: Int): Array<ExternalDoctor?> {
            return arrayOfNulls(size)
        }
    }
}
