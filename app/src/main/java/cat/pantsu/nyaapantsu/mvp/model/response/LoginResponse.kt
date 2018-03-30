package cat.pantsu.nyaapantsu.mvp.model.response


import cat.pantsu.nyaapantsu.mvp.model.LoginData
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(

        @SerializedName("data")
        @Expose
        var data: LoginData,

        @SerializedName("infos")
        @Expose
        var infos: List<String>,

        @SerializedName("ok")
        @Expose
        var ok: Boolean

)

