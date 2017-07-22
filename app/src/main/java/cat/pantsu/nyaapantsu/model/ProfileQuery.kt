package cat.pantsu.nyaapantsu.model

import android.os.Parcel
import android.os.Parcelable



class ProfileQuery : Parcelable {

    var userID = 0

    constructor()

    constructor(parcel: Parcel) {
        userID = parcel.readInt()

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userID)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProfileQuery> {
        override fun createFromParcel(parcel: Parcel): ProfileQuery {
            return ProfileQuery(parcel)
        }

        override fun newArray(size: Int): Array<ProfileQuery?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "?id=$userID"
    }

}