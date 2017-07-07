package cat.pantsu.nyaapantsu.helper

import cat.pantsu.nyaapantsu.model.RecentlyPlayed
import cat.pantsu.nyaapantsu.model.Torrent
import org.json.JSONArray
import org.json.JSONException

/**
 * Created by akuma06 on 04/07/2017.
 */

fun getRecentPlaylistAsArray(): JSONArray {
    try {
        val torrents = JSONArray(RecentlyPlayed.torrents)
        return torrents
    } catch (e: JSONException) {
    }
    return JSONArray()
}

fun setRecentPlaylist(results: JSONArray) {
    RecentlyPlayed.torrents = results.toString()
}

fun addTorrentToRecentPlaylist(torrent: Torrent) {
    var torrents = getRecentPlaylistAsArray()
    torrents.put(torrent.toJson())
    setRecentPlaylist(torrents)
}