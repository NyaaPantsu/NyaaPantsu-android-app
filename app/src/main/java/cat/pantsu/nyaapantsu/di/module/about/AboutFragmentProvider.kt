package cat.pantsu.nyaapantsu.di.module.about

import cat.pantsu.nyaapantsu.ui.fragment.AboutFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class AboutFragmentProvider {
    @ContributesAndroidInjector()
    abstract fun provideAboutFragment(): AboutFragment

}