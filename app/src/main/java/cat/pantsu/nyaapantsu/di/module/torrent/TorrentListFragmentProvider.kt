package cat.pantsu.nyaapantsu.di.module.torrent

import cat.pantsu.nyaapantsu.ui.fragment.TorrentListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class TorrentListFragmentProvider {
    @ContributesAndroidInjector(modules = [TorrentListModule::class])
    abstract fun provideTorrentListFragment(): TorrentListFragment

}