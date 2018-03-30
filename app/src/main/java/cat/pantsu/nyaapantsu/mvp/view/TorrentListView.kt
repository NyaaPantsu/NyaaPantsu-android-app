package cat.pantsu.nyaapantsu.mvp.view

import cat.pantsu.nyaapantsu.base.BaseView
import cat.pantsu.nyaapantsu.mvp.model.response.TorrentListResponse

interface TorrentListView : BaseView {

    fun onItemsLoaded(items: TorrentListResponse)

}