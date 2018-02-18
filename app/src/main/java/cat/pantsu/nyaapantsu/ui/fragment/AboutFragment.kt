package cat.pantsu.nyaapantsu.ui.fragment

import android.app.Fragment
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cat.pantsu.nyaapantsu.BuildConfig
import cat.pantsu.nyaapantsu.R
import kotlinx.android.synthetic.main.app_bar_home.*
import kotlinx.android.synthetic.main.fragment_about.*


class AboutFragment : Fragment() {

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        activity.title = getString(R.string.title_activity_about)
        activity.fab.visibility = View.GONE
        activity.buttonClose.visibility = View.GONE

        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appversion.text = BuildConfig.VERSION_NAME
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        fun newInstance(): AboutFragment {
            return AboutFragment()
        }
    }
}
