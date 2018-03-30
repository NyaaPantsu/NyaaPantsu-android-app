package cat.pantsu.nyaapantsu.mvp.model.response

import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TorrentListResponse(@SerializedName("torrents")
                               @Expose
                               val torrents: List<TorrentModel>,

                               @SerializedName("queryRecordCount")
                               @Expose
                               val queryRecordCount: Int,

                               @SerializedName("totalRecordCount")
                               @Expose
                               val totalRecordCount: Int)
