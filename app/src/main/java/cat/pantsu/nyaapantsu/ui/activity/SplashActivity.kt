package cat.pantsu.nyaapantsu.ui.activity

import android.os.Bundle
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.base.BaseActivity
import cat.pantsu.nyaapantsu.mvp.model.UserModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!UserModel.splash) {
            setContentView(R.layout.activity_main)
            meguminButton.setOnClickListener { _ ->
                run {
                    UserModel.splash = true
                    startActivity<HomeActivity>()
                }
            }
        } else {
            startActivity<HomeActivity>()
        }
    }
}
