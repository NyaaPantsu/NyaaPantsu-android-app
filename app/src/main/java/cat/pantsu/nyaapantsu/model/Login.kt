package cat.pantsu.nyaapantsu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xdk78 on 2017-10-07.
 */
data class Login(

        @SerializedName("data")
        @Expose
        val data: List<Data>,

        @SerializedName("infos")
        @Expose
        val infos: List<String>,

        @SerializedName("ok")
        @Expose
        val ok: Boolean
)

data class Data(

        @SerializedName("user_id")
        @Expose
        val userId: Int,

        @SerializedName("username")
        @Expose
        val username: String,

        @SerializedName("status")
        @Expose
        val status: Int,

        @SerializedName("md5")
        @Expose
        val md5: String,

        @SerializedName("created_at")
        @Expose
        val createdAt: String,

        @SerializedName("liking_count")
        @Expose
        val likingCount: Int,

        @SerializedName("liked_count")
        @Expose
        val likedCount: Int

)