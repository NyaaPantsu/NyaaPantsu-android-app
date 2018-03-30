package cat.pantsu.nyaapantsu.mvp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginData(

        @SerializedName("user_id")
        @Expose
        var userId: Int,

        @SerializedName("username")
        @Expose
        var username: String,

        @SerializedName("status")
        @Expose
        var status: Int,

        @SerializedName("token")
        @Expose
        var token: String,

        @SerializedName("md5")
        @Expose
        var md5: String,

        @SerializedName("created_at")
        @Expose
        var createdAt: String,

        @SerializedName("liking_count")
        @Expose
        var likingCount: Int,

        @SerializedName("liked_count")
        @Expose
        var likedCount: Int

)