package cat.pantsu.nyaapantsu.helper

import cat.pantsu.nyaapantsu.model.RecentlyPlayed
import cat.pantsu.nyaapantsu.model.TorrentOld
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
        e.printStackTrace()
    }
    return JSONArray()
}

fun setRecentPlaylist(results: JSONArray) {
    RecentlyPlayed.torrents = results.toString()
}

fun addTorrentToRecentPlaylist(torrentOld: TorrentOld) {
    val torrents = getRecentPlaylistAsArray()
    val newTorrents = JSONArray()
    for (i in 0..(torrents.length() - 1)) {
        if (torrentOld.id == torrents.getJSONObject(i).getInt("id")) {
            torrents.remove(i)
            break
        }
    }
    newTorrents.put(torrentOld.toJson())
    for (i in 0..(torrents.length() - 1)) newTorrents.put(torrents[i])
    setRecentPlaylist(newTorrents)
}