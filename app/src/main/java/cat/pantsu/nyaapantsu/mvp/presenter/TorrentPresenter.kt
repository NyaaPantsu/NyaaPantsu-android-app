package cat.pantsu.nyaapantsu.mvp.presenter

import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.base.BasePresenter
import cat.pantsu.nyaapantsu.base.Schedulers
import cat.pantsu.nyaapantsu.mvp.view.TorrentView


class TorrentPresenter(val schedulers: Schedulers, val torrentApi: TorrentApi) : BasePresenter<TorrentView>() {

    fun loadData(id: Int) {
        compositeObservable.add(
                torrentApi
                        .getTorrent(id)
                        .observeOn(schedulers.mainThread())
                        .subscribeOn(schedulers.backgroundThread())
                        .subscribe(
                                { view?.onItemsLoaded(it) },
                                { view?.onError(it) })
        )
    }
}
