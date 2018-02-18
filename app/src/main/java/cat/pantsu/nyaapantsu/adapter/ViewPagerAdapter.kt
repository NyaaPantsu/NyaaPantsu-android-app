package cat.pantsu.nyaapantsu.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import cat.pantsu.nyaapantsu.model.TorrentOld
import cat.pantsu.nyaapantsu.ui.fragment.TorrentViewFragment
import java.util.*

/**
 * Created by ltype on 2017/7/9.
 */
class ViewPagerAdapter(fm: FragmentManager?, list: LinkedList<TorrentOld>) : FragmentStatePagerAdapter(fm) {
    internal var fragments: LinkedList<TorrentOld> = LinkedList()

    init {
        fragments = list
    }

    override fun getItem(position: Int): Fragment {
        val torrent = fragments[position]
        return TorrentViewFragment.newInstance(torrent.id)
    }

    override fun getCount(): Int {
        return fragments.size
    }

}