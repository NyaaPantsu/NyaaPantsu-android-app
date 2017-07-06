package cat.pantsu.nyaapantsu.ui

import android.Manifest
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Html.FROM_HTML_MODE_COMPACT
import android.util.Log
import android.view.View
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import kotlinx.android.synthetic.main.activity_view.*
import org.jetbrains.anko.backgroundColor
import org.json.JSONObject
import android.app.DownloadManager
import android.content.ClipData
import android.content.ClipboardManager
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import org.jetbrains.anko.toast
import android.content.pm.PackageManager
import android.view.MenuItem
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Torrent


class ViewActivity : AppCompatActivity() {
    var torrent = Torrent(JSONObject())
    var id = 0
    var showDet = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        if (intent.getStringExtra("torrent") !== null) {
            torrent = Torrent(JSONObject(intent.getStringExtra("torrent")))
            id = torrent.id
            genView()
        } else if (intent.getIntExtra("id", 0) > 0) {
            id = intent.getIntExtra("id", 0)
            getData()
        } else {
            toast("No data provided")
            finish()
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun getData() {
        ("/view/"+id).httpGet().responseJson {request, response, result ->
            when (result)  {
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
        torrentName.text = torrent.name
        title = torrent.name + " - "+ getString(R.string.nyaapantsu)
        torrentCategory.text = resources.getStringArray(R.array.cat_array)[torrent.category]
        torrentUser.text = torrent.username
        torrentHash.text = torrent.hash
        torrentDate.text = torrent.date
        torrentSize.text = torrent.size

        if (Build.VERSION.SDK_INT >= 24) {
            torrentDescription.text = Html.fromHtml(torrent.description, FROM_HTML_MODE_COMPACT) // for 24 api and more
        } else {
            torrentDescription.text = Html.fromHtml(torrent.description) // or for older api
        }

        torrentDownloads.text = torrent.completed.toString()
        torrentWebsite.text = torrent.website
        torrentSeeders.text = "S: " + torrent.seeders.toString()
        torrentLeechers.text = "L: " + torrent.leechers.toString()
        torrentLastScraped.text = torrent.last_scrape
        torrentFiles.text = torrent.fileList.length().toString()

        when (torrent.status) {
            2 -> torrentName.backgroundColor = ContextCompat.getColor(this, R.color.colorRemake)
            3 -> torrentName.backgroundColor = ContextCompat.getColor(this, R.color.colorTrusted)
            4 -> torrentName.backgroundColor = ContextCompat.getColor(this, R.color.colorAPlus)
        }
        torrentProgress.max = (torrent.seeders + torrent.leechers)
        torrentProgress.progress = torrent.seeders

        torrentDetails.visibility = View.VISIBLE

        downloadButton.setOnClickListener { _ ->
            val url = torrent.download
            if (url != "") {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (isExternalStorageWritable()) {
                        Log.d("download", "URL: " +url)
                        val request = DownloadManager.Request(Uri.parse(url))
                        request.setDescription("Download a torrent file")
                        request.setTitle( torrent.name+" - NyaaPantsu")
                        // in order for this if to run, you must use the android 3.2 to compile your app
                        request.allowScanningByMediaScanner()
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, torrent.name + ".torrent")
                        Log.d("download", "request")
                        // get download service and enqueue file
                        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        manager.enqueue(request)
                    } else {
                        toast(getString(R.string.external_storage_not_available))
                    }
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)
                }
            } else {
                toast(getString(R.string.torrent_not_available))
            }
        }

        copyButton.setOnClickListener { _ ->
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var clipData = ClipData.newPlainText(torrent.name, torrent.magnet)
            clipboard.primaryClip = clipData
            toast(getString(R.string.magnet_copied))
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
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }
}
