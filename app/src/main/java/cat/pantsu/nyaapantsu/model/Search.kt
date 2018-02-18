package cat.pantsu.nyaapantsu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by xdk78 on 2017-10-07.
 */
data class Search(

        @SerializedName("torrents")
        @Expose
        val torrents: List<Torrent>,

        @SerializedName("queryRecordCount")
        @Expose
        val queryRecordCount: Int,

        @SerializedName("totalRecordCount")
        @Expose
        val totalRecordCount: Int
)
