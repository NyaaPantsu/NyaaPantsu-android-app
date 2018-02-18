package cat.pantsu.nyaapantsu.api

import cat.pantsu.nyaapantsu.model.Login
import cat.pantsu.nyaapantsu.model.Search
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by xdk78 on 2017-10-07.
 */
interface ApiService {

    @POST("/login")
    fun loginIn(@Path("username") username: String, @Path("password") password: String): Observable<Login>

    @GET("/search")
    fun search(
            @Query("c") c: List<String>,
            @Query("q") q: String,
            @Query("page") page: Int,
            @Query("limit") limit: String,
            @Query("userID") userID: String,
            @Query("fromID") fromID: String,
            @Query("s") s: String,
            @Query("maxage") maxage: String,
            @Query("toDate") toDate: String,
            @Query("fromDate") fromDate: String,
            @Query("dateType") dateType: String,
            @Query("minSize") minSize: String,
            @Query("maxSize") maxSize: String,
            @Query("sizeType") sizeType: String,
            @Query("sort") sort: String,
            @Query("order") order: Boolean,
            @Query("lang") lang: List<String>
    ): Observable<Search>

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