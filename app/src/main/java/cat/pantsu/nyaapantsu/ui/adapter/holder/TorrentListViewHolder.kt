package cat.pantsu.nyaapantsu.ui.adapter.holder

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.mvp.model.TorrentModel
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import cat.pantsu.nyaapantsu.util.download
import cat.pantsu.nyaapantsu.util.formatDate
import kotlinx.android.synthetic.main.torrent_item.view.*


class TorrentListViewHolder(view: View, var context: Context) : RecyclerView.ViewHolder(view) {
    @SuppressLint("SetTextI18n")
    fun bindView(item: TorrentModel) {
        itemView.name.text = item.name
        itemView.uploader.text = item.uploaderName
        itemView.stats.text = "S: " + item.seeders + " L: " + item.leechers
        itemView.date.text = item.date.formatDate()

        itemView.download.setOnClickListener {
            when {
                !TextUtils.isEmpty(item.torrent) -> download(context as Activity, itemView, item.torrent, item.name)
                else -> Toast.makeText(context, R.string.torrent_not_available, Toast.LENGTH_SHORT).show()
            }
        }

        itemView.copy.setOnClickListener {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(item.name, item.magnet)
            clipboard.primaryClip = clipData
            Toast.makeText(context, R.string.magnet_copied, Toast.LENGTH_SHORT).show()
        }

        itemView.cardview.setOnClickListener {
            context.startActivity(TorrentActivity.createIntent(context, item.id, item.name))
        }

        when (item.status) {
            2 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorRemake, null))
                } else {
                    itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorRemake))
                }

            }
            3 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorTrusted, null))
                } else {
                    itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorTrusted))
                }

            }
            4 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorAPlus, null))
                } else {
                    itemView.statusLine.setBackgroundColor(context.resources.getColor(R.color.colorAPlus))
                }
            }
        }
    }
}