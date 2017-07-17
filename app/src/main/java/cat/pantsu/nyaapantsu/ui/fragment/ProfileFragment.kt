package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.ProfileTorrentListAdapter
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.model.Torrent
import org.jetbrains.anko.support.v4.find
import org.json.JSONArray
import java.util.*


class ProfileFragment : Fragment() {

    var torrents: JSONArray = JSONArray()
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        torrents = getRecentPlaylistAsArray()
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //profiletorrentList[0].id= 0
        //profiletorrentList[0].name= "torrent name"
        val length = (torrents.length() - 1)
        val torrentList = (length downTo 0).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        recyclerView = find<RecyclerView>(R.id.profiletorrentlist)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ProfileTorrentListAdapter(context = activity, profiletorrentList = torrentList)
    }
}
