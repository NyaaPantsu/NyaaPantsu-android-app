package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import io.reactivex.Single
import retrofit2.Retrofit


class TorrentRepository(val retrofit: Retrofit) : TorrentApi {
    private val torrentApi by lazy { retrofit.create(TorrentRetrofitApi::class.java) }

    override fun getTorrentList(): Single<TorrentListResponse<TorrentModel>> = torrentApi.getTorrentList(
            null, null, 1, null,
            null, null, null,
            null, null, null,
            null, null, null,
            null, null, null, null)

    override fun searchTorrentList(c: String?,
                                   q: String?,
                                   limit: String?,
                                   s: String?,
                                   toDate: String?,
                                   fromDate: String?,
                                   minSize: String?,
                                   maxSize: String?,
                                   sizeType: String?
    ): Single<TorrentListResponse<TorrentModel>> = torrentApi.getTorrentList(
            c, q, 1, limit,
            null, null, s,
            null, toDate, fromDate,
            null, minSize, maxSize,
            sizeType, null, null, null)


    override fun getTorrent(id: Int): Single<TorrentModel> = torrentApi.getTorrent(id)
}
