package cat.pantsu.nyaapantsu.util.helper

import com.github.se_bastiaan.torrentstream.listeners.TorrentListener


interface ITorrentStreamHelper {
    fun isStreaming(): Boolean
    fun start(torrentUrl: String)
    fun stop()
    fun setListener(l: TorrentListener?)
}