package cat.pantsu.nyaapantsu.ui.activity

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.base.BaseActivity
import cat.pantsu.nyaapantsu.ui.fragment.AboutFragment
import cat.pantsu.nyaapantsu.ui.fragment.RecentFragment
import cat.pantsu.nyaapantsu.ui.fragment.TorrentListFragment
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.startActivity


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val toggle by lazy {
        ActionBarDrawerToggle(this,
                drawer_layout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, TorrentListFragment.newInstance()).commit()

        drawer_layout.addDrawerListener(toggle)
        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
        super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_torrents -> {
                val fragment = TorrentListFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()
            }
            R.id.nav_search -> {
                startActivity<SearchActivity>()
            }
            R.id.nav_recent -> {
                val fragment = RecentFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()
            }
            R.id.nav_about -> {
                val fragment = AboutFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment, fragment).commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
