package cat.pantsu.nyaapantsu.di.module.torrent

import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.base.Schedulers
import cat.pantsu.nyaapantsu.mvp.presenter.TorrentPresenter
import dagger.Module
import dagger.Provides


@Module
class TorrentModule {
    @Provides
    fun provideTorrentPresenter(schedulers: Schedulers, torrentApi: TorrentApi) = TorrentPresenter(schedulers, torrentApi)

}