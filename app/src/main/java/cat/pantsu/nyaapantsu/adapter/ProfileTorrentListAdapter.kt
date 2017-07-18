package cat.pantsu.nyaapantsu.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Torrent
import cat.pantsu.nyaapantsu.ui.activity.TorrentActivity
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import java.util.*

/**
 * Created by xdk78 on 2017-07-15.
 */
class ProfileTorrentListAdapter(var activity: Activity, profiletorrentList: LinkedList<Torrent>) : RecyclerView.Adapter<ProfileTorrentListAdapter.ProfileTorrentListViewHolder>() {
    private var profiletorrentList = LinkedList<Torrent>()

    init {
        this.profiletorrentList = profiletorrentList
        this.activity = activity

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProfileTorrentListViewHolder?, position: Int) {
        val item = profiletorrentList[position]

        holder?.name?.text = item.name
        holder?.cardview2?.setOnClickListener { _ ->
            activity.startActivity<TorrentActivity>("position" to position, "type" to "search")
        }

        //when (item.status) {
        //    2 -> holder?.cardview2?.cardBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.resources.getColorStateList(R.color.colorRemake, null) else context.resources.getColorStateList(R.color.colorRemake)
        //    3 -> holder?.cardview2?.cardBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.resources.getColorStateList(R.color.colorTrusted, null) else context.resources.getColorStateList(R.color.colorTrusted)
        //    4 -> holder?.cardview2?.cardBackgroundColor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) context.resources.getColorStateList(R.color.colorAPlus, null) else context.resources.getColorStateList(R.color.colorAPlus)
        //}
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProfileTorrentListViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return ProfileTorrentListViewHolder(layoutInflater.inflate(R.layout.profile_torrent_item, parent, false))

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return profiletorrentList.size
    }

    class ProfileTorrentListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.find<TextView>(R.id.name)
        val cardview2: CardView = view.find<CardView>(R.id.cardview2)
    }
}
