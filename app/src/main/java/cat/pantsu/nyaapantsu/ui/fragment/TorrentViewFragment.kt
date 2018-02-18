package cat.pantsu.nyaapantsu.ui.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LevelListDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.helper.TorrentStreamHelper
import cat.pantsu.nyaapantsu.helper.addTorrentToRecentPlaylist
import cat.pantsu.nyaapantsu.model.TorrentOld
import cat.pantsu.nyaapantsu.util.Utils
import com.facebook.common.executors.CallerThreadExecutor
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequestBuilder
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
    var torrent = TorrentOld(JSONObject())
    var showDet = false
    var progressdialog: ProgressDialog? = null

    companion object {
        fun newInstance(torrent: String): TorrentViewFragment {
            val fragment = TorrentViewFragment()
            val args = Bundle()
            args.putString("torrentOld", torrent)
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
            torrent = TorrentOld(JSONObject(arguments.getString("torrent")))
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
                        torrent = TorrentOld(json.obj())
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
        val spanned: Spanned
        if (Build.VERSION.SDK_INT >= 24) {
            spanned = Html.fromHtml(torrent.description, Html.FROM_HTML_MODE_COMPACT, imageGetter(), null)
        } else {
            spanned = Html.fromHtml(torrent.description, imageGetter(), null)
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
            if (!TextUtils.isEmpty(torrent.download)) {
                Utils.download(activity, downloadButton, torrent.download, torrent.name)
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
                    if (Utils.isExternalStorageWritable()) {
                        Log.d("stream", "Magnet: " + magnet)
                        if (!TorrentStreamHelper.instance.isStreaming())  {
                            TorrentStreamHelper.instance.start(magnet)
                            TorrentStreamHelper.torrentOld = torrent
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

    fun imageGetter(): Html.ImageGetter {
        return Html.ImageGetter { source ->
            val ld = LevelListDrawable()
            val empty = ContextCompat.getDrawable(activity, R.drawable.abc_btn_check_material)
            ld.addLevel(0, 0, empty)
            ld.setBounds(0, 0, empty.intrinsicWidth, empty.intrinsicHeight)
            val imageRequestBuilder: ImageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(source))
            val imagePipeline = Fresco.getImagePipeline()
            val dataSource = imagePipeline.fetchDecodedImage(imageRequestBuilder.build(), this)
            dataSource.subscribe(object: BaseBitmapDataSubscriber() {
                override fun onFailureImpl(ds: DataSource<CloseableReference<CloseableImage>>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onNewResultImpl(bitmap: Bitmap?) {
                    if (bitmap == null) return
                    val d = BitmapDrawable(context.resources, bitmap)
                    val size = Point()
                    (context as Activity).windowManager.defaultDisplay.getSize(size)
                    val multiplier = size.x / bitmap.width
                    ld.addLevel(1, 1, d)
                    ld.setBounds(0, 0, bitmap.width * multiplier, bitmap.height * multiplier)
                    ld.level = 1
                }
            }, CallerThreadExecutor.getInstance())
            return@ImageGetter ld
        }
    }

    fun displayProgress() {
        progressdialog!!.setTitle(TorrentStreamHelper.torrentOld!!.name)
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
        Log.d(javaClass.simpleName, "Progress: ${s?.progress}, ${s?.bufferProgress}")
        if(isAdded && s?.bufferProgress!! <= 100 && progressdialog!!.progress < 100 && progressdialog!!.progress != s.bufferProgress) {
            progressdialog!!.progress = s.bufferProgress
        }
    }

    override fun onStreamReady(t: com.github.se_bastiaan.torrentstream.Torrent) {
        Log.d(javaClass.simpleName, "onStreamReady: " + t.videoFile)
        if (!isAdded) return
        progressdialog!!.progress = 100
        progressdialog!!.dismiss()

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