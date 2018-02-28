package cat.pantsu.nyaapantsu.mvp.view

import cat.pantsu.nyaapantsu.base.BaseView
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel

interface TorrentView : BaseView {

    fun onItemsLoaded(items: TorrentModel)

}