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
import cat.pantsu.nyaapantsu.helper.ProfileHelper
import cat.pantsu.nyaapantsu.model.ProfileQuery
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.support.v4.find
import org.jetbrains.anko.support.v4.toast
import java.util.*


class ProfileFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    private var query: ProfileQuery? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        activity.title = getString(R.string.user_profile) + " - " + User.name


        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_profile_name.text = User.name

        val avatarUser = find<ImageView>(R.id.user_profile_avatar)
        Glide.with(this).load("https://www.gravatar.com/avatar/" + User.md5 + "?s=130").into(avatarUser)
        getData()
    }

    fun getData() {
        ProfileHelper.instance.query = query
        ProfileHelper.instance.search(object : ProfileHelper.Callback {
            override fun failure() {
                toast(R.string.network_error)
            }

            override fun success(torrentList: LinkedList<Torrent>) {
                recyclerView = find<RecyclerView>(R.id.profiletorrentlist)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = ProfileTorrentListAdapter(activity = activity, profiletorrentList = torrentList)
            }
        })
    }


}
