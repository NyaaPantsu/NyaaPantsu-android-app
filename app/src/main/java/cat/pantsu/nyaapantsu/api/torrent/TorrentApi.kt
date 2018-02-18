package cat.pantsu.nyaapantsu.api.torrent

import cat.pantsu.nyaapantsu.mvp.model.TorrentListModel
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import io.reactivex.Single


interface TorrentApi {
    fun getTorrentList(): Single<TorrentListResponse<TorrentListModel>>

}