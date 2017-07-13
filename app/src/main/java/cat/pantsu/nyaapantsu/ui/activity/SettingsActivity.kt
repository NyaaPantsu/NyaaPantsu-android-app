package cat.pantsu.nyaapantsu.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.ui.fragment.SettingsFragment
import org.jetbrains.anko.find


/**
 * Created by xdk78 on 2017-07-13.
 */
class SettingsActivity : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val toolbar = find<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        fragmentManager.beginTransaction().replace(R.id.content_settings, SettingsFragment()).commit()

    }
}
