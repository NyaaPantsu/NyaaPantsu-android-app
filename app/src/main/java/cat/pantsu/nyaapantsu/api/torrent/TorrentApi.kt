package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.mvp.model.response.TorrentListResponse
import io.reactivex.Single


interface TorrentApi {
    fun getTorrentList(): Single<TorrentListResponse>
    fun getTorrent(id: Int): Single<TorrentModel>
    fun searchTorrentList(c: String?, q: String?, limit: String?, s: String?, toDate: String?, fromDate: String?, minSize: String?, maxSize: String?, sizeType: String?): Single<TorrentListResponse>
}