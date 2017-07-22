package cat.pantsu.nyaapantsu.ui.activity

import android.app.Fragment
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.User
import cat.pantsu.nyaapantsu.model.Utils
import cat.pantsu.nyaapantsu.ui.fragment.*
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast


class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, TorrentListFragment.OnFragmentInteractionListener, UploadFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            val uploadFragment = UploadFragment.newInstance()
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, uploadFragment as Fragment)
                    .addToBackStack(null)
                    .commit()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        checkUser()
        val header = nav_view.getHeaderView(0)
        val memberButton = header.find<Button>(R.id.memberButton)
        val avatarUser = header.find<ImageView>(R.id.avatarUser)
        val usernameBadge = header.find<TextView>(R.id.usernameBadge)
        if (User.token !== "") {
            memberButton.text = getString(R.string.log_out)
            Glide.with(this).load("https://www.gravatar.com/avatar/"+ User.md5 +"?s=130").into(avatarUser)
            usernameBadge.text = User.name
            nav_view.menu.findItem(R.id.nav_user_profile).isVisible = true
        }
        memberButton.setOnClickListener { _ ->
            if (User.token == "") {
                startActivity<LoginActivity>()
            } else {
                resetUser()
            }
        }
        if (savedInstanceState == null) {
            val torrentListFragment = TorrentListFragment.newInstance(Query())
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment, torrentListFragment as Fragment)
                    .commit()
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    var count = 0
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }

        else if (Utils.doubleBackToExit) {
            count++
            if(count==1) {
                toast(getString(R.string.doubleBackToExit))

            }
            else {
                super.onBackPressed()
            }

        }
        else if (!Utils.doubleBackToExit){
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_torrents -> {
            val torrentListFragment = TorrentListFragment.newInstance(Query())
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, torrentListFragment as Fragment)
                        .commit()
            }
            R.id.nav_upload -> {
                val uploadFragment = UploadFragment.newInstance()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, uploadFragment as Fragment)
                        .commit()
            }
            R.id.nav_recent -> {
                val recentFragment = RecentFragment.newInstance()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, recentFragment as Fragment)
                        .commit()
            }
            R.id.nav_search -> {
                val searchFragment = SearchFragment()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, searchFragment as Fragment)
                        .commit()
            }
            R.id.nav_user_profile -> {
                if (User.token == "") {
                    startActivity<LoginActivity>()
                } else {
                    startActivity<ProfileActivity>()
                }

            }
            R.id.nav_settings -> {
                startActivity<SettingsActivity>()
            }
            R.id.nav_about -> {
                val aboutFragment = AboutFragment.newInstance()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, aboutFragment as Fragment)
                        .commit()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {
        //you can leave it empty
    }

    fun resetUser() {
        val header = nav_view.getHeaderView(0)
        val memberButton = header.find<Button>(R.id.memberButton)
        val avatarUser = header.find<ImageView>(R.id.avatarUser)
        val usernameBadge = header.find<TextView>(R.id.usernameBadge)
        User.id = 0
        User.status = 0
        User.token = ""
        User.md5 = ""
        User.name = ""
        usernameBadge.text = getString(R.string.renchon)
        avatarUser.setImageResource(android.R.color.transparent)
        avatarUser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.anon))
        memberButton.text = getString(R.string.action_sign_in)
        nav_view.menu.findItem(R.id.nav_user_profile).isVisible = false
    }

    fun checkUser() {
        if (User.token != "") {
            ("/token/check?username=" + User.name).httpGet().header("Authorization" to User.token).responseJson { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        Log.d("Network", "Big Fail :/")
                        Log.d("Network", response.toString())
                        Log.d("Network", request.toString())
                        toast(R.string.network_error)
                    }
                    is Result.Success -> {
                        Log.d("Network", result.toString())
                        Log.d("Network", request.toString())
                        Log.d("Network", response.toString())

                        val json = result.getAs<Json>()
                        if (json !== null) {
                            if (!json.obj().getBoolean("ok")) {
                                resetUser()
                                toast(R.string.token_expired)
                            }
                        }
                    }
                }
            }
        }
    }
}
