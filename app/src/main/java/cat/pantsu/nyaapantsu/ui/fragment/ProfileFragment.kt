package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.ProfileTorrentListAdapter
import cat.pantsu.nyaapantsu.helper.getRecentPlaylistAsArray
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
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
        activity.title = getString(R.string.user_profile) + " - " + User.name

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_profile_name.text = User.name

        val avatarUser = find<ImageView>(R.id.user_profile_avatar)
        Glide.with(this).load("https://www.gravatar.com/avatar/"+ User.md5 +"?s=130").into(avatarUser)

        val length = (torrents.length() - 1)
        val torrentList = (length downTo 0).mapTo(LinkedList<Torrent>()) { Torrent(torrents.getJSONObject(it)) }
        recyclerView = find<RecyclerView>(R.id.profiletorrentlist)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ProfileTorrentListAdapter(activity = activity, profiletorrentList = torrentList)
        //TODO: load data for user if have own torrents
    }
}
