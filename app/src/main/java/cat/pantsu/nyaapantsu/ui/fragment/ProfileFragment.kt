package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.helper.ProfileHelper
import cat.pantsu.nyaapantsu.helper.QueryHelper
import cat.pantsu.nyaapantsu.model.ProfileQuery
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.util.*


class ProfileFragment : Fragment() {

    private var query: Query? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        activity.title = getString(R.string.user_profile)
        if (arguments != null) {
            query = arguments.getParcelable("query")
            if (User.id.toString() == query!!.userID) {
                activity.title = getString(R.string.user_profile) + " - " + User.name
            }
        }

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user_profile_name.text = User.name

        Glide.with(this).load("https://www.gravatar.com/avatar/" + User.md5 + "?s=130").into(user_profile_avatar)
        getData()
    }

    fun getData() {
        val localQuery = query
        QueryHelper.instance.query = query
        QueryHelper.instance.search(object : QueryHelper.Callback {
            override fun failure() {
                toast(R.string.network_error)
            }

            override fun success(torrentList: LinkedList<Torrent>) {
                profiletorrentlist.layoutManager = LinearLayoutManager(activity)
                profiletorrentlist.adapter = TorrentListAdapter(activity = activity, torrentList = torrentList)
            }
        })
        // TODO: Use ProfileHelper as a helper to get profile data from the api
        /* New API endpoint /profile/?id=USERID */
        var userID = 0
        if (localQuery?.userID != null) {
            userID = localQuery?.userID.toInt()
        }
        if (User.id != userID) {
            val profileQuery = ProfileQuery()
            profileQuery.userID = userID
            ProfileHelper.instance.query = profileQuery
            ProfileHelper.instance.get(object : ProfileHelper.Callback {
                override fun failure() {
                    toast(R.string.network_error)
                }

                override fun success(profile: JSONObject) {
                    user_profile_name.text = profile.optString("username")
                    Glide.with(activity).load("https://www.gravatar.com/avatar/" + profile.optString("md5") + "?s=130").into(user_profile_avatar)
                }
            })
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param query Query.
         * *
         * @return A new instance of fragment ProfileFragment.
         */
        fun newInstance(query: Query): ProfileFragment {
            val fragment = ProfileFragment()
            val args = Bundle()
            args.putParcelable("query", query)
            fragment.arguments = args
            return fragment
        }
    }
}
