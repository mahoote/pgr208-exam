package no.kristiania.prg208_1_exam.model.models

import android.os.Parcel
import android.os.Parcelable

class DBResultImage(
    val id: Int?,
    val storeLink: String? = null,
    val name: String? = null,
    val domain: String? = null,
    val identifier: String? = null,
    val trackingID: String? = null,
    val thumbnailLink: String? = null,
    val description: String? = null,
    val imageLink: String? = null,
    val imageBlob: ByteArray? = null,
    val currentDate: String? = null,
    val originalImgID: Int?
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createByteArray(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(storeLink)
        parcel.writeString(name)
        parcel.writeString(domain)
        parcel.writeString(identifier)
        parcel.writeString(trackingID)
        parcel.writeString(thumbnailLink)
        parcel.writeString(description)
        parcel.writeString(imageLink)
        parcel.writeByteArray(imageBlob)
        parcel.writeString(currentDate)
        parcel.writeValue(originalImgID)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "DBResultImage(id=$id, storeLink=$storeLink, name=$name, domain=$domain, identifier=$identifier, trackingID=$trackingID, thumbnailLink=$thumbnailLink, description=$description, imageLink=$imageLink, imageBlob=${imageBlob?.contentToString()}, currentDate=$currentDate, originalImgID=$originalImgID)"
    }

    companion object CREATOR : Parcelable.Creator<DBResultImage> {
        override fun createFromParcel(parcel: Parcel): DBResultImage {
            return DBResultImage(parcel)
        }

        override fun newArray(size: Int): Array<DBResultImage?> {
            return arrayOfNulls(size)
        }
    }

}
