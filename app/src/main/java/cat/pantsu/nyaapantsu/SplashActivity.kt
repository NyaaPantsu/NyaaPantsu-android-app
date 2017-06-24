package cat.pantsu.nyaapantsu

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.github.kittinunf.fuel.core.FuelManager
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
