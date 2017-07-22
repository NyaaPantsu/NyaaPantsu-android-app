package cat.pantsu.nyaapantsu.ui.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import cat.pantsu.nyaapantsu.R

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
        menuInflater.inflate(R.menu.menu_pets, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (this !is HomeActivity) finish()
            }
            R.id.menu_play -> {
                //TODO support sukebei mode
                val player = MediaPlayer.create(applicationContext, R.raw.nyanpass)
                player.start()
                player.setOnCompletionListener {
                    player.release()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}