package cat.pantsu.nyaapantsu.ui.activity


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.base.BaseActivity
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.mvp.presenter.TorrentPresenter
import cat.pantsu.nyaapantsu.mvp.view.TorrentView
import cat.pantsu.nyaapantsu.util.download
import cat.pantsu.nyaapantsu.util.formatDate
import kotlinx.android.synthetic.main.content_torrent_view.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.contentView
import javax.inject.Inject

class TorrentActivity : BaseActivity(), TorrentView {

    @Inject
    lateinit var presenter: TorrentPresenter

    companion object {
        const val EXTRA_TORRENT_ID = "EXTRA_TORRENT_ID"
        const val EXTRA_TORRENT_TITLE = "EXTRA_TORRENT_TITLE"

        fun createIntent(context: Context, torrentId: Int, torrentTitle: String): Intent {
            val intent = Intent(context, TorrentActivity::class.java)
            intent.putExtra(EXTRA_TORRENT_ID, torrentId)
            intent.putExtra(EXTRA_TORRENT_TITLE, torrentTitle)
            return intent
        }
    }

    private val torrentId by lazy {
        intent.getIntExtra(EXTRA_TORRENT_ID, 0)
    }

    private val torrentTitle by lazy {
        intent.getStringExtra(EXTRA_TORRENT_TITLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = torrentTitle
        presenter.subscribe(this)
        presenter.loadData(torrentId)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    override fun onItemsLoaded(items: TorrentModel) {
        torrentName.text = items.name
        torrentCategory.text = resources.getStringArray(R.array.cat_array)[items.category.toInt()]
        torrentUser.text = items.uploaderName
        torrentHash.text = items.hash
        torrentDate.text = items.date.formatDate()
        torrentSize.text = items.filesize.toString()
        val spanned: Spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(items.description, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(items.description)
        }

        torrentDescription.text = spanned
        torrentDownloads.text = items.completed.toString()
        torrentWebsite.text = items.websiteLink
        torrentSeeders.text = "S: ${items.seeders}"
        torrentLeechers.text = "L: ${items.leechers}"
        torrentLastScraped.text = items.lastScrape
        torrentFiles.text = items.fileList.toString()

//        when (items.status) {
//            2 -> torrentName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRemake))
//            3 -> torrentName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTrusted))
//            4 -> torrentName.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAPlus))
//        }

        torrentProgress.max = (items.seeders + items.leechers)
        torrentProgress.progress = items.seeders

        downloadButton.setOnClickListener {
            if (!TextUtils.isEmpty(items.torrent)) {
                download(this, downloadButton, items.torrent, items.name)
            } else {
                Toast.makeText(this, R.string.torrent_not_available, Toast.LENGTH_SHORT).show()
            }
        }

        copyButton.setOnClickListener {
            val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(items.name, items.magnet)
            clipboard.primaryClip = clipData
            Toast.makeText(this, R.string.magnet_copied, Toast.LENGTH_SHORT).show()
        }

    }

    override fun onError(e: Throwable?) {
        Log.e("torrentview", e.toString())
        Snackbar.make(contentView!!, e.toString(), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unsubscribe()
    }


}