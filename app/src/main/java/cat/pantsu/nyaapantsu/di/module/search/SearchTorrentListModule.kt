package cat.pantsu.nyaapantsu.di.module.search

import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.base.Schedulers
import cat.pantsu.nyaapantsu.mvp.presenter.SearchTorrentListPresenter
import dagger.Module
import dagger.Provides


@Module
class SearchTorrentListModule {
    @Provides
    fun provideSearchTorrentListPresenter(schedulers: Schedulers, torrentApi: TorrentApi) = SearchTorrentListPresenter(schedulers, torrentApi)

}