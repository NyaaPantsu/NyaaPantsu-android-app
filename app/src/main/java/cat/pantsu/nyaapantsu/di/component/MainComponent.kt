package cat.pantsu.nyaapantsu.di.component

import cat.pantsu.nyaapantsu.MainApp
import cat.pantsu.nyaapantsu.di.module.ActivityBuilder
import cat.pantsu.nyaapantsu.di.module.AppModule
import cat.pantsu.nyaapantsu.di.module.api.ApiModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityBuilder::class,
    ApiModule::class])

internal interface MainComponent : AndroidInjector<MainApp> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApp>()
}