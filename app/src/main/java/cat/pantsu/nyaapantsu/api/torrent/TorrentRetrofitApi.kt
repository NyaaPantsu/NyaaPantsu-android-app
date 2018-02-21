package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface TorrentRetrofitApi {

    @GET("search")
    fun getTorrentList(@Query("c") c: List<String>?,
                       @Query("q") q: String?,
                       @Query("page") page: Int?,
                       @Query("limit") limit: String?,
                       @Query("userID") userID: String?,
                       @Query("fromID") fromID: String?,
                       @Query("s") s: String?,
                       @Query("maxage") maxage: String?,
                       @Query("toDate") toDate: String?,
                       @Query("fromDate") fromDate: String?,
                       @Query("dateType") dateType: String?,
                       @Query("minSize") minSize: String?,
                       @Query("maxSize") maxSize: String?,
                       @Query("sizeType") sizeType: String?,
                       @Query("sort") sort: String?,
                       @Query("order") order: Boolean?,
                       @Query("lang") lang: List<String>?): Single<TorrentListResponse<TorrentModel>>

    @GET("view/{id}")
    fun getTorrent(@Path("id") id: Int): Single<TorrentModel>
}