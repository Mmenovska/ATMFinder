package com.gsixacademy.android.atmfinder.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class AtmsData() : Parcelable {

    var id : String? = null
    var address : String? = null
    var cash_in : Boolean = false
    var city : String? = null
    var country : String? = null
    var logo : String? = null
    var bank : String? = null
    var lat_long : LatLongData? = null
    constructor(parcel: Parcel) : this (){
        id = parcel.readString()
        address = parcel.readString()
        cash_in = parcel.readBoolean()
        city = parcel.readString()
        country = parcel.readString()
        bank = parcel.readString()
        lat_long = parcel.readParcelable(LatLongData :: class.java.classLoader)
        logo = parcel.readString()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(address)
        parcel.writeString(city)
        parcel.writeString(country)
        parcel.writeString(bank)
        parcel.writeBoolean(cash_in)
        parcel.writeParcelable(lat_long,flags)
        parcel.writeString(logo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AtmsData>{
        override fun createFromParcel(parcel: Parcel): AtmsData {
            return AtmsData(parcel)
        }

        override fun newArray(size: Int): Array<AtmsData?> {
            return arrayOfNulls (size)
        }
    }
}