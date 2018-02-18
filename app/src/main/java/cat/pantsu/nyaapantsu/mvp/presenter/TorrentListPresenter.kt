package cat.pantsu.nyaapantsu.mvp.presenter

import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.base.BasePresenter
import cat.pantsu.nyaapantsu.base.Schedulers
import cat.pantsu.nyaapantsu.mvp.view.TorrentListView


class TorrentListPresenter(val schedulers: Schedulers, val torrentApi: TorrentApi) : BasePresenter<TorrentListView>() {

    fun loadData() {
        compositeObservable.add(
                torrentApi
                        .getTorrentList()
                        .observeOn(schedulers.mainThread())
                        .subscribeOn(schedulers.backgroundThread())
                        .subscribe(
                                { items -> view?.onItemLoaded(items) },
                                { error -> view?.onError(error) })
        )
    }
}
