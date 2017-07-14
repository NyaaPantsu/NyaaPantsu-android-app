package cat.pantsu.nyaapantsu.ui.fragment

import android.Manifest
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.graphics.drawable.LevelListDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.helper.ImageGetterAsyncTask
import cat.pantsu.nyaapantsu.helper.TorrentStreamHelper
import cat.pantsu.nyaapantsu.helper.addTorrentToRecentPlaylist
import cat.pantsu.nyaapantsu.model.Torrent
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import com.github.se_bastiaan.torrentstream.StreamStatus
import com.github.se_bastiaan.torrentstream.listeners.TorrentListener
import kotlinx.android.synthetic.main.fragment_torrent_view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject
import java.lang.Exception

/**
 * Created by ltype on 2017/7/9.
 */
class TorrentViewFragment: Fragment(), TorrentListener {
    var torrent = Torrent(JSONObject())
    var showDet = false
    var progressdialog: ProgressDialog? = null

    companion object {
        fun newInstance(torrent: String): TorrentViewFragment {
            val fragment = TorrentViewFragment()
            val args = Bundle()
            args.putString("torrent", torrent)
            fragment.arguments = args
            return fragment
        }

        fun newInstance(id: Int): TorrentViewFragment {
            val fragment = TorrentViewFragment()
            val args = Bundle()
            args.putInt("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_torrent_view, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments.getString("torrent") !== null) {
            torrent = Torrent(JSONObject(arguments.getString("torrent")))
            genView()
        } else if (arguments.containsKey("id")) {
            torrent.id = arguments.getInt("id", 0)
            getData()
        } else {
            toast("No data provided")
            activity.finish()
        }



        progressdialog = ProgressDialog(context)
    }


    fun getData() {
        ("/view/" + torrent.id).httpGet().responseJson { request, response, result ->
            when (result) {
                is Result.Failure -> {
                    Log.d("Network", "Big Fail :/")
                    Log.d("Network", response.toString())
                    Log.d("Network", request.toString())
                }
                is Result.Success -> {
                    Log.d("Network", result.toString())
                    Log.d("Network", request.toString())
                    Log.d("Network", response.toString())

                    val json = result.getAs<Json>()
                    if (json !== null) {
                        torrent = Torrent(json.obj())
                        genView()
                    }
                }
            }
        }
    }

    fun genView() {
        if (!isAdded) return
        torrentName.text = torrent.name
        torrentCategory.text = resources.getStringArray(R.array.cat_array)[torrent.category]
        torrentUser.text = torrent.username
        torrentHash.text = torrent.hash
        torrentDate.text = torrent.date
        torrentSize.text = torrent.size
        var spanned: Spanned
        if (Build.VERSION.SDK_INT >= 24) {
            spanned = Html.fromHtml(torrent.description, Html.FROM_HTML_MODE_COMPACT,
                    Html.ImageGetter { source ->
                        val d = LevelListDrawable()
                        val empty = ContextCompat.getDrawable(activity, R.drawable.abc_btn_check_material)
                        d.addLevel(0, 0, empty)
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight())
                        ImageGetterAsyncTask(context, source, d).execute(torrentDescription)
                        d
                    }, null)
        } else {
            spanned = Html.fromHtml(torrent.description,
                    Html.ImageGetter { source ->
                        val d = LevelListDrawable()
                        val empty = ContextCompat.getDrawable(activity, R.drawable.abc_btn_check_material)
                        d.addLevel(0, 0, empty)
                        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight())
                        ImageGetterAsyncTask(context, source, d).execute(torrentDescription)
                        d
                    }, null)
        }
        torrentDescription.text = spanned
        torrentDownloads.text = torrent.completed.toString()
        torrentWebsite.text = torrent.website
        torrentSeeders.text = "S: ${torrent.seeders}"
        torrentLeechers.text = "L: ${torrent.leechers}"
        torrentLastScraped.text = torrent.last_scrape
        torrentFiles.text = torrent.fileList.length().toString()

        when (torrent.status) {
            2 -> torrentName.backgroundColor = ContextCompat.getColor(context, R.color.colorRemake)
            3 -> torrentName.backgroundColor = ContextCompat.getColor(context, R.color.colorTrusted)
            4 -> torrentName.backgroundColor = ContextCompat.getColor(context, R.color.colorAPlus)
        }
        torrentProgress.max = (torrent.seeders + torrent.leechers)
        torrentProgress.progress = torrent.seeders

        torrentDetails.visibility = View.VISIBLE

        downloadButton.setOnClickListener { _ ->
            val url = torrent.download
            if (url != "") {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (isExternalStorageWritable()) {
                        Log.d("download", "URL: " + url)
                        val request = DownloadManager.Request(Uri.parse(url))
                        request.setDescription("Download a torrent file")
                        request.setTitle(torrent.name + " - NyaaPantsu")
                        // in order for this if to run, you must use the android 3.2 to compile your app
                        request.allowScanningByMediaScanner()
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, torrent.name + ".torrent")
                        Log.d("download", "request")
                        // get download service and enqueue file
                        val manager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        manager.enqueue(request)
                    } else {
                        toast(getString(R.string.external_storage_not_available))
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
                }
            } else {
                toast(getString(R.string.torrent_not_available))
            }
        }

        copyButton.setOnClickListener { _ ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(torrent.name, torrent.magnet)
            clipboard.primaryClip = clipData
            toast(getString(R.string.magnet_copied))
        }

        streamButton.setOnClickListener { _ ->
            val magnet = torrent.magnet
            if (magnet != "") {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (isExternalStorageWritable()) {
                        Log.d("stream", "Magnet: " + magnet)
                        if (!TorrentStreamHelper.instance.isStreaming())  {
                            TorrentStreamHelper.instance.start(magnet)
                            TorrentStreamHelper.torrent = torrent
                            addTorrentToRecentPlaylist(torrent)
                        }
                        // show current stream status
                        TorrentStreamHelper.instance.setListener(this)
                        displayProgress()
                    } else {
                        toast(getString(R.string.external_storage_not_available))
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
                }
            } else {
                toast(getString(R.string.torrent_not_available))
            }

        }

        showMoreToggle.setOnClickListener { _ ->
            if (showDet) {
                moreDetails.visibility = View.GONE
                showDet = false
            } else {
                moreDetails.visibility = View.VISIBLE
                showDet = true
            }
        }
    }

    fun displayProgress() {
        progressdialog!!.setTitle(TorrentStreamHelper.torrent!!.name)
        progressdialog!!.setMessage(getString(R.string.preparing))
        progressdialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressdialog!!.isIndeterminate = false
        progressdialog!!.setCanceledOnTouchOutside(true)
        progressdialog!!.setCancelable(true)
        progressdialog!!.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.ok), {
            _, _ ->
            progressdialog!!.dismiss()
        })
        progressdialog!!.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), {
            _, _ ->
            //FIXME cancel need already starting
            if (TorrentStreamHelper.instance.isStreaming()) TorrentStreamHelper.instance.stop()
            progressdialog!!.dismiss()
        })
        progressdialog!!.progress = 0
        progressdialog!!.max = 100
        progressdialog!!.show()
    }

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            activity.finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStreamPrepared(t: com.github.se_bastiaan.torrentstream.Torrent) {
        Log.d(javaClass.simpleName, "OnStreamPrepared")
        t.startDownload()
        if (!isAdded) return
        progressdialog!!.setMessage(getString(R.string.downloading))
    }

    override fun onStreamStarted(t: com.github.se_bastiaan.torrentstream.Torrent?) {
        Log.d(javaClass.simpleName, "onStreamStarted")
    }

    override fun onStreamProgress(t: com.github.se_bastiaan.torrentstream.Torrent?, s: StreamStatus?) {
        Log.d(javaClass.simpleName, "Progress: " + s?.progress)
        if(isAdded && s?.progress!! <= 100 && progressdialog!!.progress < 100 && progressdialog!!.progress != s.progress.toInt()) {
            progressdialog!!.progress = s.progress.toInt()
        }
    }

    override fun onStreamReady(t: com.github.se_bastiaan.torrentstream.Torrent) {
        Log.d(javaClass.simpleName, "onStreamReady: " + t.videoFile)
        if (!isAdded) return
        progressdialog!!.progress = 100
        progressdialog!!.dismiss()
        TorrentStreamHelper.instance.stop()

        //FIXME check file type
        val uri = Uri.parse(t.videoFile.toString())
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setDataAndType(uri, "video/*")
        startActivity(intent)
    }

    override fun onStreamStopped() {
        TorrentStreamHelper.instance.setListener(null)
        Log.d(javaClass.simpleName, "onStreamStopped")
    }

    override fun onStreamError(t: com.github.se_bastiaan.torrentstream.Torrent?, e: Exception?) {
        Log.e(javaClass.simpleName, "onStreamError", e)
        toast("Stream error: " + e)
    }
}