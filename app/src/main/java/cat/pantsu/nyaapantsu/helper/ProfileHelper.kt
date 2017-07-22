package cat.pantsu.nyaapantsu.helper

import android.util.Log
import cat.pantsu.nyaapantsu.model.ProfileQuery
import cat.pantsu.nyaapantsu.model.Torrent
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import org.json.JSONArray
import org.json.JSONObject
import java.util.*



class ProfileHelper private constructor(){
    var query: ProfileQuery ?= null
    var profile: JSONObject = JSONObject()

    private object Holder {
        val INSTANCE = ProfileHelper()
    }

    companion object {
        val instance: ProfileHelper by lazy { Holder.INSTANCE }
    }


    fun get(cb: Callback) {
        ("/profile" + query.toString()).httpGet().responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.d("Network", "Big Fail :/")
                    Log.d("Network", response.toString())
                    Log.d("Network", request.toString())
                    cb.failure()
                }
                is Result.Success -> {
                    Log.d("Network", result.toString())
                    Log.d("Network", request.toString())
                    Log.d("Network", response.toString())

                    val json = result.getAs<Json>()
                    if (json !== null) {
                        val resultObj = json.obj()
                        profile = resultObj.optJSONObject("data")
                    }
                    cb.success(profile)
                }
            }
        }
    }

    interface Callback {
        fun failure()
        fun success(profile: JSONObject)
    }
}