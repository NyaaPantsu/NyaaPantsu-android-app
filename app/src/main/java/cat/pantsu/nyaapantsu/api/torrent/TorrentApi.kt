package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import io.reactivex.Single


interface TorrentApi {
    fun getTorrentList(): Single<TorrentListResponse<TorrentModel>>
    fun getTorrent(id: Int): Single<TorrentModel>
    fun getSearchTorrentList(c: String?, q: String?, limit: String?, s: String?, toDate: String?, fromDate: String?, minSize: String?, maxSize: String?, sizeType: String?): Single<TorrentListResponse<TorrentModel>>
}