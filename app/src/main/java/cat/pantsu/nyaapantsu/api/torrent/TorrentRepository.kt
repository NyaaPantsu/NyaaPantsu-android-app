package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentListModel
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import io.reactivex.Single
import retrofit2.Retrofit


class TorrentRepository(val retrofit: Retrofit) : TorrentApi {
    private val mainApi by lazy { retrofit.create(TorrentRetrofitApi::class.java) }

    override fun getTorrentList(): Single<TorrentListResponse<TorrentListModel>> = mainApi.getTorrentList(
            null, null, null, "20",
            null, null, null,
            null, null, null,
            null, null, null,
            null, null, null, null)
}
