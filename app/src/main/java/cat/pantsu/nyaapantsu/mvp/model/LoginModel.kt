package cat.pantsu.nyaapantsu.mvp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginModel(
        @SerializedName("username")
        @Expose
        var username: String,

        @SerializedName("password")
        @Expose
        var password: String
)