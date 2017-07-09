package cat.pantsu.nyaapantsu.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.app.Fragment
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.helper.QueryHelper
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_torrent_list.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
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
    private var searchParams: String? = null
    private var myHandler = Handler()
    var timeUpdateInterval:Int? = null

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

        searchParams = ""
        if (query?.isQueryable() == true) {
            if (query?.q != "") {
                activity.title = getString(R.string.title_activity_results)+" \'" + query?.q + "\' - NyaaPantsu"
            } else {
                activity.title = getString(R.string.title_activity_search)
            }
            searchParams = query.toString()
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

        torrentlist.setOnItemClickListener { _, _, i, _ ->
            startActivity<TorrentActivity>("position" to i, "type" to "search")
        }

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
        QueryHelper.instance.search(object : QueryHelper.CallBack {
            override fun failure() {
                swiperefresh.isRefreshing = false
            }

            override fun success(torrentList: LinkedList<Torrent>) {
                swiperefresh.isRefreshing = false
                torrentlist.adapter = ListAdapter(activity, torrentList = torrentList)
            }
        })
        myHandler.postDelayed({ getData() }, (timeUpdateInterval!!.toLong()*60*1000))
    }

    fun resetTorrents() {
        searchParams = ""
        myHandler.removeCallbacksAndMessages(null)
        activity.title = "Torrents - NyaaPantsu"
        getData()
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

}// Required empty public constructor
