package cat.pantsu.nyaapantsu.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView

import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.model.RecentlyPlayed
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_recent.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.json.JSONArray
import java.util.*


class RecentFragment : Fragment() {
    var torrents: JSONArray = JSONArray()

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
        val length = (torrents.length()-1)
        val torrentList = (0..length).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        torrentlist.adapter = ListAdapter(activity, torrentList = torrentList)
    }
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (torrents.length() > 0) {
            parseTorrents()
        }
        torrentlist.setOnItemClickListener { _, _, i, _ ->
            startActivity<TorrentActivity>("position" to i, "type" to "recent")
        }
    }
    companion object {
        fun newInstance(): RecentFragment {
            val fragment = RecentFragment()
            return fragment
        }
    }
    private class ListAdapter(private val context: Context, torrentList: LinkedList<Torrent>) : BaseAdapter() {
        private val mInflator: LayoutInflater = LayoutInflater.from(context)
        private var torrentList = LinkedList<Torrent>()

        init {
            this.torrentList = torrentList
        }

        override fun getCount(): Int {
            return torrentList.size
        }

        override fun getItem(position: Int): Any {
            return torrentList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        @SuppressLint("SetTextI18n")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val view: View?
            val vh: ListRowHolder
            if (convertView == null) {
                view = this.mInflator.inflate(R.layout.list_row, parent, false)
                vh = ListRowHolder(view)
                view.tag = vh
            } else {
                view = convertView
                vh = view.tag as ListRowHolder
            }
            vh.label.text = torrentList[position].name
            vh.uploader.text = torrentList[position].username
            vh.stats.text = "S: "+torrentList[position].seeders+" L: "+torrentList[position].leechers
            view?.backgroundColor = ContextCompat.getColor(context, android.R.color.transparent)
            when (torrentList[position].status) {
                2 -> view?.backgroundColor = ContextCompat.getColor(context, R.color.colorRemake)
                3 -> view?.backgroundColor = ContextCompat.getColor(context, R.color.colorTrusted)
                4 -> view?.backgroundColor = ContextCompat.getColor(context, R.color.colorAPlus)
            }
            return view
        }
    }
    private class ListRowHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.label) as TextView
        val stats: TextView = row?.findViewById(R.id.stats) as TextView
        val uploader: TextView = row?.findViewById(R.id.uploader) as TextView

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

