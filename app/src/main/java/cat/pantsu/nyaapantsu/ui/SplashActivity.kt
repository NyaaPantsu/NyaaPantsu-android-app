package cat.pantsu.nyaapantsu.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.User
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!User.splash) {
            setContentView(R.layout.activity_main)
            meguminButton.setOnClickListener { _ ->
                run {
                    User.splash = true
                    startActivity<HomeActivity>()
                }
            }
        } else {
            startActivity<HomeActivity>()
        }
    }
}
