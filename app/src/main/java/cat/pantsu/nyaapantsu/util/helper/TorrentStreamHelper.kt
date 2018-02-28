package cat.pantsu.nyaapantsu.util.helper

import android.os.Environment
import com.github.se_bastiaan.torrentstream.TorrentOptions
import com.github.se_bastiaan.torrentstream.TorrentStream
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener


class TorrentStreamHelper private constructor() {
    private val stream: TorrentStream = newInstance()
    private var listener: TorrentListener ?= null

    private object Holder {
        val INSTANCE = TorrentStreamHelper()
    }

    companion object {
        val instance: TorrentStreamHelper by lazy { Holder.INSTANCE }
    }

    //TODO multiple stream
    private fun newInstance(): TorrentStream {
        val options = TorrentOptions.Builder()
                .saveLocation(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
                .removeFilesAfterStop(true)
                .build()
        return TorrentStream.init(options)
    }

    fun isStreaming(): Boolean {
        return stream.isStreaming
    }

    fun start(torrentUrl: String) {
        stream.startStream(torrentUrl)
    }

    fun stop() {
        stream.stopStream()
    }

    fun setListener(l: TorrentListener?) {
        stream.removeListener(listener)
        stream.addListener(l)
        listener = l
    }
}