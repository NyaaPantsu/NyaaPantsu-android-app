package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.BuildConfig
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.base.BaseFragment
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : BaseFragment() {

    companion object {
        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity!!.title = getString(R.string.title_activity_about)
        activity!!.fab.visibility = View.GONE
        activity!!.buttonClose.visibility = View.GONE

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appversion.text = BuildConfig.VERSION_NAME
    }


}
