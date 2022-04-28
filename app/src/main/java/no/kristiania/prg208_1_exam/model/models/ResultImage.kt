package no.kristiania.prg208_1_exam.model.models

import android.os.Parcel
import android.os.Parcelable

data class ResultImage (
    val current_date: String? = null,
    val description: String? = null,
    val domain: String? = null,
    val identifier: String? = null,
    val image_link: String? = null,
    val name: String? = null,
    val store_link: String? = null,
    val thumbnail_link: String? = null,
    val tracking_id: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(current_date)
        parcel.writeString(description)
        parcel.writeString(domain)
        parcel.writeString(identifier)
        parcel.writeString(image_link)
        parcel.writeString(name)
        parcel.writeString(store_link)
        parcel.writeString(thumbnail_link)
        parcel.writeString(tracking_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResultImage> {
        override fun createFromParcel(parcel: Parcel): ResultImage {
            return ResultImage(parcel)
        }

        override fun newArray(size: Int): Array<ResultImage?> {
            return arrayOfNulls(size)
        }
    }
}