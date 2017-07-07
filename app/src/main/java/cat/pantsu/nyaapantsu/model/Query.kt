package cat.pantsu.nyaapantsu.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

/**
 * Created by ltype on 2017/7/6.
 */
class Query : Parcelable {
    var q = ""
    var c = ""
    var s = ""
    var max = ""
    var fromSize = ""
    var toSize = ""
    var fromDate = ""
    var toDate = ""
    var sizeType = ""

    constructor()

    constructor(parcel: Parcel) {
        q = parcel.readString()
        c = parcel.readString()
        s = parcel.readString()
        max = parcel.readString()
        fromSize = parcel.readString()
        toSize = parcel.readString()
        fromDate = parcel.readString()
        toDate = parcel.readString()
        sizeType = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(q)
        parcel.writeString(c)
        parcel.writeString(s)
        parcel.writeString(max)
        parcel.writeString(fromSize)
        parcel.writeString(toSize)
        parcel.writeString(fromDate)
        parcel.writeString(toDate)
        parcel.writeString(sizeType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Query> {
        override fun createFromParcel(parcel: Parcel): Query {
            return Query(parcel)
        }

        override fun newArray(size: Int): Array<Query?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "?q=$q&c=$c&s=$s&limit=$max&fromDate=$fromDate&toDate=$toDate&minSize=$fromSize&maxSize=$toSize&sizeType=$sizeType&dateType=d"
    }

    fun isQueryable(): Boolean {
        return !TextUtils.isEmpty(q) || !TextUtils.isEmpty(c) || !TextUtils.isEmpty(s) || !TextUtils.isEmpty(max)
    }
}