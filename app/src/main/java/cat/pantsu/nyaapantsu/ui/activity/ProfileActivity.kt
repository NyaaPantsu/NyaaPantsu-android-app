package cat.pantsu.nyaapantsu.ui.activity

import android.app.Fragment
import android.os.Bundle
import cat.pantsu.nyaapantsu.R
import cat.pantsu.nyaapantsu.model.Query
import cat.pantsu.nyaapantsu.model.User
import cat.pantsu.nyaapantsu.ui.fragment.ProfileFragment


class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val user_id = intent.getIntExtra("user", User.id)
        val query = Query()
        query.userID = user_id.toString()
        val profileFragment = ProfileFragment.newInstance(query)
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_profile, profileFragment as Fragment)
                .commit()
    }

}
