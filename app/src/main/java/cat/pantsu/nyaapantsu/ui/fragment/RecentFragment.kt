package cat.pantsu.nyaapantsu.ui.fragment


import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

import cat.pantsu.nyaapantsu.R
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.find


class RecentFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val closeButton = activity.toolbar.find<ImageButton>(R.id.buttonClose)
        closeButton.visibility = View.GONE
        activity.fab.visibility = View.GONE
        activity.title = getString(R.string.recent)
        return inflater!!.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): RecentFragment {
            val fragment = RecentFragment()
            return fragment
        }
    }

}

