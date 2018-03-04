package cat.pantsu.nyaapantsu.di.module.favorite

import cat.pantsu.nyaapantsu.ui.fragment.FavoritesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class FavoritesFragmentProvider {
    @ContributesAndroidInjector()
    abstract fun provideFavoritesFragment(): FavoritesFragment

}