package cat.pantsu.nyaapantsu.api

import cat.pantsu.nyaapantsu.model.Login
import io.reactivex.Observable

/**
 * Created by xdk78 on 2017-10-07.
 */
class Repository(val apiService: ApiService) {
    fun loginIn(username: String, password: String): Observable<Login> {
        return apiService.loginIn(username, password)
    }
}