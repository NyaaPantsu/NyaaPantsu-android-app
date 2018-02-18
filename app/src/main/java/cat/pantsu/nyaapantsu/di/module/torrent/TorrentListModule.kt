package cat.pantsu.nyaapantsu.di.module.torrent

import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.base.Schedulers
import cat.pantsu.nyaapantsu.mvp.presenter.TorrentListPresenter
import dagger.Module
import dagger.Provides


@Module
class TorrentListModule {
    @Provides
    fun provideTorrentListPresenter(schedulers: Schedulers, torrentApi: TorrentApi) = TorrentListPresenter(schedulers, torrentApi)

}