package cat.pantsu.nyaapantsu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Torrent
import org.jetbrains.anko.backgroundColor
import java.util.*

/**
 * Created by xdk78 on 2017-07-10.
 */
class TorrentListAdapter(var context: Context, torrentList: LinkedList<Torrent>) : BaseAdapter() {
    var mInflator: LayoutInflater = LayoutInflater.from(context)
    var torrentList = LinkedList<Torrent>()

    init {
        this.torrentList = torrentList
        this.context=context
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
        val vh: TorrentListRowHolder
        if (convertView == null) {
            view = this.mInflator.inflate(R.layout.list_row, parent, false)
            vh = TorrentListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as TorrentListRowHolder
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
private class TorrentListRowHolder(row: View?) {
    val label: TextView = row?.findViewById(R.id.label) as TextView
    val stats: TextView = row?.findViewById(R.id.stats) as TextView
    val uploader: TextView = row?.findViewById(R.id.uploader) as TextView

}