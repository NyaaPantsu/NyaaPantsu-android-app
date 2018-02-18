package cat.pantsu.nyaapantsu.mvp.view

import cat.pantsu.nyaapantsu.base.BaseView
import cat.pantsu.nyaapantsu.mvp.model.TorrentListModel
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse

interface TorrentListView : BaseView {

    fun onItemLoaded(items: TorrentListResponse<TorrentListModel>)

}