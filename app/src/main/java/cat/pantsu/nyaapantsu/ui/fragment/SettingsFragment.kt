package cat.pantsu.nyaapantsu.ui.fragment

import android.os.Bundle
import android.preference.PreferenceFragment
import cat.pantsu.nyaapantsu.R


/**
 * Created by xdk78 on 2017-07-13.
 */
class SettingsFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}
