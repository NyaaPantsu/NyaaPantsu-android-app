package cat.pantsu.nyaapantsu.ui.activity

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.util.Utils

/**
 * Created by ltype on 2017/7/11.
 */
abstract class BaseActivity: AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        if (this !is HomeActivity) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val enable = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("mascot_voice_switch", true)
        if (enable) menuInflater.inflate(R.menu.menu_pets, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (this !is HomeActivity) finish()
            }
            R.id.menu_play -> {
                Utils.playVoice(applicationContext)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}