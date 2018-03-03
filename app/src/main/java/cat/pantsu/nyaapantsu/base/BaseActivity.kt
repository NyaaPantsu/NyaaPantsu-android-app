package cat.pantsu.nyaapantsu.base


import android.os.Bundle
import android.os.PersistableBundle
import cat.pantsu.nyaapantsu.R
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState, persistentState)
    }
}
