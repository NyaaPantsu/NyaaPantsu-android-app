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
import java.util.*



class ProfileHelper private constructor(){
    var query: ProfileQuery ?= null
    var torrents: JSONArray = JSONArray()
    var torrentList : LinkedList<Torrent> = LinkedList()

    private object Holder {
        val INSTANCE = ProfileHelper()
    }

    companion object {
        val instance: ProfileHelper by lazy { Holder.INSTANCE }
        fun parseTorrents(torrents: JSONArray): LinkedList<Torrent> {
            val length = (torrents.length() - 1)
            return (0..length).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        }
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
                    if (json !== null) torrents = json.array()
                    torrentList = parseTorrents(torrents)
                    cb.success(torrentList)
                }
            }
        }
    }

    interface Callback {
        fun failure()
        fun success(torrentList: LinkedList<Torrent>)
    }
}