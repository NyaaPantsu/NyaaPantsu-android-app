package cat.pantsu.nyaapantsu.helper

import android.util.Log
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.Torrent
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import org.json.JSONArray
import java.util.*

/**
 * Created by ltype on 2017/7/9.
 */
class QueryHelper private constructor(){
    var query: Query ?= null
    var torrents: JSONArray = JSONArray()

    private object Holder {
        val INSTANCE = QueryHelper()
    }

    companion object {
        val instance: QueryHelper by lazy { Holder.INSTANCE }
        fun parseTorrents(torrents: JSONArray): LinkedList<Torrent> {
            val length = (torrents.length() - 1)
            return (0..length).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        }
    }


    fun next() {
        //TODO
    }

    fun prev() {
        //TODO
    }

    fun search(cb: Callback) {
        ("/search" + query.toString()).httpGet().responseJson { request, response, result ->
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
                        torrents = resultObj.optJSONArray("torrents")
                    }
                    cb.success(parseTorrents(torrents))
                }
            }
        }
    }

    interface Callback {
        fun failure()
        fun success(torrentList: LinkedList<Torrent>)
    }
}