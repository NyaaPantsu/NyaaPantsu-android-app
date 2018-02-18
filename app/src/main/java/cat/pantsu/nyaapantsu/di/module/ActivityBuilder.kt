package cat.pantsu.nyaapantsu.di.module

import cat.pantsu.nyaapantsu.di.module.about.AboutFragmentProvider
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentListFragmentProvider
import cat.pantsu.nyaapantsu.di.module.torrent.TorrentListModule
import cat.pantsu.nyaapantsu.ui.activity.HomeActivity
import cat.pantsu.nyaapantsu.ui.activity.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [
        TorrentListModule::class, TorrentListFragmentProvider::class,
        AboutFragmentProvider::class
    ])
    abstract fun bindHomeActivity(): HomeActivity

    @ContributesAndroidInjector()
    abstract fun bindSplashActivity(): SplashActivity
}
