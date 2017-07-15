package cat.pantsu.nyaapantsu.ui.fragment


import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.ImageButton
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.helper.setRecentPlaylist
import cat.pantsu.nyaapantsu.model.RecentlyPlayed
import cat.pantsu.nyaapantsu.model.Torrent
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.find
import org.json.JSONArray
import java.util.*


class RecentFragment : Fragment() {
    var torrents: JSONArray = JSONArray()
    var torrentList: LinkedList<Torrent> = LinkedList()
    lateinit var recyclerView: RecyclerView
    lateinit var touchHelper: ItemTouchHelper

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

    fun parseTorrents(): LinkedList<Torrent> {
        val length = (torrents.length() - 1)
        return (0 .. length).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (torrents.length() == 0) return
        torrentList.clear()
        torrentList.addAll(parseTorrents())
        recyclerView = find<RecyclerView>(R.id.torrentlist)
        recyclerView.adapter = TorrentListAdapter(activity, torrentList)
        val cb = helperCallback(0, ItemTouchHelper.START or ItemTouchHelper.END)
        touchHelper = ItemTouchHelper(cb)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onStop() {
        super.onStop()
        if(torrents.length() != getRecentPlaylistAsArray().length()) setRecentPlaylist(torrents)
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

    fun helperCallback(dragDirs: Int, swipeDirs: Int): ItemTouchHelper.Callback {
        return object: ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {
            override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                torrents.remove(position)
                torrentList.removeAt(position)
                recyclerView.adapter.notifyItemRemoved(position)
                Snackbar.make(view, getString(R.string.deleted), Snackbar.LENGTH_SHORT).show()
            }

        }
    }
}

