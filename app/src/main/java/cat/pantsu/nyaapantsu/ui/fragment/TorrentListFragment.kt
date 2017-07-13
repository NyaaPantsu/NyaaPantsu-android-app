package cat.pantsu.nyaapantsu.ui.fragment

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.helper.QueryHelper
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.Torrent
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_torrent_list.*
import org.jetbrains.anko.find
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TorrentListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TorrentListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TorrentListFragment : Fragment() {

    private var query: Query? = null
    private var myHandler = Handler()
    var timeUpdateInterval: Int? = null
    lateinit var recyclerView: RecyclerView

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            query = arguments.getParcelable("query")
        }
        timeUpdateInterval = PreferenceManager.getDefaultSharedPreferences(activity).getString("sync_frequency", "15").toInt()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.VISIBLE

        if (query?.isQueryable() == true) {
            if (query?.q != "") {
                activity.title = getString(R.string.title_activity_results)+" \'" + query?.q + "\' - NyaaPantsu"
            } else {
                activity.title = getString(R.string.title_activity_search)
            }
            closeButton.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_clear_search))

            closeButton.setOnClickListener { _ ->
                resetTorrents()
                closeButton.visibility = View.GONE
            }
            closeButton.visibility = View.VISIBLE
        } else {
            activity.title = getString(R.string.title_activity_home)
            closeButton.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.ic_action_reload))
            closeButton.setOnClickListener{ _ ->
                this.getData()
            }
            closeButton.visibility = View.VISIBLE
        }

        this.getData()
        // Inflate the layout for this fragment

        return inflater!!.inflate(R.layout.fragment_torrent_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //torrentlist.setOnItemClickListener { _, _, i, _ ->
        //    startActivity<TorrentActivity>("position" to i, "type" to "search")
        //}

        swiperefresh.setColorSchemeColors(*resources.getIntArray(R.array.swipe_refresh_color))
        swiperefresh.setOnRefreshListener {
            this.getData()
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onPause() {
        myHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param query Query.
         * *
         * @return A new instance of fragment TorrentListFragment.
         */
        fun newInstance(query: Query): TorrentListFragment {
            val fragment = TorrentListFragment()
            val args = Bundle()
            args.putParcelable("query", query)
            fragment.arguments = args
            return fragment
        }
    }
    fun getData() {
        myHandler.removeCallbacksAndMessages(null)
        QueryHelper.instance.query = query
        QueryHelper.instance.search(object : QueryHelper.Callback {
            override fun failure() {
                swiperefresh.isRefreshing = false
            }

            override fun success(torrentList: LinkedList<Torrent>) {
                swiperefresh.isRefreshing = false
                //torrentlist.adapter = RTorrentListAdapter(activity, torrentList = torrentList)
                recyclerView = find<RecyclerView>(R.id.torrentlist)
                recyclerView.layoutManager = LinearLayoutManager(activity)
                recyclerView.adapter = TorrentListAdapter(activity, torrentList = torrentList)
            }
        })
        myHandler.postDelayed({ getData() }, (timeUpdateInterval!!.toLong()*60*1000))
    }

    fun resetTorrents() {
        myHandler.removeCallbacksAndMessages(null)
        query = Query() // We reset to nothing the query
        activity.title = "Torrents - NyaaPantsu"
        getData()
    }



}// Required empty public constructor
