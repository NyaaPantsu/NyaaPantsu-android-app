package cat.pantsu.nyaapantsu

import com.chibatching.kotpref.KotprefModel

/**
 * Created by akuma06 on 21/06/2017.
 */
object User : KotprefModel() {
    var id by intPref(default = 0)
    var name by stringPref(default = "れんちょん")
    var md5 by stringPref()
    var token by stringPref()
    var status by intPref(default = 0)
    var splash by booleanPref(default = false)
}
