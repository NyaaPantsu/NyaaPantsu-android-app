package cat.pantsu.nyaapantsu.api.user

import cat.pantsu.nyaapantsu.mvp.model.LoginModel
import cat.pantsu.nyaapantsu.mvp.model.response.LoginResponse
import io.reactivex.Single

interface UserApi {
    fun login(loginModel: LoginModel): Single<LoginResponse>
}