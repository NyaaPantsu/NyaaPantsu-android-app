package cat.pantsu.nyaapantsu.mvp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class TorrentListResponse<out T>(@SerializedName("torrents")
                                      @Expose
                                      val torrents: List<T>,

                                      @SerializedName("queryRecordCount")
                                      @Expose
                                      val queryRecordCount: Int,

                                      @SerializedName("totalRecordCount")
                                      @Expose
                                      val totalRecordCount: Int)
