package cat.pantsu.nyaapantsu.ui.fragment

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
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


class TorrentListFragment : Fragment() {
    var timeUpdateInterval: Int? = null
    private var query: Query? = null
    private var myHandler = Handler()
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: TorrentListAdapter
    private var mListener: OnFragmentInteractionListener? = null

    companion object {
        var mList: LinkedList<Torrent> = LinkedList()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            query = arguments.getParcelable("query")
        }
        timeUpdateInterval = 15
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_torrent_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        mRecyclerView = find<RecyclerView>(R.id.torrentlist)
        mAdapter = TorrentListAdapter(activity, mList)
        mRecyclerView.adapter = mAdapter

        swiperefresh.setColorSchemeColors(*resources.getIntArray(R.array.swipe_refresh_color))
        swiperefresh.setOnRefreshListener {
            this.getData()
        }

        this.getData()
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

    fun getData() {
        myHandler.removeCallbacksAndMessages(null)
        QueryHelper.instance.query = query
        QueryHelper.instance.search(object : QueryHelper.Callback {
            override fun failure() {
                swiperefresh.isRefreshing = false
            }

            override fun success(torrentList: LinkedList<Torrent>) {
                swiperefresh.isRefreshing = false
                mList.clear()
                mList.addAll(torrentList)
                mAdapter.notifyDataSetChanged()
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
}