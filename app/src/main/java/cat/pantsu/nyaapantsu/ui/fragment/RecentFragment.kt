package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.base.BaseFragment


class RecentFragment : BaseFragment() {

    companion object {
        fun newInstance(): RecentFragment {
            return RecentFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity!!.title = getString(R.string.recent)

        return inflater.inflate(R.layout.fragment_recent, container, false)
    }


}
