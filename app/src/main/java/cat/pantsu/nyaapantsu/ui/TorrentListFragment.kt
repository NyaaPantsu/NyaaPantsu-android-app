package cat.pantsu.nyaapantsu.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.app.Fragment
import android.preference.PreferenceManager
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.Torrent
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_torrent_list.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.json.JSONArray
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

    private var mQuery: Query? = null
    private var searchParams: String? = null
    var torrents: JSONArray = JSONArray()
    private var myHandler = Handler()
    var timeUpdateInterval:Int? = null
    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mQuery = arguments.getParcelable("query")
        }
        timeUpdateInterval = PreferenceManager.getDefaultSharedPreferences(activity).getString("sync_frequency", "15").toInt()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.VISIBLE

        searchParams = ""
        if (mQuery?.isQueryable() == true) {
            if (mQuery?.q != "") {
                activity.title = getString(R.string.title_activity_results)+" \'" + mQuery?.q + "\' - NyaaPantsu"
            } else {
                activity.title = getString(R.string.title_activity_search)
            }
            searchParams = mQuery.toString()
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
            startActivity<ViewActivity>("torrent" to torrents.getString(i))
        }
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener?
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
        ("/search"+searchParams).httpGet().responseJson {request, response, result ->
            when (result)  {
                is Result.Failure -> {
                    Log.d("Network", "Big Fail :/")
                    Log.d("Network", response.toString())
                    Log.d("Network", request.toString())
                }
                is Result.Success -> {
                    Log.d("Network", result.toString())
                    Log.d("Network", request.toString())
                    Log.d("Network", response.toString())

                    val json = result.getAs<Json>()
                    if (json !== null)
                        this.torrents = json.array()
                    parseTorrents()
                }
            }
            myHandler.postDelayed(Runnable { getData() }, (timeUpdateInterval!!.toLong()*60*1000))
        }
    }

    fun parseTorrents() {
        var torrentList = LinkedList<Torrent>()
        val length = (torrents.length()-1)
        for (i in 0..length) {
            torrentList.add(Torrent(torrents.getJSONObject(i)))
        }
        torrentlist.adapter = ListAdapter(activity, torrentList)
    }

    fun resetTorrents() {
        searchParams = ""
        myHandler.removeCallbacksAndMessages(null)
        activity.title = "Torrents - NyaaPantsu"
        getData()
    }

    private class ListAdapter(context: Context, torrentList: LinkedList<Torrent>) : BaseAdapter() {
        private val mInflator: LayoutInflater = LayoutInflater.from(context)
        private var torrentList = LinkedList<Torrent>()
        private val context = context
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
