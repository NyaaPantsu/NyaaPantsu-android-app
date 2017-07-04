package cat.pantsu.nyaapantsu

import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by akuma06 on 20/06/2017.
 */
class Torrent(torrent: JSONObject) {
    val categories = arrayOf("_", "3_", "3_12", "3_5", "3_13", "3_6", "2_", "2_3", "2_4", "4_", "4_7", "4_14", "4_8", "5_", "5_9", "5_10", "5_18", "5_11", "6_", "6_15", "6_16", "1_", "1_1", "1_2")
    internal var name: String = ""
    internal var description: String = ""
    internal var username: String = ""
    internal var id  = 0
    internal var user_id = 0
    internal var status = 0
    internal var website = ""
    internal var languages = JSONArray()
    internal var category = 0
    internal var magnet = ""
    internal var download = ""
    internal var hash = ""
    internal var date = ""
    internal var size = ""
    internal var seeders = 0
    internal var leechers = 0
    internal var completed = 0
    internal var last_scrape = ""
    internal var fileList = JSONArray()

    init {

        name = if (torrent.optString("name") !== null)  torrent.optString("name") else ""
        description = if (torrent.optString("description") !== null)  torrent.optString("description") else ""
        username = if (torrent.optString("uploader_name") !== null)  torrent.optString("uploader_name") else ""
        website = if (torrent.optString("website_link") !== null)  torrent.optString("website_link") else ""
        languages = if (torrent.optJSONArray("languages") !== null)  torrent.optJSONArray("languages") else JSONArray()
        magnet = if (torrent.optString("magnet") !== null)  torrent.optString("magnet") else ""
        download = if (torrent.optString("torrent") !== null)  torrent.optString("torrent") else ""
        hash = if (torrent.optString("hash") !== null)  torrent.optString("hash") else ""
        date = if (torrent.optString("date") !== null)  torrent.optString("date") else ""
        size = humanReadableByteCount(torrent.optLong("filesize"), false)
        id = torrent.optInt("id")
        status = torrent.optInt("status")
        user_id = torrent.optInt("uploader_id")
        seeders = torrent.optInt("seeders")
        leechers = torrent.optInt("leechers")
        completed = torrent.optInt("completed")
        last_scrape = if (torrent.optString("last_scrape") !== null)  torrent.optString("last_scrape") else ""
        fileList = if (torrent.optJSONArray("file_list") !== null) torrent.optJSONArray("file_list") else JSONArray("[]")
        category = if (torrent.optString("category") !== null) categories.indexOf(torrent.optString("category")+"_"+torrent.optString("sub_category")) else 0
        if (category == -1) {
            category = 0
        }
    }

    fun humanReadableByteCount(bytes: Long, si: Boolean): String {
        val unit = if (si) 1000 else 1024
        if (bytes < unit) return bytes.toString() + " B"
        val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1] + if (si) "" else "i"
        return String.format("%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

    fun toJson(): JSONObject {
        return JSONObject().putOpt("name", name)
                .putOpt("description", description)
                .putOpt("uploader_name", username)
                .putOpt("website_link", website)
                .putOpt("languages", languages)
                .putOpt("magnet", magnet)
                .putOpt("torrent", download)
                .putOpt("hash", hash)
                .putOpt("date", date)
                .putOpt("filesize", size)
                .putOpt("id", id)
                .putOpt("status", status)
                .putOpt("uploader_id", user_id)
                .putOpt("seeders", seeders)
                .putOpt("leechers", leechers)
                .putOpt("completed", completed)
                .putOpt("last_scrape", last_scrape)
                .putOpt("file_list", fileList)
                .putOpt("category", category)
    }
}