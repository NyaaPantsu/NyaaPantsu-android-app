package cat.pantsu.nyaapantsu.api

/**
 * Created by xdk78 on 2017-10-07.
 */
object RepositoryProvider {

    fun provideRepository(): Repository {
        return Repository(ApiService.Factory.create())
    }

}