package cat.pantsu.nyaapantsu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * Created by xdk78 on 2017-07-10.
 */
class TorrentListAdapter(var context: Context, torrentList: LinkedList<Torrent>) : RecyclerView.Adapter<TorrentListAdapter.TorrentListViewHolder>() {
    private var torrentList = LinkedList<Torrent>()

    init {
        this.torrentList = torrentList
        this.context = context

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TorrentListViewHolder?, position: Int) {
        val item = torrentList[position]

        holder?.label?.text = item.name
        holder?.uploader?.text = item.username
        holder?.stats?.text = "S: " + item.seeders + " L: " + item.leechers

        holder?.itemView?.setOnClickListener { i ->
             context.startActivity<TorrentActivity>("position" to i, "type" to "search")
        }//FIXME: this must be fixed

        when (item.status) {
            2 -> holder?.itemView?.backgroundColor = ContextCompat.getColor(context, R.color.colorRemake)
            3 -> holder?.itemView?.backgroundColor = ContextCompat.getColor(context, R.color.colorTrusted)
            4 -> holder?.itemView?.backgroundColor = ContextCompat.getColor(context, R.color.colorAPlus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TorrentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return TorrentListViewHolder(layoutInflater.inflate(R.layout.list_row, parent, false))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return torrentList.size
    }

    class TorrentListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val label: TextView = view.find<TextView>(R.id.label)
        val stats: TextView = view.find<TextView>(R.id.stats)
        val uploader: TextView = view.find<TextView>(R.id.uploader)
    }
}
