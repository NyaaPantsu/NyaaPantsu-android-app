package cat.pantsu.nyaapantsu.ui.fragment


import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.ImageButton
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.model.RecentlyPlayed
import cat.pantsu.nyaapantsu.model.Torrent
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.find
import org.json.JSONArray
import java.util.*


class RecentFragment : Fragment() {
    var torrents: JSONArray = JSONArray()
    lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance(): RecentFragment {
            val fragment = RecentFragment()
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.GONE
        activity.title = getString(R.string.recent)
        torrents = getRecentPlaylistAsArray()
        return inflater!!.inflate(R.layout.fragment_recent, container, false)
    }

    fun parseTorrents() {
        val length = (torrents.length() - 1)
        val torrentList = (length downTo 0).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        //torrentlist.adapter = RTorrentListAdapter(activity, torrentList = torrentList)
        recyclerView = find<RecyclerView>(R.id.torrentlist)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = TorrentListAdapter(activity, torrentList = torrentList)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (torrents.length() > 0) {
            parseTorrents()
        }
        //torrentlist.setOnItemClickListener { _, _, i, _ ->
        //    startActivity<TorrentActivity>("position" to i, "type" to "recent")
        //}
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recent_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.clear_recent -> {
                RecentlyPlayed.torrents = ""
                val recentFragment = RecentFragment.newInstance() // refresh placeholder if we switch to recycler view I will use better solution
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, recentFragment as Fragment)
                        .commit()
            }
        }
        return false
    }
}

