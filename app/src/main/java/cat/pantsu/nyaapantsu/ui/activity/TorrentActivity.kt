package cat.pantsu.nyaapantsu.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.ViewPagerAdapter
import cat.pantsu.nyaapantsu.helper.QueryHelper
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.model.Torrent
import com.github.se_bastiaan.torrentstream.Torrent as TorrentLib
import kotlinx.android.synthetic.main.activity_torrent.*
import org.json.JSONArray
import java.util.*

class TorrentActivity : AppCompatActivity() {
    private var list:LinkedList<Torrent> = LinkedList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_torrent)
        val type = intent.getStringExtra("type")
        when (type) {
            "search" -> {
                list = QueryHelper.instance.torrentList
            }
            "recent" -> {
                list = QueryHelper.parseTorrents(getRecentPlaylistAsArray())
            }
            "upload" -> {
                val arr = JSONArray().put(intent.getStringExtra("torrent"))
                list = QueryHelper.parseTorrents(arr)
            }
        }
        val adapter = ViewPagerAdapter(supportFragmentManager, list)
        val current = intent.getIntExtra("position", 0)
        view_pager.adapter = adapter
        view_pager.setCurrentItem(current, false)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                title = list[position].name + " - " + getString(R.string.nyaapantsu)
            }
        })

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = list[current].name + " - " + getString(R.string.nyaapantsu)
    }
}

