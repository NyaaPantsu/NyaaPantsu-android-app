package cat.pantsu.nyaapantsu.api

import cat.pantsu.nyaapantsu.model.Login
import cat.pantsu.nyaapantsu.model.Search
import io.reactivex.Observable

/**
 * Created by xdk78 on 2017-10-07.
 */
class Repository(val apiService: ApiService) {

    fun loginIn(username: String, password: String): Observable<Login> {
        return apiService.loginIn(username, password)
    }

    fun search(
            c: List<String>,
            q: String,
            page: Int,
            limit: String,
            userID: String,
            fromID: String,
            s: String,
            maxage: String,
            toDate: String,
            fromDate: String,
            dateType: String,
            minSize: String,
            maxSize: String,
            sizeType: String,
            sort: String,
            order: Boolean,
            lang: List<String>
    ): Observable<Search> {
        return apiService.search(
                c,
                q,
                page,
                limit,
                userID,
                fromID,
                s,
                maxage,
                toDate,
                fromDate,
                dateType,
                minSize,
                maxSize,
                sizeType,
                sort,
                order,
                lang)
    }
}