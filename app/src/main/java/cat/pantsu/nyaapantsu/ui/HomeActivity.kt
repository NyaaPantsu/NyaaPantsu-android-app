package cat.pantsu.nyaapantsu.ui

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import com.github.kittinunf.fuel.android.core.Json
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.getAs
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_home.*
import android.app.Fragment
import android.widget.*
import org.jetbrains.anko.*
import android.net.Uri
import android.support.v4.content.ContextCompat
import cat.pantsu.nyaapantsu.*
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.User
import com.bumptech.glide.Glide


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, TorrentListFragment.OnFragmentInteractionListener, UploadFragment.OnFragmentInteractionListener, AboutFragment.OnFragmentInteractionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            var uploadFragment = UploadFragment.newInstance()
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
        var header = nav_view.getHeaderView(0)
        var memberButton = header.find<Button>(R.id.memberButton)
        var avatarUser = header.find<ImageView>(R.id.avatarUser)
        var usernameBadge = header.find<TextView>(R.id.usernameBadge)
        if (User.token !== "") {
            memberButton.text = getString(R.string.log_out)
            Glide.with(this).load("https://www.gravatar.com/avatar/"+ User.md5 +"?s=130").into(avatarUser)
            usernameBadge?.text = User.name
        }
        memberButton.setOnClickListener { _ ->
            if (User.token == "") {
                startActivity<LoginActivity>()
            } else {
                resetUser()
            }
        }
        if (savedInstanceState == null) {
            var torrentListFragment = TorrentListFragment.newInstance(Query())
            fragmentManager.beginTransaction()
                    .add(R.id.main_fragment, torrentListFragment as Fragment)
                    .addToBackStack(null)
                    .commit()
        }
        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_torrents -> {
            var torrentListFragment = TorrentListFragment.newInstance(Query())
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, torrentListFragment as Fragment)
                        .addToBackStack(null)
                        .commit()
            }
            R.id.nav_upload -> {
                var uploadFragment = UploadFragment.newInstance()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, uploadFragment as Fragment)
                        .addToBackStack(null)
                        .commit()
            }
            R.id.nav_search -> {
                var searchFragment = SearchFragment()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, searchFragment as Fragment)
                        .addToBackStack(null)
                        .commit()
            }

            R.id.nav_settings -> {
                startActivity<SettingsActivity>()
            }
            R.id.nav_about -> {
                var aboutFragment = AboutFragment.newInstance()
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, aboutFragment as Fragment)
                        .addToBackStack(null)
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
        var header = nav_view.getHeaderView(0)
        var memberButton = header.find<Button>(R.id.memberButton)
        var avatarUser = header.find<ImageView>(R.id.avatarUser)
        var usernameBadge = header.find<TextView>(R.id.usernameBadge)
        User.id = 0
        User.status = 0
        User.token = ""
        User.md5 = ""
        User.name = ""
        usernameBadge.text = getString(R.string.renchon)
        avatarUser.setImageResource(android.R.color.transparent)
        avatarUser.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.anon))
        memberButton.text = getString(R.string.action_sign_in)
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
