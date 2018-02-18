package cat.pantsu.nyaapantsu.mvp.model


data class TorrentListResponse<out T>(val torrents: List<T>, val queryRecordCount: Int, val totalRecordCount: Int)
