package cat.pantsu.nyaapantsu.di.module.recent

import cat.pantsu.nyaapantsu.ui.fragment.RecentFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class RecentFragmentProvider {
    @ContributesAndroidInjector()
    abstract fun provideRecentFragment(): RecentFragment

}