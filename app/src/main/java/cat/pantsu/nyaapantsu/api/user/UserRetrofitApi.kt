package cat.pantsu.nyaapantsu.api.user


import cat.pantsu.nyaapantsu.mvp.model.LoginModel
import cat.pantsu.nyaapantsu.mvp.model.response.LoginResponse
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRetrofitApi {

    @POST("login")
    fun login(@Body loginModel: LoginModel): Single<LoginResponse>
}