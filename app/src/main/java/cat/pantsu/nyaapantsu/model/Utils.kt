package cat.pantsu.nyaapantsu.model

import com.chibatching.kotpref.KotprefModel

/**
 * Created by xdk78 on 2017-07-13.
 */
object Utils : KotprefModel() {
    var doubleBackToExit by booleanPref(default = true)
}