package cat.pantsu.nyaapantsu.mvp.view

import cat.pantsu.nyaapantsu.base.BaseView
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel

interface TorrentListView : BaseView {

    fun onItemLoaded(items: TorrentListResponse<TorrentModel>)

}