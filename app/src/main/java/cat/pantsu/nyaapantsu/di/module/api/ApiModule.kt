package cat.pantsu.nyaapantsu.di.module.api

import cat.pantsu.nyaapantsu.api.ApiConstants
import cat.pantsu.nyaapantsu.api.torrent.TorrentApi
import cat.pantsu.nyaapantsu.api.torrent.TorrentRepository
import cat.pantsu.nyaapantsu.api.user.UserApi
import cat.pantsu.nyaapantsu.api.user.UserRepository
import cat.pantsu.nyaapantsu.base.MainAppSchedulers
import cat.pantsu.nyaapantsu.base.Schedulers
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideTorrentApi(retrofit: Retrofit): TorrentApi = TorrentRepository(retrofit)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = UserRepository(retrofit)

    @Provides
    @Singleton
    fun provideRetrofit(
            rxJavaCallAdapterFactory: RxJava2CallAdapterFactory,
            gsonConverterFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder()
                .baseUrl(ApiConstants.BASE_URL)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(gsonConverterFactory)
                .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRxJavaCallAdapter(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun provideMainAppSchedulers(): Schedulers = MainAppSchedulers()
}