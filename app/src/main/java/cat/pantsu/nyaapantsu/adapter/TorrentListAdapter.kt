package cat.pantsu.nyaapantsu.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.util.Utils
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import java.util.*

/**
 * Created by xdk78 on 2017-07-10.
 */
class TorrentListAdapter(var activity: Activity, torrentList: LinkedList<Torrent>) : RecyclerView.Adapter<TorrentListAdapter.TorrentListViewHolder>() {
    private var torrentList = LinkedList<Torrent>()

    init {
        this.torrentList = torrentList
        this.activity = activity

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TorrentListViewHolder?, position: Int) {
        if (holder == null) return
        val item = torrentList[position]

        holder.name.text = item.name
        holder.uploader.text = item.username
        holder.stats.text = "S: " + item.seeders + " L: " + item.leechers
        holder.date.text = item.date

        holder.expand.setOnClickListener { _ ->
            holder.expand.isSelected = !holder.expand.isSelected
            val visibility = if (holder.expand.isSelected) View.VISIBLE else View.GONE
            holder.download.visibility = visibility
            holder.copy.visibility = visibility
        }

        holder.download.setOnClickListener { _ ->
            if (!TextUtils.isEmpty(item.download)) {
                Utils.download(activity, holder.itemView, item.download, item.name)
            } else {
                activity.toast(activity.getString(R.string.torrent_not_available))
            }
        }

        holder.copy.setOnClickListener { _ ->
            val clipboard = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(item.name, item.magnet)
            clipboard.primaryClip = clipData
            activity.toast(activity.getString(R.string.magnet_copied))
        }

        holder.cardview.setOnClickListener { _ ->
            activity.startActivity<TorrentActivity>("position" to position, "type" to "search")
        }

        when (item.status) {
            2 -> holder.cardview.cardBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) activity.resources.getColorStateList(R.color.colorRemake, null) else activity.resources.getColorStateList(R.color.colorRemake)
            3 -> holder.cardview.cardBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) activity.resources.getColorStateList(R.color.colorTrusted, null) else activity.resources.getColorStateList(R.color.colorTrusted)
            4 -> holder.cardview.cardBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) activity.resources.getColorStateList(R.color.colorAPlus, null) else activity.resources.getColorStateList(R.color.colorAPlus)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TorrentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return TorrentListViewHolder(layoutInflater.inflate(R.layout.torrent_item, parent, false))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return torrentList.size
    }

    class TorrentListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.find<TextView>(R.id.name)
        val uploader: TextView = view.find<TextView>(R.id.uploader)
        val stats: TextView = view.find<TextView>(R.id.stats)
        val date: TextView = view.find<TextView>(R.id.date)
        val expand: ImageView = view.find<ImageView>(R.id.expand)
        val download: Button = view.find<Button>(R.id.download)
        val copy: Button = view.find<Button>(R.id.copy)
        val cardview: CardView = view.find<CardView>(R.id.cardview)
    }
}
