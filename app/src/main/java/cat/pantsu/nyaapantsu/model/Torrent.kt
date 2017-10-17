package cat.pantsu.nyaapantsu.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.json.JSONArray


/**
 * Created by xdk78 on 2017-10-17.
 */
data class Torrent(

        @SerializedName("id")
        @Expose
        val id: Int,

        @SerializedName("name")
        @Expose
        val name: String,

        @SerializedName("status")
        @Expose
        val status: Int,

        @SerializedName("hash")
        @Expose
        val hash: String,

        @SerializedName("date")
        @Expose
        val date: String,

        @SerializedName("filesize")
        @Expose
        val filesize: Int,

        @SerializedName("description")
        @Expose
        val description: String,

        @SerializedName("comments")
        @Expose
        val comments: List<String>,

        @SerializedName("sub_category")
        @Expose
        val subCategory: String,

        @SerializedName("category")
        @Expose
        val category: String,

        @SerializedName("anidbid")
        @Expose
        val anidbid: Int,

        @SerializedName("vndbid")
        @Expose
        val vndbid: Int,

        @SerializedName("vgmdbid")
        @Expose
        val vgmdbid: Int,

        @SerializedName("dlsite")
        @Expose
        val dlsite: String,

        @SerializedName("videoquality")
        @Expose
        val videoquality: String,

        @SerializedName("tags")
        @Expose
        val tags: Any,

        @SerializedName("uploader_id")
        @Expose
        val uploaderId: Int,

        @SerializedName("uploader_name")
        @Expose
        val uploaderName: String,

        @SerializedName("uploader_old")
        @Expose
        val uploaderOld: String,

        @SerializedName("website_link")
        @Expose
        val websiteLink: String,

        @SerializedName("languages")
        @Expose
        val languages: List<JSONArray>,

        @SerializedName("magnet")
        @Expose
        val magnet: String,

        @SerializedName("seeders")
        @Expose
        val seeders: Int,

        @SerializedName("leechers")
        @Expose
        val leechers: Int,

        @SerializedName("completed")
        @Expose
        val completed: Int,

        @SerializedName("last_scrape")
        @Expose
        val lastScrape: String,

        @SerializedName("file_list")
        @Expose
        val fileList: List<JSONArray>

)