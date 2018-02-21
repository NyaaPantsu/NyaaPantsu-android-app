package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import io.reactivex.Single


interface TorrentApi {
    fun getTorrentList(): Single<TorrentListResponse<TorrentModel>>
    fun getTorrent(id: Int): Single<TorrentModel>
}