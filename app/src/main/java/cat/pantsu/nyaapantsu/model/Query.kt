package cat.pantsu.nyaapantsu.model

import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils

/**
 * Created by ltype on 2017/7/6.
 * @see <a href="https://dev.pantsu.cat/apidoc/#api-Torrents-FindTorrents"> API </a>
 */
class Query() : Parcelable {
    var c = ""
    var q = ""
    var limit = ""
    var userID = ""
    var fromID = ""
    var s = ""
    var maxage = ""
    var toDate = ""
    var fromDate = ""
    var dateType = "d"
    var minSize = ""
    var maxSize = ""
    var sizeType = ""
    var sort = ""
    var order = ""
    var lang = ""
    var page = 1

    override fun toString(): String {
        return "?c=$c&q=$q&limit=$limit&userID=$userID&fromID=$fromID&s=$s&maxage=$maxage" +
                "&toDate=$toDate&fromDate=$fromDate&dateType=$dateType&minSize=$minSize" +
                "&maxSize=$maxSize&sizeType=$sizeType&sort=$sort&order=$order&lang=$lang&page=$page"
    }

    fun isQueryable(): Boolean {
        return !TextUtils.isEmpty(q) || !TextUtils.isEmpty(c) || !TextUtils.isEmpty(s) || !TextUtils.isEmpty(limit)
    }

    constructor(parcel: Parcel) : this() {
        c = parcel.readString()
        q = parcel.readString()
        limit = parcel.readString()
        userID = parcel.readString()
        fromID = parcel.readString()
        s = parcel.readString()
        maxage = parcel.readString()
        toDate = parcel.readString()
        fromDate = parcel.readString()
        dateType = parcel.readString()
        minSize = parcel.readString()
        maxSize = parcel.readString()
        sizeType = parcel.readString()
        sort = parcel.readString()
        order = parcel.readString()
        lang = parcel.readString()
        page = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(c)
        parcel.writeString(q)
        parcel.writeString(limit)
        parcel.writeString(userID)
        parcel.writeString(fromID)
        parcel.writeString(s)
        parcel.writeString(maxage)
        parcel.writeString(toDate)
        parcel.writeString(fromDate)
        parcel.writeString(dateType)
        parcel.writeString(minSize)
        parcel.writeString(maxSize)
        parcel.writeString(sizeType)
        parcel.writeString(sort)
        parcel.writeString(order)
        parcel.writeString(lang)
        parcel.writeInt(page)
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
}