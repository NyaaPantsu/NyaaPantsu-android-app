package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.adapter.TorrentListAdapter
import cat.pantsu.nyaapantsu.base.BaseFragment
import cat.pantsu.nyaapantsu.mvp.model.TorrentListResponse
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.mvp.presenter.TorrentListPresenter
import cat.pantsu.nyaapantsu.mvp.view.TorrentListView
import kotlinx.android.synthetic.main.fragment_torrent_list.*
import javax.inject.Inject


class TorrentListFragment : BaseFragment(), TorrentListView {

    lateinit var adapter: TorrentListAdapter

    @Inject
    lateinit var presenter: TorrentListPresenter

    lateinit var recyclerView: RecyclerView

    companion object {
        fun newInstance(): TorrentListFragment {
            return TorrentListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_torrent_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity!!.title = getString(R.string.title_activity_home)
        swiperefresh.setOnRefreshListener {
            presenter.subscribe(this)
            swiperefresh.isRefreshing = true
            presenter.loadData()
        }
        presenter.subscribe(this)
        presenter.loadData()
    }

    override fun onItemsLoaded(items: TorrentListResponse<TorrentModel>) {
        recyclerView = torrentlist
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = TorrentListAdapter(context!!, items)
        recyclerView.adapter = adapter

        swiperefresh.isRefreshing = false
    }

    override fun onError(e: Throwable?) {
        Log.e("torrentlist", e.toString())
        Snackbar.make(view!!, e.toString(), Snackbar.LENGTH_LONG).show()
        swiperefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.unsubscribe()
    }

}