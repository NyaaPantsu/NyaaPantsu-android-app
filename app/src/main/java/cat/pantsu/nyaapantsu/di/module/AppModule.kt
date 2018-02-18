package cat.pantsu.nyaapantsu.di.module

import android.content.Context
import cat.pantsu.nyaapantsu.MainApp
import dagger.Binds
import dagger.Module


@Module
abstract class AppModule {
    @Binds
    abstract fun provideContext(application: MainApp): Context
}
