package cat.pantsu.nyaapantsu.api.user

import cat.pantsu.nyaapantsu.mvp.model.LoginModel
import cat.pantsu.nyaapantsu.mvp.model.response.LoginResponse
import io.reactivex.Single
import retrofit2.Retrofit

class UserRepository(val retrofit: Retrofit) : UserApi {
    private val userApi by lazy { retrofit.create(UserRetrofitApi::class.java) }

    override fun login(loginModel: LoginModel): Single<LoginResponse> = userApi.login(loginModel)

}