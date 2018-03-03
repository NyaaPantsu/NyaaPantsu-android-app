package cat.pantsu.nyaapantsu.mvp.presenter

import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.base.BasePresenter
import cat.pantsu.nyaapantsu.base.Schedulers
import cat.pantsu.nyaapantsu.mvp.view.SearchTorrentListView


class SearchTorrentListPresenter(val schedulers: Schedulers, val torrentApi: TorrentApi) : BasePresenter<SearchTorrentListView>() {
    fun loadData(c: String?,
                 q: String?,
                 limit: String?,
                 s: String?,
                 toDate: String?,
                 fromDate: String?,
                 minSize: String?,
                 maxSize: String?,
                 sizeType: String?) {
        compositeObservable.add(
                torrentApi
                        .searchTorrentList(c, q, limit, s, toDate, fromDate,
                                minSize, maxSize, sizeType)
                        .observeOn(schedulers.mainThread())
                        .subscribeOn(schedulers.backgroundThread())
                        .subscribe(
                                { view?.onItemsLoaded(it) },
                                { view?.onError(it) })
        )
    }
}
