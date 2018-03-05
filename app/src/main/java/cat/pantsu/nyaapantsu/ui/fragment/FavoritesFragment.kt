package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.view.*
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.base.BaseFragment


class FavoritesFragment : BaseFragment() {

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity!!.title = getString(R.string.favorites)
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_favorites, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_favorites, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
