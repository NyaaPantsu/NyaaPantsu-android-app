package cat.pantsu.nyaapantsu.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.mvp.model.response.TorrentListResponse
import cat.pantsu.nyaapantsu.ui.adapter.holder.TorrentListViewHolder
import javax.inject.Inject


class TorrentListAdapter @Inject constructor(var context: Context, private var torrentList: TorrentListResponse) : RecyclerView.Adapter<TorrentListViewHolder>() {
    override fun onBindViewHolder(holder: TorrentListViewHolder, position: Int) {
        holder.bindView(torrentList.torrents[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TorrentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TorrentListViewHolder(layoutInflater.inflate(R.layout.torrent_item, parent, false), parent.context)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return torrentList.torrents.size
    }

}
