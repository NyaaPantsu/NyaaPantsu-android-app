package cat.pantsu.nyaapantsu.api

import cat.pantsu.nyaapantsu.model.Login
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Created by xdk78 on 2017-10-07.
 */
interface ApiService {

    @POST("/login")
    fun loginIn(@Path("username") username: String, @Path("password") password: String): Observable<Login>

    companion object Factory {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://nyaa.pantsu.cat/api")
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}