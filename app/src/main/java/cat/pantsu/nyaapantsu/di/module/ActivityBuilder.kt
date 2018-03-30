package cat.pantsu.nyaapantsu.di.module

import cat.pantsu.nyaapantsu.di.module.about.AboutFragmentProvider
import cat.pantsu.nyaapantsu.di.module.favorite.FavoritesFragmentProvider
import cat.pantsu.nyaapantsu.di.module.search.SearchTorrentListModule
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentListFragmentProvider
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentListModule
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentModule
import cat.pantsu.nyaapantsu.ui.activity.*
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
        TorrentListModule::class, TorrentListFragmentProvider::class,
        AboutFragmentProvider::class, FavoritesFragmentProvider::class
    ])

    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector()
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [TorrentModule::class])
    abstract fun bindTorrentActivity(): TorrentActivity

    @ContributesAndroidInjector(modules = [SearchTorrentListModule::class])
    abstract fun bindSearchActivity(): SearchActivity

    @ContributesAndroidInjector()
    abstract fun bindLoginActivity(): LoginActivity

}
