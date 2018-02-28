package cat.pantsu.nyaapantsu.di.module

import cat.pantsu.nyaapantsu.di.module.about.AboutFragmentProvider
import cat.pantsu.nyaapantsu.di.module.recent.RecentFragmentProvider
import cat.pantsu.nyaapantsu.di.module.search.SearchTorrentListModule
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentListFragmentProvider
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentListModule
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentModule
import cat.pantsu.nyaapantsu.ui.activity.HomeActivity
import cat.pantsu.nyaapantsu.ui.activity.SearchActivity
import cat.pantsu.nyaapantsu.ui.activity.SplashActivity
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
        TorrentListModule::class, TorrentListFragmentProvider::class,
        AboutFragmentProvider::class, RecentFragmentProvider::class
    ])

    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector()
    abstract fun bindSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [TorrentModule::class])
    abstract fun bindTorrentActivity(): TorrentActivity

    @ContributesAndroidInjector(modules = [SearchTorrentListModule::class])
    abstract fun bindSearchActivity(): SearchActivity

}
